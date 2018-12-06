package by.stepovoy.dao;

import by.stepovoy.utils.MyException;
import by.stepovoy.model.Seance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SeanceDaoImplement extends AbstractDao<Seance> {


    public SeanceDaoImplement(Connection connection) {
        super(connection);
    }


    @Override
    String getSelectQuery() {
        return SqlConstants.SEANCE_SELECT_ALL_QUERY;
    }

    @Override
    String getUpdateQuery() {
        return SqlConstants.SEANCE_UPDATE_QUERY;
    }

    @Override
    String getDeleteQuery() {
        return SqlConstants.SEANCE_DELETE_QUERY;
    }

    @Override
    String getInsertQuery() {
        return SqlConstants.SEANCE_INSERT_QUERY;
    }


    @Override
    void prepareUpdateStatement(PreparedStatement statement, Seance object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            i = setSeancePreparedStatement(statement, object, i);
            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    private int setSeancePreparedStatement(PreparedStatement statement, Seance object, int i) throws SQLException {
        statement.setTime(++i, object.getSeanceTime());
        statement.setDate(++i, object.getSeanceDate());
        statement.setDouble(++i, object.getTicketCost());
        statement.setInt(++i, object.getTicketsLeft());
        return i;
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, Seance object) throws MyException {
        try {
            logger.info("INSERT TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            statement.setInt(++i, object.getHallID());
            statement.setInt(++i, object.getFilmID());
            setSeancePreparedStatement(statement, object, i);
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    List<Seance> parseResultSet(ResultSet resultSet) throws MyException {
        List<Seance> result = new LinkedList<>();
        try {
            logger.info("PARSE OF  =========== >  " + this.getClass().getSimpleName());
            while (resultSet.next()) {
                Seance seance = new Seance();
                seance.setID(resultSet.getInt("ID"));
                seance.setHallID(resultSet.getInt("hallID"));
                seance.setFilmID(resultSet.getInt("filmID"));
                seance.setSeanceTime(resultSet.getTime("seanceTime"));
                seance.setSeanceDate(resultSet.getDate("seanceDate"));
                seance.setTicketCost(resultSet.getDouble("ticketCost"));
                seance.setTicketsLeft(resultSet.getInt("ticketsLeft"));
                result.add(seance);
            }
        } catch (Exception e) {
            throw new MyException(e);
        }
        return result;
    }

    @Override
    public Seance create() throws MyException {
        logger.info("SAVING  =========== >  " + this.getClass().getSimpleName());
        return save(new Seance());
    }

}

