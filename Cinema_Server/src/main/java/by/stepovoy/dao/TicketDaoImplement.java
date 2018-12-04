package by.stepovoy.dao;

import by.stepovoy.MyException;
import by.stepovoy.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class TicketDaoImplement extends AbstractDao<Ticket> {

    private static final String TABLE_NAME = " cinema.ticket ";
    private final static String SELECT_ALL_QUERY = "select * from " + TABLE_NAME;
    private final static String INSERT_QUERY = "INSERT INTO " + TABLE_NAME +
            " (seanceID, userID, amount, cost, seatNumber) " +
            "VALUES (?, ?, ?, ?, ?);";
    private final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?;";
    private final static String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET seanceID = ?," +
            " userID = ?, amount = ?, cost = ?, seatNumber = ? WHERE ID = ?;";

    public TicketDaoImplement(Connection connection) {
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
    void prepareUpdateStatement(PreparedStatement statement, Ticket object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            statement.setInt(++i, object.getSeanceID());
            statement.setInt(++i, object.getUserID());
            statement.setInt(++i, object.getAmountTickets());
            statement.setDouble(++i, object.getCost());
            statement.setInt(++i, object.getSeatNumber());
            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, Ticket object) throws MyException {
        try {
            logger.info("INSERT TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            statement.setInt(++i, object.getSeanceID());
            statement.setInt(++i, object.getUserID());
            statement.setInt(++i, object.getAmountTickets());
            statement.setDouble(++i, object.getCost());
            statement.setInt(++i, object.getSeatNumber());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    List<Ticket> parseResultSet(ResultSet resultSet) throws MyException {
        List<Ticket> result = new LinkedList<>();
        try {
            logger.info("PARSE OF  =========== >  " + this.getClass().getSimpleName());
            while (resultSet.next()) {
                Ticket ticket = new Ticket();
                ticket.setID(resultSet.getInt("ID"));
                ticket.setSeanceID(resultSet.getInt("seanceID"));
                ticket.setUserID(resultSet.getInt("userID"));
                ticket.setAmountTickets(resultSet.getInt("amount"));
                ticket.setCost(resultSet.getDouble("cost"));
                ticket.setSeatNumber(resultSet.getInt("seatNumber"));
                result.add(ticket);
            }
        } catch (Exception e) {
            throw new MyException(e);
        }
        return result;
    }

    @Override
    public Ticket create() throws MyException {
        logger.info("SAVING  =========== >  " + this.getClass().getSimpleName());
        return save(new Ticket());
    }

}

