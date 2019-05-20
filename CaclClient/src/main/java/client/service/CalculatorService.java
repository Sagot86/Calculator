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

    private BigDecimal firstValue() {
        return unitForCalc.getInitVal();
    }

    private BigDecimal secondValue() {
        return unitForCalc.getOpVal();
    }

    private Operation currentOperation() {
        return unitForCalc.getOperation();
    }

    private boolean inputIsEmpty() {
        return stringForCalc.length() == 0;
    }

    private BigDecimal getNumberFromInpit() {
        return new BigDecimal(stringForCalc.toString());
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
        if (!operation.equals(Operation.EQUAL) && currentOperation() == null && !inputIsEmpty()) {
            System.out.println("Операция не равно, поле операции еще не заполнено, символы уже вводились");
            unitForCalc.setInitVal(getNumberFromInpit());
            unitForCalc.setOperation(operation);
            clearSB();
            return buildString("");
        } else if (firstValue() == null && currentOperation() == null && inputIsEmpty()) {
            System.out.println("Начальное значение не заполнено, поле операции еще не заполнено, символов еще не вводилось");
            unitForCalc.setInitVal(BigDecimal.ZERO);
            unitForCalc.setOperation(operation);
            return buildString("");
        } else if (firstValue() != null && currentOperation() == null && inputIsEmpty()) {
            System.out.println("Начальное значение уже есть, второго значения еще нет, символы еще не вводились");
            unitForCalc.setOperation(operation);
            return buildString("");
        } else if (!operation.equals(Operation.EQUAL) && firstValue() != null && currentOperation() != null && secondValue() == null && !inputIsEmpty()) {
            System.out.println("Начальное значение уже есть, поле операции уже заполнено, второго значения еще нет, символы уже вводились, знак не равно");
            unitForCalc.setOpVal(getNumberFromInpit());
            calculate();
            unitForCalc.setOperation(operation);
            return buildString("");
        } else if (operation.equals(Operation.EQUAL)) {
            if (secondValue() == null && currentOperation() == null) {
                unitForCalc.setOperation(null);
                System.out.println("метод для многократного нажатия равно");
                return buildString("");
            } else {
                System.out.println("Пришедшая операция - равно");
                unitForCalc.setOpVal(getNumberFromInpit());
                return calculate();
            }
        }
        return null;
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
