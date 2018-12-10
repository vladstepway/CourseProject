package by.stepovoy.view;

import by.stepovoy.client.ClientThread;
import by.stepovoy.utils.Message;
import by.stepovoy.utils.MessageType;
import by.stepovoy.model.*;
import by.stepovoy.model.User;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class UserBuyHistoryPanel extends JFrame {

    private JFrame parentFrame;
    private JPanel mainPanel;

    public UserBuyHistoryPanel(JFrame parentFrame, User user) {
        this.parentFrame = parentFrame;
        setLocationRelativeTo(null);
        parentFrame.setVisible(false);
        setTitle("Просмотр истории покупок " + user.getLogin());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showConfirmDialog(mainPanel,
                        "Вы действительно хотите выйти из программы?",
                        "Выход",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setBounds(400, 200, 800, 400);
        setResizable(false);
        List<Ticket> ticketList = null;
        try {
            ticketList = ClientThread.getUserTickets(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] columnNames = {"Фильм", "Зал", "Дата",
                "Время","Место", "Кол-во билетов", "Сумма заказа (BYN)", "Действительность"};
        tableModel.setColumnIdentifiers(columnNames);
        if (ticketList != null) {
            for (Ticket ticket : ticketList) {
                Seance seance = null;
                Film film = null;
                Hall hall = null;
                Message message = new Message();
                message.setOperationType(MessageType.GET);
                message.setMessageType(MessageType.SEANCE);
                message.setMessage(ticket.getSeanceID());
                try {
                    ClientThread.sendMessage(message);
                    seance = (Seance) ClientThread.receiveMessage().getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message = new Message();
                message.setOperationType(MessageType.GET);
                message.setMessageType(MessageType.FILM);
                if (seance != null) {
                    message.setMessage(seance.getFilmID());
                }
                String filmName = null;
                try {
                    ClientThread.sendMessage(message);
                    if (message.getMessageType() == MessageType.FILM) {
                        filmName = ((Film) ClientThread.receiveMessage().getMessage()).getName();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message = new Message();
                message.setOperationType(MessageType.GET);
                message.setMessageType(MessageType.HALL);
                if (seance != null) {
                    message.setMessage(seance.getHallID());
                }
                try {
                    ClientThread.sendMessage(message);
                    message = ClientThread.receiveMessage();
                    hall = (Hall) message.getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Object[] data = {
                        filmName, hall != null ? hall.getName() : null, seance != null ?
                        seance.getSeanceDate() : null,
                        seance != null ? seance.getSeanceTime() : null, ticket.getSeatNumber(), ticket.getAmountTickets(), ticket.getCost(), ticket.isValid()
                };
                tableModel.addRow(data);
            }
        }
        JTable table = new JTable(tableModel);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setPreferredSize(new Dimension(1000, 700));
        jScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(jScrollPane);
        setContentPane(mainPanel);
        JButton goBackButton = new JButton("Назад");
        goBackButton.setPreferredSize(new Dimension(250, 25));
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackActionPerformed(e);
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(goBackButton);
        mainPanel.add(buttonsPanel);
    }

    public void goBackActionPerformed(ActionEvent e) {
        this.dispose();
        parentFrame.setVisible(true);
    }

}

