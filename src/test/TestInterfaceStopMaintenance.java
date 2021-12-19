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

public class TestInterfaceStopMaintenance implements MyConstants{
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
		frame.actionPerformed(new ActionEvent(button, 0, STOP_MAINTENANCE));
	}
	@Test
	public void serverIsNotInMaintenance() {
		assertFalse("Error: serverIsNotInMaintenance", webServer.serverIsInMaintenance());
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
		assertFalse("Error: checkboxIsSelected", frame.checkboxIsSelected());
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
