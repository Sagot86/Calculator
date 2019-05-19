package client.service;

import client.controller.UpdateHistoryListener;
import client.dao.H2DBWorker;
import client.model.Calculator;
import client.model.HistoryUnit;
import client.model.JsonConverterNTransporter;
import client.model.Operation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculatorService {

    private final H2DBWorker dbWorker = new H2DBWorker();
    private final JsonConverterNTransporter jcNt = new JsonConverterNTransporter();
    private final Calculator calculator = new Calculator();
    private final UpdateHistoryListener listener;

    private final HistoryUnit unitForCalc = new HistoryUnit();
    private StringBuilder stringForCalc = new StringBuilder();

    private long idCount;

    public CalculatorService(UpdateHistoryListener listener) {
        this.listener = listener;
    }

    public void onAppStart() {
        /* Create inmemoryDB */
        dbWorker.createTable();
    }


    /**
     * Load history from server DB & convert to List<String>
     */
    public List<String> getHistory() {
        List<HistoryUnit> history = jcNt.loadHistory();
        return unitToString(history);
    }

    /**
     * Обработка в случае нажатия цифры или точки
     */
    public String processValue(String s) {
            return buildString(s);
    }

    /**
     * Обработка в случае нажатия точки
     */
    public String processDot(String dot) {
        if (stringForCalc.length() == 0) {
            return buildString("0" + dot);
        }
        if (stringForCalc.indexOf(".") == -1) {
            return buildString(dot);
        }
        return buildString("");
    }


    /**
     * Обработка в случае нажатия символа операции
     */
    public String getInputValue(Operation operation) {
        if (!operation.equals(Operation.EQUAL) && unitForCalc.getOperation() == null && stringForCalc.length() > 0) {
            System.out.println("Операция не равно, поле операции еще не заполнено, символы уже вводились");
            unitForCalc.setInitVal(new BigDecimal(stringForCalc.toString()));
            unitForCalc.setOperation(operation);
            clearSB();
            return buildString("");
        } else if (unitForCalc.getInitVal() == null && unitForCalc.getOperation() == null && stringForCalc.length() == 0) {
            System.out.println("Начальное значение не заполнено, поле операции еще не заполнено, символов еще не вводилось");
            unitForCalc.setInitVal(BigDecimal.ZERO);
            unitForCalc.setOperation(operation);
            return buildString("");
        } else if (unitForCalc.getInitVal() != null && unitForCalc.getOpVal() == null && stringForCalc.length() == 0) {
            System.out.println("Начальное значение уже есть, второго значения еще нет, символы еще не вводились");
            unitForCalc.setOperation(operation);
            return buildString("");
        } else if (!operation.equals(Operation.EQUAL) && unitForCalc.getInitVal() != null && unitForCalc.getOperation() != null && unitForCalc.getOpVal() == null && stringForCalc.length() > 0) {
            System.out.println("Начальное значение уже есть, поле операции уже заполнено, второго значения еще нет, символы уже вводились, знак не равно");
            unitForCalc.setOpVal(new BigDecimal(stringForCalc.toString()));
            calculate();
            unitForCalc.setOperation(operation);
            return buildString("");
        } else if (operation.equals(Operation.EQUAL)) {
            if (unitForCalc.getOpVal() == null && unitForCalc.getOperation() == null)  {
                unitForCalc.setOperation(null);
                System.out.println("метод для многократного нажатия равно");
                return buildString("");
            } else {
                System.out.println("Пришедшая операция - равно");
                unitForCalc.setOpVal(new BigDecimal(stringForCalc.toString()));
                return calculate();
            }
        } else {
            System.out.println("Все остальное");
            unitForCalc.setOperation(operation);
            return buildString("");
        }
    }

    private String buildString(String string) {
        if (unitForCalc.getOperation() == null) {
            return stringForCalc.append(string).toString();
        } else return (
                unitForCalc.getInitVal().toString() +
                unitForCalc.getOperation().getSymbol() +
                stringForCalc.append(string).toString());
    }

    private String calculate() {
        //Произвести рассчет
        try {
            unitForCalc.setFinVal(calculator.calculate(unitForCalc));
        } catch (ArithmeticException ex) {
            clearUnitForCalc();
            clearSB();
            return "Здесь на ноль делить нельзя!";
        }

        //Сохраняем значение unitForCalc в DB
        dbWorker.saveData(unitForCalc);

        //Increment count
        idCount++;

        //Выгрузка записей в основную базу в случае, если в памяти их больше тридцати.
        if (idCount > 30) {
            jcNt.uploadHistory(dbWorker.getData());
            dbWorker.clearData(idCount);
            idCount = 0;
        }

        /* Сохраняем стринг для печати на экран */
        String forReturn = unitToString(unitForCalc);

        if (listener != null) {
            listener.onHistoryUnitAdded(forReturn);
        }

        /* Обуляем значения */
        clearSB();
        unitForCalc.setInitVal(unitForCalc.getFinVal());
        unitForCalc.setOperation(null);
        unitForCalc.setOpVal(null);
        unitForCalc.setFinVal(null);

        /* Возвращаем строку для печати */
        return forReturn;
    }

    /**
     * Conversion List<HistoryUnit> to List<String>
     */
    private List<String> unitToString(List<HistoryUnit> units) {
        List<String> testList = new ArrayList<>();
        for (HistoryUnit unit : units) {
            testList.add(
                    unit.getInitVal() +
                            " " +
                            unit.getOperation().getSymbol() +
                            " " +
                            unit.getOpVal() +
                            " = " +
                            unit.getFinVal()
            );
        }
        return testList;
    }

    /**
     * Conversion HistoryUnit to String
     */
    private String unitToString(HistoryUnit unit) {
        return
                unit.getInitVal() +
                        " " +
                        unit.getOperation().getSymbol() +
                        " " +
                        unit.getOpVal() +
                        " = " +
                        unit.getFinVal();
    }

    public void clearSB() {
        stringForCalc.setLength(0);
    }

    public void clearUnitForCalc() {
        unitForCalc.setInitVal(null);
        unitForCalc.setOperation(null);
        unitForCalc.setOpVal(null);
    }

    public void onAppStop() {
        //выгрузка оставшихся записей из памяти в основную базу
        jcNt.uploadHistory(dbWorker.getData());
    }

}
