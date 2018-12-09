package by.stepovoy.view;


import by.stepovoy.client.ClientThread;
import by.stepovoy.utils.DateValidator;
import by.stepovoy.utils.Message;
import by.stepovoy.utils.MessageType;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
import by.stepovoy.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class BuyTicketPanel extends JDialog {

    private User user;
    private Seance seance;
    private JTextField tickets;
    private JLabel cost;
    private JButton buyButton;
    private double totalCost;
    private int totalTickets;
    private String ticketNumber = "";

    public BuyTicketPanel(JFrame parentFrame, User user, final Seance seance, boolean isModal) throws IOException, ClassNotFoundException {
        super(parentFrame, isModal);
        setLocationRelativeTo(null);
        this.user = user;
        this.seance = seance;
        setBounds(550, 600, 450, 400);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        setContentPane(mainPanel);

        setTitle("Покупка билетов");
        List<String> numbersList = new LinkedList<>();
        String delimiter = ", ";
        String[] seats;
        String str = "";
        List<Ticket> ticketList = ClientThread.getAllTickets();
        for (Ticket ticket : ticketList) {
            if (ticket.getSeanceID() == seance.getID()) {
                str = ticket.getSeatNumber();
            }
        }
        seats = str.split(delimiter);
        List<String> soldTickets = new LinkedList<>(Arrays.asList(seats));

        ArrayList<JToggleButton> buttonList = new ArrayList<>();
        for (String string : soldTickets) {
            for (int i = 0; i < 40; i++) {
                buttonList.add(new JToggleButton("" + (i + 1)));
                mainPanel.add(buttonList.get(i));
                if (string.equals(buttonList.get(i).getText())) {
                    buttonList.get(i).setEnabled(false);
                }
            }
        }

        cost = new JLabel("Общая стоимость: 0 BYN");
        cost.setHorizontalAlignment(SwingConstants.CENTER);
        tickets = new JTextField();
        tickets.setPreferredSize(new Dimension(80, 35));
        tickets.setEditable(false);
        tickets.setHorizontalAlignment(JTextField.CENTER);
        tickets.setText("0");

        totalTickets = 0;
        totalCost = 0;

        for (final JToggleButton button : buttonList) {
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    if (e.getSource() == button && button.isSelected()) {
                        button.setSelected(true);
                        if (totalTickets < seance.getTicketsLeft()) {
                            totalTickets++;
                            totalCost += seance.getTicketCost();
                            tickets.setText(String.valueOf(totalTickets));
                            cost.setText("Общая стоимость: " + totalCost + " BYN");

                            numbersList.add(button.getText());

                            if (totalTickets == seance.getTicketsLeft() - 1) {
                                buyButton.setEnabled(false);
                            } else {
                                buyButton.setEnabled(true);
                            }
                        }
                    } else {
                        if (totalTickets > 0) {
                            totalTickets--;
                            totalCost -= seance.getTicketCost();
                            tickets.setText(String.valueOf(totalTickets));
                            cost.setText("Общая стоимость: " + totalCost + " BYN");
                            numbersList.remove(numbersList.size() - 1);
                            if (totalTickets < 1) {
                                buyButton.setEnabled(false);
                            } else {
                                buyButton.setEnabled(true);
                            }
                        }
                    }
                    String delimiter = ", ";
                    ticketNumber = String.join(delimiter, numbersList);
                    System.out.println(ticketNumber);

                }
            });

        }


        JPanel costPanel = new JPanel();
        costPanel.setLayout(new FlowLayout());
        costPanel.add(tickets);

        buyButton = new JButton("ОК");
        buyButton.setPreferredSize(new Dimension(80, 25));
        buyButton.setEnabled(false);
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyActionPerformed();
            }
        });
        JButton cancelButton = new JButton("Отмена");
        cancelButton.setPreferredSize(new Dimension(80, 25));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelActionPerformed();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(buyButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(costPanel);
        mainPanel.add(cost);
        mainPanel.add(buttonPanel);
    }


    private void cancelActionPerformed() {
        dispose();
    }

    private void buyActionPerformed() {
        Ticket ticket = new Ticket();
        ticket.setSeatNumber(ticketNumber);
        ticket.setSeanceID(seance.getID());
        seance.setTicketsLeft(seance.getTicketsLeft() - totalTickets);
        ticket.setUserID(user.getID());
        ticket.setAmountTickets(totalTickets);
        ticket.setCost(totalCost);
        Date currentDate = new Date();

        if (currentDate.before(seance.getSeanceDate()) ||
                currentDate.equals(seance.getSeanceDate())) {
            ticket.setValid(true);
        } else {
            ticket.setValid(false);
        }

        Message message = new Message();
        message.setOperationType(MessageType.ADD);
        message.setMessageType(MessageType.TICKET);
        message.setMessage(ticket);
        try {
            ClientThread.sendMessage(message);
            ClientThread.receiveMessage();
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        message = new

                Message();
        message.setOperationType(MessageType.UPDATE);
        message.setMessageType(MessageType.SEANCE);
        message.setMessage(seance);
        try {
            ClientThread.sendMessage(message);
            ClientThread.receiveMessage();
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        dispose();
    }

}
