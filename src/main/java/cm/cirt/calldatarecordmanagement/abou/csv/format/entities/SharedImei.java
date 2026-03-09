/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.csv.format.entities;

/**
 *
 * @author Harry Wanki
 */
public class SharedImei {
    private String numero;
    private String imei;
    private String identite;
    private String occurrence;
    private String firstUse;
    private String lastUse;

    public SharedImei() {
    }

    public SharedImei(String numero, String imei, String identite, String occurrence, String firstUse, String lastUse) {
        this.numero = numero;
        this.imei = imei;
        this.identite = identite;
        this.occurrence = occurrence;
        this.firstUse = firstUse;
        this.lastUse = lastUse;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getIdentite() {
        return identite;
    }

    public void setIdentite(String identite) {
        this.identite = identite;
    }

    public String getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(String occurrence) {
        this.occurrence = occurrence;
    }

    public String getFirstUse() {
        return firstUse;
    }

    public void setFirstUse(String firstUse) {
        this.firstUse = firstUse;
    }

    public String getLastUse() {
        return lastUse;
    }

    public void setLastUse(String lastUse) {
        this.lastUse = lastUse;
    }
    
    
}
