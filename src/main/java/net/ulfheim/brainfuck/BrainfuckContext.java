/*
 * Copyright (c) 2012 PAX8
 */

package net.ulfheim.brainfuck;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author mdriscoll
 */
class BrainfuckContext {

	private static final Logger logger = LogManager.getLogger(BrainfuckContext.class.getName());

	List<Byte> mill = new ArrayList<Byte>(1000);
	int index;
	int level;

	public BrainfuckContext() {
		mill.add((byte)0);
	}

	private void printDebugInfo(String command) {
		Formatter formatter = new Formatter();
		byte current = mill.get(index);

		String depthLine = "          ".substring(0, level > 10 ? 10 : level);
		String logLine;
		if (current >= 0x20 && current < 0x7f) {
			logLine = formatter.format("[%s%s %d:0x%02X (%c)]",
					depthLine,
					command,
					index,
					(int)current,
					(char)current).toString();
		} else {
			logLine = formatter.format("[%s%s %d:0x%02X]",
					depthLine,
					command,
					index,
					(int)current).toString();
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
		StringBuilder subProgramString = new StringBuilder();
		for (;;) {
			int inputChar = program.read();
			if (inputChar == -1) {
				throw new BrainfuckConstraint("Unmatched '['");
			}
			byte c = (byte)inputChar;
			if (c == ']') {
				break;
			}
			subProgramString.append((char)c);
		}

		String subProgram = subProgramString.toString();
		byte[] subProgramBytes = subProgram.getBytes("UTF-8");
		level++;
		while (mill.get(index) != 0) {
			printDebugInfo("@");
			InputStream newProgram = new ByteArrayInputStream(subProgramBytes);
			parse(newProgram, in, out);
		}
		printDebugInfo("@!");
		level--;
	}
}
