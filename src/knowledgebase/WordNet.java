package knowledgebase;

import java.io.*;
import java.net.*;
import java.util.*;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

public class WordNet {
	
	final private String Path = "D:/Applications/WordNet2.1/dict";
	private IDictionary WordNetDict;
	
	public WordNet() throws Exception
	{
		URL url = new URL("file", null, Path);
		WordNetDict = new Dictionary(url);
		WordNetDict.open();
	}
	
	public void queryWord(String queryWord, POS pos) throws Exception{
		IIndexWord idxWord = WordNetDict.getIndexWord(queryWord, pos);
		List<IWordID> wordIDList = idxWord.getWordIDs();		
		for(IWordID wordID : wordIDList)
		{
			IWord word = WordNetDict.getWord(wordID);
			System.out.println("Lemma = " + word.getLemma());
			System.out.println("Gloss = " + word.getSynset().getGloss());
		}
	}
	
	public void queryRelatedWords(String queryWord, POS pos, IPointer relationPtr){
		IIndexWord idxWord = WordNetDict.getIndexWord(queryWord, pos);
		List<IWordID> wordIDList = idxWord.getWordIDs();		
		for(IWordID wordID : wordIDList)
		{
			IWord word = WordNetDict.getWord(wordID);
			System.out.println("Gloss = " + word.getSynset().getGloss());
			ISynset synset = word.getSynset ();
			
			List<ISynsetID> hypernymSynIDList = synset.getRelatedSynsets(relationPtr);
			for(ISynsetID hsId : hypernymSynIDList)
			{
				List<IWord> hypernymList = WordNetDict.getSynset(hsId).getWords();
				for(IWord hypernym : hypernymList)
					System.out.println(hypernym.getLemma());
				System.out.println();
			}
			System.out.println("=================================");
		}
	}
	
	public List<String> queryRelatedWordsOfSenseK(String queryWord, POS pos, int k, IPointer relationPtr){
		List<String> relatedSynsetList = new ArrayList<String>();
		
		IIndexWord idxWord = WordNetDict.getIndexWord(queryWord, pos);
		List<IWordID> wordIDList = idxWord.getWordIDs();		
		IWord word = WordNetDict.getWord(wordIDList.get(k));
		System.out.println("Gloss = " + word.getSynset().getGloss());
		ISynset synset = word.getSynset ();
		
		List<ISynsetID> relatedSynIDList = synset.getRelatedSynsets(relationPtr);
		for(ISynsetID relatedSynId : relatedSynIDList)
		{
			List<IWord> relatedSynWordList = WordNetDict.getSynset(relatedSynId).getWords();
			String relatedSynLine = "";
			
			for(int i=0; i<relatedSynWordList.size(); ++i )
			{
				IWord hypernym = relatedSynWordList.get(i);
				relatedSynLine += hypernym.getLemma();
				if(i < relatedSynWordList.size() - 1)
					relatedSynLine += "\t";
			}
			if(!relatedSynLine.isEmpty())
				relatedSynsetList.add(relatedSynLine);
		}
		
		return relatedSynsetList;
	}
}
