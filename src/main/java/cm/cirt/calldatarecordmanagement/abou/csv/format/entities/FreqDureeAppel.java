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
public class FreqDureeAppel {

    private String numero;
    private String identite;
    private String dureeAppel;
    private int nombreMessage;

    public FreqDureeAppel() {
    }

    public FreqDureeAppel(String numero, String identite, String dureeAppel, int nombreMessage) {
        this.numero = numero;
        this.identite = identite;
        this.dureeAppel = dureeAppel;
        this.nombreMessage = nombreMessage;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getIdentite() {
        return identite;
    }

    public void setIdentite(String identite) {
        this.identite = identite;
    }

    public String getDureeAppel() {
        return dureeAppel;
    }

    public void setDureeAppel(String dureeAppel) {
        this.dureeAppel = dureeAppel;
    }

    public int getNombreMessage() {
        return nombreMessage;
    }

    public void setNombreMessage(int nombreMessage) {
        this.nombreMessage = nombreMessage;
    }

}
