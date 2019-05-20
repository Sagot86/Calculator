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
    public String processingPressOperation(Operation operationIn) {
        String str = "";

        if (operationIn.equals(Operation.EQUAL)) {
            if (firstValue() == null) {
                if (inputIsEmpty()) {
                    str = "0";
                }
                return buildString(str);
            }
            if (currentOperation() == null) {
                if (inputIsEmpty()) {
                    return firstValue().toString();
                }
                return buildString(str);
            }
            if (inputIsEmpty()) {
                return firstValue().toString();
            } else {
                unitForCalc.setOpVal(getNumberFromInput());
                return calculate();
            }
        } else {
            if (firstValue() == null) {
                unitForCalc.setOperation(operationIn);
                if (inputIsEmpty()) {
                    unitForCalc.setInitVal(BigDecimal.ZERO);
                } else {
                    unitForCalc.setInitVal(getNumberFromInput());
                    clearSB();
                }
                return buildString(str);
            } else {
                if (currentOperation() == null) {
                    unitForCalc.setOperation(operationIn);
                    if (!inputIsEmpty()) {
                        unitForCalc.setInitVal(getNumberFromInput());
                        clearSB();
                    }
                    return buildString(str);
                } else {
                    unitForCalc.setOperation(operationIn);
                    if (!inputIsEmpty()) {
                        unitForCalc.setOpVal(getNumberFromInput());
                        /* да, костыль */
                        if (calculate().equals("Слишком длинное число!")) {
                            return "Слишком длинное число!";
                        }
                        unitForCalc.setOperation(operationIn);
                    }
                    return buildString(str);
                }
            }
        }
    }


    private String buildString(String string) {
        if (unitForCalc.getOperation() == null) {
            return stringForCalc.append(string).toString();
        } else return (unitForCalc.getInitVal().toString() +
                unitForCalc.getOperation().getSymbol() +
                stringForCalc.append(string).toString());
    }

    private String calculate() {
        /* Произвести рассчет */
        try {
            unitForCalc.setFinVal(calculator.calculate(unitForCalc));
        } catch (ArithmeticException ex) {
            clearUnitForCalc();
            clearSB();
            return "Здесь на ноль делить нельзя!";
        }

        if (unitForCalc.getFinVal().toString().length() >= 20) {
            clearSB();
            unitForCalc.setOperation(null);
            unitForCalc.setOpVal(null);
            unitForCalc.setFinVal(null);
            return "Слишком длинное число!";
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
