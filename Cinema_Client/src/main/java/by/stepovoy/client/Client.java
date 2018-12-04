package by.stepovoy.client;

import by.stepovoy.view.MainPanel;

public class Client {

    public static final int PORT = 1502;

    public static void main(String[] args) {
        int port = 0;
        if (args.length == 0) {
            port = PORT;
            System.out.println("1502");
        } else if (Integer.parseInt(args[0]) != 0 && args[0].length() == 4) {
            port = Integer.parseInt(args[0]);
            System.out.println("second");
        } else {
            System.out.println("Invalid port!");
            System.exit(0);
        }
        MainPanel mainPanel = new MainPanel(port);
        mainPanel.setVisible(true);
    }
}
