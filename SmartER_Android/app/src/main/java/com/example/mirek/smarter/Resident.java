package com.example.mirek.smarter;

import java.util.Date;

/**
 * Created by Mirek on 27/04/2018.
 */

public class Resident {
    private int resid;
    private String fname;
    private String lname;
    private Date dob;
    private String address;
    private String postcode;
    private String email;
    private String mobile;
    private short noofresidents;
    private String provider;

    public Resident(int resid, String fname, String lname, Date dob, String address, String postcode, String email, String mobile, short noofresidents, String provider) {
        this.resid = resid;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.address = address;
        this.postcode = postcode;
        this.email = email;
        this.mobile = mobile;
        this.noofresidents = noofresidents;
        this.provider = provider;
    }

    public Resident() {

    }

    public String getAddress() {
        return address;
    }

    public Date getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getMobile() {
        return mobile;
    }

    public short getNoofresidents() {
        return noofresidents;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getProvider() {
        return provider;
    }

    public int getResid() {
        return resid;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setNoofresidents(short noofresidents) {
        this.noofresidents = noofresidents;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }
}
