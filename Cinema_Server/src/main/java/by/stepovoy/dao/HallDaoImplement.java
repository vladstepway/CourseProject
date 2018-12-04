package by.stepovoy.dao;

import by.stepovoy.MyException;
import by.stepovoy.model.Hall;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class HallDaoImplement extends AbstractDao<Hall> {

    private static final String TABLE_NAME = " cinema.hall ";
    private final static String SELECT_ALL_QUERY = "select * from " + TABLE_NAME;
    private final static String INSERT_QUERY = "insert into " + TABLE_NAME +
            " (type, name, floor, description, managerPhone, capacity)" +
            "VALUES (?, ?, ?, ?, ?, ?);";
    private final static String DELETE_QUERY = "delete from " + TABLE_NAME + " WHERE ID = ?;";
    private final static String UPDATE_QUERY = "update " + TABLE_NAME + " set type = ?, name = ?, floor = ?, description = ?, " +
            "managerPhone = ?, capacity = ? WHERE ID = ?;";

    public HallDaoImplement(Connection connection) {
        super(connection);
    }


    @Override
    String getSelectQuery() {
        return SELECT_ALL_QUERY;
    }

    @Override
    String getUpdateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    String getDeleteQuery() {
        return DELETE_QUERY;
    }

    @Override
    String getInsertQuery() {
        return INSERT_QUERY;
    }

    @Override
    void prepareUpdateStatement(PreparedStatement statement, Hall object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  :" + this.getClass().getSimpleName());
            int i = 0;
            statement.setString(++i, object.getType());
            statement.setString(++i, object.getName());
            statement.setString(++i, object.getFloor());
            statement.setString(++i, object.getDescription());
            statement.setString(++i, object.getManagerPhone());
            statement.setInt(++i, object.getCapacity());
            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, Hall object) throws MyException {
        try {
            logger.info("INSERT TO  =========== > " + this.getClass().getSimpleName());
            int i = 0;
            statement.setString(++i, object.getType());
            statement.setString(++i, object.getName());
            statement.setString(++i, object.getFloor());
            statement.setString(++i, object.getDescription());
            statement.setString(++i, object.getManagerPhone());
            statement.setInt(++i, object.getCapacity());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    List<Hall> parseResultSet(ResultSet resultSet) throws MyException {
        List<Hall> result = new LinkedList<>();
        try {
            logger.info("PARSE OF  =========== >  :" + this.getClass().getSimpleName());
            while (resultSet.next()) {
                Hall hall = new Hall();
                hall.setID(resultSet.getInt("ID"));
                hall.setType(resultSet.getString("type"));
                hall.setName(resultSet.getString("name"));
                hall.setFloor(resultSet.getString("floor"));
                hall.setDescription(resultSet.getString("description"));
                hall.setManagerPhone(resultSet.getString("managerPhone"));
                hall.setCapacity(resultSet.getInt("capacity"));
                System.out.println("IN PARSE OF\n" + hall);
                result.add(hall);
            }
        } catch (Exception e) {
            throw new MyException(e);
        }
        return result;
    }

    @Override
    public Hall create() throws MyException {
        logger.info("SAVING OF  =========== > " + this.getClass().getSimpleName());
        return save(new Hall());
    }

}
