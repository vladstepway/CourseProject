package by.stepovoy.view;


import by.stepovoy.FormValidator;
import by.stepovoy.client.ClientThread;
import by.stepovoy.message.Message;
import by.stepovoy.message.MessageType;
import by.stepovoy.model.Film;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddFilmPanel extends JFrame {

    private JPanel background;
    private JFrame parentFrame;
    private JTextField nameField;
    private JTextField durationField;
    private JTextArea descriptionField;
    private JTextField genreField;
    private JTextField yearField;
    private JTextField countryField;
    private JTextField ageLimitField;
    private JTextField directorField;
    private JRadioButton yesRadioButton;
    private JRadioButton noRadioButton;
    private JPanel[] panels;
    private static final String[] filmLabels = {"Название: ", "Длительность (мин): ", "Описание: ", "Год выхода: ", "Жанр: ",
            "Страна производства: ", "Режиссёр: ", "Доступен в 3D? ", "Возрастное ограничение: "};


    public AddFilmPanel(JFrame parentFrame) {
        parentFrame.setVisible(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showConfirmDialog(background,
                        "Вы действительно хотите выйти из программы?",
                        "Выход из приложения",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        this.parentFrame = parentFrame;
        setTitle("Добавление фильма");
        setBounds(700, 270, 640, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        background = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                Image img = null;
                try {
                    img = ImageIO.read(getClass().getResource("/image/add_panel.jpg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                graphics.drawImage(img, 0, 0, 640, 480, null);
            }
        };
        setContentPane(background);
        JPanel mainPanel = new JPanel() {
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
        mainPanel.setOpaque(false);
        mainPanel.setBounds(20, 80, 640, 600);

        addMovie();

        for (JPanel panel : panels) {
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            mainPanel.add(panel);
        }
        background.add(mainPanel);

        JButton acceptButton = new JButton("Добавить");
        acceptButton.setPreferredSize(new Dimension(170, 25));
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptActionPerformed(e);
            }
        });
        JButton cancelButton = new JButton("Назад");
        cancelButton.setPreferredSize(new Dimension(170, 25));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelActionPerformed(e);
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        buttonsPanel.add(acceptButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.setAlignmentX(SwingConstants.CENTER);
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));
    }

    private void acceptActionPerformed(ActionEvent e) {
        if (FormValidator.checkFilmFields(descriptionField, nameField, yearField, durationField, genreField,
                countryField, ageLimitField)) {
            MessageType messageType = MessageType.FILM;
            Message message = new Message();
            message.setOperationType(MessageType.ADD);
            message.setMessageType(messageType);

            Film film = new Film();
            film.setName(nameField.getText());
            film.setYearProduction(Integer.parseInt(yearField.getText()));
            film.setAgeLimit(Integer.parseInt(ageLimitField.getText()));
            film.setCountry(countryField.getText());
            film.setGenre(genreField.getText());
            film.setDescription(descriptionField.getText());
            film.setDuration(Integer.parseInt(durationField.getText()));
            film.setDirector(directorField.getText());
            film.setShow3D(yesRadioButton.isSelected());
            message.setMessage(film);

            try {
                ClientThread.sendMessage(message);
                ClientThread.receiveMessage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            cancelActionPerformed(e);
        } else {
            JOptionPane.showMessageDialog(parentFrame,
                    "Проверьте правильность введённых данных",
                    "Ошибка добавления",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelActionPerformed(ActionEvent event) {
        this.dispose();
        parentFrame.setVisible(true);
    }


    private void addMovie() {
        setBounds(700, 270, 640, 600);
        nameField = new JTextField();
        durationField = new JTextField();
        descriptionField = new JTextArea();
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionField.setRows(5);
        descriptionField.setBorder(durationField.getBorder());
        yearField = new JTextField();
        genreField = new JTextField();
        countryField = new JTextField();
        directorField = new JTextField();
        ageLimitField = new JTextField();
        JPanel is3DPanel = new JPanel();
        is3DPanel.setLayout(new FlowLayout());
        is3DPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        yesRadioButton = new JRadioButton("да");
        yesRadioButton.setOpaque(false);
        yesRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (noRadioButton.isSelected()) {
                    noRadioButton.setSelected(false);
                }
            }
        });
        noRadioButton = new JRadioButton("нет");
        noRadioButton.setOpaque(false);
        noRadioButton.setSelected(true);
        noRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yesRadioButton.isSelected()) {
                    yesRadioButton.setSelected(false);
                }
            }
        });
        is3DPanel.setOpaque(false);
        is3DPanel.add(yesRadioButton);
        is3DPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        is3DPanel.add(noRadioButton);

        fillWithLabels(filmLabels);

        panels[0].add(nameField);
        panels[0].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[1].add(durationField);
        panels[1].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[2].add(descriptionField);
        panels[2].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[3].add(yearField);
        panels[3].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[4].add(genreField);
        panels[4].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[5].add(countryField);
        panels[5].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[6].add(directorField);
        panels[6].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[7].add(is3DPanel);
        panels[7].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[8].add(ageLimitField);
        panels[8].add(Box.createRigidArea(new Dimension(15, 0)));
    }


    private void fillWithLabels(String[] labels) {
        panels = new JPanel[labels.length];
        JLabel[] labelLayers = new JLabel[labels.length];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            labelLayers[i] = new JLabel(labels[i]);
            labelLayers[i].setPreferredSize(new Dimension(150, 25));
            labelLayers[i].setHorizontalAlignment(SwingConstants.LEFT);
            labelLayers[i].setForeground(new Color(0, 0, 255));
            panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.LINE_AXIS));
            panels[i].add(Box.createRigidArea(new Dimension(15, 0)));
            panels[i].add(labelLayers[i]);
            panels[i].setOpaque(false);
            if (i == 2) {
                panels[i].setPreferredSize(new Dimension(300, 75));
            } else {
                panels[i].setPreferredSize(new Dimension(300, 25));
            }
        }
    }

}
