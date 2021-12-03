package webserver;

import java.net.*;
import java.util.Scanner;

import java.io.*;

public class WebServer extends Thread implements Runnable, MyConstants {
	private static boolean serverIsOpen = true;
	private static boolean serverIsRunning = false;
	private static boolean serverIsInMaintenance = false;
	private static ServerSocket serverSocket = null;
	private static String rootDirectory = DEFAULT_ROOT_DIR;
	private static String maintenanceDirectory = DEFAULT_MAINTENANCE_DIR;
	//setters
	public static void setServerIsRunning(boolean value) {
		serverIsRunning = value;
	}
	public static void setServerIsInMaintenance(boolean value) {
		serverIsInMaintenance = value;
	}
	public static boolean validateDirectoryPath(String path) throws NullPointerException {
        File file = new File(path);
        return file.isDirectory();
	}
	public static boolean setRootDirectory(String path) {
		if (validateDirectoryPath(path)) {
			rootDirectory = path;
			return true;
		}
		return false;
	}
	public static boolean setMaintenanceDirectory(String path) {
		if (validateDirectoryPath(path)) {
			maintenanceDirectory = path;
			return true;
		}
		return false;
	}
	public static void setServerSocket(int port) throws IllegalArgumentException {
		if(port < MIN_PORT || port > MAX_PORT)
			throw new IllegalArgumentException();
		try {
			if(serverSocket!=null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			System.err.println("Could not close port: " + serverSocket.getLocalPort());
			System.exit(-1);
		}
		try {
			serverSocket = new ServerSocket(port);
			System.err.println(serverSocket.getLocalPort());
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}
	}
	//getters
	public static ServerSocket getServerSocket() {
		return serverSocket;
	}
	//webserver
	private Socket clientSocket = null;
	
	public WebServer(Socket socket) {
		clientSocket = socket;
	}
	
	public static String read(String filepath) throws FileNotFoundException, NullPointerException {
		String s="";
		File file = new File(filepath);
		if(!file.exists()) {
			throw new FileNotFoundException();
		}
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			s += scanner.nextLine();
		}
		scanner.close();
		return s;
	}
	
	public String[] getResource(String filename) throws NullPointerException {
		if(filename==null)throw new NullPointerException();
		String[] file = new String[2];
		if(serverIsRunning) {
			try {
				if(filename.equals(FILE_NOT_FOUND_CSS)) {
					file[0] = read(DEFAULT_404_DIR + FILE_NOT_FOUND_CSS);
				} else if(!serverIsInMaintenance) {
					if(filename.equals("")) {
						file[0] = read(rootDirectory + DEFAULT_FILE);	
					} else {
						file[0] = read(rootDirectory + filename);
					}
				} else if(filename.endsWith(".css")) {
					file[0] = read(maintenanceDirectory + filename);
				} else {
					file[0] = read(maintenanceDirectory + MAINTENANCE_FILE);
				}
				file[1] = OK_200;
			} catch (FileNotFoundException e) {
				try {
					file[0] = read(DEFAULT_404_DIR + FILE_NOT_FOUND_HTML);
				} catch (FileNotFoundException e1) {
					System.err.println("404 error page not found!");
					System.exit(-1);
				}
				file[1] = NOT_FOUND_404;
		    }
		} else {
			file[0] = "";
			file[1] = REQUEST_TIMEOUT_408;
		}
		return file;
	}
	
	public void sendResponse(String[] file, OutputStream out) throws NullPointerException, IllegalArgumentException {
		if(file==null||file[0]==null||file[1]==null||out==null) {
			throw new NullPointerException();
		}
		if(file.length!=2) {
			throw new IllegalArgumentException();
		}
		if(!file[1].equals(OK_200)&&!file[1].equals(NOT_FOUND_404)&&!file[1].equals(REQUEST_TIMEOUT_408)) {
			throw new IllegalArgumentException();
		}
		
		String CRLF = "\n\r";
		String response = "HTTP/1.1 " + file[1] + CRLF + "Content-Length: " + file[0].getBytes().length + CRLF + CRLF + file[0] + CRLF + CRLF;
		try {
			out.write(response.getBytes());
		} catch (IOException e) {
			System.err.println("Error writing response!");
			System.exit(-1);
		}
    }
	
	public String format(String s) {
		String http = "HTTP/";
		String[] pieces = s.split(http)[0].split(" ");
		String result = "";
		for(int i = 0; i<pieces.length; i++) {
			if(i!=0) {
				result += " " + pieces[i];
			}
		}
		return result.substring(1);
	}
	
	public void run() {
		System.out.println("New Communication Thread Started");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			OutputStream out = clientSocket.getOutputStream();
			
			String inputLine;
			try {
				while ((inputLine = in.readLine()) != null) {
					if(inputLine.startsWith("GET")) {
						System.err.println(inputLine);
						//inputLine.split(" ")[1].substring(1)
						sendResponse(getResource(format(inputLine)), out);
				    }
					if (inputLine.trim().equals("")) {
						break;
					}
				}
			} catch (IOException e1) {
				System.err.println("Error reading request");
			}
			
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Frame();
		WebServer.setServerSocket(INITIAL_PORT);
		System.out.println("Connection Socket Created");
		while(serverIsOpen) {
			System.out.println(serverIsRunning);
			if(serverIsRunning)
			try {
				//while (true) {
					System.out.println("Waiting for Connection");
					new WebServer(serverSocket.accept()).start();
				//}
			} catch (IOException e) {
				System.err.println("Accept failed.");
				//System.exit(1);
			}
		}
		try {
			WebServer.getServerSocket().close();
		} catch (IOException e) {
			System.err.println("Could not close port: " + WebServer.getServerSocket().getLocalPort());
			System.exit(1);
		}
	}
}