package com.example.mirek.smarter;

import java.sql.Date;

/**
 * Created by Mirek on 27/04/2018.
 */

public class Credential {
    private String username;
    private Integer resid;
    private String passwordhash;
    private Date regodate;

    public Credential (String username, Integer resid, String passwordhash, Date regodate) {
        this.username = username;
        this.resid = resid;
        this.passwordhash = passwordhash;
        this.regodate = regodate;
    }

    public Credential() {

    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public Date getRegodate() {
        return regodate;
    }

    public Integer getResid() {
        return resid;
    }

    public String getUsername() {
        return username;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public void setRegodate(Date regodate) {
        this.regodate = regodate;
    }

    public void setResid(Integer resid) {
        this.resid = resid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
