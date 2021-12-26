package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.Socket;

import javax.swing.JButton;

import org.junit.BeforeClass;
import org.junit.Test;

import webserver.Frame;
import webserver.MyConstants;
import webserver.WebServer;

public class WebServerMaintenance implements MyConstants{
	private static WebServer webServer;
	@BeforeClass
	public static void setup() {
		webServer = new WebServer(new Socket());
		webServer.setServerIsRunning(true);
		webServer.setServerIsInMaintenance(true);
	}
	@Test
	public void testGetServerIsInMaintenanceAfterSet() {
		assertTrue("Test failed: testGetServerIsInMaintenanceAfterSet",webServer.serverIsInMaintenance());
	}
	@Test(expected=NullPointerException.class)
	public void getResourceStatusCode_NullWhileInMaintenance() {
		WebServer.setServerIsInMaintenance(true);
		webServer.getResource(null);
		System.out.println("Test failed: getResourceStatusCode_NullWhileInMaintenance");
	}
	@Test
	public void getResource_StatusCode_EmptyWhileInMaintenance() {
		assertEquals("Test failed: getResourceStatusCode_EmptyWhileInMaintenance", OK_200, (String)webServer.getResource("")[2]);
	}
	@Test
	public void getResource_ContentType_EmptyWhileInMaintenance() {
		assertEquals("Test failed: getResourceStatusCode_EmptyWhileInMaintenance", HTML, (String)webServer.getResource("")[1]);
	}
	@Test
	public void getResource_StatusCode_Default() {
		assertEquals("Test failed: getResource_StatusCode_Default", OK_200, (String)webServer.getResource(DEFAULT_FILE)[2]);
	}
	@Test
	public void getResource_ContentType_Default() {
		assertEquals("Test failed: getResource_StatusCode_Default", HTML, (String)webServer.getResource(DEFAULT_FILE)[1]);
	}
	@Test
	public void getResource_StatusCode_MaintenanceCSS() {
		assertEquals("Test failed: getResource_StatusCode_MaintenanceCSS", OK_200, (String)webServer.getResource(MAINTENANCE_CSS)[2]);
	}
	@Test
	public void getResource_ContentType_MaintenanceCSS() {
		assertEquals("Test failed: getResource_ContentType_MaintenanceCSS", CSS, (String)webServer.getResource(MAINTENANCE_CSS)[1]);
	}
	@Test
	public void getResource_StatusCode_TXT() {
		assertEquals("Test failed: getResource_StatusCode_TXT", OK_200, (String)webServer.getResource("a.txt")[2]);
	}
	@Test
	public void getResource_ContentType_TXT() {
		assertEquals("Test failed: getResource_ContentType_TXT", HTML, (String)webServer.getResource("a.txt")[1]);
	}
	@Test
	public void getResource_StatusCode_NotFound() {
		assertEquals("Test failed: getResource_StatusCode_NotFound", OK_200, (String)webServer.getResource("abc")[2]);
	}
	@Test
	public void getResource_ContentType_NotFound() {
		assertEquals("Test failed: getResource_ContentType_NotFound", HTML, (String)webServer.getResource("abc")[1]);
	}
}
