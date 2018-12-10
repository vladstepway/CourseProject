package by.stepovoy.dao;

import by.stepovoy.utils.MyException;
import by.stepovoy.model.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FilmDaoFactoryImplement extends AbstractDaoFactory<Film> {


    public FilmDaoFactoryImplement(Connection connection) {
        super(connection);
    }

    @Override
    String getSelectQuery() {
        return SqlConstants.FILM_SELECT_ALL_QUERY;
    }

    @Override
    String getUpdateQuery() {
        return SqlConstants.FILM_UPDATE_QUERY;
    }

    @Override
    String getDeleteQuery() {
        return SqlConstants.FILM_DELETE_QUERY;
    }

    @Override
    String getInsertQuery() {
        return SqlConstants.FILM_INSERT_QUERY;
    }

    @Override
    void prepareUpdateStatement(PreparedStatement statement, Film object) throws MyException {
        try {
            logger.info("UPDATE TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            i = setFilmPreparedStatement(statement, object, i);
            statement.setInt(++i, object.getID());
        } catch (Exception e) {
            throw new MyException(e);
        }
    }

    private int setFilmPreparedStatement(PreparedStatement statement, Film object, int i) throws SQLException {
        statement.setString(++i, object.getName());
        statement.setInt(++i, object.getDuration());
        statement.setString(++i, object.getDescription());
        statement.setString(++i, object.getGenre());
        statement.setString(++i, object.getCountry());
        statement.setString(++i, object.getDirector());
        statement.setBoolean(++i, object.isShow3D());
        statement.setInt(++i, object.getAgeLimit());
        statement.setInt(++i, object.getYearProduction());
        return i;
    }

    @Override
    void prepareInsertStatement(PreparedStatement statement, Film object) throws MyException {
        try {
            logger.info("INSERT TO  =========== >  " + this.getClass().getSimpleName());
            int i = 0;
            setFilmPreparedStatement(statement, object, i);
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
