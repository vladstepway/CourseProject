package by.stepovoy.view;


import by.stepovoy.client.ClientThread;
import by.stepovoy.utils.Message;
import by.stepovoy.utils.MessageType;
import by.stepovoy.model.Film;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.jfree.chart.plot.PlotOrientation.VERTICAL;

public class ChooseMonthPanel extends JDialog {

    public ChooseMonthPanel(final JFrame parentFrame) {
        super(parentFrame);
        setLocationRelativeTo(null);
        final JComboBox monthsList = new JComboBox();
        JPanel comboBoxPanel = new JPanel();
        try {
            List<Seance> seanceList = ClientThread.getAllSeances();
            List<Integer> listOfMonths = new ArrayList<>();
            for (Seance seance : seanceList) {
                DateTime datetime = new DateTime(seance.getSeanceDate());
                listOfMonths.add(Integer.parseInt(datetime.toString("MM")));
            }
            List<Integer> uniqueElements = makeUniqueElements(listOfMonths);
            for (Integer month : uniqueElements) {
                monthsList.addItem(month);
            }


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        comboBoxPanel.add(monthsList);
        JButton okButton = new JButton("OK");
        okButton.setEnabled(true);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Seance seance = new Seance(0);
                int filmID = 0;
                int previousID = 0;
                Film film;
                String filmName = null;
                List<Ticket> ticketList;
                double sumOfOrder = 0.;
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                try {
                    ticketList = ClientThread.getAllTickets();
                    for (Ticket ticket : ticketList) {

                        Message message = new Message();
                        message.setOperationType(MessageType.GET);
                        message.setMessageType(MessageType.SEANCE);
                        message.setMessage(ticket.getSeanceID());
                        ClientThread.sendMessage(message);
                        previousID = seance.getFilmID();
                        seance = (Seance) ClientThread.receiveMessage().getMessage();
                        DateTime datetime = new DateTime(seance.getSeanceDate());
                        if (monthsList.getSelectedItem() == (Integer) Integer.parseInt(datetime.toString("MM"))) {
                            message = new Message();
                            message.setOperationType(MessageType.GET);
                            message.setMessageType(MessageType.FILM);
                            message.setMessage(seance.getFilmID());
                            ClientThread.sendMessage(message);

                            if (message.getMessageType() == MessageType.FILM) {
                                film = (Film) ClientThread.receiveMessage().getMessage();
                                filmID = film.getID();
                                filmName = film.getName();
                                if (filmID != previousID) {
                                    sumOfOrder = 0.;
                                }
                            }

                            sumOfOrder += ticket.getCost();
                            dataset.setValue(sumOfOrder, filmName, "");
                        }
                    }
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

                JFreeChart jFreeChart = ChartFactory.createBarChart("BarChart",
                        "Фильм",
                        "Продажи",
                        dataset, VERTICAL,
                        true, true, false);
                ChartFrame frame = new ChartFrame(jFreeChart.getTitle().getText(), jFreeChart);
                frame.setSize(700, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                dispose();
            }

        });
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentFrame.setVisible(true);
                dispose();
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(okButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(40, 0)));
        buttonsPanel.add(cancelButton);
        this.setBounds(720, 480, 300, 140);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel mainPanel = new JPanel();
        mainPanel.add(comboBoxPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(buttonsPanel);
        mainPanel.setVisible(true);
        this.setContentPane(mainPanel);
        this.setTitle("Выберите месяц");
        this.setResizable(false);
        this.setVisible(true);
    }


    public static List<Integer> makeUniqueElements(List<Integer> arrayList) {
        Set<Integer> set = new LinkedHashSet<>(arrayList);
        return new ArrayList<>(set);
    }
}
