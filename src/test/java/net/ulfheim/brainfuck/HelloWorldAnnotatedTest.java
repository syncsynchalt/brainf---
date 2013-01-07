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
public class HelloWorldAnnotatedTest
{
	
	/**
	 * Test the annotated HelloWorld program.
	 */
	@Test
	public void testProgram() throws Exception {
		String code = FileReader.asString("programs/hello-annotated.bf");
		InputStream in = new FileInputStream("/dev/null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext bf = new BrainfuckContext();
		bf.parse(code, in, out);

		System.out.println("Got: " + out.toString("UTF-8"));
		assertEquals("Hello World!\n", out.toString("UTF-8"));
	}
}
