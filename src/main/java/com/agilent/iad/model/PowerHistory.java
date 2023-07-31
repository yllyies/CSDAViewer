package com.agilent.iad.model;

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
    private String instrumentName;
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


    public PowerHistory(String instrumentName, String ip, String token, Double power, Timestamp createdDate) {
        this.instrumentName = instrumentName;
        this.ip = ip;
        this.token = token;
        this.power = power;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
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

    @Override
    public String toString() {
        return "PowerHistory{" +
                "id='" + id + '\'' +
                ", instrumentName='" + instrumentName + '\'' +
                ", ip='" + ip + '\'' +
                ", token='" + token + '\'' +
                ", power=" + power +
                '}';
    }
}