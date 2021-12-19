package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.net.Socket;

import javax.swing.JButton;

import org.junit.BeforeClass;
import org.junit.Test;

import webserver.Frame;
import webserver.MyConstants;
import webserver.WebServer;

public class TestInterfaceStopAfterMaintenance implements MyConstants {
	private static WebServer webServer;
	private static Frame frame;
	private static JButton button;
	@BeforeClass
	public static void setup() {
		webServer = new WebServer(new Socket());
		frame = new Frame();
		button = new JButton();
		frame.actionPerformed(new ActionEvent(button, 0, START_SERVER));
		frame.actionPerformed(new ActionEvent(button, 0, START_MAINTENANCE));
		frame.actionPerformed(new ActionEvent(button, 0, STOP_SERVER));
	}
	@Test
	public void serverIsRunning() {
		assertFalse("Error: serverIsRunning", webServer.serverIsRunning());
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
	public void portSpinnerIsEnabled() {
		assertTrue("Error: portSpinnerIsEnabled", frame.portSpinnerIsEnabled());
	}
	@Test
	public void rootDirTextfieldIsEnabled() {
		assertTrue("Error: rootDirTextfieldIsEnabled", frame.rootDirTextfieldIsEnabled());
	}
	@Test
	public void maintenanceDirTextfieldIsEnabled() {
		assertTrue("Error: maintenanceDirTextfieldIsEnabled", frame.maintenanceDirTextfieldIsEnabled());
	}
}
