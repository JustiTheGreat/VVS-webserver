package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;

import webserver.MyConstants;
import webserver.WebServer;

public class WebServerTest implements MyConstants {
	@Test(expected=IllegalArgumentException.class)
	public void testSetServerSocketPortSmallerThan1024() {
		WebServer webServer = new WebServer();
		webServer.setServerSocket(1023);
		System.out.println("Test failed: PortSmallerThan1024");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSetServerSocketPortBiggerThan65535() {
		WebServer webServer = new WebServer();
		webServer.setServerSocket(65536);
		System.out.println("Test failed: PortBiggerThan65535");
	}
//----------------------------------------------------------------------------------------------------------------------------------
	@Test
	public void testGetServerSocket() {
		WebServer webServer = new WebServer();
		assertEquals("Test failed: getServerSocket",null,webServer.getServerSocket());
	}
	@Test
	public void testGetServerSocketAfterInitialization() throws IOException {
		WebServer webServer = new WebServer();
		webServer.initialize();
		assertEquals("Test failed: getServerSocketAfterInitialization",10008,webServer.getServerSocket().getLocalPort());
	}
//----------------------------------------------------------------------------------------------------------------------------------	
	@Test(expected=NullPointerException.class)
	public void testValidateNullDirPath() {
		WebServer.validateDirPath(null);
		System.out.println("Test failed: NullDirPath");
	}
	@Test
	public void testValidateRelativeDirPath() {
		assertTrue("Test failed: RelativeDirPath",WebServer.validateDirPath("src/"));
	}
	@Test
	public void testValidateaAbsoluteDirPath() {
		assertTrue("Test failed: AbsoluteDirPath",WebServer.validateDirPath("C:/Users/justi/Desktop/"));
	}
	@Test
	public void testValidateaNotADirPath() {
		assertFalse("Test failed: NotADirPath",WebServer.validateDirPath("src/nonexist/"));
	}
//----------------------------------------------------------------------------------------------------------------------------------	
	@Test(expected = NullPointerException.class)
	public void testNullPathRead() {
		WebServer.validateDirPath(null);
		System.out.println("Test failed: ReadNull");
	}
	@Test(expected = FileNotFoundException.class)
	public void testNotFoundRead() {
		WebServer.validateDirPath("@1234");
		System.out.println("Test failed: ReadNull");
	}
//----------------------------------------------------------------------------------------------------------------------------------	
//	@Test
//	public void getResourceResource_Empty() {
//		WebServer mockWebServer = mock(WebServer.class);
//		when(mockWebServer.isRunning()).thenReturn(true);
//		mockWebServer.initialize();
//		assertEquals("Test failed: getResourceResource_Empty", DEFAULT_ROOT_DIR + DEFAULT_FILE, mockWebServer.getResource("")[0]);
//	}
	
	@Test(expected=NullPointerException.class)
	public void getResourceStatusCode_Null() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.setServerIsRunning(true);
		webServer.getResource(null);
		System.out.println("Test failed: getResourceStatusCode_Null");
	}
	@Test
	public void getResourceStatusCode_Empty() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.setServerIsRunning(true);
		assertEquals("Test failed: getResourceStatusCode_Null", OK_200, webServer.getResource("")[1]);
	}
	@Test
	public void getResourceStatusCode_Default() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.setServerIsRunning(true);
		assertEquals("Test failed: getResourceStatusCode_Default", OK_200, webServer.getResource(DEFAULT_FILE)[1]);
	}
	@Test
	public void getResourceStatusCode_NotFound() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.setServerIsRunning(true);
		assertEquals("Test failed: getResourceStatusCode_NotFound", OK_200, webServer.getResource("abc")[1]);
	}
	@Test(expected=NullPointerException.class)
	public void getResourceStatusCode_NullWhileInMaintenance() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.setServerIsRunning(true);
		webServer.setMaintenace(true);
		webServer.getResource(null);
		System.out.println("Test failed: getResourceStatusCode_NullWhileInMaintenance");
	}
	@Test
	public void getResourceStatusCode_EmptyWhileInMaintenance() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.setServerIsRunning(true);
		webServer.setMaintenace(true);
		assertEquals("Test failed: getResourceStatusCode_EmptyWhileInMaintenance", OK_200, webServer.getResource("")[1]);
	}
	@Test
	public void getResourceStatusCode_WhileInMaintenance() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.setServerIsRunning(true);
		webServer.setMaintenace(true);
		assertEquals("Test failed: getResourceStatusCode_WhileInMaintenance", OK_200, webServer.getResource(MAINTENANCE_FILE)[1]);
	}
	@Test
	public void getResourceStatusCode_WhileStopped() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.setServerIsRunning(false);
		assertEquals("Test failed: getResourceStatusCode_WhileStopped", REQUEST_TIMEOUT_408, webServer.getResource(DEFAULT_FILE)[1]);
	}
//----------------------------------------------------------------------------------------------------------------------------------	
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullFile() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.sendResponse(null,System.out);
		System.out.println("Test failed: sendResponse_NullFile");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullFile0() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.sendResponse(new String[]{null,OK_200},System.out);
		System.out.println("Test failed: sendResponse_NullFile0");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullFile1() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.sendResponse(new String[]{"",null},System.out);
		System.out.println("Test failed: sendResponse_NullFile1");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullOutputStream() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.sendResponse(new String[]{"",OK_200},null);
		System.out.println("Test failed: sendResponse_NullOutputStream");
	}
	@Test(expected = IllegalArgumentException.class)
	public void sendResponse_WrongSizeFileVector() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.sendResponse(new String[]{},System.out);
		System.out.println("Test failed: sendResponse_WrongSizeFileVector");
	}
	@Test(expected = IllegalArgumentException.class)
	public void sendResponse_WrongResponseStatus() {
		WebServer webServer = new WebServer();
		webServer.initialize();
		webServer.sendResponse(new String[]{"","ceva"},System.out);
		System.out.println("Test failed: sendResponse_WrongResponseStatus");
	}
}
