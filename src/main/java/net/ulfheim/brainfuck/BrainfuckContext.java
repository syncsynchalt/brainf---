package net.ulfheim.brainfuck;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;
import org.apache.log4j.Logger;

/**
 *
 * @author mdriscoll
 */
class BrainfuckContext {

	private static final Logger logger = Logger.getLogger(BrainfuckContext.class.getName());

	private byte[] mill;
	private int index;
	private int depth;

	public BrainfuckContext(int size) {
		mill = new byte[size];
	}

	public BrainfuckContext() {
		this(256);
	}

	private void printDebugInfo(String command) {
		if (logger.isDebugEnabled() == false) {
			return;
		}

		Formatter formatter = new Formatter();
		byte current = mill[index];
		int currentAsInt;
		if (current < 0) {
			currentAsInt = 0x100 + current;
		} else {
			currentAsInt = (int)current;
		}
		assert(currentAsInt <= 0xFF && currentAsInt >= 0x00);

		String depthLine = "          ".substring(0, depth > 10 ? 10 : depth);
		String logLine;
		if (current >= 0x20 && current < 0x7f) {
			logLine = formatter.format("[%s%s %d:0x%02X (%c)]",
					depthLine,
					command,
					index,
					currentAsInt,
					(char)current).toString();
		} else {
			logLine = formatter.format("[%s%s %d:0x%02X]",
					depthLine,
					command,
					index,
					currentAsInt).toString();
		}
		logger.debug(logLine);
	}

	public void parse(InputStream program, InputStream in, OutputStream out)
			throws IOException, BrainfuckConstraint
	{
		for (;;) {
			int inputChar = program.read();
			if (inputChar == -1) {
				return;
			}
			byte c = (byte)inputChar;

			byte value;
			switch (c) {
				case '<':
					index--;
					if (index < 0) {
						throw new BrainfuckConstraint("Negative mill index");
					}
					printDebugInfo("<");
					break;
				case '>':
					index++;
					if (index >= mill.length) {
						byte[] newMill = new byte[mill.length * 2];
						System.arraycopy(mill, 0, newMill, 0, mill.length);
						mill = newMill;
					}
					printDebugInfo(">");
					break;
				case '+':
					mill[index]++;
					printDebugInfo("+");
					break;
				case '-':
					mill[index]--;
					printDebugInfo("-");
					break;
				case '.':
					value = mill[index];
					out.write(value);
					printDebugInfo(".");
					break;
				case ',':
					int inputValue = (byte)in.read();
					value = (byte)inputValue;
					if (inputValue != -1) {
						mill[index] = value;
						printDebugInfo(",");
					} else {
						printDebugInfo(",!");
					}
					break;
				case '[':
					printDebugInfo("[");
					runSubProgram(program, in, out);
					break;
			}
		}
	}

	private void runSubProgram(InputStream program, InputStream in, OutputStream out)
			throws IOException, BrainfuckConstraint
	{
		String subProgram = readSubProgram(program);

		byte[] subProgramBytes = subProgram.getBytes("UTF-8");
		depth++;
		while (mill[index] != 0) {
			printDebugInfo("@");
			InputStream newProgram = new ByteArrayInputStream(subProgramBytes);
			parse(newProgram, in, out);
		}
		printDebugInfo("@!");
		depth--;
	}

	private String readSubProgram(InputStream program)
			throws BrainfuckConstraint, IOException
	{
		int curDepth = 1;
		StringBuilder subProgramString = new StringBuilder();
loop:	for (;;) {
			int inputChar = program.read();
			if (inputChar == -1) {
				throw new BrainfuckConstraint("Unmatched '['");
			}

			byte c = (byte)inputChar;
			switch (c) {
				case '[':
					curDepth++;
					break;
				case ']':
					curDepth--;
					if (curDepth == 0) {
						break loop;
					}
					break;
			}
			subProgramString.append((char)c);
		}
		return subProgramString.toString();
	}
}
