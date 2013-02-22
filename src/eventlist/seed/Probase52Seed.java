package eventlist.seed;

import java.io.*;
import java.util.*;

import knowledgebase.Probase52;
import utils.Utils;

public class Probase52Seed {
	final private String SeedDir = "data/audible events/";
	final private String TmpDir = "data/audible events/tmp/";
	private HashMap<String, Integer> conceptFreqMap;
	
	public void collectEvents() throws Exception
	{
		Probase52 pb = new Probase52();
		/*String [] concepts = {"sound", "noise", "sound effect", "animal"};
		pb.findHypoOf(concepts, TmpDir + "Probase.52.seed.raw");
		pb.filterPairs(TmpDir + "Probase.52.seed.raw", TmpDir + "Probase.52.seed.thresh.2", 2);*/
		/*String [] prefix = {"sound of "};
		Utils.trimPrefix(TmpDir + "Probase.52.seed.thresh.2", TmpDir + "Probase.52.seed.thresh.2.pre.trimmed", 1, prefix);
		String [] suffix = {" sound"};
		Utils.trimSuffix(TmpDir + "Probase.52.seed.thresh.2.pre.trimmed", TmpDir + "Probase.52.seed.thresh.2.suf.trimmed", 1, suffix);*/
	}
	
	public void loadConceptFreq() throws Exception
	{
		File pbFile = new File(TmpDir + "Probase.52.seed.raw");
		BufferedReader pbBr = new BufferedReader(new FileReader(pbFile));
		String line = null;
		conceptFreqMap = new HashMap<String, Integer>();
		
		while((line = pbBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			if(conceptFreqMap.containsKey(splitted[1]))
				conceptFreqMap.put(splitted[1], conceptFreqMap.get(splitted[1]) + Integer.parseInt(splitted[2]));
			else
				conceptFreqMap.put(splitted[1], Integer.parseInt(splitted[2]));
		}
		
		pbBr.close();
	}
	
	public void seeFrequency(String [] seedUris) throws Exception
	{
		File freqFile = new File(TmpDir + "seed.freq");
		BufferedWriter freqWr = new BufferedWriter(new FileWriter(freqFile, false));
		HashSet<String> conceptSet = new HashSet<String>();
		String line = null;
		int totalFreq = 0;
		
		// Load all the seed concepts
		for(int i=0; i<seedUris.length; ++i)
		{
			File seedFile = new File(seedUris[i]);
			BufferedReader seedBr = new BufferedReader(new FileReader(seedFile));

			while((line = seedBr.readLine()) != null)
			{
				if(!line.contains("\t"))
				{
					if(!conceptSet.contains(line))
						conceptSet.add(line);
				}
				else
				{
					String [] splitted = line.split("\t");
					for(int j=0; j<splitted.length; ++j)
						if(!conceptSet.contains(splitted[j]))
							conceptSet.add(splitted[j]);
				}
			}
			seedBr.close();
		}
		
		// Output concept frequencies
		for(Iterator<String> itr = conceptSet.iterator(); itr.hasNext();)
		{
			String concept = itr.next();
			if(conceptFreqMap.containsKey(concept))
			{
				Integer freq = conceptFreqMap.get(concept);
				Utils.bwWriteLine(freqWr, concept + "\t" + freq); 
				totalFreq += freq;
			}
		}
		freqWr.close();
		
		System.out.println(totalFreq/conceptSet.size());
	}
}
