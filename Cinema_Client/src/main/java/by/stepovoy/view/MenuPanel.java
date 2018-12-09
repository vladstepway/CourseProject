package by.stepovoy.view;

import by.stepovoy.model.Role;
import by.stepovoy.model.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MenuPanel extends JFrame {
    private JPanel background;
    private static JFrame parentFrame;
    private User user;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public MenuPanel(JFrame parentFrame, User user) {
        this.user = user;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitActionPerformed();
            }
        });
        MenuPanel.parentFrame = parentFrame;
        setTitle(" Меню " + user.getName());

        setBounds(700, 250, 500, 500);
        setResizable(false);
        background = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                Image img = null;
                try {
                    img = ImageIO.read(getClass().getResource("/image/menu_admin.jpg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                graphics.drawImage(img, 0, 0, 500, 640, null);
            }
        };
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.add(Box.createRigidArea(new Dimension(230, 130)));
        setContentPane(background);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        mainPanel.setPreferredSize(new Dimension(250, 450));
        mainPanel.setOpaque(false);


        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton profileButton = new JButton("Мой профиль");
        profileButton.setPreferredSize(new Dimension(250, 25));
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileActionPerformed(e);
            }
        });
        mainPanel.add(profileButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton myHistoryButton = new JButton("Моя история покупок");
        myHistoryButton.setPreferredSize(new Dimension(250, 25));
        myHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myHistoryActionPerformed(e);
            }
        });
        mainPanel.add(myHistoryButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        if (user.getRole() != Role.USER) {
            JButton totalHistoryButton = new JButton("Полная история покупок");
            totalHistoryButton.setPreferredSize(new Dimension(250, 25));
            totalHistoryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    totalHistoryActionPerformed(e);
                }
            });
            mainPanel.add(totalHistoryButton);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        JButton eventsButton = new JButton("Просмотр киносеансов");
        eventsButton.setPreferredSize(new Dimension(250, 25));
        eventsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filmsActionPerformed(e);
            }
        });
        mainPanel.add(eventsButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        if (user.getRole() == Role.ADMIN) {
            JButton usersButton = new JButton("Управление пользователями");
            usersButton.setPreferredSize(new Dimension(250, 25));
            usersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    usersActionPerformed(e);
                }
            });
            mainPanel.add(usersButton);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        if (user.getRole() != Role.USER) {
            JButton showStatisticsButton = new JButton("Статистика заказов");
            showStatisticsButton.setPreferredSize(new Dimension(250, 25));
            showStatisticsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showStatisticsActionPerformed(e);
                }
            });
            mainPanel.add(showStatisticsButton);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

            JButton getReportButton = new JButton("Сохранить отчёт в txt файл");
            getReportButton.setPreferredSize(new Dimension(250, 25));
            getReportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        getReportActionPerformed(e);
                    } catch (IOException | ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            mainPanel.add(getReportButton);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        JButton logoutButton = new JButton("Выйти из аккаунта");
        logoutButton.setPreferredSize(new Dimension(250, 25));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutActionPerformed(e);
            }
        });
        mainPanel.add(logoutButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        background.add(mainPanel);
    }

    private void profileActionPerformed(ActionEvent e) {
        setVisible(false);
        ProfilePanel profilePanel = new ProfilePanel(this, user);
        profilePanel.setLocationRelativeTo(null);
        profilePanel.setVisible(true);
    }

    private void filmsActionPerformed(ActionEvent e) {
        setVisible(false);
        FilmPanel filmPanel = new FilmPanel(this, user);
        filmPanel.setLocationRelativeTo(null);
        filmPanel.setVisible(true);
    }

    private void usersActionPerformed(ActionEvent e) {
        setVisible(false);
        UserControlPanel userControlPanel = new UserControlPanel(this);
        userControlPanel.setLocationRelativeTo(null);
        userControlPanel.setVisible(true);
    }

    private void logoutActionPerformed(ActionEvent e) {
        dispose();
        parentFrame.setVisible(true);
    }

    private void exitActionPerformed() {
        int reply = JOptionPane.showConfirmDialog(background,
                "Вы действительно хотите выйти из программы?",
                "Выход",
                JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void myHistoryActionPerformed(ActionEvent e) {
        UserBuyHistoryPanel userBuyHistoryPanel = new UserBuyHistoryPanel(this, user);
        userBuyHistoryPanel.setLocationRelativeTo(null);
        userBuyHistoryPanel.setVisible(true);
    }

    private void totalHistoryActionPerformed(ActionEvent e) {
        TotalBuyHistoryPanel totalBuyHistoryPanel = new TotalBuyHistoryPanel(this);
        totalBuyHistoryPanel.setLocationRelativeTo(null);
        totalBuyHistoryPanel.setVisible(true);
    }

    private void showStatisticsActionPerformed(ActionEvent e) {
        ChooseMonthPanel monthPanel = new ChooseMonthPanel(this);
        monthPanel.setLocationRelativeTo(null);
        monthPanel.setVisible(true);
    }

    @SuppressFBWarnings("DM_DEFAULT_ENCODING")
    private void getReportActionPerformed(ActionEvent e) throws IOException, ClassNotFoundException {

        ReportGeneratorPanel reportGeneratorPanel = new ReportGeneratorPanel();
        reportGeneratorPanel.setVisible(true);

    }
}
