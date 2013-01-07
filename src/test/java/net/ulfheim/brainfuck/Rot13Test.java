package net.ulfheim.brainfuck;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author mdriscoll
 */
public class Rot13Test
{
	
	@Test
	public void testProgram() throws Exception {
		String code = FileReader.asString("programs/rot13.bf");
		InputStream in = new ByteArrayInputStream("Hi There".getBytes("UTF-8"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext bf = new BrainfuckContext();
		bf.parse(code, in, out);

		System.out.println("Got: " + out.toString("UTF-8"));
		assertEquals("Uv Gurer", out.toString("UTF-8"));
	}
}
