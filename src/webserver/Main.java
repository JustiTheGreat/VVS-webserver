package webserver;

import java.io.IOException;
import java.net.Socket;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		WebServer webServer = new WebServer();
		webServer.initialize();
		System.out.println("Connection Socket Created");
		while (webServer.isOpen()) {
			System.out.println("Waiting for Connection");
			try {
				Socket socket = webServer.getServerSocket().accept();
				webServer.setClientSocket(socket);
				new Thread(webServer).start();
			} catch (IOException e) {
				System.err.println("Accept failed");
				//System.exit(1);
			}
		}
		try {
			webServer.getServerSocket().close();
		} catch (IOException e) {
			System.err.println("Could not close port: " + webServer.getServerSocket().getLocalPort());
			System.exit(1);
		}
	}
}
