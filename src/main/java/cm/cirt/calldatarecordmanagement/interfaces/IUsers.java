/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.interfaces;

import cm.cirt.calldatarecordmanagement.entities.Users;

/**
 *
 * @author Harry Wanki
 */
public interface IUsers extends CrudInterface<Users> {

    public  Users getUserByLoginPassword(String login, String password);
}
