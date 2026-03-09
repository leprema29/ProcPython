/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.csv.format.entities;

/**
 *
 * ABOUBECKER 88
 */
public class listing {
    
	private String NumeroAppelant;
	private String LocalisationNumeroAppelant;
	private String IMEINumeroAppelant;
	private String DateDebutAppel;
	private String DureeAppel;
	private String NumeroAppele;
	/**
     * @param NumeroAppelant
     * @param LocalisationNumeroAppelant
     * @param IMEINumeroAppelant
     * @param DateDebutAppel
     * @param DureeAppel
     * @param NumeroAppele
	 */
	public listing(String NumeroAppelant,String LocalisationNumeroAppelant,String IMEINumeroAppelant,String DateDebutAppel,String DureeAppel,String NumeroAppele) {
		super();
		this.NumeroAppelant = NumeroAppelant;
		this.LocalisationNumeroAppelant = LocalisationNumeroAppelant;
		this.IMEINumeroAppelant = IMEINumeroAppelant;
		this.DateDebutAppel = DateDebutAppel;
		this.DureeAppel = DureeAppel;
		this.NumeroAppele = NumeroAppele;
	}

    public String getNumeroAppelant() {
        return NumeroAppelant;
    }

    public String getLocalisationNumeroAppelant() {
        return LocalisationNumeroAppelant;
    }

    public String getIMEINumeroAppelant() {
        return IMEINumeroAppelant;
    }

    public String getDateDebutAppel() {
        return DateDebutAppel;
    }

    public void setNumeroAppelant(String NumeroAppelant) {
        this.NumeroAppelant = NumeroAppelant;
    }

    public void setLocalisationNumeroAppelant(String LocalisationNumeroAppelant) {
        this.LocalisationNumeroAppelant = LocalisationNumeroAppelant;
    }

    public void setIMEINumeroAppelant(String IMEINumeroAppelant) {
        this.IMEINumeroAppelant = IMEINumeroAppelant;
    }

    public void setDateDebutAppel(String DateDebutAppel) {
        this.DateDebutAppel = DateDebutAppel;
    }

    public void setDureeAppel(String DureeAppel) {
        this.DureeAppel = DureeAppel;
    }

    public void setNumeroAppele(String NumeroAppele) {
        this.NumeroAppele = NumeroAppele;
    }

    public String getDureeAppel() {
        return DureeAppel;
    }

    public String getNumeroAppele() {
        return NumeroAppele;
    }
	
	
	@Override
	public String toString() {
		return "Student [NumeroAppelant=" + NumeroAppelant + ",LocalisationNumeroAppelant=" + LocalisationNumeroAppelant + ", IMEINumeroAppelant=" + IMEINumeroAppelant
				+ ", DateDebutAppel=" + DateDebutAppel + ", DureeAppel=" + DureeAppel + ", NumeroAppele="
				+ NumeroAppele + "]";
	}
}