package by.stepovoy.dao;

import by.stepovoy.utils.MyException;
import by.stepovoy.model.Hall;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class HallDaoImplement extends AbstractDao<Hall> {

    public HallDaoImplement(Connection connection) {
        super(connection);
    }


    @Override
    String getSelectQuery() {
        return SqlConstants.HALL_SELECT_ALL_QUERY;
    }

    @Override
    String getUpdateQuery() {
        return SqlConstants.HALL_UPDATE_QUERY;
    }

    @Override
    String getDeleteQuery() {
        return SqlConstants.HALL_DELETE_QUERY;
    }

    @Override
    String getInsertQuery() {
        return SqlConstants.HALL_INSERT_QUERY;
    }

    @Override
    void prepareUpdateStatement(PreparedStatement statement, Hall object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  :" + this.getClass().getSimpleName());

            int i = 0;
            i = setPreparedStatementHall(statement, object, i);

            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    private int setPreparedStatementHall(PreparedStatement statement, Hall object, int i) throws SQLException {
        statement.setString(++i, object.getType());
        statement.setString(++i, object.getName());
        statement.setString(++i, object.getFloor());
        statement.setString(++i, object.getDescription());
        statement.setString(++i, object.getManagerPhone());
        statement.setInt(++i, object.getCapacity());
        return i;
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, Hall object) throws MyException {
        try {
            logger.info("INSERT TO  =========== > " + this.getClass().getSimpleName());
            int i = 0;
            setPreparedStatementHall(statement, object, i);
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
