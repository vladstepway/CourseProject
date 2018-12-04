package by.stepovoy.client;

import by.stepovoy.message.Message;
import by.stepovoy.message.MessageType;
import by.stepovoy.model.Film;
import by.stepovoy.model.Hall;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
import by.stepovoy.user.User;
import by.stepovoy.view.MenuPanel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread {

    private static ObjectOutput objectOutput;
    private static ObjectInput objectInput;
    private Socket socket;
    private MessageType messageType;
    private MessageType operationType;
    private User user;
    private JFrame mainFrame;
    private JFrame parentFrame;
    private Object[] additions;
    private int port;

    public ClientThread(MessageType operationType, MessageType messageType, User user,
                        JFrame mainFrame, JFrame parentFrame, int port, Object... additions) {
        this.operationType = operationType;
        this.messageType = messageType;
        this.user = user;

        this.mainFrame = mainFrame;
        this.parentFrame = parentFrame;
        this.additions = additions;

        Socket socket;
        try {
            socket = new Socket(InetAddress.getLocalHost(), port);
            if (objectOutput == null) {
                objectOutput = new ObjectOutputStream(socket.getOutputStream());
            }
            if (objectInput == null) {
                objectInput = new ObjectInputStream(socket.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressFBWarnings({"UUF_UNUSED_FIELD", "REC_CATCH_EXCEPTION"})
    @Override
    public void run() {
        try {
            Message message = new Message();
            message.setMessageType(messageType);
            message.setOperationType(operationType);
            message.setMessage(user);
            objectOutput.writeObject(message);
            Message messageFromServer;

            switch (operationType) {
                case SIGN: {
                    switch (messageType) {
                        case UP: {
                            messageFromServer = (Message) objectInput.readObject();
                            if (messageFromServer.getMessageType() == MessageType.COMPLETE) {
                                JOptionPane.showMessageDialog(parentFrame,
                                        "Вы были успешно зарегистрированы!",
                                        "Регистрация произведена",
                                        JOptionPane.INFORMATION_MESSAGE);
                                parentFrame.dispose();
                                MenuPanel menuPanel = new MenuPanel(mainFrame, (User) messageFromServer.getMessage());
                                menuPanel.setVisible(true);
                            } else {
//                                System.out.println(incomingMsg.getMessageType());
                                JOptionPane.showMessageDialog(parentFrame,
                                        "Введённый логин уже зарегистрирован. Пожалуйста, выберите другой.",
                                        "Ошибка регистрации",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        }
                        case IN: {
                            messageFromServer = (Message) objectInput.readObject();
//                            System.out.println(message.getMessage().toString());
                            if (messageFromServer.getMessageType() == MessageType.COMPLETE) {
                                for (Object field : additions) {
                                    ((JTextField) field).setBorder(BorderFactory.createLineBorder(Color.GREEN));
                                }
                                JOptionPane.showMessageDialog(parentFrame,
                                        "Вход был успешно произведён!",
                                        "Вход произведён",
                                        JOptionPane.INFORMATION_MESSAGE);
                                parentFrame.dispose();
                                MenuPanel menuPanel = new MenuPanel(mainFrame, (User) messageFromServer.getMessage());
                                menuPanel.setVisible(true);
                            } else {
                                for (Object field : additions) {
                                    ((JTextField) field).setBorder(BorderFactory.createLineBorder(Color.RED));
                                }
                                JOptionPane.showMessageDialog(parentFrame,
                                        "Логин и/или пароль введены неверно",
                                        "Ошибка авторизации",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                default:
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame,
                    "Сервер недоступен.",
                    "Ошибка подключения к серверу",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void sendMessage(Object message) throws IOException {
        objectOutput.writeObject(message);
        objectOutput.flush();
    }

    public static Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) objectInput.readObject();
    }

    public static List<User> getAllUsers() throws IOException, ClassNotFoundException {
        Message message = new Message();
        message.setOperationType(MessageType.GET);
        message.setMessageType(MessageType.USER);
        message.setMessage("all");
        objectOutput.writeObject(message);
        objectOutput.flush();
        message = (Message) objectInput.readObject();
//        System.out.println(message.getMessage().toString());
        return message.getUserList();
    }

    public static List<Seance> getAllSeances() throws IOException, ClassNotFoundException {
        Message message = new Message();
        message.setOperationType(MessageType.GET);
        message.setMessageType(MessageType.SEANCE);
        message.setMessage("all");
        objectOutput.writeObject(message);
        objectOutput.flush();
        message = (Message) objectInput.readObject();
        return message.getSeanceList();
    }


    public static List<Film> getAllFilms() throws IOException, ClassNotFoundException {
        Message message = new Message();
        message.setOperationType(MessageType.GET);
        message.setMessageType(MessageType.FILM);
        message.setMessage("all");
        objectOutput.writeObject(message);
        objectOutput.flush();
        message = (Message) objectInput.readObject();
//        System.out.println(message.getMessage().toString());
        return message.getFilmList();
    }

//    public static List<Session> getInstitutionSessions(Institution institution) throws IOException, ClassNotFoundException {
//        Message message = new Message();
//        message.setOperationType(MessageType.GET);
//        message.setMessageType(MessageType.INSTITUTION_SESSION);
//        message.setMessage(institution.getID());
//        objectOutput.writeObject(message);
//        objectOutput.flush();
//        message = (Message) objectInput.readObject();
////        System.out.println(message.getMessage().toString());
//        return message.getSeanceList();
//    }

    public static List<Seance> getFilmSeance(Film film) throws IOException, ClassNotFoundException {
        Message message = new Message();
        message.setOperationType(MessageType.GET);
        message.setMessageType(MessageType.FILM_SEANCE);
        message.setMessage(film.getID());
        objectOutput.writeObject(message);
        objectOutput.flush();
        message = (Message) objectInput.readObject();
        //System.out.println(message.getMessage().toString());
        List<Seance> seanceList = message.getSeanceList();
        List<Seance> resultList = new ArrayList<>();
        for (Seance seance : seanceList) {
            System.out.println(seance);
            if (seance.getFilmID() == film.getID()) {
                resultList.add(seance);
            }
        }
        return resultList;
    }

    public static List<Hall> getAllHalls() throws IOException, ClassNotFoundException {
        Message message = new Message();
        message.setOperationType(MessageType.GET);
        message.setMessageType(MessageType.HALL);
        message.setMessage("all");
        objectOutput.writeObject(message);
        objectOutput.flush();
        message = (Message) objectInput.readObject();
        System.out.println(message.getMessage().toString());
        return message.getHallList();
    }

    public static List<Ticket> getAllTickets() throws IOException, ClassNotFoundException {
        Message message = new Message();
        message.setOperationType(MessageType.GET);
        message.setMessageType(MessageType.TICKET);
        message.setMessage("all");
        objectOutput.writeObject(message);
        objectOutput.flush();
        message = (Message) objectInput.readObject();
//        System.out.println(message.getMessage().toString());
        return message.getTicketList();
    }

    public static List<Ticket> getTicketsOf(User user) throws IOException, ClassNotFoundException {
        Message message = new Message();
        message.setOperationType(MessageType.GET);
        message.setMessageType(MessageType.TICKET);
        message.setMessage(user.getID());
        objectOutput.writeObject(message);
        objectOutput.flush();
        message = (Message) objectInput.readObject();
        return message.getTicketList();
    }

}

