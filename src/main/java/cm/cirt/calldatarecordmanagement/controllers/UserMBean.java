/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.controllers;

import cm.cirt.calldatarecordmanagement.beans.Telephones;
import cm.cirt.calldatarecordmanagement.entities.Users;
import cm.cirt.calldatarecordmanagement.interfaces.IUsers;
import cm.cirt.calldatarecordmanagement.managers.RequisitionsManager;
import cm.cirt.calldatarecordmanagement.managers.UsersManager;
import cm.cirt.calldatarecordmanagement.metier.Security;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Harry Wanki
 */
@ManagedBean
@javax.faces.bean.SessionScoped
public class UserMBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private IUsers usersManager;

    private Users user;
    
    /**
     * Creates a new instance of UserMBean
     */
    public UserMBean() {
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    @PostConstruct
    public void init() {
        this.usersManager = new UsersManager();
        this.user = new Users();
    }
    
    public void createUser() {
        FacesMessage msg = null;
        this.user.setPassword(Security.md5(this.user.getPassword()));
        Users u = this.usersManager.create(this.user);
        if (u != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "User created successfully."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", "User creation failed."));
        }
        this.user = new Users();
    }
}
