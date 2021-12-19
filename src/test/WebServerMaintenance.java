package test;

import static org.junit.Assert.assertEquals;

import java.net.Socket;

import javax.swing.JButton;

import org.junit.BeforeClass;
import org.junit.Test;

import webserver.Frame;
import webserver.MyConstants;
import webserver.WebServer;

public class WebServerMaintenance implements MyConstants{
	private static WebServer webServer;
	private static Frame frame;
	private static JButton button;
	@BeforeClass
	public static void setup() {
		webServer = new WebServer(new Socket());
		webServer.setServerIsRunning(true);
		webServer.setServerIsInMaintenance(true);
	}
	@Test(expected=NullPointerException.class)
	public void getResourceStatusCode_NullWhileInMaintenance() {
		WebServer.setServerIsInMaintenance(true);
		webServer.getResource(null);
		System.out.println("Test failed: getResourceStatusCode_NullWhileInMaintenance");
	}
	@Test
	public void getResourceStatusCode_EmptyWhileInMaintenance() {
		webServer.setServerIsInMaintenance(true);
		assertEquals("Test failed: getResourceStatusCode_EmptyWhileInMaintenance", OK_200, (String)webServer.getResource("")[2]);
	}
	@Test
	public void getResourceStatusCode_WhileInMaintenance() {
		webServer.setServerIsInMaintenance(true);
		assertEquals("Test failed: getResourceStatusCode_WhileInMaintenance", OK_200, (String)webServer.getResource(MAINTENANCE_FILE)[2]);
	}
}
