import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
	public static void main(String[] args) {
		final ServerSocket serverSocket;
		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;
		final Scanner sc = new Scanner(System.in);
		
		
		try {
			serverSocket = new ServerSocket(5000);
			clientSocket = serverSocket.accept();
			
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			Thread sender = new Thread(new Runnable() {
				String message;
				@Override
				public void run() {
					while (true) {
						message = sc.nextLine();
						out.println(message);
						out.flush();
					}
				}
			});
			
			sender.start();
			
			
			Thread receiver = new Thread(new Runnable() {
				String message;
				@Override
				public void run() {
					try {
						message = in.readLine();
						while (message!=null) {
							System.out.println("Client: " + message);
							message = in.readLine();
						}
						System.out.println("Client deconnects");
						out.close();
						clientSocket.close();
						serverSocket.close();
					} catch (Exception e) {
							e.printStackTrace();
					}
				}
			});
			
			receiver.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
