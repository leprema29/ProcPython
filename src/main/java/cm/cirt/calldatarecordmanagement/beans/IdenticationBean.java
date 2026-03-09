/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.beans;

import java.util.Date;

/**
 *
 * @author Harry Wanki
 */
public class IdenticationBean {
    private String telephone;
    private String name;
    private Date birthday;
    private String cni;
    private Date expireDate;
    private String address;
    private String status;
    private String operator;

    public IdenticationBean() {
    }

    public IdenticationBean(String telephone, String name, Date birthday, String cni, Date expireDate, String address, String status, String operator) {
        this.telephone = telephone;
        this.name = name;
        this.birthday = birthday;
        this.cni = cni;
        this.expireDate = expireDate;
        this.address = address;
        this.status = status;
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
