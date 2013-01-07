package net.ulfheim.brainfuck;

import java.io.FileNotFoundException;

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
			String code = FileReader.asString(args[0]);
			BrainfuckContext bf = new BrainfuckContext();
			bf.parse(code, System.in, System.out);
			System.out.flush();
		} catch (FileNotFoundException ex) {
			System.err.println("No such file " + args[0]);
			System.exit(1);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
