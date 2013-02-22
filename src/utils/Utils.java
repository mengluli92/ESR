package utils;

import java.io.*;
import java.lang.Runtime;

public class Utils {

	static public void bwWriteLine(BufferedWriter bw, String toWrite) throws IOException
	{
		bw.write(toWrite + "\n");
	}	
	
	static public void printMemUsage() throws Exception
	{
		long free = Runtime.getRuntime().freeMemory()/(1024*1024);
		long max = Runtime.getRuntime().maxMemory()/(1024*1024); 
		
		System.out.println("max " + max + "M ,used " + (max-free) + "M");
	}
	
	/*static public String removeOneWordFromMultiple(String str, String substr)
	{
		String [] splitted = str.split(" ");
		String removed = "";
		for(int i = 0; i < splitted.length; ++i)
			if(!splitted[i].equals(substr))
				removed += (splitted[i] + " ");
		return removed.trim();
	}*/
	
	static public String removeSpaces(String str)
	{
		String [] splitted = str.split(" ");
		String removed = "";
		for(int i = 0; i < splitted.length; ++i)
			removed += splitted[i];
		return removed;
	}
}
