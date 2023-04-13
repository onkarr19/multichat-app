import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		final Socket clientSocket;
		final BufferedReader in;
		final PrintWriter out;
		final Scanner sc = new Scanner(System.in);
		
		
		try {
			clientSocket = new Socket("127.0.0.1", 5000);
			
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
							System.out.println("Server: " + message);
							message = in.readLine();
						}
						System.out.println("Out of Service...");
						out.close();
						clientSocket.close();
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
