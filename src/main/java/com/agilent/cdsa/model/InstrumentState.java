package com.agilent.cdsa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 记录仪器实时状态
 */
@Entity
@Table(name = "instrument_state")
public class InstrumentState implements Serializable {
    private static final long serialVersionUID = 3905628661906255711L;

    @Id
    @Column(nullable = false)
    private String id;
    @Column
    private BigDecimal instrumentId;
    @Column
    private String instrumentName;
    @Column
    private String instrumentDescription;
    @Column
    private String instrumentState;
    @Column
    private Integer instrumentRuntime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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