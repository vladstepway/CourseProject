package by.stepovoy.client;

import by.stepovoy.view.MainPanel;

import java.util.logging.Logger;

public class Client {

    private static final int PORT = 1502;
    private static Logger LOGGER = Logger.getLogger("MyLogger");

    public static void main(String[] args) {
        int port = 0;
        if (args.length == 0) {
            port = PORT;
        } else if (Integer.parseInt(args[0]) != 0 && args[0].length() == 4) {
            port = Integer.parseInt(args[0]);
        } else {
            LOGGER.info("Invalid port!");
            System.exit(0);
        }
        MainPanel mainPanel = new MainPanel(port);
        mainPanel.setVisible(true);
    }
}
