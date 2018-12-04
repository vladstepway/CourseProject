package by.stepovoy.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainPanel extends JFrame {

    protected int port;

    private JPanel background;

    private static JFrame mainFrame;


    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public MainPanel(final int port) {
        this.port = port;
        setSize(300, 350);
        setResizable(false);
        setTitle("Добро пожаловать в GoldenScreen!");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int again = JOptionPane.showConfirmDialog(background,
                        "Вы уверены, что хотите выйти из программы ?",
                        "Выход",
                        JOptionPane.YES_NO_OPTION);
                if (again == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        background = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                Image img = null;
                try {
                    img = ImageIO.read(getClass().getResource("/image/gs_main.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                graphics.drawImage(img, 0, 0, 300, 350, null);
            }
        };
        setContentPane(background);

        JButton authButton = new JButton("Авторизация");
        authButton.setPreferredSize(new Dimension(100, 40));
        authButton.setVerticalAlignment(SwingConstants.CENTER);
        authButton.setHorizontalAlignment(SwingConstants.CENTER);
        JButton regButton = new JButton("Регистрация");
        regButton.setPreferredSize(new Dimension(100, 40));
        regButton.setVerticalAlignment(SwingConstants.CENTER);


        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.add(Box.createRigidArea(new Dimension(120, 150)));
        background.add(authButton);
        background.add(Box.createRigidArea(new Dimension(0, 20)));
        background.add(regButton);

        mainFrame = this;


        regButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistrationPanel registerWindow = new RegistrationPanel(mainFrame, port);
                registerWindow.setVisible(true);
            }
        });

        authButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AuthorizationPanel authorizationPanel = new AuthorizationPanel(mainFrame, port);
                authorizationPanel.setVisible(true);
            }
        });
    }
}
