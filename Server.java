import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ClientHandler> clients;

    public static void main(String[] args) {

        int port = 5001;
        if (args.length == 0) {
        }
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            System.out.println("Usage: java Server <port>");
            System.out.println("default: 5001");
            System.exit(1);
        }
        Server server = new Server();
        server.start(port);
    }

    public void start(int port) {
        clients = new ArrayList<>();
        try {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server started on port " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected");

                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    clients.add(clientHandler);
                    clientHandler.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected");
    }
}
