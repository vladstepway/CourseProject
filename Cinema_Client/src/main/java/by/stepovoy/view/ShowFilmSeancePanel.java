package by.stepovoy.view;

import by.stepovoy.client.ClientThread;
import by.stepovoy.utils.Message;
import by.stepovoy.utils.MessageType;
import by.stepovoy.model.Film;
import by.stepovoy.model.Hall;
import by.stepovoy.model.Seance;
import by.stepovoy.model.user.Role;
import by.stepovoy.model.user.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

public class ShowFilmSeancePanel extends JFrame {

    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel mainPanel;
    private User user;
    private Film film;
    private JButton buyButton;
    private JButton addSessionButton;
    private JButton deleteSessionButton;

    public ShowFilmSeancePanel(JFrame parentFrame, Film film, final User user) {
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
                        "Изменение роли",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setBounds(400, 200, 1000, 700);
        setResizable(false);
        this.film = film;
        this.user = user;
        JPanel background = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                Image img = null;
                try {
                    img = ImageIO.read(getClass().getResource("/image/menu_admin.jpg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                graphics.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            }
        };
        setContentPane(background);

        JLabel nameLabel = new JLabel(film.getName() + " (" + film.getAgeLimit() + "+)");


        if (film.isShow3D()) {
            nameLabel.setIcon(new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Image img = null;
                    try {
                        img = ImageIO.read(getClass().getResource("/image/kino-496x330.jpg"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    g.drawImage(img, 0, 0, getIconWidth(), getIconHeight(), null);
                }

                @Override
                public int getIconWidth() {
                    return 25;
                }

                @Override
                public int getIconHeight() {
                    return 25;
                }
            });
        }

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

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(Box.createRigidArea(new Dimension(100, 20)));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(genreLabel);
        infoPanel.add(yearLabel);
        infoPanel.add(durationLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(descriptionLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JScrollPane infoScrollPane = new JScrollPane(infoPanel);
        infoScrollPane.setSize(new Dimension(getWidth(), 350));

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
                    deleteSessionButton.setEnabled(true);
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(getWidth(), 320));
        tableScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));

        background.setPreferredSize(new Dimension(getWidth(), getHeight()));
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
        mainPanel.setOpaque(false);
        mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(background.getPreferredSize());
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buyButton = new JButton("Купить билет");
        buyButton.setEnabled(false);
        buyButton.setPreferredSize(new Dimension(150, 25));
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyActionPerformed(e);
            }
        });
        buttonsPanel.add(buyButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        if (user.getRole() != Role.USER) {
            JButton editButton = new JButton("Редактировать");
            editButton.setPreferredSize(new Dimension(150, 25));
            editButton.setEnabled(false);
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateActionPerformed(e);
                }
            });
            buttonsPanel.add(editButton);
            buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
            addSessionButton = new JButton("Добавить сеанс");
            addSessionButton.setPreferredSize(new Dimension(150, 25));
            addSessionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addActionPerformed(e);
                }
            });
            buttonsPanel.add(addSessionButton);
            buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
            deleteSessionButton = new JButton("Удалить сеанс");
            deleteSessionButton.setPreferredSize(new Dimension(150, 25));
            deleteSessionButton.setEnabled(false);
            deleteSessionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteActionPerformed(e);
                }
            });
            buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        }
        JButton returnButton = new JButton("Назад");
        returnButton.setPreferredSize(new Dimension(150, 25));
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackActionPerformed(e);
            }
        });
        buttonsPanel.add(returnButton);
        buttonsPanel.setPreferredSize(new Dimension(getWidth(), 30));
        buttonsPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(infoPanel);
        mainPanel.add(tableScrollPane);
        mainPanel.add(buttonsPanel);
        background.add(mainPanel);
    }

    private void buyActionPerformed(ActionEvent e) {
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

    private void updateActionPerformed(ActionEvent e) {
        UpdateFilmPanel window = new UpdateFilmPanel(this, film);
        window.setVisible(true);
    }

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
                    addSessionButton.setEnabled(false);
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
                System.out.println("MESSAGE HERE\n" + message);
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

