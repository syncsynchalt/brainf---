/*
 * Copyright (c) 2012 PAX8
 */

package net.ulfheim.brainfuck;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mdriscoll
 */
class BrainfuckContext {

	List<Byte> mill = new ArrayList<Byte>(1000);
	int index;

	public BrainfuckContext() {
		mill.add((byte)0);
	}

	public void parse(InputStream in)
			throws IOException, BrainFuckConstraint
	{
		parse(in, 0);
	}

	public void parse(InputStream in, int recursionLevel)
			throws IOException, BrainFuckConstraint
	{
		for (;;) {
			int inputChar = in.read();
			if (inputChar == -1) {
				return;
			}
			byte c = (byte)inputChar;
			byte value;
			switch (c) {
				case '<':
					index--;
					if (index <= 1) {
						throw new BrainFuckConstraint("Negative mill index");
					}
					break;
				case '>':
					index++;
					if (index >= mill.size()) {
						mill.add((byte)0);
					}
					break;
				case '+':
					value = mill.get(index);
					value++;
					mill.set(index, value);
					break;
				case '-':
					value = mill.get(index);
					value--;
					mill.set(index, value);
					break;
				case '.':
					value = mill.get(index);
					System.out.print(value);
					break;
				case ',':
					int inputValue = (byte)System.in.read();
					value = (byte)inputValue;
					if (inputValue != -1) {
						mill.set(index, value);
					}
					break;
				case '[':
					parse(in, recursionLevel + 1);
					break;
				case ']':
					if (recursionLevel < 1) {
						throw new BrainFuckConstraint("Unmatched ']'");
					}
					return;
			}
		}
	}
}
