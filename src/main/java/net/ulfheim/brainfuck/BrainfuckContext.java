package net.ulfheim.brainfuck;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author mdriscoll
 */
class BrainfuckContext {

	private static final Logger logger = Logger.getLogger(BrainfuckContext.class.getName());

	List<Byte> mill = new ArrayList<Byte>(1000);
	int index;
	int depth;

	public BrainfuckContext() {
		mill.add((byte)0);
	}

	private void printDebugInfo(String command) {
		Formatter formatter = new Formatter();
		byte current = mill.get(index);
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
					if (index >= mill.size()) {
						mill.add((byte)0);
					}
					printDebugInfo(">");
					break;
				case '+':
					value = mill.get(index);
					value++;
					mill.set(index, value);
					printDebugInfo("+");
					break;
				case '-':
					value = mill.get(index);
					value--;
					mill.set(index, value);
					printDebugInfo("-");
					break;
				case '.':
					value = mill.get(index);
					out.write(value);
					printDebugInfo(".");
					break;
				case ',':
					int inputValue = (byte)in.read();
					value = (byte)inputValue;
					if (inputValue != -1) {
						mill.set(index, value);
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
		while (mill.get(index) != 0) {
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
