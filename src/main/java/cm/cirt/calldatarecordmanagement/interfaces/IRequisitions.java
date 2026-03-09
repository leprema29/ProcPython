/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.interfaces;

import cm.cirt.calldatarecordmanagement.entities.Requisitions;
import cm.cirt.calldatarecordmanagement.entities.Users;
import java.util.List;

/**
 *
 * @author Harry Wanki
 */
public interface IRequisitions extends CrudInterface<Requisitions>{
    public List<Requisitions> getRequisitionseByUser(Users users);
}
