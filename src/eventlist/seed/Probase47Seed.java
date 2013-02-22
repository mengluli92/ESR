package eventlist.seed;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import utils.Inflector;
import utils.Utils;

import knowledgebase.Probase47;

public class Probase47Seed {
	final private String SeedDir = "data/audible events/";
	final private String TmpDir = "data/audible events/tmp/";
	private HashMap<String, Integer> OneWordMap;
	private HashMap<String, Integer> InstFreqMap;
	Inflector Inflct;
	
	public void collectEvents() throws Exception
	{
		/*Probase47 pb = new Probase47();
		String [] concepts = {"sound", "noise", "sound effect", "animal"};
		pb.findHypoOf(concepts, TmpDir + "Probase.47.seed.raw");*/
		Inflct = new Inflector();
		
		LoadFreq(TmpDir + "Probase.47.seed.raw");
		filterIllegal();
		seeMultiWord();
		solveOverlap();
		
		//pb.filterPairs(TmpDir + "Probase.52.seed.raw", TmpDir + "Probase.52.seed.thresh.2", 2);*/
		/*String [] prefix = {"sound of "};
		Utils.trimPrefix(TmpDir + "Probase.52.seed.thresh.2", TmpDir + "Probase.52.seed.thresh.2.pre.trimmed", 1, prefix);
		String [] suffix = {" sound"};
		Utils.trimSuffix(TmpDir + "Probase.52.seed.thresh.2.pre.trimmed", TmpDir + "Probase.52.seed.thresh.2.suf.trimmed", 1, suffix);*/
	}
	
