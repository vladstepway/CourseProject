package by.stepovoy.dao;

import by.stepovoy.utils.MyException;

public interface IDaoCreator {
    IGenericDao createDao(Object object) throws MyException;
}
