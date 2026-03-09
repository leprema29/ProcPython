/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.controllers;

import cm.cirt.calldatarecordmanagement.entities.Requisitions;
import cm.cirt.calldatarecordmanagement.entities.Users;
import cm.cirt.calldatarecordmanagement.interfaces.IRequisitions;
import cm.cirt.calldatarecordmanagement.managers.RequisitionsManager;
import cm.cirt.calldatarecordmanagement.metier.Variables111;
import static cm.cirt.calldatarecordmanagement.metier.Variables111.AUTH_KEY;
import java.io.File;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Harry Wanki
 */
@ManagedBean
@javax.faces.bean.RequestScoped
public class HistoriqueListingMBean implements Serializable {

    private IRequisitions requisitionsManager;
    private List<Requisitions> allRequisitions;
    private Requisitions requisitionToRemove;

    public Requisitions getRequisitionToRemove() {
        return requisitionToRemove;
    }

    public void setRequisitionToRemove(Requisitions requisitionToRemove) {
        this.requisitionToRemove = requisitionToRemove;
    }
    /**
     * Creates a new instance of HistoriqueListing
     */
    public HistoriqueListingMBean() {
    }

    public List<Requisitions> getAllRequisitions() {
        return allRequisitions;
    }

    public void setAllRequisitions(List<Requisitions> allRequisitions) {
        this.allRequisitions = allRequisitions;
    }
    
    @PostConstruct
    public void init() {
        this.requisitionsManager = new RequisitionsManager();
        begin();
    }
    public void begin() {
        Users loggedUser = (Users) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY);
        this.allRequisitions = this.requisitionsManager.getRequisitionseByUser(loggedUser);
        this.requisitionToRemove = new Requisitions();
    }
    
    public void deleteRequisition(Long id) {
        this.requisitionToRemove = this.requisitionsManager.getById(id, Requisitions.class);
        System.out.println("out " + id);
        System.out.println("Telephone " + this.requisitionToRemove.getTelephone());
        this.requisitionsManager.delete(requisitionToRemove);
        String dateReq = formatDate(requisitionToRemove.getDateRequisition());
        System.out.println("Deletion of requisition No " + this.requisitionToRemove.getTelephone() + " owned by ");
        try {
            FileUtils.deleteDirectory(new File(Variables111.DESTINATION_DOSSIERS + dateReq + "/" + this.requisitionToRemove.getTelephone()));
            FileUtils.deleteQuietly(new File(Variables111.DESTINATION_DOSSIERS + dateReq + "/requisition/" + this.requisitionToRemove.getTelephone() + ".xlsx"));
            FileUtils.deleteQuietly(new File(Variables111.DESTINATION_DOSSIERS + dateReq + "/requisition/Requisition_" + this.requisitionToRemove.getTelephone() + ".pdf"));
            FileUtils.deleteQuietly(new File(Variables111.DESTINATION_DOSSIERS + dateReq + "/requisition.zip"));
        } catch (IOException ex) {
            Logger.getLogger(HistoriqueListingMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        begin();
    }
    
    public String formatDate(Date date) {
        DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
        return df2.format(date);
    }
    
    public String getEtatRequisition(int etat) {
        switch (etat) {
            case 1:
                return "Finish";
            case 0:
                return "Ongoing";
            default:
                return "Ongoing";
        }
    }
}