	private void LoadFreq(String toLoadUri) throws Exception
	{
		BufferedReader toLoadBr = new BufferedReader(new FileReader(toLoadUri));
		String line = null;
		InstFreqMap = new HashMap<String, Integer>();
		OneWordMap = new HashMap<String, Integer>();
		
		while((line = toLoadBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			if(InstFreqMap.containsKey(splitted[1]))
				InstFreqMap.put(splitted[1], InstFreqMap.get(splitted[1]) + Integer.parseInt(splitted[2]));
			else
				InstFreqMap.put(splitted[1], Integer.parseInt(splitted[2]));
			
			if(!splitted[1].contains(" "))
				if(OneWordMap.containsKey(splitted[1]))
					OneWordMap.put(splitted[1], OneWordMap.get(splitted[1]) + Integer.parseInt(splitted[2]));
				else if(OneWordMap.containsKey(Inflct.singularize(splitted[1])))
				{
					System.out.println(Inflct.singularize(splitted[1]));
					OneWordMap.put(Inflct.singularize(splitted[1]), OneWordMap.get(Inflct.singularize(splitted[1])) + Integer.parseInt(splitted[2]));
					InstFreqMap.put(Inflct.singularize(splitted[1]), OneWordMap.get(Inflct.singularize(splitted[1])) + Integer.parseInt(splitted[2]));
					OneWordMap.remove(splitted[1]);
					InstFreqMap.remove(splitted[1]);
				}
				else if(OneWordMap.containsKey(Inflct.pluralize(splitted[1])))
				{
					System.err.println(splitted[1]);
					OneWordMap.put(splitted[1], OneWordMap.get(Inflct.pluralize(splitted[1])) + Integer.parseInt(splitted[2]));
					InstFreqMap.put(splitted[1], OneWordMap.get(Inflct.pluralize(splitted[1])) + Integer.parseInt(splitted[2]));
					OneWordMap.remove(Inflct.pluralize(splitted[1]));
					InstFreqMap.remove(Inflct.pluralize(splitted[1]));
				}
				else
					OneWordMap.put(splitted[1], Integer.parseInt(splitted[2]));
		}
		
		toLoadBr.close();
	}
	
	private void printInstFreqMap(String printUri) throws Exception
	{
		File solvedFile = new File(printUri);
		BufferedWriter solvedWr = new BufferedWriter(new FileWriter(solvedFile, false));
		
		for(Iterator<Map.Entry<String, Integer>> itr = InstFreqMap.entrySet().iterator(); itr.hasNext();)
		{
			Map.Entry<String, Integer> entry = itr.next();
			Utils.bwWriteLine(solvedWr, entry.getKey() + "\t" + entry.getValue());
		}
		solvedWr.close();
	}
	
	private void filterIllegal() throws Exception
	{
		List<String> toRemoveList = new ArrayList<String>();
		
		for(Iterator<Map.Entry<String, Integer>> itr = InstFreqMap.entrySet().iterator(); itr.hasNext();)
		{
			Map.Entry<String, Integer> entry = itr.next();
			String inst = entry.getKey();
			Pattern p = Pattern.compile("[a-zA-Z ]+");
	        Matcher m = p.matcher(inst);
	        if(!m.matches())
	        	toRemoveList.add(inst);
		}
		
		for(String toRemove : toRemoveList)
			InstFreqMap.remove(toRemove);
		
		printInstFreqMap(TmpDir + "Probase.47.seed.illegal.filtered.2");
	}
	
	private void seeMultiWord() throws Exception
	{
		File multiWordFile = new File(TmpDir + "Probase.47.seed.multi.2");
		BufferedWriter multiWordWr = new BufferedWriter(new FileWriter(multiWordFile, false));
		
		for(Iterator<Map.Entry<String, Integer>> itr2 = InstFreqMap.entrySet().iterator(); itr2.hasNext();)
		{
			Map.Entry<String, Integer> entry = itr2.next();
			String instance = entry.getKey();
			if(instance.contains(" "))
			{
				String [] splitted = instance.split(" ");
				for(int i=0; i<splitted.length; ++i)
					if(OneWordMap.containsKey(splitted[i]))
						Utils.bwWriteLine(multiWordWr, splitted[i] + "\t" + instance + "\t" + InstFreqMap.get(splitted[i]) + "\t" + entry.getValue());
					else if(OneWordMap.containsKey(Inflct.singularize(splitted[i])))
						Utils.bwWriteLine(multiWordWr, Inflct.singularize(splitted[i]) + "\t" + instance + "\t" + InstFreqMap.get(Inflct.singularize(splitted[i])) + "\t" + entry.getValue());
					else if(OneWordMap.containsKey(Inflct.pluralize(splitted[i])))
						Utils.bwWriteLine(multiWordWr, Inflct.pluralize(splitted[i]) + "\t" + instance + "\t" + InstFreqMap.get(Inflct.pluralize(splitted[i])) + "\t" + entry.getValue());
				
				String combinedInst = Utils.removeSpaces(instance);
				if(OneWordMap.containsKey(combinedInst))
					Utils.bwWriteLine(multiWordWr, combinedInst + "\t" + instance + "\t" + InstFreqMap.get(combinedInst) + "\t" + entry.getValue());
				else if(OneWordMap.containsKey(Inflct.singularize(combinedInst)))
					Utils.bwWriteLine(multiWordWr, Inflct.singularize(combinedInst) + "\t" + instance + "\t" + InstFreqMap.get(Inflct.singularize(combinedInst)) + "\t" + entry.getValue());
				else if(OneWordMap.containsKey(Inflct.pluralize(combinedInst)))
					Utils.bwWriteLine(multiWordWr, Inflct.pluralize(combinedInst) + "\t" + instance + "\t" + InstFreqMap.get(Inflct.pluralize(combinedInst)) + "\t" + entry.getValue());
			}
		}
		
		multiWordWr.close();
	}
	
	private void solveOverlap() throws Exception
	{
		File multiWordFile = new File(TmpDir + "Probase.47.seed.multi.2");
		BufferedReader multiWordBr = new BufferedReader(new FileReader(multiWordFile));
		String line = null;
		
		while((line = multiWordBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			int oneFreq = Integer.parseInt(splitted[2]), multiFreq = Integer.parseInt(splitted[3]);
			
			// give away its weight to existing substrings
			InstFreqMap.put(splitted[0], InstFreqMap.get(splitted[0]) + multiFreq);
			InstFreqMap.remove(splitted[1]);
		}
		multiWordBr.close();
		
		printInstFreqMap(TmpDir + "Probase.47.seed.multi.filtered.2");
	}
}
