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
public class listingOrangeSMS {

    private String NumeroEnvoi;
    private String LocalisationNumeroDest;
    private String IMEINumeroDest;
    private String DateSMS;
    private String NumeroDest;

    /**
     *
     * private String NumeroEnvoi; private String LocalisationNumeroDest;
     * private String IMEINumeroDest; private String DateSMS; private String
     * NumeroDest;
     */
    public listingOrangeSMS(String NumeroEnvoi, String LocalisationNumeroDest, String IMEINumeroDest, String DateSMS, String NumeroDest) {
        super();
        this.NumeroEnvoi = NumeroEnvoi;
        this.LocalisationNumeroDest = LocalisationNumeroDest;
        this.IMEINumeroDest = IMEINumeroDest;
        this.DateSMS = DateSMS;
        this.NumeroDest = NumeroDest;

    }

    public listingOrangeSMS(String inputcall, String string, String inputcall0, String inputcall1, String inputcall2, String inputcall3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getNumeroEnvoi() {
        return NumeroEnvoi;
    }

    public String getLocalisationNumeroDest() {
        return LocalisationNumeroDest;
    }

    public String getIMEINumeroDest() {
        return IMEINumeroDest;
    }

    public String getDateSMS() {
        return DateSMS;
    }

    public String getNumeroDest() {
        return NumeroDest;
    }

    public void setNumeroEnvoi(String NumeroEnvoi) {
        this.NumeroEnvoi = NumeroEnvoi;
    }

    public void setLocalisationNumeroDest(String LocalisationNumeroDest) {
        this.LocalisationNumeroDest = LocalisationNumeroDest;
    }

    public void setIMEINumeroDest(String IMEINumeroDest) {
        this.IMEINumeroDest = IMEINumeroDest;
    }

    public void setDateSMS(String DateSMS) {
        this.DateSMS = DateSMS;
    }

    public void setNumeroDest(String NumeroDest) {
        this.NumeroDest = NumeroDest;
    }

    @Override
    public String toString() {
        return "Student [NumeroEnvoi=" + NumeroEnvoi + ",LocalisationNumeroDest=" + LocalisationNumeroDest + ", IMEINumeroDest=" + IMEINumeroDest
                + ", DateSMS=" + DateSMS + ", NumeroDest=" + NumeroDest + "]";
    }
}
    