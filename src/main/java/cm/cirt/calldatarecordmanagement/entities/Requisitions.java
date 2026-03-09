/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Harry Wanki
 */
@Entity
@Table(name = "requisitions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Requisitions.findAll", query = "SELECT r FROM Requisitions r")
    ,
    @NamedQuery(name = "Requisitions.findById", query = "SELECT r FROM Requisitions r WHERE r.id = :id")
    ,
    @NamedQuery(name = "Requisitions.findByTelephones", query = "SELECT r FROM Requisitions r WHERE r.telephone = :telephone")
    ,
    @NamedQuery(name = "Requisitions.findByUser", query = "SELECT r FROM Requisitions r WHERE r.user = :user")
})
public class Requisitions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "telephone")
    private String telephone;
    @Basic(optional = false)
    @Column(name = "date_requisition")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRequisition;

    @JoinColumn(name = "user", referencedColumnName = "id")
    @ManyToOne
    private Users user;

    @Basic(optional = false)
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Basic(optional = false)
    @Column(name = "date_modification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;
    @Basic(optional = false)
    @Column(name = "version")
    private int version;
    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "signature")
    private String signature;
    @Basic(optional = false)
    @Column(name = "etat")
    private int etat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateRequisition() {
        return dateRequisition;
    }

    public void setDateRequisition(Date dateRequisition) {
        this.dateRequisition = dateRequisition;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Requisitions)) {
            return false;
        }
        Requisitions other = (Requisitions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cm.cirt.calldatarecordmanagement.entities.Requisitions[ id=" + id + " ]";
    }

    @PrePersist
    void onCreate() {
        this.setDateCreation(new Timestamp((new Date()).getTime()));
        this.setDateModification(new Timestamp((new Date()).getTime()));
        this.setVersion(1);
        this.setSignature("sig");
    }

    @PreUpdate
    void onUpdate() {
        this.setDateModification(new Timestamp((new Date()).getTime()));
        this.setVersion(this.getVersion() + 1);
        this.setSignature("sig");
    }
}
