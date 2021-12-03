package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import org.junit.BeforeClass;
import org.junit.Test;

import webserver.MyConstants;
import webserver.WebServer;

public class WebServerTest implements MyConstants {
	private static WebServer webServer;
	@BeforeClass
	public static void setup() {
		webServer = new WebServer(new Socket());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSetServerSocketPortSmallerThan1024() {
		WebServer.setServerSocket(1023);
		System.out.println("Test failed: PortSmallerThan1024");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSetServerSocketPortBiggerThan65535() {
		WebServer.setServerSocket(65536);
		System.out.println("Test failed: PortBiggerThan65535");
	}
//----------------------------------------------------------------------------------------------------------------------------------
	@Test
	public void testGetServerSocket() {
		assertEquals("Test failed: getServerSocket",null,WebServer.getServerSocket());
	}
	@Test
	public void testGetServerSocketAfterSet() throws IOException {
		WebServer.setServerSocket(10008);
		assertEquals("Test failed: getServerSocketAfterSet",10008,WebServer.getServerSocket().getLocalPort());
	}
//----------------------------------------------------------------------------------------------------------------------------------	
	@Test(expected=NullPointerException.class)
	public void testValidateNullDirPath() {
		WebServer.validateDirectoryPath(null);
		System.out.println("Test failed: NullDirPath");
	}
	@Test
	public void testValidateRelativeDirPath() {
		assertTrue("Test failed: RelativeDirPath",WebServer.validateDirectoryPath("src/"));
	}
	@Test
	public void testValidateaAbsoluteDirPath() {
		assertTrue("Test failed: AbsoluteDirPath",WebServer.validateDirectoryPath("C:/Users/justi/Desktop/"));
	}
	@Test
	public void testValidateaNotADirPath() {
		assertFalse("Test failed: NotADirPath",WebServer.validateDirectoryPath("src/nonexist/"));
	}
//----------------------------------------------------------------------------------------------------------------------------------	
	@Test(expected = NullPointerException.class)
	public void testNullPathRead() throws FileNotFoundException, NullPointerException {
		WebServer.read(null);
		System.out.println("Test failed: ReadNull");
	}
	@Test(expected = FileNotFoundException.class)
	public void testNotFoundRead() throws FileNotFoundException, NullPointerException {
		WebServer.read("@1234");
		System.out.println("Test failed: ReadNull");
	}
//----------------------------------------------------------------------------------------------------------------------------------	
//	@Test
//	public void getResourceResource_Empty() {
//		WebServer webServer = new WebServer(new Socket());
//		Main main = mock(Main.class);
//		when(main.serverIsRunning()).thenReturn(true);
//		assertEquals("Test failed: getResourceResource_Empty", DEFAULT_ROOT_DIR + DEFAULT_FILE, webServer.getResource("")[0]);
//	}
	
	@Test(expected=NullPointerException.class)
	public void getResourceStatusCode_Null() {
		WebServer.setServerIsRunning(true);
		webServer.getResource(null);
		System.out.println("Test failed: getResourceStatusCode_Null");
	}
	@Test
	public void getResourceStatusCode_Empty() {
		WebServer.setServerIsRunning(true);
		assertEquals("Test failed: getResourceStatusCode_Null", OK_200, webServer.getResource("")[1]);
	}
	@Test
	public void getResourceStatusCode_Default() {
		WebServer.setServerIsRunning(true);
		assertEquals("Test failed: getResourceStatusCode_Default", OK_200, webServer.getResource(DEFAULT_FILE)[1]);
	}
	@Test
	public void getResourceStatusCode_NotFound() {
		WebServer.setServerIsRunning(true);
		assertEquals("Test failed: getResourceStatusCode_NotFound", OK_200, webServer.getResource("abc")[1]);
	}
	@Test(expected=NullPointerException.class)
	public void getResourceStatusCode_NullWhileInMaintenance() {
		WebServer.setServerIsRunning(true);
		WebServer.setServerIsInMaintenance(true);
		webServer.getResource(null);
		System.out.println("Test failed: getResourceStatusCode_NullWhileInMaintenance");
	}
	@Test
	public void getResourceStatusCode_EmptyWhileInMaintenance() {
		WebServer.setServerIsRunning(true);
		WebServer.setServerIsInMaintenance(true);
		assertEquals("Test failed: getResourceStatusCode_EmptyWhileInMaintenance", OK_200, webServer.getResource("")[1]);
	}
	@Test
	public void getResourceStatusCode_WhileInMaintenance() {
		WebServer.setServerIsRunning(true);
		WebServer.setServerIsInMaintenance(true);
		assertEquals("Test failed: getResourceStatusCode_WhileInMaintenance", OK_200, webServer.getResource(MAINTENANCE_FILE)[1]);
	}
	@Test
	public void getResourceStatusCode_WhileStopped() {
		WebServer.setServerIsRunning(false);
		assertEquals("Test failed: getResourceStatusCode_WhileStopped", REQUEST_TIMEOUT_408, webServer.getResource(DEFAULT_FILE)[1]);
	}
//----------------------------------------------------------------------------------------------------------------------------------	
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullFile() {
		webServer.sendResponse(null,System.out);
		System.out.println("Test failed: sendResponse_NullFile");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullFile0() {
		webServer.sendResponse(new String[]{null,OK_200},System.out);
		System.out.println("Test failed: sendResponse_NullFile0");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullFile1() {
		webServer.sendResponse(new String[]{"",null},System.out);
		System.out.println("Test failed: sendResponse_NullFile1");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullOutputStream() {
		webServer.sendResponse(new String[]{"",OK_200},null);
		System.out.println("Test failed: sendResponse_NullOutputStream");
	}
	@Test(expected = IllegalArgumentException.class)
	public void sendResponse_WrongSizeFileVector() {
		webServer.sendResponse(new String[]{},System.out);
		System.out.println("Test failed: sendResponse_WrongSizeFileVector");
	}
	@Test(expected = IllegalArgumentException.class)
	public void sendResponse_WrongResponseStatus() {
		webServer.sendResponse(new String[]{"","ceva"},System.out);
		System.out.println("Test failed: sendResponse_WrongResponseStatus");
	}
}
