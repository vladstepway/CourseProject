package by.stepovoy.dao;

import by.stepovoy.MyException;
import by.stepovoy.model.Seance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class SeanceDaoImplement extends AbstractDao<Seance> {

    private static final String TABLE_NAME = " cinema.seance ";
    private final static String SELECT_ALL_QUERY = "select * from " + TABLE_NAME;
    private final static String INSERT_QUERY = "INSERT INTO " + TABLE_NAME +
            " (hallID, filmID, seanceTime, seanceDate, ticketCost, ticketsLeft)" +
            "VALUES (?, ?, ?, ?, ?, ?);";
    private final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?;";
    private final static String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET sessionTime = ?," +
            " sessionDate = ?, ticketCost = ?, ticketsLeft = ? WHERE ID = ?;";

    public SeanceDaoImplement(Connection connection) {
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
    void prepareUpdateStatement(PreparedStatement statement, Seance object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            statement.setTime(++i, object.getSeanceTime());
            statement.setDate(++i, object.getSeanceDate());
            statement.setDouble(++i, object.getTicketCost());
            statement.setInt(++i, object.getTicketsLeft());
            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, Seance object) throws MyException {
        try {
            logger.info("INSERT TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            statement.setInt(++i, object.getHallID());
            statement.setInt(++i, object.getFilmID());
            statement.setTime(++i, object.getSeanceTime());
            statement.setDate(++i, object.getSeanceDate());
            statement.setDouble(++i, object.getTicketCost());
            statement.setInt(++i, object.getTicketsLeft());
            System.out.println("INSERT TO\n" + object);
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
                System.out.println("PARSE OF SEANCE\n " + seance.toString());
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
        System.out.println("SEANCE SAVE " + this.toString());
        return save(new Seance());
    }

}

