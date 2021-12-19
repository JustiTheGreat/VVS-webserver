package webserver;

import java.net.*;
import java.util.Collection;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;

public class WebServer extends Thread implements Runnable, MyConstants {
	private static boolean serverIsOpen = true;
	private static boolean serverIsRunning = false;
	private static boolean serverIsInMaintenance = false;
	private static ServerSocket serverSocket = null;
	private static String rootDirectory = DEFAULT_ROOT_DIR;
	private static String maintenanceDirectory = DEFAULT_MAINTENANCE_DIR;
	//verifiers
	public boolean serverIsRunning() {
		return serverIsRunning;
	}
	public boolean serverIsInMaintenance() {
		return serverIsInMaintenance;
	}
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
	public synchronized static void setServerSocket(int port) throws IllegalArgumentException {
		if(port < MIN_PORT || port > MAX_PORT)
			throw new IllegalArgumentException();
		if(serverSocket!=null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}
	}
	//getters
	public static ServerSocket getServerSocket() {
		return serverSocket;
	}
	public static String getRootDir() {
		return rootDirectory;
	}
	public static String getMaintenanceDir() {
		return maintenanceDirectory;
	}
	//web server
	private Socket clientSocket = null;
	
	public WebServer(Socket socket) {
		clientSocket = socket;
	}
	
	private static File search(File file, String filename) {
		if (file.isDirectory()) {
			if (file.canRead()) {
				for (File temp : file.listFiles()) {
					if (temp.isDirectory()) {
						File wantedFile = search(temp, filename);
						if(wantedFile!=null) {
							return wantedFile;
						}
					} else {
						if (filename.equals(temp.getName().toLowerCase())) {			
							return new File(temp.getAbsoluteFile().toString());
						}
					}
				}
			}
		}
		return null;
	}
	
	public static Object read(String filepath) throws NullPointerException, IOException, FileNotFoundException {
		File file = new File(filepath);
		if(!file.exists()) {
			String filename = filepath.split("/")[filepath.split("/").length-1];
			File root = new File(rootDirectory);
			if (root.isDirectory()) {
				File wantedFile = search(root, filename);
				if(wantedFile!=null) {
					file = wantedFile;
				} else {
					throw new FileNotFoundException();
				}
			} else {
				throw new FileNotFoundException();
			}
		}
		if(filepath.endsWith(".jpg")) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(ImageIO.read(file), "jpg", baos);
			baos.flush();
			return baos.toByteArray();
		}
		String s="";
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			s += scanner.nextLine();
		}
		scanner.close();
		return s.getBytes();
	}
	
	public Object[] getResource(String filename) throws NullPointerException {
		if(filename==null)throw new NullPointerException();
		if(serverIsRunning) {
			try {
				if(serverIsInMaintenance) {
					if(filename.equals(MAINTENANCE_CSS)) {
						return new Object[] {read(maintenanceDirectory + filename), CSS, OK_200};
					}
					if(filename.endsWith(".jpg")) {
						return new Object[] {read(maintenanceDirectory + filename), JPG, OK_200};
					}
					return new Object[] {read(maintenanceDirectory + MAINTENANCE_FILE), HTML, OK_200};
				} else {
					if(filename.equals("")) {
						return new Object[] {read(rootDirectory + DEFAULT_FILE), HTML, OK_200};
					}
					if(filename.endsWith(".css")) {
						return new Object[] {read(rootDirectory + filename), CSS, OK_200};
					}
					if(filename.endsWith(".jpg")) {
						return new Object[] {read(rootDirectory + filename), JPG, OK_200};
					}
					if(filename.endsWith(".html")) {
						return new Object[] {read(rootDirectory + filename), HTML, OK_200};
					}
					return new Object[] {read(rootDirectory + filename), HTML, OK_200};
				}
			} catch (FileNotFoundException e) {
				try {
					if(filename.equals(FILE_NOT_FOUND_CSS)) {
						return new Object[] {read(DEFAULT_404_DIR + FILE_NOT_FOUND_CSS), CSS, OK_200};
					}
					if(filename.endsWith(".jpg")) {
						return new Object[] {read(DEFAULT_404_DIR + filename), JPG, OK_200};
					}
					return new Object[] {read(DEFAULT_404_DIR + FILE_NOT_FOUND_HTML), HTML, NOT_FOUND_404};
				} catch (FileNotFoundException e1) {
					System.err.println("404 error page not found!");
					System.exit(-1);
				} catch (IOException e1) {
					e1.printStackTrace();
					System.exit(-1);
				}
		    } catch (IOException e) {
		    	e.printStackTrace();
				System.exit(-1);
			}
		}
		return new Object[] {"".getBytes(), HTML, REQUEST_TIMEOUT_408};
	}
	
	public void sendResponse(Object[] data, OutputStream out) throws NullPointerException, IllegalArgumentException {
		if(data==null||out==null) {
			throw new NullPointerException();
		}
		if(data.length != 3) {
			throw new IllegalArgumentException();
		}
		if(data[0]==null||data[1]==null||data[2]==null) {
			throw new NullPointerException();
		}
		if(!data[1].equals(HTML)&&!data[1].equals(CSS)&&!data[1].equals(JPG)) {
			throw new IllegalArgumentException();
		}
		if(!data[2].equals(OK_200)&&!data[2].equals(NOT_FOUND_404)&&!data[2].equals(REQUEST_TIMEOUT_408)) {
			throw new IllegalArgumentException();
		}
		
		String CRLF = "\r\n";
		byte[] bytes = (byte[])data[0];
		String contentType = (String)data[1];
		String status = (String)data[2];
		byte[] responsePart1 = null;
		
		responsePart1 = ("HTTP/1.1 " + status + CRLF + "Content-Type: " + contentType + CRLF + "Content-Length: " + bytes.length + CRLF + CRLF).getBytes();
		byte[] responsePart2 = bytes;
		byte[] responsePart3 = (CRLF + CRLF).getBytes();
		
		byte[] response = new byte[responsePart1.length + responsePart2.length + responsePart3.length];
		System.arraycopy(responsePart1, 0, response, 0, responsePart1.length);
		System.arraycopy(responsePart2, 0, response, responsePart1.length, responsePart2.length);
		System.arraycopy(responsePart3, 0, response, responsePart1.length + responsePart2.length, responsePart3.length);
		
		try {
			out.write(response);
		} catch (IOException e) {
			System.err.println("Error writing response!");
			System.exit(-1);
		}
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
						sendResponse(getResource(inputLine.split(" ")[1].substring(1).replace("%20", " ")), out);
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
			Thread.sleep(10);
			if(serverIsRunning) {
				try {
					System.out.println("Waiting for Connection");
					new WebServer(serverSocket.accept()).start();
				} catch (IOException e) {
					System.err.println("Accept failed.");
					//System.exit(1);
				}
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