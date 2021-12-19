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

public class WebServerTest implements MyConstants {
	private static WebServer webServer;
	private static Frame frame;
	private static JButton button;
	@BeforeClass
	public static void setup() {
		webServer = new WebServer(new Socket());
		webServer.setServerIsRunning(true);
	}
//----------------------------------------------------------------------------------------------------------setServerSocket
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
		System.out.println("Test failed: NullDirPath");
	}
	@Test
	public void validateRelativeDirPath() {
		assertTrue("Test failed: RelativeDirPath",webServer.validateDirectoryPath("src/"));
	}
	@Test
	public void validateaAbsoluteDirPath() {
		assertTrue("Test failed: AbsoluteDirPath",webServer.validateDirectoryPath("C:/Users/justi/Desktop/"));
	}
	@Test
	public void validateaNotADirPath() {
		assertFalse("Test failed: NotADirPath",webServer.validateDirectoryPath("src/nonexist/"));
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
	public void getResourceStatusCode_Null() {
		webServer.getResource(null);
		System.out.println("Test failed: getResourceStatusCode_Null");
	}
	@Test
	public void getResourceStatusCode_Empty() {
		assertEquals("Test failed: getResourceStatusCode_Null", OK_200, (String)webServer.getResource("")[2]);
	}
	@Test
	public void getResourceStatusCode_Default() {
		assertEquals("Test failed: getResourceStatusCode_Default", OK_200, (String)webServer.getResource(DEFAULT_FILE)[2]);
	}
	@Test
	public void getResourceStatusCode_NotFound() {
		assertEquals("Test failed: getResourceStatusCode_NotFound", NOT_FOUND_404, (String)webServer.getResource("abc")[2]);
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
