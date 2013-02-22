package eventlist.seed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import utils.Utils;

import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import knowledgebase.WordNet;

public class WordNetSeed {
	final private String SeedDir = "data/audible events/";
	
	public void collectEvents() throws Exception
	{
		WordNet wn = new WordNet();
		File seedFile = new File(SeedDir + "wordNet.seed");
		BufferedWriter seedBw = new BufferedWriter(new FileWriter(seedFile, true));
		
		List<String> soundHypoList = wn.queryRelatedWordsOfSenseK("sound", POS.NOUN, 3, Pointer.HYPONYM);
		List<String> noiseHypoList = wn.queryRelatedWordsOfSenseK("noise", POS.NOUN, 0, Pointer.HYPONYM);
		List<String> animalHypoList = wn.queryRelatedWordsOfSenseK("animal", POS.NOUN, 0, Pointer.HYPONYM);
		for(String soundHypo : soundHypoList)
			Utils.bwWriteLine(seedBw, soundHypo);
		for(String noiseHypo : noiseHypoList)
			Utils.bwWriteLine(seedBw, noiseHypo);
		for(String animalHypo : animalHypoList)
			Utils.bwWriteLine(seedBw, animalHypo);
		
		seedBw.close();
	}
}
