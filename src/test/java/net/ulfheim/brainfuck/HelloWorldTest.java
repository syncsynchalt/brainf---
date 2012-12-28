/*
 * Copyright (c) 2012 PAX8
 */
package net.ulfheim.brainfuck;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author mdriscoll
 */
public class HelloWorldTest
{
	
	public HelloWorldTest() {
	}
	
	/**
	 * Test of parse method, of class BrainfuckContext.
	 */
	@Test
	public void testHelloWorld() throws Exception {
		InputStream program = new FileInputStream("programs/hello.bf");
		InputStream in = new FileInputStream("/dev/null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext bf = new BrainfuckContext();
		bf.parse(program, in, out);

		System.out.println("Got: " + out.toString("UTF-8"));
		assertEquals("Hello World!\n", out.toString("UTF-8"));
	}
}
