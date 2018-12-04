package by.stepovoy.dao;

import by.stepovoy.MyException;
import by.stepovoy.model.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class FilmDaoImplement extends AbstractDao<Film> {
    private final static String TABLE_NAME = " cinema.film ";
    private final static String SELECT_ALL_QUERY = "select * from " + TABLE_NAME;
    private final static String INSERT_QUERY = "INSERT INTO " + TABLE_NAME +
            " (name, duration, description, genre, country, director, is3D, ageLimit, yearProduction) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final static String DELETE_QUERY = "delete from " + TABLE_NAME +
            " where ID = ?;";
    private final static String UPDATE_QUERY = "update " + TABLE_NAME +
            " set name = ?, duration = ?, description = ?, genre = ?," +
            " country = ?, director = ?, is3D = ?, ageLimit = ?, yearProduction = ? WHERE ID = ?;";

    public FilmDaoImplement(Connection connection) {
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
    void prepareUpdateStatement(PreparedStatement statement, Film object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            statement.setString(++i, object.getName());
            statement.setInt(++i, object.getDuration());
            statement.setString(++i, object.getDescription());
            statement.setString(++i, object.getGenre());
            statement.setString(++i, object.getCountry());
            statement.setString(++i, object.getDirector());
            statement.setBoolean(++i, object.isShow3D());
            statement.setInt(++i, object.getAgeLimit());
            statement.setInt(++i, object.getYearProduction());
            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, Film object) throws MyException {
        try {
            logger.info("INSERT TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            statement.setString(++i, object.getName());
            statement.setInt(++i, object.getDuration());
            statement.setString(++i, object.getDescription());
            statement.setString(++i, object.getGenre());
            statement.setString(++i, object.getCountry());
            statement.setString(++i, object.getDirector());
            statement.setBoolean(++i, object.isShow3D());
            statement.setInt(++i, object.getAgeLimit());
            statement.setInt(++i, object.getYearProduction());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    @Override
    List<Film> parseResultSet(ResultSet resultSet) throws MyException {
        List<Film> result = new LinkedList<>();
        try {
            logger.info("PARSE OF  =========== >  " + this.getClass().getSimpleName());
            while (resultSet.next()) {
                Film film = new Film();
                film.setName(resultSet.getString("name"));
                film.setID(resultSet.getInt("ID"));
                film.setDuration(resultSet.getInt("duration"));
                film.setDescription(resultSet.getString("description"));
                film.setGenre(resultSet.getString("genre"));
                film.setCountry(resultSet.getString("country"));
                film.setDirector(resultSet.getString("director"));
                film.setShow3D(resultSet.getBoolean("is3D"));
                film.setAgeLimit(resultSet.getInt("ageLimit"));
                film.setYearProduction(resultSet.getInt("yearProduction"));
                result.add(film);
            }
        } catch (Exception e) {
            throw new MyException(e);
        }
        return result;
    }

    @Override
    public Film create() throws MyException {
        logger.info("SAVE  =========== >  " + this.getClass().getSimpleName());
        return save(new Film());
    }

}
