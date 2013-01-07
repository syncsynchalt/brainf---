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
public class BrainfuckContextTest
{
	
	@Test(expected=BrainfuckConstraint.class)
	public void testNegativeMill() throws Exception {
		String code = "><<";
		InputStream in = new FileInputStream("/dev/null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext instance = new BrainfuckContext();
		instance.parse(code, in, out);
	}

	@Test(expected=BrainfuckConstraint.class)
	public void testUnmatchedWhile() throws Exception {
		String code = ">>>[[].";
		InputStream in = new FileInputStream("/dev/null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext instance = new BrainfuckContext();
		instance.parse(code, in, out);
	}

	@Test
	public void testResize() throws Exception {
		String code = "++++[>>>>>++++++++<+<+<+<+<-]+.>.>.>.>.>.";
		InputStream in = new FileInputStream("/dev/null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		BrainfuckContext instance = new BrainfuckContext(2);
		instance.parse(code, in, out);
		assertEquals("\u0001\u0004\u0004\u0004\u0004\u0020", out.toString("UTF-8"));
	}
}
