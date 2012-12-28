package net.ulfheim.brainfuck;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Test;

/**
 *
 * @author mdriscoll
 */
public class BrainfuckContextTest
{
	
	@Test(expected=BrainfuckConstraint.class)
	public void testNegativeMill() throws Exception {
		InputStream program = new ByteArrayInputStream("><<".getBytes("UTF-8"));
		InputStream in = new FileInputStream("/dev/null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext instance = new BrainfuckContext();
		instance.parse(program, in, out);
	}

	@Test(expected=BrainfuckConstraint.class)
	public void testUnmatchedWhile() throws Exception {
		InputStream program = new ByteArrayInputStream(">>>[[].".getBytes("UTF-8"));
		InputStream in = new FileInputStream("/dev/null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext instance = new BrainfuckContext();
		instance.parse(program, in, out);
	}
}
