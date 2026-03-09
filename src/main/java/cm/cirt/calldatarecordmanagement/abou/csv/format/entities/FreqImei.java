/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.csv.format.entities;

import java.util.Date;

/**
 *
 * @author Harry Wanki
 */
public class FreqImei {
    private int total;
    private String imei;
    private Date firtUse;
    private Date lastUse;

    public FreqImei() {
    }

    public FreqImei(int total, String imei, Date firtUse, Date lastUse) {
        this.total = total;
        this.imei = imei;
        this.firtUse = firtUse;
        this.lastUse = lastUse;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Date getFirtUse() {
        return firtUse;
    }

    public void setFirtUse(Date firtUse) {
        this.firtUse = firtUse;
    }

    public Date getLastUse() {
        return lastUse;
    }

    public void setLastUse(Date lastUse) {
        this.lastUse = lastUse;
    }
    
    
}
