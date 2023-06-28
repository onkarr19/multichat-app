import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            String username = reader.readLine();
            System.out.println("New user connected: " + username);

            String message;
            while ((message = reader.readLine()) != null) {
                server.broadcastMessage(username + ": " + message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    private void close() {
        try {
            reader.close();
            writer.close();
            socket.close();
            server.removeClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
