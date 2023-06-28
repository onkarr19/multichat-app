import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public static void main(String[] args) {

        int port = 5001;
        String serverIP = "localhost";

        if (args.length == 0) {
        } else if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else if (args.length == 2) {
            serverIP = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            System.out.println("Usage: java Client <server-ip> <port>");
            System.out.println("default: localhost 5001");
            System.exit(1);
        }

        System.out.println("Using server IP " + serverIP + " and port " + port);

        try {
            Client client = new Client();
            client.start(serverIP, port);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number");
            System.exit(1);
        }

        // Client client = new Client();
        // client.start("localhost", 5001);
    }

    public void start(String serverIP, int port) {
        try {
            socket = new Socket(serverIP, port);
            System.out.println("Connected to server on port " + port);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            Thread readThread = new Thread(this::readLoop);
            Thread writeThread = new Thread(this::writeLoop);

            readThread.start();
            writeThread.start();

            readThread.join();
            writeThread.join();
        } catch (IOException | InterruptedException e) {
            // e.printStackTrace();
            System.out.println("Failed to connect to server on port " + port);
            System.out.println("Make sure the server is running and try again");
        }
    }

    private void readLoop() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void writeLoop() {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String message;
            while ((message = consoleReader.readLine()) != null) {
                writer.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        try {
            reader.close();
            writer.close();
            socket.close();
            System.out.println("Disconnected from server");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
