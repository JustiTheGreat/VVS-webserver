package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;

import org.junit.BeforeClass;
import org.junit.Test;

import webserver.Frame;
import webserver.MyConstants;
import webserver.WebServer;

public class TestInterfaceStart implements MyConstants {
	private static WebServer webServer;
	private static Frame frame;
	private static JButton button;
	@BeforeClass
	public static void setup() {
		webServer = new WebServer(new Socket());
		frame = new Frame();
		button = new JButton();
		frame.actionPerformed(new ActionEvent(button, 0, START_SERVER));
	}
	@Test
	public void serverIsRunning() {
		assertTrue("Error: serverIsRunning", webServer.serverIsRunning());
	}
	@Test
	public void IPAddressIsCorrect() {
		System.err.println(webServer.getServerSocket().getLocalSocketAddress());
		System.err.println(webServer.getServerSocket().getInetAddress().getHostAddress());
		assertTrue("Error: IPAddressIsCorrect", frame.getIPAddress().equals(webServer.getServerSocket().getInetAddress().getHostAddress()));
	}
	@Test
	public void portIsCorrectInFrame() {
		assertTrue("Error: portIsCorrectInFrame", frame.getDisplayedPort().equals(frame.getSpinnerPort()));
	}
	@Test
	public void portIsCorrectInWebServer() {
		assertTrue("Error: portIsCorrectInWebServer", String.valueOf(webServer.getServerSocket().getLocalPort()).equals(frame.getSpinnerPort()));
	}
	@Test
	public void rootDirIsCorrectInWebServer() {
		assertTrue("Error: rootDirIsCorrectInWebServer", frame.getTextfieldRootDir().equals(webServer.getRootDir()));
	}
	@Test
	public void maintenanceDirIsCorrectInWebServer() {
		assertTrue("Error: maintenanceDirIsCorrectInWebServer", frame.getTextfieldMaintenanceDir().equals(webServer.getMaintenanceDir()));
	}
	@Test
	public void checkboxIsNotSelected() {
		assertFalse("Error: checkboxIsNotSelected", frame.checkboxIsSelected());
	}
	@Test
	public void portSpinnerIsNotEnabled() {
		assertFalse("Error: portSpinnerIsNotEnabled", frame.portSpinnerIsEnabled());
	}
	@Test
	public void rootDirTextfieldIsNotEnabled() {
		assertFalse("Error: rootDirTextfieldIsNotEnabled", frame.rootDirTextfieldIsEnabled());
	}
	@Test
	public void maintenanceDirTextfieldIsEnabled() {
		assertTrue("Error: maintenanceDirTextfieldIsEnabled", frame.maintenanceDirTextfieldIsEnabled());
	}
}
