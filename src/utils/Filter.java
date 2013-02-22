package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Filter {

	static public void trimPrefix(String toTrimUri, String trimmedUri, int colToTrim, String [] prefix) throws Exception
	{
		File toTrimFile = new File(toTrimUri);
		File trimmedFile = new File(trimmedUri);
		BufferedReader toTrimBr = new BufferedReader(new FileReader(toTrimFile));
		BufferedWriter trimmedBw = new BufferedWriter(new FileWriter(trimmedFile));
		String line = null;
		
		while((line = toTrimBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			for(int i=0; i<prefix.length; ++i)
				if( !splitted[colToTrim].contains(prefix[i]) )
					Utils.bwWriteLine(trimmedBw, line);
				else
				{
					String trimmed = splitted[colToTrim].substring(splitted[colToTrim].lastIndexOf(prefix[i]) + prefix[i].length());
					Utils.bwWriteLine(trimmedBw, line.replaceAll(splitted[colToTrim], trimmed));
				}
		}
		
		toTrimBr.close();
		trimmedBw.close();
	}
	
	static public void trimSuffix(String toTrimUri, String trimmedUri, int colToTrim, String [] suffix) throws Exception
	{
		File toTrimFile = new File(toTrimUri);
		File trimmedFile = new File(trimmedUri);
		BufferedReader toTrimBr = new BufferedReader(new FileReader(toTrimFile));
		BufferedWriter trimmedBw = new BufferedWriter(new FileWriter(trimmedFile));
		String line = null;
		
		while((line = toTrimBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			for(int i=0; i<suffix.length; ++i)
				if( !splitted[colToTrim].contains(suffix[i]) )
					Utils.bwWriteLine(trimmedBw, line);
				else
				{
					String trimmed = splitted[colToTrim].substring(0, splitted[colToTrim].indexOf(suffix[i]));
					Utils.bwWriteLine(trimmedBw, line.replaceAll(splitted[colToTrim], trimmed));
				}
		}
		
		toTrimBr.close();
		trimmedBw.close();
	}
}
