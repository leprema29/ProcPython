/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.abou.cm.backup;

/**
 *
 * @author aboubecker
 */
public class StatistiqueMTNLieux11 {
    

 	
	private String Localisation;
	private int occurence;
	
	

	public StatistiqueMTNLieux11(String Localisation,int occurence) {
		super();
		this.Localisation = Localisation;
		this.occurence = occurence;
                
		
	}

    public String getLocalisation() {
        return Localisation;
    }

    public int getOccurence() {
        return occurence;
    }

    public void setLocalisation(String Localisation) {
        this.Localisation = Localisation;
    }

    public void setOccurence(int occurence) {
        this.occurence = occurence;
    }

      	
	@Override
	public String toString() {
		return "Student [Localisation=" + Localisation + ",occurence=" + occurence +  "]";
	}
}