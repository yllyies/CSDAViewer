package com.agilent.cdsa.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 仪器信息表，用于记录第三方仪器的ip、token，功率信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "power_history")
public class PowerHistory implements Serializable
{
    private static final long serialVersionUID = 7434120365325073766L;

    @Id
    @Column(nullable = false)
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator" )
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column
    private String instrumentId;

    @Column
    private String ip;

    @Column
    private String token;

    @Column
    private Double power;

    @Column(name = "created_date")
    private Timestamp createdDate;

    public PowerHistory(String id, String ip, String token) {
        this.id = id;
        this.ip = ip;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}