package com.agilent.iad.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 记录仪器实时状态
 */
@Entity
@Table(name = "instrument_state")
public class InstrumentState implements Serializable {
    private static final long serialVersionUID = 3905628661906255711L;

//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    private Integer id;

    @Id
    @Column
    private BigDecimal instrumentId;
    @Column
    private String instrumentName;
    @Column
    private String instrumentDescription;
    @Column(name = "instrument_state")
    private String instrumentState;
    @Column
    private Integer instrumentRuntime;

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public BigDecimal getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(BigDecimal instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getInstrumentDescription() {
        return instrumentDescription;
    }

    public void setInstrumentDescription(String instrumentDescription) {
        this.instrumentDescription = instrumentDescription;
    }

    public String getInstrumentState() {
        return instrumentState;
    }

    public void setInstrumentState(String instrumentState) {
        this.instrumentState = instrumentState;
    }

    public Integer getInstrumentRuntime() {
        return instrumentRuntime;
    }

    public void setInstrumentRuntime(Integer instrumentRuntime) {
        this.instrumentRuntime = instrumentRuntime;
    }
}