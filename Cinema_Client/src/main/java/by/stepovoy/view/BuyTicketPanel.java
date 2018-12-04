package by.stepovoy.view;


import by.stepovoy.client.ClientThread;
import by.stepovoy.message.Message;
import by.stepovoy.message.MessageType;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
import by.stepovoy.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class BuyTicketPanel extends JDialog {

    private User user;
    private Seance seance;
    private JTextField tickets;
    private JLabel cost;
    private JButton buyButton;
    private double totalCost;
    private int totalTickets;
    private String ticketNumber = null;

    public BuyTicketPanel(JFrame parentFrame, User user, final Seance seance, boolean isModal) {
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

        ArrayList<JToggleButton> buttonList = new ArrayList<>();
        for (int i = 0; i < seance.getTicketsLeft(); i++) {
            buttonList.add(new JToggleButton("" + (i + 1)));
            mainPanel.add(buttonList.get(i));
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
                            ticketNumber = button.getText();
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
                            if (totalTickets < 1) {
                                buyButton.setEnabled(false);
                            } else {
                                buyButton.setEnabled(true);
                            }
                        }
                    }
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
        ticket.setSeatNumber(Integer.parseInt(ticketNumber));
        ticket.setSeanceID(seance.getID());
        ticket.setUserID(user.getID());
        ticket.setAmountTickets(totalTickets);
        ticket.setCost(totalCost);
        Message message = new Message();
        message.setOperationType(MessageType.ADD);
        message.setMessageType(MessageType.TICKET);
        message.setMessage(ticket);
        try {
            ClientThread.sendMessage(message);
            ClientThread.receiveMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispose();
    }

}
