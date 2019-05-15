package client.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class HistoryUnit implements Serializable {

    private static final long serialVersionUID = -7049957706738879274L;

    private long id;
    private BigDecimal initVal;
    private Operation operation;
    private BigDecimal opVal;
    private BigDecimal finVal;

    public HistoryUnit(long id, BigDecimal initVal, Operation operation, BigDecimal opVal, BigDecimal finVal) {
        this.id = id;
        this.initVal = initVal;
        this.operation = operation;
        this.opVal = opVal;
        this.finVal = finVal;
    }

    public HistoryUnit() {
    }

    public long getId() {
        return id;
    }

    public BigDecimal getInitVal() {
        return initVal;
    }

    public Operation getOperation() {
        return operation;
    }

    public BigDecimal getOpVal() {
        return opVal;
    }

    public BigDecimal getFinVal() {
        return finVal;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setInitVal(BigDecimal initVal) {
        this.initVal = initVal;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setOpVal(BigDecimal opVal) {
        this.opVal = opVal;
    }

    public void setFinVal(BigDecimal finVal) {
        this.finVal = finVal;
    }
}
