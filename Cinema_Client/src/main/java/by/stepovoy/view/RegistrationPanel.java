package by.stepovoy.view;

import by.stepovoy.utils.FormValidator;
import by.stepovoy.client.ClientThread;
import by.stepovoy.utils.MessageType;
import by.stepovoy.model.User;

import by.stepovoy.utils.DateValidator;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.sql.Date;

import java.util.Properties;

public class RegistrationPanel extends JFrame {
    private int port;
    private JPanel background;
    private JFrame parentFrame;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JPasswordField repeatPasswordField;
    private JTextField surnameField;
    private JTextField nameField;
    private JTextField emailField;
    private JDatePickerImpl dateField;

    private static final String[] labels = {"Логин: ",
            "Пароль: ", "Повторите пароль: ",
            "Фамилия: ", "Имя: ", "Электронная почта: ", "Дата рождения: "};

    public RegistrationPanel(JFrame parentFrame, int port) {
        this.port = port;
        parentFrame.setVisible(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showConfirmDialog(background,
                        "Вы действительно хотите выйти из программы?",
                        "Изменение роли",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        this.parentFrame = parentFrame;
        setTitle("Регистрация");
        setBounds(700, 270, 640, 480);
        setResizable(false);
        background = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                Image img = null;
                try {
                    img = ImageIO.read(getClass().getResource("/image/registration.jpg"));
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
        mainPanel.setBounds(20, 80, 620, 440);

        JLabel title = new JLabel("РЕГИСТРАЦИЯ");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        loginField = new JTextField();
        passwordField = new JPasswordField();
        repeatPasswordField = new JPasswordField();
        surnameField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(new UtilDateModel(), properties);
        dateField = new JDatePickerImpl(datePanel, new DateValidator());
        dateField.setSize(new Dimension(200, 40));

        JPanel[] panels = new JPanel[labels.length];
        JLabel[] labelLayers = new JLabel[labels.length];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            labelLayers[i] = new JLabel(labels[i]);
            labelLayers[i].setPreferredSize(new Dimension(150, 25));
            labelLayers[i].setHorizontalAlignment(SwingConstants.LEFT);
            labelLayers[i].setForeground(new Color(0, 0, 255));
            panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.LINE_AXIS));
            panels[i].setPreferredSize(new Dimension(300, 25));
            panels[i].add(Box.createRigidArea(new Dimension(15, 0)));
            panels[i].add(labelLayers[i]);
            panels[i].setOpaque(false);
        }

        panels[0].add(loginField);
        panels[0].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[1].add(passwordField);
        panels[1].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[2].add(repeatPasswordField);
        panels[2].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[3].add(surnameField);
        panels[3].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[4].add(nameField);
        panels[4].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[5].add(emailField);
        panels[5].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[6].add(dateField);
        panels[6].add(Box.createRigidArea(new Dimension(15, 0)));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        for (JPanel panel : panels) {
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            mainPanel.add(panel);
        }
        background.add(mainPanel);

        JButton acceptButton = new JButton("Зарегистрироваться");
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
        mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        mainPanel.setAlignmentX(SwingConstants.CENTER);
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));

    }

    private void acceptActionPerformed(ActionEvent event) {
        if (FormValidator.checkUserFields(dateField, loginField, passwordField, repeatPasswordField,
                surnameField, nameField, emailField)) {
            User user = new User();
            user.setLogin(loginField.getText());
            user.setPassword(String.valueOf(passwordField.getPassword()));
            user.setSurname(surnameField.getText());
            user.setName(nameField.getText());
            user.setEmail(emailField.getText());
            user.setBirthday(new Date(((java.util.Date) dateField.getModel().getValue()).getTime()));

            ClientThread clientThread = new ClientThread(MessageType.SIGN, MessageType.UP, user, parentFrame, this, port);
            clientThread.start();
        } else {
            JOptionPane.showMessageDialog(parentFrame,
                    "Проверьте правильность введённых данных",
                    "Ошибка сохранения",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelActionPerformed(ActionEvent event) {
        this.dispose();
        parentFrame.setVisible(true);
    }
}
