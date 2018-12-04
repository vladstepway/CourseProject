package by.stepovoy.view;

import by.stepovoy.MyException;
import by.stepovoy.client.ClientThread;
import by.stepovoy.message.Message;
import by.stepovoy.message.MessageType;
import by.stepovoy.model.Film;
import by.stepovoy.user.Role;
import by.stepovoy.user.User;
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
import java.util.ArrayList;
import java.util.List;

public class FilmPanel extends JFrame {

    private JFrame parentFrame;
    private DefaultTableModel filmTableModel;
    private JTable filmTable;
    private User user;
    private JPanel mainPanel;
    private int selectedRow;
    private JButton editButton;
    private JButton showButton;
    private JButton deleteButton;
    private JTextField searchField;
    private List<Film> films;

    public FilmPanel(JFrame parentFrame, final User user) {
        this.parentFrame = parentFrame;
        setTitle("Просмотр фильмов");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showConfirmDialog(mainPanel,
                        "Вы действительно хотите выйти из программы?",
                        "Закрытие",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setBounds(400, 200, 1000, 700);
        setResizable(false);
        this.user = user;
        films = new ArrayList<>();
        filmTableModel = new DefaultTableModel();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateTable();
            }
        });
        String[] columnNames = {"ID", "Название", "Год выхода", "Жанр", "Страна", "Возрастное ограничение"};
        filmTableModel.setColumnIdentifiers(columnNames);
        filmTable = new JTable(filmTableModel);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(filmTableModel);
        filmTable.setRowSorter(sorter);
        filmTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jScrollPane = new JScrollPane(filmTable);
        jScrollPane.setPreferredSize(new Dimension(1000, 700));
        jScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
        filmTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                selectedRow = filmTable.getSelectedRow();
                showButton.setEnabled(true);
                if (user.getRole() != Role.USER) {
                    deleteButton.setEnabled(true);
                    editButton.setEnabled(true);
                }
            }
        });


        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 26));
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                fillTable();
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        JButton searchButton = new JButton();
        searchButton.setIcon(new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Image img = null;
                try {
                    img = ImageIO.read(getClass().getResource("/image/search.png"));
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
        searchButton.setPreferredSize(new Dimension(25, 25));
        JPanel checkBoxPanel = new JPanel();

        checkBoxPanel.add(searchField);
        checkBoxPanel.add(searchButton);

        showButton = new JButton("Просмотреть");
        showButton.setPreferredSize(new Dimension(150, 25));
        showButton.setEnabled(false);
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showActionPerformed(e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JButton returnButton = new JButton("Назад");
        returnButton.setPreferredSize(new Dimension(150, 25));
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackActionPerformed(e);
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        if (user.getRole() != Role.USER) {
            JButton addButton = new JButton("Добавить");
            addButton.setPreferredSize(new Dimension(150, 25));
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addActionPerformed(e);
                }
            });
            buttonsPanel.add(addButton);
            buttonsPanel.add(Box.createRigidArea(new Dimension(30, 0)));
            editButton = new JButton("Изменить");
            editButton.setPreferredSize(new Dimension(150, 25));
            editButton.setEnabled(false);
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editActionPerformed(e);
                }
            });
            buttonsPanel.add(editButton);
            buttonsPanel.add(Box.createRigidArea(new Dimension(30, 0)));
            deleteButton = new JButton("Удалить");
            deleteButton.setPreferredSize(new Dimension(150, 25));
            deleteButton.setEnabled(false);
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteActionPerformed(e);
                }
            });
            buttonsPanel.add(deleteButton);
            buttonsPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        }
        buttonsPanel.add(showButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        buttonsPanel.add(returnButton);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(checkBoxPanel);
        mainPanel.add(jScrollPane);
        mainPanel.add(buttonsPanel);

        setContentPane(mainPanel);

    }

    private void fillTableWith(List<Film> films) {
        filmTableModel.setRowCount(0);
        for (Film film : films) {
            Object[] data = {
                    String.valueOf(film.getID()), film.getName(), film.getYearProduction(),
                    film.getGenre(), film.getCountry(), film.getAgeLimit() + "+"
            };
            filmTableModel.addRow(data);
        }
    }

    private void goBackActionPerformed(ActionEvent e) {
        this.dispose();
        parentFrame.setVisible(true);
    }

    @SuppressFBWarnings({"DM_BOXED_PRIMITIVE_FOR_PARSING", "DLS_DEAD_LOCAL_STORE"})
    public void showActionPerformed(ActionEvent e) {
        int selectedID = -1;
        selectedID = Integer.valueOf(String.valueOf(filmTable.getValueAt(selectedRow, 0)));
        if (selectedID != -1) {
            MessageType messageType = MessageType.FILM;
            Message message = new Message();
            message.setOperationType(MessageType.GET);
            message.setMessageType(messageType);
            message.setMessage(selectedID);
            Film film = null;
            try {
                ClientThread.sendMessage(message);
                film = (Film) ClientThread.receiveMessage().getMessage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ShowFilmSeancePanel window = null;
            if (film != null) {
                window = new ShowFilmSeancePanel(this, film, user);

            }
            if (window != null) {
                window.setVisible(true);
                window.setLocationRelativeTo(null);
            }
        } else {
            try {
                throw new MyException("Invalid index");
            } catch (MyException e1) {
                e1.printStackTrace();
            }
            selectedID = JOptionPane.showConfirmDialog(this,
                    "Вы ничего не выделили!",
                    "Не выбран фильм",
                    JOptionPane.YES_NO_OPTION);
        }
    }

    public void addActionPerformed(ActionEvent e) {
        AddFilmPanel window = new AddFilmPanel(parentFrame);
        window.setVisible(true);
    }

    @SuppressFBWarnings({"DM_BOXED_PRIMITIVE_FOR_PARSING", "DLS_DEAD_LOCAL_STORE"})
    public void editActionPerformed(ActionEvent e) {
        int selectedID = Integer.valueOf(String.valueOf(filmTable.getValueAt(selectedRow, 0)));
        MessageType messageType = MessageType.FILM;
        Message message = new Message();
        message.setOperationType(MessageType.GET);
        message.setMessageType(messageType);
        message.setMessage(selectedID);
        Film film = null;
        try {
            ClientThread.sendMessage(message);
            film = (Film) ClientThread.receiveMessage().getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        UpdateFilmPanel window = new UpdateFilmPanel(this, film);
        window.setVisible(true);
    }

    @SuppressFBWarnings({"DM_BOXED_PRIMITIVE_FOR_PARSING", "DLS_DEAD_LOCAL_STORE"})
    public void deleteActionPerformed(ActionEvent e) {
        int reply = JOptionPane.showConfirmDialog(this,
                "Вы действительно хотите удалить \"" + filmTable.getValueAt(selectedRow, 2) + "\"?",
                "Удаление мероприятия",
                JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            int selectedID = Integer.valueOf(String.valueOf(filmTable.getValueAt(selectedRow, 0)));
            MessageType messageType = MessageType.FILM;
            Message message = new Message();
            message.setOperationType(MessageType.DELETE);
            message.setMessageType(messageType);
            message.setMessage(selectedID);
            try {
                ClientThread.sendMessage(message);
                ClientThread.receiveMessage();
                films = ClientThread.getAllFilms();
                filmTableModel.removeRow(selectedRow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateTable() {
        showButton.setEnabled(false);
        if (user.getRole() != Role.USER) {
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        }
        try {

            films = ClientThread.getAllFilms();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fillTable();
    }

    private void fillTable() {
        String query = searchField.getText().toLowerCase();
        ArrayList<Film> foundList = new ArrayList<Film>();
        if (query.length() != 0) {
            for (Film film : films) {
                if (String.valueOf(film.getID()).contains(query) ||
                        (film.getAgeLimit() + "+").toLowerCase().contains(query) ||
                        String.valueOf(film.getYearProduction()).toLowerCase().contains(query) ||
                        film.getCountry().toLowerCase().contains(query) ||
                        film.getGenre().toLowerCase().contains(query) ||
                        film.getName().toLowerCase().contains(query)) {
                    foundList.add(film);
                }
            }
            fillTableWith(foundList);
        } else {
            fillTableWith(films);
        }
    }

}
