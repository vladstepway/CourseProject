package by.stepovoy.view;

import by.stepovoy.client.ClientThread;
import by.stepovoy.utils.Message;
import by.stepovoy.utils.MessageType;
import by.stepovoy.model.Film;
import by.stepovoy.model.Hall;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Role;
import by.stepovoy.model.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;

import java.io.IOException;
import java.util.List;

public class SeancePanel extends JFrame {

    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel mainPanel;
    private User user;
    private Film film;
    private JButton buyButton;
    private JButton addButton;
    private JButton deleteButton;

    public SeancePanel(JFrame parentFrame, Film film, final User user) {
        this.parentFrame = parentFrame;
        parentFrame.setVisible(false);
        setTitle("Просмотр киносеанса");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
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
        setBounds(new Rectangle(900, 600));
        setResizable(false);
        this.film = film;
        this.user = user;
        JPanel background = new JPanel();
        JPanel headerPanel = new JPanel();
        JPanel infoPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setPreferredSize(new Dimension(500, 50));
        buttonsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        setContentPane(background);

        JLabel nameLabel = new JLabel(film.getName() + " (" + film.getAgeLimit() + "+)");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel genreLabel = new JLabel("Жанр: " + film.getGenre());
        genreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel yearLabel = new JLabel(("Год выхода: " + film.getYearProduction()));
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel durationLabel = new JLabel(("Длительность: " + film.getDuration() + " мин"));
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel descriptionLabel = new JLabel(("Описание: " + film.getDescription()));
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel countryLabel = new JLabel(film.getCountry());
        countryLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        infoPanel.add(nameLabel);
        infoPanel.add(genreLabel);
        infoPanel.add(yearLabel);
        infoPanel.add(durationLabel);
        infoPanel.add(descriptionLabel);
        infoPanel.setSize(new Dimension(400, 200));
        JScrollPane infoScrollPane = new JScrollPane(infoPanel);
        infoScrollPane.setSize(new Dimension(400, 200));
        background.add(infoScrollPane);
        tableModel = new DefaultTableModel();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateTable();
            }
        });
        String[] columnNames = {"ID", "Зал", "Этаж", "Дата", "Время", "Осталось билетов", "Стоимость (BYN)"};
        tableModel.setColumnIdentifiers(columnNames);
        table = new JTable(tableModel);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                buyButton.setEnabled(true);
                if (user.getRole() != Role.USER) {
                    deleteButton.setEnabled(true);
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 400));
        tableScrollPane.setBorder(new BevelBorder(BevelBorder.RAISED));

        background.setPreferredSize(new Dimension(900, 600));
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
        mainPanel.setPreferredSize(background.getPreferredSize());

        buyButton = new JButton("Купить билет");
        buyButton.setEnabled(false);
        buyButton.setPreferredSize(new Dimension(150, 25));
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buyActionPerformed(e);
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        buttonsPanel.add(buyButton);
        if (user.getRole() != Role.USER) {
            addButton = new JButton("Добавить сеанс");
            addButton.setPreferredSize(new Dimension(150, 25));
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addActionPerformed(e);
                }
            });
            buttonsPanel.add(addButton);
            deleteButton = new JButton("Удалить сеанс");
            deleteButton.setPreferredSize(new Dimension(150, 25));
            deleteButton.setEnabled(false);
            buttonsPanel.add(deleteButton);
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteActionPerformed(e);
                }
            });
        }
        JButton returnButton = new JButton("Назад");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackActionPerformed(e);
            }
        });


        buttonsPanel.add(returnButton);

        headerPanel.add(infoPanel);
        headerPanel.add(buttonsPanel);

        mainPanel.add(headerPanel);
        mainPanel.add(tableScrollPane);
        background.add(mainPanel);
    }

    private void buyActionPerformed(ActionEvent e) throws IOException, ClassNotFoundException {
        BuyTicketPanel window = new BuyTicketPanel(this, user, getSelectedSeance(), true);
        window.setVisible(true);
    }

    @SuppressFBWarnings("DM_BOXED_PRIMITIVE_FOR_PARSING")
    private Seance getSelectedSeance() {
        int selectedRow = table.getSelectedRow();
        int selectedID = Integer.valueOf(String.valueOf(table.getValueAt(selectedRow, 0)));
        Message message = new Message();
        message.setOperationType(MessageType.GET);
        message.setMessageType(MessageType.SEANCE);
        message.setMessage(selectedID);
        Seance seance = null;
        try {
            ClientThread.sendMessage(message);
            seance = (Seance) ClientThread.receiveMessage().getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return seance;
    }

    private void goBackActionPerformed(ActionEvent e) {
        this.dispose();
        parentFrame.setVisible(true);
    }

//    private void updateActionPerformed(ActionEvent e) {
//        UpdateFilmPanel window = new UpdateFilmPanel(this, film);
//        window.setVisible(true);
//    }

    private void addActionPerformed(ActionEvent e) {
        AddSeancePanel window = new AddSeancePanel(this, film);
        window.setVisible(true);
    }

    @SuppressFBWarnings("DM_BOXED_PRIMITIVE_FOR_PARSING")
    private void deleteActionPerformed(ActionEvent e) {
        int reply = JOptionPane.showConfirmDialog(this,
                "Вы действительно хотите удалить этот сеанс?",
                "Удаление сеанса",
                JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            int selectedRow = table.getSelectedRow();
            int selectedID = Integer.valueOf(String.valueOf(table.getValueAt(selectedRow, 0)));
            Message message = new Message();
            message.setOperationType(MessageType.DELETE);
            message.setMessageType(MessageType.SEANCE);
            message.setMessage(selectedID);
            try {
                ClientThread.sendMessage(message);
                ClientThread.receiveMessage();
                tableModel.removeRow(selectedRow);
                if (tableModel.getRowCount() == 0) {
                    addButton.setEnabled(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        try {
            List<Seance> list = ClientThread.getFilmSeance(film);
            for (Seance session : list) {
                Message message = new Message();
                message.setOperationType(MessageType.GET);
                message.setMessageType(MessageType.HALL);
                message.setMessage(session.getHallID());
                ClientThread.sendMessage(message);
                message = ClientThread.receiveMessage();
                Hall hall = (Hall) message.getMessage();
                Object[] data = {
                        session.getID(), hall.getName(), hall.getFloor(),
                        session.getSeanceDate(), session.getSeanceTime(), session.getTicketsLeft(), session.getTicketCost()
                };
                tableModel.addRow(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

