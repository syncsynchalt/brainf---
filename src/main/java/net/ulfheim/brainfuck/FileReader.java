package net.ulfheim.brainfuck;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 *
 * @author mdriscoll
 */
public class FileReader
{
	public static String asString(String filename) throws FileNotFoundException, IOException
	{
		FileInputStream stream = new FileInputStream(filename);
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			return Charset.forName("utf-8").decode(bb).toString();
		} finally {
			stream.close();
		}
	}
}
