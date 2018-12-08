package by.stepovoy.dao;

import by.stepovoy.utils.MyException;
import by.stepovoy.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TicketDaoImplement extends AbstractDao<Ticket> {


    public TicketDaoImplement(Connection connection) {
        super(connection);
    }


    @Override
    String getSelectQuery() {
        return SqlConstants.TICKET_SELECT_ALL_QUERY;
    }

    @Override
    String getUpdateQuery() {
        return SqlConstants.TICKET_UPDATE_QUERY;
    }

    @Override
    String getDeleteQuery() {
        return SqlConstants.TICKET_DELETE_QUERY;
    }

    @Override
    String getInsertQuery() {
        return SqlConstants.TICKET_INSERT_QUERY;
    }


    @Override
    void prepareUpdateStatement(PreparedStatement statement, Ticket object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            i = setTicketPreparedStatement(statement, object, i);
            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    private int setTicketPreparedStatement(PreparedStatement statement, Ticket object, int i) throws SQLException {
        statement.setInt(++i, object.getSeanceID());
        statement.setInt(++i, object.getUserID());
        statement.setInt(++i, object.getAmountTickets());
        statement.setDouble(++i, object.getCost());
        statement.setInt(++i, object.getSeatNumber());
        return i;
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, Ticket object) throws MyException {
        try {
            logger.info("INSERT TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            setTicketPreparedStatement(statement, object, i);
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

