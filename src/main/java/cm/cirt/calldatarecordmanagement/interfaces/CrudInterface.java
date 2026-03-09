/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.cirt.calldatarecordmanagement.interfaces;

import java.util.List;

/**
 *
 * @author Harry Wanki
 * @param <T>
 */
public interface CrudInterface<T> {

    public T getById(Long id, Class<T> type);

    public List<T> getAll();

    public T create(T entity);

    public void update(T entity);

    public void delete(T entity);
}
