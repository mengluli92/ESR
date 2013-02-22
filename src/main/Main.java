package main;

import java.io.IOException;
import java.util.*;

import utils.Inflector;
import utils.Utils;

import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

import audio.crawl.*;
import eventlist.seed.*;
import knowledgebase.*;

public class Main {
	
	public static void main(String[] args) throws Exception {
		/*ProbaseSeed ps = new ProbaseSeed();
		String seedDir = "data/audible events/";
		ps.loadConceptFreq();
		String [] seedUris = {seedDir + "findSounds.seed", seedDir + "mediaCollege.seed", seedDir + "soundRangers.seed", seedDir + "wordNet.seed"};
		ps.seeFrequency(seedUris);*/
		
		Probase47Seed ps = new Probase47Seed();
		ps.collectEvents();
		/*Inflector inf = new Inflector();
		System.out.println(inf.pluralize("mudskipper"));*/
	}

}
