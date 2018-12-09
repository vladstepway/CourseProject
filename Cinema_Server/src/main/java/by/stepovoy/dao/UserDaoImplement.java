package by.stepovoy.dao;

import by.stepovoy.utils.MyException;
import by.stepovoy.model.Role;
import by.stepovoy.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDaoImplement extends AbstractDao<User> {

    public UserDaoImplement(Connection connection) {
        super(connection);
    }


    @Override
    String getSelectQuery() {
        return SqlConstants.USER_SELECT_ALL_QUERY;
    }

    @Override
    String getUpdateQuery() {
        return SqlConstants.USER_UPDATE_QUERY;
    }

    @Override
    String getDeleteQuery() {
        return SqlConstants.USER_DELETE_QUERY;
    }

    @Override
    String getInsertQuery() {
        return SqlConstants.USER_INSERT_QUERY;
    }


    @Override
    void prepareUpdateStatement(PreparedStatement statement, User object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            i = setUserPreparedStatement(statement, object, i);
            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    private int setUserPreparedStatement(PreparedStatement statement, User object, int i) throws SQLException {
        statement.setString(++i, object.getLogin());
        statement.setString(++i, object.getPassword());
        statement.setString(++i, object.getRole().toString());
        statement.setString(++i, object.getSurname());
        statement.setString(++i, object.getName());
        statement.setString(++i, object.getEmail());
        statement.setDate(++i, object.getBirthdayDate());
        return i;
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, User object) throws MyException {
        try {
            logger.info("INSERT TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            setUserPreparedStatement(statement, object, i);
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
