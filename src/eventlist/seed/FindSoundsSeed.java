package eventlist.seed;

import java.net.URL;
import java.io.*;

import utils.Utils;

public class FindSoundsSeed {

	final private String SeedDir = "data/audible events/";
	final private String CategoryUrlStr = "http://www.findsounds.com/types.html"; 
	
	public void collectEvents() throws Exception{
		URL categoryUrl = new URL(CategoryUrlStr);
		InputStream categoryIs = categoryUrl.openStream();
		BufferedReader categoryBr = new BufferedReader(new InputStreamReader(categoryIs,"UTF-8"));
		File seedFile = new File(SeedDir + "findSounds.seed");
		BufferedWriter seedBw = new BufferedWriter(new FileWriter(seedFile, false));
		
		String line = "";
		while((line = categoryBr.readLine()) != null){
			if(line.contains("</A>"))
				Utils.bwWriteLine(seedBw, line.substring(line.indexOf(">")+1, line.indexOf("</A>")));
		}
		
		categoryIs.close();
		categoryBr.close();
		seedBw.close();
	}
}
