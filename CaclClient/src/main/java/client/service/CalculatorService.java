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

    private BigDecimal getNumberFromInput() {
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
        if (operation.equals(Operation.EQUAL)) {
            if ((currentOperation() == null) || (currentOperation() != null && firstValue() == null)) {
                if (!inputIsEmpty()) {
                    return buildString("");
                } else return "0";
            } else if (!inputIsEmpty()) {
                unitForCalc.setOpVal(getNumberFromInput());
                return calculate();
            } else {
                return firstValue().toString();
            }
        } else if (currentOperation() == null && !inputIsEmpty()) {
            System.out.println("Операция не равно, поле операции еще не заполнено, символы уже вводились");
            unitForCalc.setInitVal(getNumberFromInput());
            unitForCalc.setOperation(operation);
            clearSB();
            return buildString("");
        } else if (firstValue() == null && currentOperation() == null && inputIsEmpty()) {
            System.out.println("Начальное значение не заполнено, поле операции еще не заполнено, символов еще не вводилось");
            unitForCalc.setInitVal(BigDecimal.ZERO);
            unitForCalc.setOperation(operation);
            return buildString("");
        } else if (firstValue() != null && secondValue() == null && inputIsEmpty()) {
            System.out.println("Начальное значение уже есть, второго значения еще нет, символы еще не вводились");
            unitForCalc.setOperation(operation);
            return buildString("");
        } else if (firstValue() != null && currentOperation() != null && secondValue() == null && !inputIsEmpty()) {
            System.out.println("Начальное значение уже есть, поле операции уже заполнено, второго значения еще нет, символы уже вводились, знак не равно");
            unitForCalc.setOpVal(getNumberFromInput());
            calculate();
            unitForCalc.setOperation(operation);
            return buildString("");
        } else {
            System.out.println("Все остальное");
            unitForCalc.setOperation(operation);
            return buildString("");
        }
    }

    public String processInOper(Operation operationIn) {
        String str = "";

        if (operationIn.equals(Operation.EQUAL)) {
            System.out.println("=");
            if (firstValue() == null) {
                if (inputIsEmpty()) {
                    str = "0";
                }
            } else {
                if (currentOperation() == null) {
                    if (inputIsEmpty()) {
                        return firstValue().toString();
                    }
                } else {
                    if (inputIsEmpty()) {
                        unitForCalc.setOperation(null);
                        return firstValue().toString();
                    } else {
                        System.out.println("try calc");
                        unitForCalc.setOpVal(getNumberFromInput());
                        return calculate();
                    }
                }
            }
            return buildString(str);
        } else {
            System.out.println("not =");
            unitForCalc.setOperation(operationIn);
            if (firstValue() == null) {
                System.out.println("1");
                if (inputIsEmpty()) {
                    unitForCalc.setInitVal(BigDecimal.ZERO);
                    return buildString(str);
                } else {
                    unitForCalc.setInitVal(getNumberFromInput());
                    clearSB();
                    return buildString(str);
                }
            } else {
                System.out.println("2");
                if (currentOperation() == null) {
                    unitForCalc.setOpVal(getNumberFromInput());
                    if (!inputIsEmpty()) {
                        clearSB();
                    }
                    return buildString(str);
                } else {
                    System.out.println("trycalc");
                    if (!inputIsEmpty()) {
                        calculate();
                    }
                }
            }
        }
        return buildString(str);
    }

    private String upgradedBS(String string) {
        if (unitForCalc.getOperation() == null) {
            if (firstValue() != null) {
                return unitForCalc.getInitVal().toString() + stringForCalc.append(string).toString();
            } else return stringForCalc.append(string).toString();
        } else return (unitForCalc.getInitVal().toString() +
                unitForCalc.getOperation().getSymbol() +
                stringForCalc.append(string).toString());
    }

    private String buildString(String string) {
        if (unitForCalc.getOperation() == null) {
            return stringForCalc.append(string).toString();
        } else return (unitForCalc.getInitVal().toString() +
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
