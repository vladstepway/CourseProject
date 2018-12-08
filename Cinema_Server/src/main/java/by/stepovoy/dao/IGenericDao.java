package by.stepovoy.dao;

import by.stepovoy.utils.MyException;

import java.util.List;

public interface IGenericDao<T> {
    T create() throws MyException;

    void update(T object) throws MyException;

    void delete(T object) throws MyException;

    T save(T object) throws MyException;

    T get(int id) throws Exception;

    List<T> getAll() throws MyException;

    List<T> getBy(String criteria, String value) throws MyException;


}


