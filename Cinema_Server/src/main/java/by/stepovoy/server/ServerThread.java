package by.stepovoy.server;

import by.stepovoy.MyException;

import by.stepovoy.dao.DaoFactorySQL;
import by.stepovoy.dao.IGenericDao;

import by.stepovoy.message.Message;
import by.stepovoy.message.MessageType;

import by.stepovoy.model.Film;
import by.stepovoy.model.Hall;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;

import by.stepovoy.user.User;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ServerThread extends Thread {

    private Socket socket;
    private Connection connection;
    private DaoFactorySQL daoFactory;
    private java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(String.valueOf(this.getClass()));

    ServerThread(Socket socket) throws SQLException {
        this.socket = socket;
        daoFactory = new DaoFactorySQL();
        connection = daoFactory.getConnection();
    }

    @Override
    public void run() {
        System.out.println(socket.getInetAddress().getHostName() +
                " " + socket.getInetAddress() + " connected ");
        try {
            ObjectInput objectInput = new ObjectInputStream(socket.getInputStream());
            ObjectOutput objectOutput = new ObjectOutputStream(socket.getOutputStream());

            daoFactory = new DaoFactorySQL();
            IGenericDao dao = null;
            Message request;
            Message answer = null;
            User user;
            MessageType operationType;
            MessageType messageType;

            while (true) {
                request = (Message) objectInput.readObject();
                operationType = request.getOperationType();
                messageType = request.getMessageType();
                switch (operationType) {
                    case SIGN:
                        LOGGER.info("********* SIGN operation *********:");
                        switch (messageType) {
                            case UP:
                                LOGGER.info("-------- SIGN UP -------");
                                user = (User) request.getMessage();
                                if (daoFactory.isLoginFree(user)) {
                                    if (daoFactory.isEmailFree(user)) {
                                        answer = new Message();
                                        answer.setMessageType(MessageType.COMPLETE);
                                        dao = daoFactory.getDaoClass(connection, User.class);
                                        user = (User) dao.save(user);
                                        answer.setMessage(user);
                                        LOGGER.info("********* SUCCESSFUL REGISTRATION! *********");
                                    } else {
                                        answer = new Message();
                                        answer.setMessageType(MessageType.FAILED);
                                        answer.setMessage("Sign up failed");
                                        throw new MyException("Such email exists");
                                    }
                                } else {
                                    answer = new Message();
                                    answer.setMessageType(MessageType.FAILED);
                                    answer.setMessage("Sign up failed");
                                    throw new MyException("Such email exists");
                                }
                                break;
                            case IN:
                                LOGGER.info("--------- SIGN IN --------");
                                dao = daoFactory.getDaoClass(connection, User.class);
                                user = (User) request.getMessage();
                                List<User> userList = dao.getBy("login", user.getLogin());
                                if (userList.size() != 0) {
                                    User existingUser = userList.iterator().next();
                                    if (existingUser.getPassword().equals(user.getPassword())) {
                                        answer = new Message();
                                        answer.setMessageType(MessageType.COMPLETE);
                                        answer.setMessage(existingUser);
                                        LOGGER.info("********* SUCCESSFUL SIGN IN! *********");
                                        break;
                                    }
                                }
                                answer = new Message();
                                answer.setMessageType(MessageType.FAILED);
                                answer.setMessage("Sign in failed");
                                throw new MyException("Invalid password!");
                        }
                        break;
                    case ADD:
                        LOGGER.info("--------- ADD operation ---------");
                        switch (messageType) {
                            case FILM:
                                LOGGER.info(" ********* FILM ********* ");
                                dao = daoFactory.getDaoClass(connection, Film.class);
                                dao.save(request.getMessage());
                                answer = new Message();
                                answer.setMessage("film added");
                                break;
                            case HALL:
                                LOGGER.info("********* HALL *********");
                                dao = daoFactory.getDaoClass(connection, Hall.class);
                                dao.save(request.getMessage());
                                answer = new Message();
                                answer.setMessage("hall added");
                                break;
                            case SEANCE:
                                LOGGER.info("********* SEANCE *********");
                                dao = daoFactory.getDaoClass(connection, Seance.class);
                                System.out.println("ADDING SEANCE\n" + request.getMessage().toString());
                                System.out.println("ANSWER \n" + answer.getMessage().toString());
                                dao.save(request.getMessage());

                                answer = new Message();
                                answer.setMessage("seance added");
                                break;
                            case TICKET:
                                LOGGER.info("********* TICKET *********");
                                dao = daoFactory.getDaoClass(connection, Ticket.class);
                                dao.save(request.getMessage());
                                answer = new Message();
                                answer.setMessage("ticket added");
                                break;
                        }
                        break;
                    case DELETE:
                        LOGGER.info("-------- DELETE operation ---------");
                        int id;
                        switch (messageType) {
                            case FILM:
                                LOGGER.info("********* FILM *********");
                                dao = daoFactory.getDaoClass(connection, Film.class);
                                answer = new Message();
                                answer.setMessage("film deleted");
                                break;
                            case HALL:
                                LOGGER.info("********* HALL *********");
                                dao = daoFactory.getDaoClass(connection, Hall.class);
                                answer = new Message();
                                answer.setMessage("hall deleted");
                                break;
                            case SEANCE:
                                LOGGER.info("********* SEANCE *********");
                                dao = daoFactory.getDaoClass(connection, Seance.class);
                                answer = new Message();
                                answer.setMessage("seance deleted");
                                break;
                        }
                        id = (Integer) request.getMessage();
                        dao.delete(dao.get(id));
                        break;
                    case EDIT:
                        LOGGER.info("********* EDIT operation *********");
                        switch (messageType) {
                            case USER:
                                LOGGER.info("********* USER *********");
                                dao = daoFactory.getDaoClass(connection, User.class);
                                dao.update(request.getMessage());
                                answer = new Message();
                                answer.setMessage("user edited");
                                break;
                            case FILM:
                                LOGGER.info("********* FILM *********");
                                dao = daoFactory.getDaoClass(connection, Film.class);
                                dao.update(request.getMessage());
                                answer = new Message();
                                answer.setMessage("film edited");
                                break;

                            case HALL:
                                LOGGER.info("********* HALL *********");
                                dao = daoFactory.getDaoClass(connection, Hall.class);
                                dao.update(request.getMessage());
                                answer = new Message();
                                answer.setMessage("hall edited");
                                break;
                            case SEANCE:
                                LOGGER.info("********* SEANCE *********");
                                dao = daoFactory.getDaoClass(connection, Seance.class);
                                dao.update(request.getMessage());
                                answer = new Message();
                                answer.setMessage("seance edited");
                                break;
                        }
                        break;
                    case GET:
                        LOGGER.info("------- GET operation --------");
                        switch (messageType) {
                            case USER:
                                LOGGER.info("********* USER *********");
                                dao = daoFactory.getDaoClass(connection, User.class);
                                if (request.getMessage().equals("all")) {
                                    LOGGER.info(" ********* ALL USERS *********");
                                    answer = new Message();
                                    answer.setUserList(dao.getAll());
                                    answer.setMessage("all users");
                                } else {
                                    answer = new Message();
                                    answer.setMessage(dao.get((Integer) request.getMessage()));
                                }
                                break;
                            case FILM:
                                LOGGER.info("------- FILM --------");
                                dao = daoFactory.getDaoClass(connection, Film.class);
                                if (request.getMessage().equals("all")) {
                                    LOGGER.info("********* ALL FILMS *********");
                                    answer = new Message();
                                    answer.setFilmList(dao.getAll());
                                    answer.setMessage("all films");
                                } else {
                                    answer = new Message();
                                    answer.setMessage(dao.get((Integer) request.getMessage()));
                                }
                                break;
                            case HALL:
                                LOGGER.info("********* HALL *********");
                                dao = daoFactory.getDaoClass(connection, Hall.class);
                                if (request.getMessage().equals("all")) {
                                    LOGGER.info("********* ALL HALLS *********");
                                    answer = new Message();
                                    answer.setMessage("all halls");
                                    answer.setHallList(dao.getAll());
                                } else {
                                    answer = new Message();
                                    answer.setMessage(dao.get((Integer) request.getMessage()));
                                }
                                break;
                            case SEANCE:
                                LOGGER.info("********* SEANCE *********");
                                dao = daoFactory.getDaoClass(connection, Seance.class);
                                if (request.getMessage().equals("all")) {
                                    LOGGER.info("********* ALL SEANCES *********");
                                    answer = new Message();
                                    answer.setMessage("all seances");
                                    System.out.println("ALL SEANCES\n" + Arrays.toString(dao.getAll().toArray()));
                                    answer.setSeanceList(dao.getAll());
                                } else {
                                    answer = new Message();
                                    answer.setMessage(dao.get((Integer) request.getMessage()));
                                }
                                break;
                            case HALL_SEANCE:
                                LOGGER.info(" ********* HALL_SEANCE *********");
                                dao = daoFactory.getDaoClass(connection, Seance.class);
                                answer = new Message();
                                System.out.println("HALL SEANCE HERE\n" + answer);
                                answer.setMessage("all seances");
                                answer.setSeanceList(dao.getBy("hallID", String.valueOf(request.getMessage())));
                                break;
                            case FILM_SEANCE:
                                LOGGER.info("********* FILM_SEANCE *********");
                                dao = daoFactory.getDaoClass(connection, Seance.class);
                                answer = new Message();
                                System.out.println("HALL SEANCE HERE\n" + answer);
                                answer.setMessage("all seances");
                                answer.setSeanceList(dao.getBy("filmID", String.valueOf(request.getMessage())));
                                break;
                            case TICKET:
                                LOGGER.info("********* TICKETS *********");
                                dao = daoFactory.getDaoClass(connection, Ticket.class);
                                if (request.getMessage().equals("all")) {
                                    LOGGER.info("ALL TICKETS");
                                    answer = new Message();
                                    answer.setMessage("all tickets");
                                    answer.setTicketList(dao.getAll());
                                    break;
                                } else {
                                    answer = new Message();
                                    answer.setMessage("user tickets");
                                    answer.setTicketList(dao.getBy("userID", String.valueOf((Integer) request.getMessage())));
                                }
                        }
                        break;
                }
                objectOutput.writeObject(answer);
                //objectOutput.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Client disconnected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

