package knowledgebase;

import java.io.*;
import java.util.*;
import java.lang.Runtime;

import utils.Utils;

public class Probase47 {
	final private String ConceptDictUri = "F:/Corpus/probase_v47/conceptDict.txt";
	final private String InstanceDictUri = "F:/Corpus/probase_v47/instanceDict.txt";
	final private String PairUri = "F:/Corpus/probase_v47/probaseMatrix.txt";
	private HashMap<String, Integer> ConceptIdMap;
	private List<String> ConceptList;
	private HashMap<String, Integer> InstanceIdMap;
	private List<String> InstanceList;
	private HashMap<Integer, List<Integer>> ConceptInstanceMap;
	private HashMap<Integer, List<Integer>> InstanceConceptMap;
	private HashMap<List<Integer>, Integer> PairFreqMap;
	
	public Probase47() throws Exception
	{
		loadProbase();
	}
	
	private void loadProbase() throws Exception
	{
		File conceptFile = new File(ConceptDictUri);
		File instanceFile = new File(InstanceDictUri);
		File pairFile = new File(PairUri);
		BufferedReader conceptBr = new BufferedReader(new FileReader(conceptFile));
		BufferedReader instanceBr = new BufferedReader(new FileReader(instanceFile));
		BufferedReader pairBr = new BufferedReader(new FileReader(pairFile));
		String line = null;
		ConceptIdMap = new HashMap<String, Integer>();
		ConceptList = new ArrayList<String>();
		InstanceIdMap = new HashMap<String, Integer>();
		InstanceList = new ArrayList<String>();
		ConceptInstanceMap = new HashMap<Integer, List<Integer>>();
		InstanceConceptMap = new HashMap<Integer, List<Integer>>();
		PairFreqMap = new HashMap<List<Integer>, Integer>();
		
		System.out.println("Loading Probase v4.7...");
		
		Utils.printMemUsage();
		while((line = conceptBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			Integer id = Integer.parseInt(splitted[1]);
			ConceptIdMap.put(splitted[0], id);
			ConceptList.add(splitted[0]);
		}
		conceptBr.close();
		System.gc();
		Utils.printMemUsage();
		
		while((line = instanceBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			Integer id = Integer.parseInt(splitted[1]);
			InstanceIdMap.put(splitted[0], id);
			InstanceList.add(splitted[0]);
		}
		instanceBr.close();
		System.gc();
		Utils.printMemUsage();
		
		int cnt = 0;
		while((line = pairBr.readLine()) != null)
		{
			String [] splitted = line.split("\t");
			Integer conceptId = Integer.parseInt(splitted[0]), instanceId = Integer.parseInt(splitted[1]), freq = Integer.parseInt(splitted[2]);
			if(ConceptInstanceMap.containsKey(conceptId))
			{
				List<Integer> tmpList = ConceptInstanceMap.get(conceptId);
				tmpList.add(instanceId);
				ConceptInstanceMap.put(conceptId, tmpList);
			}
			else
			{
				List<Integer> tmpList = new ArrayList<Integer>();
				tmpList.add(instanceId);
				ConceptInstanceMap.put(conceptId, tmpList);
			}
			
			if(InstanceConceptMap.containsKey(instanceId))
			{
				List<Integer> tmpList = InstanceConceptMap.get(instanceId);
				tmpList.add(conceptId);
				InstanceConceptMap.put(instanceId, tmpList);
			}
			else
			{
				List<Integer> tmpList = new ArrayList<Integer>();
				tmpList.add(conceptId);
				InstanceConceptMap.put(instanceId, tmpList);
			}
			
			Integer [] pair = {conceptId, instanceId};
			PairFreqMap.put(Arrays.asList(pair), freq);
			
			++cnt;
			if(cnt % 1000000 == 0)
			{
				System.gc();
				Utils.printMemUsage();
			}
		}
		pairBr.close();
		System.gc();
		Utils.printMemUsage();
		
		System.out.println("Probase v4.7 loaded:)");
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
		File hypoFile = new File(hypoUri);
		BufferedWriter hypoBw = new BufferedWriter(new FileWriter(hypoFile, false));
		
		for(int i=0; i<concepts.length; ++i)
			if(ConceptIdMap.containsKey(concepts[i]))
			{
				Integer cId = ConceptIdMap.get(concepts[i]);
				List<Integer> tmpList = ConceptInstanceMap.get(cId);
				for(Integer iId : tmpList)
				{
					String instance = InstanceList.get(iId);
					Integer [] pair = {cId, iId};
					Utils.bwWriteLine(hypoBw, concepts[i] + "\t" + instance + "\t" + PairFreqMap.get(Arrays.asList(pair)));
				}
			}
		
		hypoBw.close();
		System.gc();
		Utils.printMemUsage();
		System.out.println("Hyponyms found:)");
	}
}
