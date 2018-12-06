package by.stepovoy.dao;

import by.stepovoy.utils.MyException;

import java.sql.Connection;

public interface IDaoFactory {
    IGenericDao getDaoClass(Connection connection, Class daoClass) throws MyException;
}