package by.stepovoy.view;

import by.stepovoy.client.ClientThread;
import by.stepovoy.message.Message;
import by.stepovoy.message.MessageType;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
import by.stepovoy.user.Role;
import by.stepovoy.user.User;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

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

        setBounds(700, 250, 500, 640);
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
                eventsActionPerformed(e);
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

//            JButton getReportButton = new JButton("Сохранить отчёт");
//            getReportButton.setPreferredSize(new Dimension(250, 25));
//            getReportButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    getReportActionPerformed(e);
//                }
//            });
//            mainPanel.add(getReportButton);
//            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
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
        JButton exitButton = new JButton("Выйти из программы");
        exitButton.setPreferredSize(new Dimension(250, 25));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitActionPerformed();
            }
        });
        mainPanel.add(exitButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        background.add(mainPanel);
        this.setLocationRelativeTo(null);
    }

    private void profileActionPerformed(ActionEvent e) {
        setVisible(false);
        ProfilePanel profilePanel = new ProfilePanel(this, user);
        profilePanel.setVisible(true);
    }

    private void eventsActionPerformed(ActionEvent e) {
        setVisible(false);
        FilmPanel filmPanel = new FilmPanel(this, user);
        filmPanel.setVisible(true);
    }

    private void usersActionPerformed(ActionEvent e) {
        setVisible(false);
        UserControlPanel userControlPanel = new UserControlPanel(this);
        userControlPanel.setVisible(true);
    }

    private void logoutActionPerformed(ActionEvent e) {
        dispose();
        parentFrame.setVisible(true);
    }

    private void exitActionPerformed() {
        int reply = JOptionPane.showConfirmDialog(background,
                "Вы действительно хотите выйти из программы?",
                "Изменение роли",
                JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void myHistoryActionPerformed(ActionEvent e) {
        UserBuyHistoryPanel window = new UserBuyHistoryPanel(this, user);
        window.setVisible(true);
    }

    private void totalHistoryActionPerformed(ActionEvent e) {
        TotalBuyHistoryPanel window = new TotalBuyHistoryPanel(this);
        window.setVisible(true);
    }

    private void showStatisticsActionPerformed(ActionEvent e) {
        ChooseMonthPanel monthPanel = new ChooseMonthPanel(this);
        monthPanel.setLocationRelativeTo(null);
        monthPanel.setVisible(true);
    }

    @SuppressFBWarnings("DM_DEFAULT_ENCODING")
    private void getReportActionPerformed(ActionEvent e) {
        List<Ticket> ticketList = null;
        try {
            ticketList = ClientThread.getAllTickets();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int totalTickets = 0;
        if (ticketList != null) {
            totalTickets = ticketList.size();
        }
        int totalTicketsCount = 0;
        int totalPrice = 0;
        int concertCount = 0;
        int movieCount = 0;
        int theatricalCount = 0;
        if (ticketList != null) {
            for (Ticket ticket : ticketList) {
                Message message = new Message();
                message.setOperationType(MessageType.GET);
                message.setMessageType(MessageType.SEANCE);
                message.setMessage(ticket.getSeanceID());
                Seance seance = null;
                try {
                    ClientThread.sendMessage(message);
                    seance = (Seance) ClientThread.receiveMessage().getMessage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                totalTicketsCount += ticket.getAmountTickets();
                totalPrice += ticket.getCost();
                movieCount++;
            }
        }

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("report.txt");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        if (writer != null) {
            writer.write("C помощью нашего приложени было оформлено " + totalTickets + " заказов " +
                    "на " + totalTicketsCount + " билетов на общую сумму " + totalPrice + " BYN" +
                    "\r\nСреди которых:\r\n" +
                    "\t" + movieCount + " - на кино\r\n"

            );
        }
        if (writer != null) {
            writer.close();
        }
    }
}
