package client.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {

    public Calculator() {
    }

    public BigDecimal calculate(HistoryUnit historyUnit) {
        return calculateOne(historyUnit);
    }

    private BigDecimal calculateOne(HistoryUnit historyUnit) throws ArithmeticException {

        switch (historyUnit.getOperation()) {
            case ADDITION:
                return historyUnit.getInitVal().add(historyUnit.getOpVal());
            case SUBTRACTION:
                return historyUnit.getInitVal().subtract(historyUnit.getOpVal());
            case MULTIPLICATION:
                return historyUnit.getInitVal().multiply(historyUnit.getOpVal());
            case DIVISION:
                return historyUnit.getInitVal().divide(historyUnit.getOpVal(), 6, RoundingMode.CEILING);
            case EQUAL:
                return historyUnit.getInitVal();
        }

        return null;

    }
}
