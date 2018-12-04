package by.stepovoy.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {

    private static final int PORT = 1502;
    private static int clientAmount = 1;
    private static Logger LOGGER = Logger.getLogger("MyLogger");

    public static void main(String[] args) {
        try {

            int port = 0;
            if (args.length != 0 && Integer.parseInt(args[0]) != 0 && args[0].length() == 4) {
                port = Integer.parseInt(args[0]);
            } else if (args.length == 0) {
                port = PORT;
            } else {
                LOGGER.info("INVALID PORT!");
                System.exit(0);
            }

            ServerSocket serverSocket = new ServerSocket(port);
            LOGGER.info("Server waiting connections ....");
            while (true) {
                Socket client = serverSocket.accept();
                LOGGER.info("Connected " + clientAmount + " client :\n" +
                        "Client information: " +
                        client.getInetAddress().getHostAddress());
                clientAmount++;
                ServerThread serverThread = new ServerThread(client);
                LOGGER.info("Current users online : " + ServerThread.connectionNumber);
                serverThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
