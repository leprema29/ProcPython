/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.managers;

import cm.cirt.calldatarecordmanagement.entities.Users;
import cm.cirt.calldatarecordmanagement.interfaces.IUsers;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Harry Wanki
 */
public class UsersManager implements IUsers {

    private final String PERSISTENCE_UNIT_NAME = "cm.cirt_TrackingSecuritySystem_war_1.0-SNAPSHOTPU";
    private EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
    private EntityTransaction transactionObj = em.getTransaction();

    @Override
    public Users getById(Long id, Class<Users> type) {
        Users user = null;
        Query q = em.createNamedQuery("Users.findById");
        q.setParameter("id", id);
        try {
            user = (Users) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

    @Override
    public List<Users> getAll() {
        List<Users> allUsers = null;
        Query q = em.createNamedQuery("Users.findAll");
        try {
            allUsers = q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return allUsers;
    }

    @Override
    public Users create(Users entity) {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception ex) {
            return null;
        }
        return entity;
    }

    @Override
    public void update(Users entity) {
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception ex) {

        }
    }

    @Override
    public void delete(Users entity) {
        try {
            em.remove(entity);
        } catch (Exception ex) {

        }
    }

    @Override
    public Users getUserByLoginPassword(String login, String password) {
        Users user = null;
        Query q = em.createNamedQuery("Users.authentication");
        q.setParameter("login", login);
        q.setParameter("password", password);
        try {
            user = (Users) q.getSingleResult();
        } catch (Exception ex) {
            user = null;
        }
        return user;
    }
}
