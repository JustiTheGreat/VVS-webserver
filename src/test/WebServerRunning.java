package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;

import org.junit.BeforeClass;
import org.junit.Test;

import webserver.Frame;
import webserver.MyConstants;
import webserver.WebServer;

public class WebServerRunning implements MyConstants {
	private static WebServer webServer;
	@BeforeClass
	public static void setup() {
		webServer = new WebServer(new Socket());
		webServer.setServerIsRunning(true);
	}
	@Test
	public void testGetServerIsRunningAfterSet() {
		assertTrue("Test failed: testGetServerIsRunningAfterSet",webServer.serverIsRunning());
	}
	@Test
	public void testGetServerIsInMaintenance() {
		assertFalse("Test failed: testGetServerIsInMaintenance",webServer.serverIsInMaintenance());
	}
//----------------------------------------------------------------------------------------------------------set/getRootDirectory
	@Test(expected=NullPointerException.class)
	public void setRootDir_NULL() {
		webServer.setRootDirectory(null);
		System.out.println("Test failed: setRootDir_NULL");
	}
	@Test
	public void setRootDir_RelativeDirPath() {
		assertTrue("Test failed: setRootDir_RelativeDirPath",webServer.setRootDirectory("src/"));
	}
	@Test
	public void setRootDir_AbsoluteDirPath() {
		assertTrue("Test failed: setRootDir_AbsoluteDirPath",webServer.setRootDirectory("C:/Users/justi/Desktop/"));
	}
	@Test
	public void setRootDir_NotADirPath() {
		assertFalse("Test failed: setRootDir_NotADirPath",webServer.setRootDirectory("src/nonexistent/"));
	}
	@Test
	public void getRootDir() {
		webServer.setRootDirectory("C:/Users/justi/Desktop/");
		assertTrue("Test failed: getRootDir",webServer.getRootDir().equals("C:/Users/justi/Desktop/"));
	}
	@Test
	public void getRootDir_AfterFailedSet() {
		webServer.setRootDirectory("C:/Users/justi/Desktop/");
		webServer.setRootDirectory("src/nonexistent/");
		assertTrue("Test failed: getRootDir",webServer.getRootDir().equals("C:/Users/justi/Desktop/"));
	}
//----------------------------------------------------------------------------------------------------------set/getMaintenanceDirectory
	@Test(expected=NullPointerException.class)
	public void setMaintenanceDir_NULL() {
		webServer.setMaintenanceDirectory(null);
		System.out.println("Test failed: setMaintenanceDir_NULL");
	}
	@Test
	public void setMaintenanceDir_RelativeDirPath() {
		assertTrue("Test failed: setMaintenanceDir_RelativeDirPath",webServer.setMaintenanceDirectory("src/"));
	}
	@Test
	public void setMaintenanceDir_AbsoluteDirPath() {
		assertTrue("Test failed: setMaintenanceDir_AbsoluteDirPath",webServer.setMaintenanceDirectory("C:/Users/justi/Desktop/"));
	}
	@Test
	public void setMaintenanceDir_NotADirPath() {
		assertFalse("Test failed: setMaintenanceDir_NotADirPath",webServer.setMaintenanceDirectory("src/nonexistent/"));
	}
	@Test
	public void getMaintenanceDir() {
		webServer.setMaintenanceDirectory("C:/Users/justi/Desktop/");
		assertTrue("Test failed: getMaintenanceDir",webServer.getMaintenanceDir().equals("C:/Users/justi/Desktop/"));
	}
	@Test
	public void getMaintenanceDir_AfterFailedSet() {
		webServer.setMaintenanceDirectory("C:/Users/justi/Desktop/");
		webServer.setMaintenanceDirectory("src/nonexistent/");
		assertTrue("Test failed: getMaintenanceDir",webServer.getMaintenanceDir().equals("C:/Users/justi/Desktop/"));
	}
//----------------------------------------------------------------------------------------------------------set/getServerSocket
	@Test
	public void testGetServerSocketAfterSet() throws IOException {
		webServer.setServerSocket(10008);
		assertTrue("Test failed: getServerSocketAfterSet",10008==WebServer.getServerSocket().getLocalPort());
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSetServerSocketPortSmallerThan1024() {
		webServer.setServerSocket(1023);
		System.out.println("Test failed: PortSmallerThan1024");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testSetServerSocketPortBiggerThan65535() {
		webServer.setServerSocket(65536);
		System.out.println("Test failed: PortBiggerThan65535");
	}
//----------------------------------------------------------------------------------------------------------validateDirPath
	@Test(expected=NullPointerException.class)
	public void validateNullDirPath() {
		webServer.validateDirectoryPath(null);
		System.out.println("Test failed: validateNullDirPath");
	}
	@Test
	public void validateRelativeDirPath() {
		assertTrue("Test failed: validateRelativeDirPath",webServer.validateDirectoryPath("src/"));
	}
	@Test
	public void validateAbsoluteDirPath() {
		assertTrue("Test failed: validateAbsoluteDirPath",webServer.validateDirectoryPath("C:/Users/justi/Desktop/"));
	}
	@Test
	public void validateNotADirPath() {
		assertFalse("Test failed: validateNotADirPath",webServer.validateDirectoryPath("src/nonexistent/"));
	}
//----------------------------------------------------------------------------------------------------------read
	@Test(expected = NullPointerException.class)
	public void testNullPathRead() throws NullPointerException, IOException {
		webServer.read(null);
		System.out.println("Test failed: ReadNull");
	}
	@Test(expected = FileNotFoundException.class)
	public void testNotFoundRead() throws NullPointerException, IOException {
		webServer.read("@1234");
		System.out.println("Test failed: ReadNull");
	}
//----------------------------------------------------------------------------------------------------------getResource
//	@Test
//	public void getResourceResource_Empty() {
//		WebServer webServer = new WebServer(new Socket());
//		Main main = mock(Main.class);
//		when(main.serverIsRunning()).thenReturn(true);
//		assertEquals("Test failed: getResourceResource_Empty", DEFAULT_ROOT_DIR + DEFAULT_FILE, webServer.getResource("")[0]);
//	}
	
	@Test(expected=NullPointerException.class)
	public void getResource_Null() {
		webServer.getResource(null);
		System.out.println("Test failed: getResource_Null");
	}
	@Test
	public void getResource_StatusCode_Empty() {
		assertEquals("Test failed: getResource_StatusCode_Empty", OK_200, (String)webServer.getResource("")[2]);
	}
	@Test
	public void getResource_ContentType_Empty() {
		assertEquals("Test failed: getResource_ContentType_Empty", HTML, (String)webServer.getResource("")[1]);
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
	public void getResource_StatusCode_404CSS() {
		assertEquals("Test failed: getResource_StatusCode_MaintenanceCSS", OK_200, (String)webServer.getResource(FILE_NOT_FOUND_CSS)[2]);
	}
	@Test
	public void getResource_ContentType_404CSS() {
		assertEquals("Test failed: getResource_ContentType_404CSS", CSS, (String)webServer.getResource(FILE_NOT_FOUND_CSS)[1]);
	}
	@Test
	public void getResource_StatusCode_JPG() {
		assertEquals("Test failed: getResource_StatusCode_JPG", OK_200, (String)webServer.getResource("yes.jpg")[2]);
	}
	@Test
	public void getResource_ContentType_JPG() {
		assertEquals("Test failed: getResource_ContentType_JPG", JPG, (String)webServer.getResource("yes.jpg")[1]);
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
		assertEquals("Test failed: getResource_StatusCode_NotFound", NOT_FOUND_404, (String)webServer.getResource("abc")[2]);
	}
	@Test
	public void getResource_ContentType_NotFound() {
		assertEquals("Test failed: getResource_ContentType_NotFound", HTML, (String)webServer.getResource("abc")[1]);
	}
//----------------------------------------------------------------------------------------------------------sendResponse
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullData() {
		webServer.sendResponse(null,System.out);
		System.out.println("Test failed: sendResponse_NullData");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullData0() {
		webServer.sendResponse(new String[]{null,HTML,OK_200},System.out);
		System.out.println("Test failed: sendResponse_NullData0");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullData1() {
		webServer.sendResponse(new String[]{"",null,OK_200},System.out);
		System.out.println("Test failed: sendResponse_NullData1");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullData2() {
		webServer.sendResponse(new String[]{"",HTML,null},System.out);
		System.out.println("Test failed: sendResponse_NullData2");
	}
	@Test(expected = NullPointerException.class)
	public void sendResponse_NullOutputStream() {
		webServer.sendResponse(new String[]{"",HTML,OK_200},null);
		System.out.println("Test failed: sendResponse_NullOutputStream");
	}
	@Test(expected = IllegalArgumentException.class)
	public void sendResponse_WrongSizeFileVector() {
		webServer.sendResponse(new Object[] {"",""},System.out);
		System.out.println("Test failed: sendResponse_WrongSizeFileVector");
	}
	@Test(expected = IllegalArgumentException.class)
	public void sendResponse_WrongContentType() {
		webServer.sendResponse(new String[]{"","ceva",OK_200},System.out);
		System.out.println("Test failed: sendResponse_WrongResponseStatus");
	}
	@Test(expected = IllegalArgumentException.class)
	public void sendResponse_WrongResponseStatus() {
		webServer.sendResponse(new String[]{"",HTML,"ceva"},System.out);
		System.out.println("Test failed: sendResponse_WrongResponseStatus");
	}
}
