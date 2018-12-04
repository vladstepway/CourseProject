package by.stepovoy.dao;

import by.stepovoy.MyException;

import java.sql.Connection;

public interface IDaoFactory {
    IGenericDao getDaoClass(Connection connection, Class daoClass) throws MyException;
}