package webserver;

import java.awt.Color;

public interface MyConstants {
	//strings
  	static final String TITLE_WHILE_STOPPED = "VVS Web Server - [Stopped]";
  	static final String TITLE_WHILE_RUNNING = "VVS Web Server - [Running]";
  	static final String TITLE_WHILE_MAINTAINING = "VVS Web Server - [Maintenance]";
  	static final String NOT_RUNNING = "not running";
  	static final String RUNNING = "running";
  	static final String MAINTENANCE = "maintenance";
  	static final String START_SERVER = "Start server";
  	static final String STOP_SERVER = "Stop server";
  	static final String START_MAINTENANCE = "Start maintenance";
  	static final String STOP_MAINTENANCE = "Stop maintenance";
  	static final String SERVER_LISTENING_ON_PORT = "Server listening on port:";
  	static final String WEB_ROOT_DIRECTORY = "Web root directory:";
  	static final String MAINTENANCE_DIRECTORY = "Maintenance directory:";
	//colors
	static final Color BLACK = new Color(0,0,0);
	static final Color RED = new Color(255,0,0);
	//connection
	static final int MIN_PORT = 1024;
	static final int MAX_PORT = 65535;
	static final int INITIAL_PORT = 10008;
	//default directories and files
	static final String DEFAULT_ROOT_DIR = "src/htdocs/TestSite/";
	static final String DEFAULT_MAINTENANCE_DIR = "src/htdocs/maintenance_directory/";
	static final String DEFAULT_404_DIR = "src/htdocs/";
	static final String DEFAULT_FILE = "a.html";
	static final String MAINTENANCE_FILE = "maintenance.html";
	static final String MAINTENANCE_CSS = "maintenance.css";
	static final String FILE_NOT_FOUND_HTML = "404.html";
	static final String FILE_NOT_FOUND_CSS = "404.css";
	//response statuses
	static final String OK_200 = "200 OK";
	static final String NOT_FOUND_404 = "404 Not Found";
	static final String REQUEST_TIMEOUT_408 = "408 Request Timeout";
	//response content types
	static final String HTML = "text/html";
	static final String CSS = "text/css";
	static final String JPG = "image/jpeg";
}
