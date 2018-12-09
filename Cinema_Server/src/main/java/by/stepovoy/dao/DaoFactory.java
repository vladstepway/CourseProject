package by.stepovoy.dao;

import by.stepovoy.model.Film;
import by.stepovoy.model.Hall;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
import by.stepovoy.model.User;
import by.stepovoy.utils.MyException;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DaoFactory implements IDaoFactory {

    private Map<Class, IDaoCreator> creators;

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public DaoFactory() throws SQLException, IOException, ClassNotFoundException {


        Properties props = new Properties();
        FileInputStream in = new FileInputStream("src/main/resources/database-config.properties");
        props.load(in);
        in.close();

        String driver = props.getProperty("jdbc.driver");
        if (driver != null) {
            Class.forName(driver);
        }

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");


        connection = DriverManager.getConnection(url, username, password);

        try {
            Class.forName(driver);
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
                public IGenericDao createDao(Object object) throws MyException {
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

    public boolean isLoginFree(User user) throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery(findLogin(user.getLogin()));
        return !resultSet.first();
    }

    public boolean isEmailFree(User user) throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery(findEmail(user.getEmail()));
        return !resultSet.first();
    }


    public static String findLogin(String login) {
        return "select * from users where Login = " + login;
    }

    public static String findEmail(String email) {
        return "select * from users where Email = " + email;
    }

}
