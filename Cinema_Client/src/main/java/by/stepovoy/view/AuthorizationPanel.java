package by.stepovoy.view;

import by.stepovoy.utils.FormValidator;
import by.stepovoy.client.ClientThread;
import by.stepovoy.utils.MessageType;
import by.stepovoy.model.user.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AuthorizationPanel extends JFrame {

    private int port;
    private JPanel background;
    private JFrame parentFrame;
    private JTextField loginField;
    private JPasswordField passwordField;
    private static final String[] labels = {"Логин: ", "Пароль: "};

    public AuthorizationPanel(JFrame parentFrame, int port) {
        parentFrame.setVisible(false);
        this.port = port;
        this.parentFrame = parentFrame;
        setTitle(" Авторизация");
        setBounds(650, 280, 450, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
        setResizable(false);
        background = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                Image img = null;
                try {
                    img = ImageIO.read(getClass().getResource("/image/auth.png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                graphics.drawImage(img, 0, 0, 580, 420, null);
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

        JLabel title = new JLabel("АВТОРИЗАЦИЯ");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        loginField = new JTextField();
        passwordField = new JPasswordField();

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
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        for (JPanel panel : panels) {
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            mainPanel.add(panel);
        }
        background.add(mainPanel);
        mainPanel.setBounds(20, 150, 500, 440);

        JButton acceptButton = new JButton("Авторизироваться");
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
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        buttonsPanel.add(acceptButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        mainPanel.setAlignmentX(SwingConstants.CENTER);
        mainPanel.add(buttonsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        loginField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                switch (key) {
                    case KeyEvent.VK_ENTER:
                        acceptActionPerformed(null);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        cancelActionPerformed(null);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        passwordField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char key = e.getKeyChar();
                switch (key) {
                    case KeyEvent.VK_ENTER:
                        acceptActionPerformed(null);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        cancelActionPerformed(null);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void acceptActionPerformed(ActionEvent event) {
        if (FormValidator.checkFields(loginField, passwordField)) {
            User user = new User();
            user.setLogin(loginField.getText());
            user.setPassword(String.valueOf(passwordField.getPassword()));
            ClientThread clientThread = new ClientThread(MessageType.SIGN, MessageType.IN, user, parentFrame,
                    this, port, loginField, passwordField);
            clientThread.start();
        } else {
            loginField.setBorder(BorderFactory.createLineBorder(Color.RED));
            passwordField.setBorder(BorderFactory.createLineBorder(Color.RED));
            JOptionPane.showMessageDialog(parentFrame,
                    "Проверьте правильность введённых данных",
                    "Ошибка авторизации",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelActionPerformed(ActionEvent event) {
        this.dispose();
        parentFrame.setVisible(true);
    }
}
