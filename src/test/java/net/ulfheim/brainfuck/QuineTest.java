package net.ulfheim.brainfuck;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author mdriscoll
 */
public class QuineTest
{
	@Test
	public void testProgram() throws Exception {
		InputStream stream = new FileInputStream("programs/quine.bf");
		byte[] b  = new byte[10000];
		int numBytes = stream.read(b);
		InputStream program = new ByteArrayInputStream(b, 0, numBytes);
		String programAsString = new String(b, 0, numBytes, "UTF-8");

		InputStream in = new FileInputStream("/dev/null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext bf = new BrainfuckContext();
		bf.parse(program, in, out);

		assertEquals(programAsString, out.toString("UTF-8"));
	}
}
