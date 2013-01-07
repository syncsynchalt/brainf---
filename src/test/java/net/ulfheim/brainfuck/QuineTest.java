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
public class QuineTest
{
	@Test
	public void testProgram() throws Exception {
		String code = FileReader.asString("programs/quine.bf");
		byte[] b  = new byte[10000];

		InputStream in = new FileInputStream("/dev/null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext bf = new BrainfuckContext();
		bf.parse(code, in, out);

		assertEquals(code, out.toString("UTF-8"));
	}
}
