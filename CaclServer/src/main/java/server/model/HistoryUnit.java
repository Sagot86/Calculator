package server.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "calc_history")
public class HistoryUnit implements Serializable {

    private static final long serialVersionUID = -7049957706738879274L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    private Long id;

    @Column(name = "init_value")
    private BigDecimal initVal;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private Operation operation;

    @Column(name = "op_value")
    private BigDecimal opVal;

    @Column(name = "fin_value")
    private BigDecimal finVal;

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
}
