package by.stepovoy.dao;

import by.stepovoy.MyException;
import by.stepovoy.model.Film;
import by.stepovoy.model.Hall;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
import by.stepovoy.user.User;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DaoFactorySQL implements IDaoFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/cinema?useUnicode=" +
            "true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=" +
            "false&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";


    private Map<Class, IDaoCreator> creators;

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public DaoFactorySQL() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Class[] classes = new Class[]{
                Film.class, Hall.class,
                Seance.class, User.class, Ticket.class
        };
        creators = new HashMap<>();
        for (final Class someClass : classes) {
            creators.put(someClass, new IDaoCreator() {
                @Override
                public IGenericDao createDao(Object o) throws MyException {
                    try {
                        Class<?> daoClass = Class.forName("by.stepovoy.dao." + someClass.getSimpleName() + "DaoImplement");
                        Constructor<?> constructor = daoClass.getConstructor(Connection.class);
                        Object daoObject = constructor.newInstance(connection);
                        return (IGenericDao) daoObject;
                    } catch (Exception e) {
                        throw new MyException(e);
                    }
                }
            });
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public IGenericDao getDaoClass(Connection connection, Class daoClass) throws MyException {
        IDaoCreator creator = creators.get(daoClass);
        if (creator == null) {
            throw new MyException("There is no object of : " + daoClass);
        }
        return creator.createDao(connection);
    }

//    public boolean isConnected() throws SQLException {
//        return connection.isValid(0);
//    }
//
//    public boolean isLoginFree(String login) throws SQLException {
//        statement = connection.createStatement();
//        resultSet = statement.executeQuery(findLogin(login));
//        return !resultSet.first();
//    }

    public boolean isLoginFree(User user) throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery(findLogin(user.getLogin()));
        return !resultSet.first();
    }

//    public boolean isEmailFree(String email) throws SQLException {
//        statement = connection.createStatement();
//        resultSet = statement.executeQuery(findEmail(email));
//        return !resultSet.first();
//    }

    public boolean isEmailFree(User user) throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery(findEmail(user.getEmail()));
        return !resultSet.first();
    }

//    public Map<Class, IDaoCreator> getCreators() {
//        return creators;
//    }

    public static String findLogin(String login) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM users WHERE Login = \'");
        sb.append(login);
        sb.append("\'");
        return sb.toString();
    }

    public static String findEmail(String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM users WHERE Email = \'");
        sb.append(email);
        sb.append("\'");
        return sb.toString();
    }

}
