/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.managers;

import cm.cirt.calldatarecordmanagement.entities.Requisitions;
import cm.cirt.calldatarecordmanagement.entities.Users;
import cm.cirt.calldatarecordmanagement.interfaces.IRequisitions;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Harry Wanki
 */
public class RequisitionsManager implements IRequisitions, Serializable {

    private final String PERSISTENCE_UNIT_NAME = "cm.cirt_TrackingSecuritySystem_war_1.0-SNAPSHOTPU";
    private EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
    private EntityTransaction transactionObj = em.getTransaction();

    @Override
    public List<Requisitions> getRequisitionseByUser(Users users) {
        List<Requisitions> allRequisitions = null;
        Query q = em.createNamedQuery("Requisitions.findByUser");
        q.setParameter("user", users);
        try {
            allRequisitions = q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return allRequisitions;
    }

    @Override
    public Requisitions getById(Long id, Class<Requisitions> type) {
        Requisitions requisition = null;
        Query q = em.createNamedQuery("Requisitions.findById");
        q.setParameter("id", id);
        try {
            requisition = (Requisitions) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return requisition;
    }

    @Override
    public List<Requisitions> getAll() {
        List<Requisitions> allRequisitions = null;
        Query q = em.createNamedQuery("Requisitions.findAll");
        try {
            allRequisitions = q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return allRequisitions;
    }

    @Override
    public Requisitions create(Requisitions entity) {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return entity;
    }

    @Override
    public void update(Requisitions entity) {
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {

        }
    }

    @Override
    public void delete(Requisitions entity) {
        try {
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {

        }
    }

}
