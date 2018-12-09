package by.stepovoy.utils;

import by.stepovoy.model.Hall;
import by.stepovoy.model.Film;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
import by.stepovoy.model.User;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

    private MessageType operationType;
    private MessageType messageType;
    private Object message;
    private List<User> userList;
    private List<Film> filmList;
    private List<Hall> hallList;
    private List<Seance> seanceList;
    private List<Ticket> ticketList;

    public Message() {
        setMessageType(MessageType.NULL);
    }

    public MessageType getOperationType() {
        return operationType;
    }

    public void setOperationType(MessageType operationType) {
        this.operationType = operationType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Film> getFilmList() {
        return filmList;
    }

    public void setFilmList(List<Film> filmList) {
        this.filmList = filmList;
    }

    public List<Hall> getHallList() {
        return hallList;
    }

    public void setHallList(List<Hall> hallList) {
        this.hallList = hallList;
    }

    public List<Seance> getSeanceList() {
        return seanceList;
    }

    public void setSeanceList(List<Seance> seanceList) {
        this.seanceList = seanceList;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }
}
