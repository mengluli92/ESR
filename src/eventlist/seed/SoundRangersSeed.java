package eventlist.seed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import utils.Utils;

public class SoundRangersSeed {

	final private String SeedDir = "data/audible events/";
	final private String CategoryUrlStr = "http://www.soundrangers.com/index.cfm?category=1&left_cat=1"; 
	
	public void collectEvents() throws Exception{
		URL categoryUrl = new URL(CategoryUrlStr);
		InputStream categoryIs = categoryUrl.openStream();
		BufferedReader categoryBr = new BufferedReader(new InputStreamReader(categoryIs,"UTF-8"));
		File seedFile = new File(SeedDir + "soundRangers.seed");
		BufferedWriter seedBw = new BufferedWriter(new FileWriter(seedFile, false));
        
		String line = "";
		while((line = categoryBr.readLine()) != null){
			if(line.contains("<td width=\"33%\">"))
				Utils.bwWriteLine(seedBw, line.substring(line.indexOf("\" >") + 3, line.indexOf("</a>")));
		}
		
		categoryIs.close();
		categoryBr.close();
		seedBw.close();
	}
}
