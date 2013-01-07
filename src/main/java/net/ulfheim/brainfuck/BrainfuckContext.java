package net.ulfheim.brainfuck;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.Stack;
import org.apache.log4j.Logger;

/**
 *
 * @author mdriscoll
 */
class BrainfuckContext {

	private static final Logger logger = Logger.getLogger(BrainfuckContext.class.getName());

	private byte[] mill;
	private int index;
	private Stack<Integer> loopStarts;

	public BrainfuckContext(int size) {
		mill = new byte[size];
		loopStarts = new Stack<Integer>();
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

		int depth = loopStarts.size();
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

	public void parse(String code, InputStream in, OutputStream out)
			throws IOException, BrainfuckConstraint
	{
		for (int codePtr = 0; codePtr < code.length(); codePtr++) {
			char c = code.charAt(codePtr);

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
					if (mill[index] == 0) {
						int endLoop = findEndOfLoop(code, codePtr);
						codePtr = endLoop;
					} else {
						loopStarts.push(codePtr);
					}
					break;
				case ']':
					printDebugInfo("]");
					if (mill[index] == 0) {
						loopStarts.pop();
					} else {
						int startLoop = loopStarts.peek();
						codePtr = startLoop;
					}
					break;
			}
		}
	}

	private int findEndOfLoop(String code, int codePtr)
			throws BrainfuckConstraint
	{
		int matchDepth = 1;
		for (int i = codePtr + 1; ; i++) {
			if (i >= code.length()) {
				throw new BrainfuckConstraint("Unmatched '['");
			}
			switch (code.charAt(i)) {
				case '[':
					matchDepth++;
					break;
				case ']':
					matchDepth--;
					if (matchDepth == 0) {
						return i;
					}
					break;
				default:
					break;
			}
		}
	}
}
