package knowledgebase;

import java.io.*;
import java.util.*;

import utils.Utils;

public class Probase52 {
	final private String ProbasePairUri = "F:/Corpus/probase_v52/Isa_Pairs.txt";
	
	public void extractPairs() throws Exception
	{
		String probaseItrUri = "F:/Corpus/probase_v52/Isa_Iteration.txt";
		File probaseItrFile = new File(probaseItrUri);
		File probasePairFile = new File(ProbasePairUri);
		BufferedReader probaseItrBr = new BufferedReader(new FileReader(probaseItrFile));
		BufferedWriter probasePairBw = new BufferedWriter(new FileWriter(probasePairFile, false));
		String line = null;
		
		System.out.println("Extracting Isa Pairs in Probase v5.2...");
		while((line = probaseItrBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			if(splitted.length < 11)
				continue;
			Utils.bwWriteLine(probasePairBw, splitted[0] + "\t" + splitted[1] + "\t" + splitted[2]);
		}
		System.out.println("Extraction done:)");
		
		probaseItrBr.close();
		probasePairBw.close();
	}
	
	public void filterPairs(String pairUri, String filteredUri, int freqThresh) throws Exception
	{
		File pairFile = new File(pairUri);
		File filteredFile = new File(filteredUri);
		BufferedReader pairBr = new BufferedReader(new FileReader(pairFile));
		BufferedWriter filteredBw = new BufferedWriter(new FileWriter(filteredFile));
		String line = null;
		
		while((line = pairBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			if(Integer.parseInt(splitted[2]) >= freqThresh)
				Utils.bwWriteLine(filteredBw, line);
		}
		
		pairBr.close();
		filteredBw.close();
	}
	
	public void findHypoOf(String [] concepts, String hypoUri) throws Exception
	{
		File pairFile = new File(ProbasePairUri);
		File hypoFile = new File(hypoUri);
		BufferedReader pairBr = new BufferedReader(new FileReader(pairFile));
		BufferedWriter hypoBw = new BufferedWriter(new FileWriter(hypoFile, false));
		String line = null;
		
		if((line = pairBr.readLine()) != null);
		while((line = pairBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			for(int i=0; i<concepts.length; ++i)
				if(concepts[i].equals(splitted[0]))
						Utils.bwWriteLine(hypoBw, line);
		}
		
		pairBr.close();
		hypoBw.close();
	}
}
