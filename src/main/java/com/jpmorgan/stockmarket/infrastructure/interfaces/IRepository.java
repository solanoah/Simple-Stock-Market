package com.jpmorgan.stockmarket.infrastructure.interfaces;

import com.jpmorgan.stockmarket.Exceptions.ItemExistException;

import java.util.Collection;

/**
 * Created by solanoah on 10/11/2016.
 */
public interface IRepository<T> {
    Collection GetAll();
    void Add(T t) throws ItemExistException;
    T GetSingle(String key);
}
