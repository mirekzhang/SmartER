/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SmartER;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mirek
 */
@Entity
@Table(name = "USAGE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usage.findAll", query = "SELECT u FROM Usage u")
    , @NamedQuery(name = "Usage.findByUsageid", query = "SELECT u FROM Usage u WHERE u.usageid = :usageid")
    , @NamedQuery(name = "Usage.findByResid", query = "SELECT u FROM Usage u WHERE u.resid.resid = :resid")
    , @NamedQuery(name = "Usage.findByDate", query = "SELECT u FROM Usage u WHERE u.date = :date")
    , @NamedQuery(name = "Usage.findByHours", query = "SELECT u FROM Usage u WHERE u.hours = :hours")
    , @NamedQuery(name = "Usage.findByFridgeusage", query = "SELECT u FROM Usage u WHERE u.fridgeusage = :fridgeusage")
    , @NamedQuery(name = "Usage.findByAcusage", query = "SELECT u FROM Usage u WHERE u.acusage = :acusage")
    , @NamedQuery(name = "Usage.findByWmusage", query = "SELECT u FROM Usage u WHERE u.wmusage = :wmusage")
    , @NamedQuery(name = "Usage.findByTemperature", query = "SELECT u FROM Usage u WHERE u.temperature = :temperature")
    , @NamedQuery(name = "Usage.findByMobileANDDate", query = "SELECT u FROM Usage u WHERE u.resid.mobile = :mobile AND u.date = :date")})
public class Usage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USAGEID")
    private Integer usageid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HOURS")
    private short hours;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FRIDGEUSAGE")
    private double fridgeusage;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACUSAGE")
    private double acusage;
    @Basic(optional = false)
    @NotNull
    @Column(name = "WMUSAGE")
    private double wmusage;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TEMPERATURE")
    private double temperature;
    @JoinColumn(name = "RESID", referencedColumnName = "RESID")
    @ManyToOne(optional = false)
    private Resident resid;

    public Usage() {
    }

    public Usage(Integer usageid) {
        this.usageid = usageid;
    }

    public Usage(Integer usageid, Date date, short hours, double fridgeusage, double acusage, double wmusage, double temperature) {
        this.usageid = usageid;
        this.date = date;
        this.hours = hours;
        this.fridgeusage = fridgeusage;
        this.acusage = acusage;
        this.wmusage = wmusage;
        this.temperature = temperature;
    }

    public Integer getUsageid() {
        return usageid;
    }

    public void setUsageid(Integer usageid) {
        this.usageid = usageid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public short getHours() {
        return hours;
    }

    public void setHours(short hours) {
        this.hours = hours;
    }

    public double getFridgeusage() {
        return fridgeusage;
    }

    public void setFridgeusage(double fridgeusage) {
        this.fridgeusage = fridgeusage;
    }

    public double getAcusage() {
        return acusage;
    }

    public void setAcusage(double acusage) {
        this.acusage = acusage;
    }

    public double getWmusage() {
        return wmusage;
    }

    public void setWmusage(double wmusage) {
        this.wmusage = wmusage;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Resident getResid() {
        return resid;
    }

    public void setResid(Resident resid) {
        this.resid = resid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usageid != null ? usageid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usage)) {
            return false;
        }
        Usage other = (Usage) object;
        if ((this.usageid == null && other.usageid != null) || (this.usageid != null && !this.usageid.equals(other.usageid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SmartER.Usage[ usageid=" + usageid + " ]";
    }
    
}
