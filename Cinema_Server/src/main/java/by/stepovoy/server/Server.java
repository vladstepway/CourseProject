package by.stepovoy.server;

import java.net.ServerSocket;

public class Server {

    private static final int PORT = 1502;

    public static void main(String[] args) {
        int port = 0;
        try {
            if (args.length != 0 && Integer.parseInt(args[0]) != 0 && args[0].length() == 4) {
                port = Integer.parseInt(args[0]);
                System.out.println("first");
            } else if (args.length == 0) {
                port = PORT;
                System.out.println("1502");
            } else {
                System.out.println("Invalid port!");
                System.exit(0);
            }
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                new ServerThread(serverSocket.accept()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
