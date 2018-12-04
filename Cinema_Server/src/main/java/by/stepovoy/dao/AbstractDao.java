package by.stepovoy.dao;

import by.stepovoy.IKey;
import by.stepovoy.MyException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractDao<T extends IKey> implements IGenericDao<T> {

    Logger logger = Logger.getLogger(String.valueOf(this.getClass()));
    private Connection connection;

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    abstract String getSelectQuery();

    abstract String getUpdateQuery();

    abstract String getDeleteQuery();

    abstract String getInsertQuery();

    abstract void prepareUpdateStatement(PreparedStatement statement, T object) throws MyException;

    abstract void prepareInsertStatement(PreparedStatement statement, T object) throws MyException;

    abstract List<T> parseResultSet(ResultSet resultSet) throws MyException;

    public T get(int id) throws MyException {
        logger.info("GET FROM  =========== > " + this.getClass().getSimpleName());
        List<T> list = null;
        String selectQuery = getSelectQuery() + " where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            list = parseResultSet(resultSet);
            System.out.println("IN GET\n" + Arrays.toString(list.toArray()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list == null || list.size() == 0) {
            return null;
        } else {
            if (list.size() > 1) {
                throw new MyException("A lot of records!");
            } else {
                return list.iterator().next();
            }
        }
    }

    public List<T> getBy(String argument, String value) throws MyException {
        logger.info("GET BY FROM  =========== > " + this.getClass().getSimpleName());
        List<T> list = null;

        String selectQuery = getSelectQuery() + " WHERE " + argument + " = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            list = parseResultSet(resultSet);
            System.out.println("IN GET BY\n" + Arrays.toString(list.toArray()));
        } catch (Exception e) {
            throw new MyException(e);
        }
        return list;
    }

    public List<T> getAll() throws MyException {
        logger.info("GET ALL FROM =========== >" + this.getClass().getSimpleName());
        List<T> list;
        String selectQuery = getSelectQuery();
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            list = parseResultSet(resultSet);
            System.out.println("IN GET ALL\n" + Arrays.toString(list.toArray()));
        } catch (Exception e) {
            throw new MyException(e);
        }
        return list;
    }

    public void update(T object) throws MyException {
        logger.info("UPDATE IN  =========== > " + this.getClass().getSimpleName());
        String updateQuery = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            prepareUpdateStatement(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new MyException("There are more than 1 record on update :" + count);
            }
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    public void delete(T object) throws MyException {
        logger.info("DELETE IN  =========== > " + this.getClass().getSimpleName());
        String deleteQuery = getDeleteQuery();
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, object.getID());
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new MyException("There are more than 1 record on delete :" + count);
            }
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    public T save(T object) throws MyException {
        logger.info("SAVING OBJECT  =========== >  " + this.getClass().getSimpleName());
        if (object.getID() != 0) {
            throw new MyException("Object is already save.");
        }
        System.out.println("AFTER SAVE ENTERING\n" + object);
        String createQuery = getInsertQuery();
        try (PreparedStatement preparedStatement = connection.prepareStatement(createQuery)) {
            prepareInsertStatement(preparedStatement, object);
            System.out.println("AFTER TRY\n" + object.toString());
            int count = preparedStatement.executeUpdate();
            if (count != 1) {
                throw new MyException("There are more than 1 record on saving :" + count);
            }
        } catch (Exception e) {
            throw new MyException(e);
        }
        createQuery = getSelectQuery() + " WHERE ID = last_insert_id();";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> list = parseResultSet(resultSet);
            System.out.println("IN GET\n" + Arrays.toString(list.toArray()));
            if (list.size() != 1) {
                throw new MyException("Cannot find new data");
            }
            System.out.println(list.iterator().next());
            return list.iterator().next();
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

}
