/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.csv.format.entities;

/**
 *
 * @author aboubecker
 */
public class StatistiqueAppels {
    

 	
	private String NumeroAppelant;
	private int occurence;
	private String DureeAppel;
	

	public StatistiqueAppels(String NumeroAppelant,int occurence,String DureeAppel) {
		super();
		this.NumeroAppelant = NumeroAppelant;
		this.occurence = occurence;
                this.DureeAppel = DureeAppel;
		
	}


    public String getNumeroAppelant() {
        return NumeroAppelant;
    }

    public int getOccurence() {
        return occurence;
    }

    public String getDureeAppel() {
        return DureeAppel;
    }

    public void setNumeroAppelant(String NumeroAppelant) {
        this.NumeroAppelant = NumeroAppelant;
    }

    public void setOccurence(int occurence) {
        this.occurence = occurence;
    }

    public void setDureeAppel(String DureeAppel) {
        this.DureeAppel = DureeAppel;
    }

    	
	@Override
	public String toString() {
		return "Student [NumeroAppelant=" + NumeroAppelant + ",occurence=" + occurence + ", DureeAppel=" + DureeAppel + "]";
	}
}