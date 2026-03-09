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
public class identificationMTN {

    private String Numero;
    private String NomPrenom;
    private String DateNaissance;
    private String NumeroCNI;
    private String DateExpCNI;
    private String Quartier;
    private String Nationalite;

    /**
     * @param Numero
     * @param NomPrenom
     * @param DateNaissance
     * @param NumeroCNI
     * @param DateExpCNI
     * @param Quartier
     * @param Nationalite
     */
    public identificationMTN(String Numero, String NomPrenom, String DateNaissance, String NumeroCNI, String DateExpCNI, String Quartier, String Nationalite) {
        super();
        this.Numero = Numero;
        this.NomPrenom = NomPrenom;
        this.DateNaissance = DateNaissance;
        this.NumeroCNI = NumeroCNI;
        this.DateExpCNI = DateExpCNI;
        this.Quartier = Quartier;
        this.Nationalite = Nationalite;
    }

    public identificationMTN(String inputcall, String string, String inputcall0, String inputcall1, String inputcall2, String inputcall3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getNumero() {
        return Numero;
    }

    public String getNomPrenom() {
        return NomPrenom;
    }

    public String getDateNaissance() {
        return DateNaissance;
    }

    public String getNumeroCNI() {
        return NumeroCNI;
    }

    public void setNumero(String Numero) {
        this.Numero = Numero;
    }

    public void setNomPrenom(String NomPrenom) {
        this.NomPrenom = NomPrenom;
    }

    public void setDateNaissance(String DateNaissance) {
        this.DateNaissance = DateNaissance;
    }

    public void setNumeroCNI(String NumeroCNI) {
        this.NumeroCNI = NumeroCNI;
    }

    public void setDateExpCNI(String DateExpCNI) {
        this.DateExpCNI = DateExpCNI;
    }

    public void setQuartier(String Quartier) {
        this.Quartier = Quartier;
    }

    public void setNationalite(String Nationalite) {
        this.Nationalite = Nationalite;
    }

    public String getDateExpCNI() {
        return DateExpCNI;
    }

    public String getQuartier() {
        return Quartier;
    }

    public String getNationalite() {
        return Nationalite;
    }

    @Override
    public String toString() {
        return "Student [Numero=" + Numero + ",NomPrenom=" + NomPrenom + ", DateNaissance=" + DateNaissance
                + ", NumeroCNI=" + NumeroCNI + ", DateExpCNI=" + DateExpCNI + ", Quartier=" + Quartier + ", Nationalite="
                + Nationalite + "]";
    }
}
