package by.stepovoy.dao;

import by.stepovoy.utils.MyException;

public interface IDaoCreator<Context> {
    IGenericDao createDao(Context context) throws MyException;
}
