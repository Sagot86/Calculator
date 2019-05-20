package client.service;

import client.controller.UpdateHistoryListener;
import client.model.Calculator;
import client.model.HistoryUnit;
import client.model.Operation;

import java.math.BigDecimal;

public class CalculatorService {

    private DataTransferService dts = new DataTransferService();
    private final Calculator calculator = new Calculator();
    private final UpdateHistoryListener listener;

    private final HistoryUnit unitForCalc = new HistoryUnit();
    private StringBuilder stringForCalc = new StringBuilder();

    public CalculatorService(UpdateHistoryListener listener) {
        this.listener = listener;
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
        dts.saveToDB(unitForCalc);

        /* Сохраняем строку для печати на экран */
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
}
