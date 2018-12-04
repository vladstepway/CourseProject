package by.stepovoy.view;

import by.stepovoy.FormValidator;
import by.stepovoy.client.ClientThread;
import by.stepovoy.message.Message;
import by.stepovoy.message.MessageType;
import by.stepovoy.model.Film;
import by.stepovoy.model.Hall;

import by.stepovoy.model.Seance;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class AddSeancePanel extends JFrame {

    private JPanel background;
    private JFrame parentFrame;
    private Film film;
    private JComboBox institutionsComboBox;
    private JDatePickerImpl dateField;
    private JFormattedTextField timeField;
    private JTextField costField;
    private JPanel[] panels;
    private List<Hall> list;
    private static final String[] labels = {"Зал: ", "Дата: ", "Время: ", "Стоимость (BYN): ",};

    public AddSeancePanel(JFrame parentFrame, Film film) {
        this.film = film;
        parentFrame.setVisible(false);
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
        this.parentFrame = parentFrame;
        setTitle("Добавление киносеанса");
        setBounds(700, 270, 640, 480);
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
        mainPanel.setBounds(20, 80, 620, 440);

        try {
            institutionsComboBox = new JComboBox();
            list = ClientThread.getAllHalls();
            for (Hall hall : list) {
                institutionsComboBox.addItem(hall.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(new UtilDateModel(), properties);
        dateField = new JDatePickerImpl(datePanel, new DateFormatter());
        dateField.setSize(new Dimension(200, 40));
        try {
            MaskFormatter mask = new MaskFormatter("##:##");
            mask.setPlaceholderCharacter('#');
            timeField = new JFormattedTextField(mask);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        costField = new JTextField();

        fillPanelsWith(labels);
        panels[0].add(institutionsComboBox);
        panels[0].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[1].add(dateField);
        panels[1].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[2].add(timeField);
        panels[2].add(Box.createRigidArea(new Dimension(15, 0)));
        panels[3].add(costField);
        panels[3].add(Box.createRigidArea(new Dimension(15, 0)));

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
        if (FormValidator.checkSeanceFields(institutionsComboBox, dateField, timeField, costField)) {
            Seance seance = new Seance();
            seance.setSeanceDate(new Date(((java.util.Date) dateField.getModel().getValue()).getTime()));
            DateFormat format = new SimpleDateFormat("HH:mm");
            try {
                seance.setSeanceTime(new Time(format.parse(timeField.getText()).getTime()));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            for (Hall hall : list) {
                if (hall.getName().equals(institutionsComboBox.getSelectedItem())) {
                    seance.setHallID(hall.getID());
                    seance.setTicketsLeft(hall.getCapacity());
                    break;
                }
            }
            seance.setTicketCost(Double.parseDouble(costField.getText()));
            seance.setFilmID(film.getID());
            Message message = new Message();
            message.setOperationType(MessageType.ADD);
            message.setMessageType(MessageType.SEANCE);
            System.out.println(seance);
            message.setMessage(seance);
            try {
                ClientThread.sendMessage(message);
                ClientThread.receiveMessage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            this.dispose();
            parentFrame.setVisible(true);
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

    private void fillPanelsWith(String[] labels) {
        panels = new JPanel[labels.length];
        JLabel[] labelLayers = new JLabel[labels.length];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            labelLayers[i] = new JLabel(labels[i]);
            labelLayers[i].setPreferredSize(new Dimension(150, 25));
            labelLayers[i].setHorizontalAlignment(SwingConstants.LEFT);
            labelLayers[i].setForeground(new Color(255, 189, 0));
            panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.LINE_AXIS));
            panels[i].setPreferredSize(new Dimension(300, 25));
            panels[i].add(Box.createRigidArea(new Dimension(15, 0)));
            panels[i].add(labelLayers[i]);
            panels[i].setOpaque(false);
        }
    }

}
