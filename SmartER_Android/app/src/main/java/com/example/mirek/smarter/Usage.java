package com.example.mirek.smarter;

import java.sql.Date;

/**
 * Created by Mirek on 27/04/2018.
 */

public class Usage {
    private Integer usageid;
    private Integer resid;
    private Date date;
    private Integer hours;
    private Double fridgeusage;
    private Double acusage;
    private Double wmusage;
    private Double temperature;

    public Usage(Integer usageid, Integer resid, Date date, Integer hours, Double fridgeusage, Double acusage, Double wmusage, Double temperature) {
        this.usageid = usageid;
        this.resid = resid;
        this.date = date;
        this.hours = hours;
        this.fridgeusage = fridgeusage;
        this.acusage = acusage;
        this.wmusage = wmusage;
        this.temperature = temperature;
    }

    public Usage() {

    }

    public Double getAcusage() {
        return acusage;
    }

    public Date getDate() {
        return date;
    }

    public Double getFridgeusage() {
        return fridgeusage;
    }

    public Integer getHours() {
        return hours;
    }

    public Integer getResid() {
        return resid;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Integer getUsageid() {
        return usageid;
    }

    public Double getWmusage() {
        return wmusage;
    }

    public void setAcusage(Double acusage) {
        this.acusage = acusage;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFridgeusage(Double fridgeusage) {
        this.fridgeusage = fridgeusage;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public void setResid(Integer resid) {
        this.resid = resid;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setUsageid(Integer usageid) {
        this.usageid = usageid;
    }

    public void setWmusage(Double wmusage) {
        this.wmusage = wmusage;
    }
}
