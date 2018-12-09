package by.stepovoy.view;

import by.stepovoy.client.ClientThread;
import by.stepovoy.utils.Message;
import by.stepovoy.utils.MessageType;
import by.stepovoy.model.Film;
import by.stepovoy.model.Hall;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
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

public class TotalBuyHistoryPanel extends JFrame {

    private JFrame parentFrame;
    private JPanel mainPanel;

    public TotalBuyHistoryPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        parentFrame.setVisible(false);
        setTitle("Просмотр истории покупок");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showConfirmDialog(mainPanel,
                        "Вы действительно хотите выйти из программы?",
                        "Изменение роли",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setBounds(400, 200, 1000, 700);
        setResizable(false);
        List<Ticket> ticketList = null;
        try {
            ticketList = ClientThread.getAllTickets();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] columnNames = {"Пользователь", "Фильм", "Зал",
                "Дата", "Время", "Кол-во билетов", "Сумма заказа (BYN)"};
        tableModel.setColumnIdentifiers(columnNames);
        if (ticketList != null) {
            for (Ticket ticket : ticketList) {
                User user = null;
                Seance seance = null;
                Hall hall = null;
                Message message = new Message();
                message.setOperationType(MessageType.GET);
                message.setMessageType(MessageType.USER);
                message.setMessage(ticket.getUserID());
                try {
                    ClientThread.sendMessage(message);
                    user = (User) ClientThread.receiveMessage().getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                message = new Message();
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
                    hall = (Hall) ClientThread.receiveMessage().getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Object[] data = {
                        user != null ? user.getLogin() : null, filmName, hall != null ?
                        hall.getName()
                        : null, seance != null ? seance.getSeanceDate() : null, seance != null ?
                        seance.getSeanceTime() : null, ticket.getAmountTickets(), ticket.getCost()
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

        mainPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
                g2d.setColor(getBackground());
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(jScrollPane);
        setContentPane(mainPanel);
        JButton returnButton = new JButton("Назад");
        returnButton.setPreferredSize(new Dimension(250, 25));
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackActionPerformed(e);
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(returnButton);
        mainPanel.add(buttonsPanel);
    }

    public void goBackActionPerformed(ActionEvent e) {
        this.dispose();
        parentFrame.setVisible(true);
    }

}

