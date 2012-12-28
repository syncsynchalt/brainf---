package net.ulfheim.brainfuck;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Parse a brainfuck program!
 */
public class App 
{
	public static void main( String[] args )
	{
		if (args.length != 1) {
			System.err.println("Usage: bf [file]");
			System.exit(1);
		}

		try {
			InputStream in = new FileInputStream(args[0]);
			BrainfuckContext bf = new BrainfuckContext();
			bf.parse(in, System.in, System.out);
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		} catch (BrainfuckConstraint ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
