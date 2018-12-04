package by.stepovoy.dao;

import by.stepovoy.MyException;

public interface IDaoCreator<Context> {
    IGenericDao createDao(Context context) throws MyException;
}
