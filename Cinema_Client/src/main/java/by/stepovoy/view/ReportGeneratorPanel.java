package by.stepovoy.view;

import by.stepovoy.client.ClientThread;
import by.stepovoy.model.Film;
import by.stepovoy.model.Seance;
import by.stepovoy.model.Ticket;
import org.joda.time.DateTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReportGeneratorPanel extends JFrame {

    public ReportGeneratorPanel() {
        setSize(400, 400);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        setContentPane(panel);

        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new FlowLayout());
        panel.add(comboPanel);

        JComboBox filmComboBox = new JComboBox();
        comboPanel.add(filmComboBox);

        JComboBox monthComboBox = new JComboBox();
        comboPanel.add(monthComboBox);


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        panel.add(buttonsPanel);

        JButton showOneFilmAllMonthReport = new JButton("Отчёт по фильму по всем месяцам");
        buttonsPanel.add(showOneFilmAllMonthReport);
        JButton showAllFilmsAllMonthReport = new JButton("Все фильмы все месяцы");
        buttonsPanel.add(showAllFilmsAllMonthReport);

        try {
            List<Seance> seanceList = ClientThread.getAllSeances();
            List<Integer> listOfMonths = new ArrayList<>();
            List<Film> filmList = ClientThread.getAllFilms();
            for (Film film : filmList) {
                filmComboBox.addItem(film.getName());
            }

            for (Seance seance : seanceList) {
                DateTime datetime = new DateTime(seance.getSeanceDate());
                listOfMonths.add(Integer.parseInt(datetime.toString("MM")));
            }
            List<Integer> uniqueElements = ChooseMonthPanel.makeUniqueElements(listOfMonths);
            for (Integer month : uniqueElements) {
                monthComboBox.addItem(month);
            }

            showAllFilmsAllMonthReport.addActionListener(new ActionListener() {
                List<Ticket> tickets = null;
                List<Film> films = null;
                List<Seance> seances = null;
                int previousID = 0;
                double profit = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    PrintWriter printWriter;


                    try {
                        tickets = ClientThread.getAllTickets();
                        films = ClientThread.getAllFilms();
                        seances = ClientThread.getAllSeances();
                        printWriter = new PrintWriter("allFilmsReport.txt");
                        if (printWriter != null) {
                            printWriter.write("Выручка с продаж билетов на фильм : \n");
                        }
                        for (Film film : films) {
                            for (Seance seance : seances) {
                                previousID = seance.getFilmID();
                                if (film.getID() == seance.getFilmID()) {
                                    for (Ticket ticket : tickets) {
                                        if (seance.getID() == ticket.getSeanceID()) {
                                            profit += ticket.getCost();
                                        }
                                    }
                                }
                            }
                            if (printWriter != null) {
                                printWriter.write(film.getName() +
                                        " ---> составила : " + profit + " BYN\n");
                            }
                            if (film.getID() != previousID) {
                                profit = 0;
                            }
                        }
                        if (printWriter != null) {
                            printWriter.close();
                        }
                    } catch (IOException |
                            ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            showOneFilmAllMonthReport.addActionListener(new ActionListener() {
                List<Film> films = null;
                List<Seance> seances = null;
                List<Ticket> tickets = null;

                @Override
                public void actionPerformed(ActionEvent e) {
                    PrintWriter printWriter = null;
                    try {
                        films = ClientThread.getAllFilms();
                        seances = ClientThread.getAllSeances();
                        tickets = ClientThread.getAllTickets();
                        int previousID = 0;
                        double profit = 0.;
                        printWriter = new PrintWriter("oneFilmFullReport.txt");
                        if (printWriter != null) {
                            printWriter.write("Выручка с продаж билетов на фильм : \n");
                        }
                        for (Film film : films) {
                            for (Seance seance : seances) {
                                previousID = seance.getFilmID();
                                if (film.getID() == seance.getFilmID()) {
                                    for (Ticket ticket : tickets) {
                                        if (seance.getID() == ticket.getSeanceID()) {
                                            profit += ticket.getCost();
                                        }
                                    }
                                }
                            }
                            if (Objects.equals(filmComboBox.getSelectedItem(), film.getName())) {
                                if (printWriter != null) {
                                    printWriter.write(film.getName() +
                                            " ---> составила : " + profit + " BYN\n");
                                }
                            }
                            if (film.getID() != previousID) {
                                profit = 0;
                            }

                        }
                        if (printWriter != null) {
                            printWriter.close();
                        }


                    } catch (Exception e1) {
                        e1.getCause();
                    }
                }
            });

        } catch (IOException |
                ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
