package by.stepovoy.view;

import by.stepovoy.client.ClientThread;
import by.stepovoy.utils.Message;
import by.stepovoy.utils.MessageType;
import by.stepovoy.model.Role;
import by.stepovoy.model.User;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class UserControlPanel extends JFrame {

    private JFrame parentFrame;
    private DefaultTableModel usersTableModel;
    private JTable usersTable;
    private JPanel mainPanel;
    private User selectedUser;
    private JButton setRoleButton;

    public UserControlPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLocationRelativeTo(null);
        setTitle("Управление пользователями");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showConfirmDialog(mainPanel,
                        "Вы действительно хотите выйти из программы?",
                        "Изменение роли",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setBounds(400, 200, 1000, 700);
        setResizable(false);
        List<User> userList = null;
        try {
            userList = ClientThread.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        usersTableModel = new DefaultTableModel();
        String[] columnNames = {"ID", "Логин", "Роль", "Фамилия", "Имя", "Email", "Дата рождения"};
        usersTableModel.setColumnIdentifiers(columnNames);
        if (userList != null) {
            for (User user : userList) {
                Object[] data = {
                        user.getID(), user.getLogin(), user.getRole(),
                        user.getSurname(), user.getName(), user.getEmail(), user.getBirthday()
                };
                usersTableModel.addRow(data);
            }
        }
        usersTable = new JTable(usersTableModel);
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(usersTableModel);
        usersTable.setRowSorter(sorter);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jScrollPane = new JScrollPane(usersTable);
        jScrollPane.setPreferredSize(new Dimension(1000, 700));
        jScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
        usersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                selectedUser = new User();
                selectedUser.setID((Integer) usersTableModel.getValueAt(usersTable.getSelectedRow(), 0));
                selectedUser.setLogin(String.valueOf(usersTableModel.getValueAt(usersTable.getSelectedRow(), 1)));
                selectedUser.setRole(Role.valueOf(String.valueOf(usersTableModel.getValueAt(usersTable.getSelectedRow(), 2)).toUpperCase()));
                if (selectedUser.getRole() != Role.ADMIN) {
                    setRoleButton.setEnabled(true);
                } else {
                    setRoleButton.setEnabled(false);
                }
            }
        });

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(jScrollPane);
        setContentPane(mainPanel);
        setRoleButton = new JButton("Изменить роль");
        setRoleButton.setPreferredSize(new Dimension(170, 25));
        setRoleButton.setEnabled(false);
        setRoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setRoleActionPerformed(e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JButton goBackButton = new JButton("Назад");
        goBackButton.setPreferredSize(new Dimension(170, 25));
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackActionPerformed(e);
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(setRoleButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        buttonsPanel.add(goBackButton);
        mainPanel.add(buttonsPanel);
    }

    private void setRoleActionPerformed(ActionEvent e) throws IOException, ClassNotFoundException {
        int reply = JOptionPane.showConfirmDialog(this,
                "Вы действительно хотите сделать " + selectedUser.getLogin() + " " +
                        (selectedUser.getRole() == Role.USER ? "модератором" : "обычным пользователем") + "?",
                "Изменение роли",
                JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            Message message = new Message();
            message.setOperationType(MessageType.GET);
            message.setMessageType(MessageType.USER);
            message.setMessage(selectedUser.getID());
            ClientThread.sendMessage(message);
            message = (Message) ClientThread.receiveMessage();
            User user = (User) message.getMessage();
            user.setRole(
                    (user.getRole() == Role.USER ? Role.MODER : Role.USER)
            );
            message = new Message();
            message.setOperationType(MessageType.UPDATE);
            message.setMessageType(MessageType.USER);
            message.setMessage(user);
            ClientThread.sendMessage(message);
            ClientThread.receiveMessage();
            usersTableModel.setValueAt(user.getRole(), usersTable.getSelectedRow(), 2);
        }
    }

    public void goBackActionPerformed(ActionEvent e) {
        this.dispose();
        parentFrame.setVisible(true);
    }

}

