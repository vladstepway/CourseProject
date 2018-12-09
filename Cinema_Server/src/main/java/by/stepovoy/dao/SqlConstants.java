package by.stepovoy.dao;

final class SqlConstants {

    final static String FILM_SELECT_ALL_QUERY = "select * from  cinema.film";
    final static String FILM_INSERT_QUERY = "INSERT INTO cinema.film" +
            " (name, duration, description, genre, country, director, is3D, ageLimit, yearProduction) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    final static String FILM_DELETE_QUERY = "delete from cinema.film " +
            " where ID = ?;";
    final static String FILM_UPDATE_QUERY = "update cinema.film " +
            " set name = ?, duration = ?, description = ?, genre = ?," +
            " country = ?, director = ?, is3D = ?, ageLimit = ?, yearProduction = ? WHERE ID = ?;";

    final static String HALL_SELECT_ALL_QUERY = "select * from cinema.hall";
    final static String HALL_INSERT_QUERY = "insert into cinema.hall" +
            " (type, name, floor, description, managerPhone, capacity)" +
            "VALUES (?, ?, ?, ?, ?, ?);";
    final static String HALL_DELETE_QUERY = "delete from cinema.hall WHERE ID = ?;";
    final static String HALL_UPDATE_QUERY = "update cinema.hall set type = ?, name = ?, floor = ?, description = ?, " +
            "managerPhone = ?, capacity = ? WHERE ID = ?;";

    final static String SEANCE_SELECT_ALL_QUERY = "select * from cinema.seance";
    final static String SEANCE_INSERT_QUERY = "INSERT INTO cinema.seance" +
            " (hallID, filmID, seanceTime, seanceDate, ticketCost, ticketsLeft)" +
            "VALUES (?, ?, ?, ?, ?, ?);";
    final static String SEANCE_DELETE_QUERY = "DELETE FROM cinema.seance WHERE ID = ?;";
    final static String SEANCE_UPDATE_QUERY = "UPDATE cinema.seance SET sessionTime = ?," +
            " sessionDate = ?, ticketCost = ?, ticketsLeft = ? WHERE ID = ?;";

    final static String TICKET_SELECT_ALL_QUERY = "select * from cinema.ticket";
    final static String TICKET_INSERT_QUERY = "INSERT INTO cinema.ticket" +
            " (seanceID, userID, amount, cost, seatNumber, valid) " +
            "VALUES (?, ?, ?, ?, ?, ?);";
    final static String TICKET_DELETE_QUERY = "DELETE FROM cinema.ticket WHERE ID = ?;";
    final static String TICKET_UPDATE_QUERY = "UPDATE cinema.ticket SET seanceID = ?," +
            " userID = ?, amount = ?, cost = ?, seatNumber = ?, valid = ? WHERE ID = ?;";

    final static String USER_SELECT_ALL_QUERY = "select * from cinema.user";
    final static String USER_INSERT_QUERY = "INSERT INTO cinema.user" +
            " (login, password, role, surname, name, email, birthday) VALUES (?, ?, ?, ?, ?, ?, ?);";
    final static String USER_DELETE_QUERY = "DELETE FROM cinema.user WHERE ID = ?;";
    final static String USER_UPDATE_QUERY = "UPDATE cinema.user SET login = ?, password = ?, role = ?, " +
            "surname = ?, name = ?, email = ?, birthday = ? WHERE ID = ?;";

}


