package by.stepovoy.dao;

import by.stepovoy.MyException;
import by.stepovoy.user.Role;
import by.stepovoy.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class UserDaoImplement extends AbstractDao<User> {

    private static final String TABLE_NAME = "cinema.user ";
    private final static String SELECT_ALL_QUERY = "select * from " + TABLE_NAME;
    private final static String INSERT_QUERY = "INSERT INTO " + TABLE_NAME +
            " (login, password, role, surname, name, email, birthday) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?;";
    private final static String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET login = ?, password = ?, role = ?, " +
            "surname = ?, name = ?, email = ?, birthday = ? WHERE ID = ?;";

    public UserDaoImplement(Connection connection) {
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
    void prepareUpdateStatement(PreparedStatement statement, User object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            statement.setString(++i, object.getLogin());
            statement.setString(++i, object.getPassword());
            statement.setString(++i, object.getRole().toString());
            statement.setString(++i, object.getSurname());
            statement.setString(++i, object.getName());
            statement.setString(++i, object.getEmail());
            statement.setDate(++i, object.getBirthdayDate());
            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, User object) throws MyException {
        try {
            logger.info("INSERT TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            statement.setString(++i, object.getLogin());
            statement.setString(++i, object.getPassword());
            statement.setString(++i, object.getRole().toString());
            statement.setString(++i, object.getSurname());
            statement.setString(++i, object.getName());
            statement.setString(++i, object.getEmail());
            statement.setDate(++i, object.getBirthdayDate());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    List<User> parseResultSet(ResultSet resultSet) throws MyException {
        List<User> result = new LinkedList<>();
        try {
            logger.info("PARSE OF  =========== >  " + this.getClass().getSimpleName());
            while (resultSet.next()) {
                User user = new User();
                String role = resultSet.getString("role");
                user.setRole(
                        (role.equals("User") ? Role.USER :
                                role.equals("Moder") ? Role.MODER : Role.ADMIN)
                );
                user.setID(resultSet.getInt("ID"));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setSurname(resultSet.getString("surname"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setBirthday(resultSet.getDate("birthday"));
                result.add(user);
            }
        } catch (Exception e) {
            throw new MyException(e);
        }
        return result;
    }

    @Override
    public User create() throws MyException {
        logger.info("SAVING  =========== >  " + this.getClass().getSimpleName());
        return save(new User());
    }
}
