package test;

import static org.junit.Assert.assertEquals;

import java.net.Socket;

import org.junit.BeforeClass;
import org.junit.Test;

import webserver.MyConstants;
import webserver.WebServer;

public class WebServerStopped implements MyConstants {
	private static WebServer webServer;
	@BeforeClass
	public static void setup() {
		webServer = new WebServer(new Socket());
	}
	@Test(expected=NullPointerException.class)
	public void getResource_Null() {
		webServer.getResource(null);
		System.out.println("Test failed: getResource_Null");
	}
	@Test
	public void getResource_StatusCode_Something() {
		assertEquals("Test failed: getResource_StatusCode_Something",REQUEST_TIMEOUT_408,(String)webServer.getResource(DEFAULT_FILE)[2]);
	}
	@Test
	public void getResource_ContentType_Something() {
		assertEquals("Test failed: getResource_ContentType_Something",HTML,(String)webServer.getResource(DEFAULT_FILE)[1]);
	}
}
