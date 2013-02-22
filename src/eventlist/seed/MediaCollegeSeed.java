package eventlist.seed;

import java.net.URL;
import java.util.HashSet;
import java.io.*;

import utils.Utils;

public class MediaCollegeSeed {
	final private String SeedDir = "data/audible events/";
	final private String CategoryUrlStr = "http://www.mediacollege.com/downloads/sound-effects/";
	private HashSet<String> eventMap;
	
	public void collectEvents() throws Exception{
		URL categoryUrl = new URL(CategoryUrlStr);
		InputStream categoryIs = categoryUrl.openStream();
		BufferedReader categoryBr = new BufferedReader(new InputStreamReader(categoryIs,"UTF-8"));
		eventMap = new HashSet<String>();
		
		String line = "";
		boolean isBody = false;
		while((line = categoryBr.readLine()) != null){
			if(line.equals("<h1>Free Sound Effects</h1>"))
				isBody = true;
			if(!isBody)
				continue;
			
			if(line.contains("<li><a href=\""))
			{
				String eventUrlStr = CategoryUrlStr + line.substring(line.indexOf("\"") + 1 , line.indexOf("/\">")) + "/";
				collectThisPage(eventUrlStr);
			}
		}
		
		categoryIs.close();
		categoryBr.close();
	}
	
	private void collectThisPage(String eventUrlStr) throws Exception{
		URL eventUrl = new URL(eventUrlStr);
		InputStream categoryIs = eventUrl.openStream();
		BufferedReader categoryBr = new BufferedReader(new InputStreamReader(categoryIs,"UTF-8"));
		File seedFile = new File(SeedDir + "mediaCollege.seed");
		BufferedWriter seedBw = new BufferedWriter(new FileWriter(seedFile, true));
        
		String line = "";
		boolean isBody = false;
		while((line = categoryBr.readLine()) != null){
			if(line.startsWith("<h1>"))
				isBody = true;
			if(!isBody)
				continue;
			
			if(line.contains("<li><a href=\""))
			{
				String subEventUrlStr = eventUrlStr + line.substring(line.indexOf("\"") + 1 , line.indexOf("/\">")) + "/";
				collectThisPage(subEventUrlStr);
			}
			if(line.contains("class=\"dl_title\">"))
			{
				 String event = line.substring(line.indexOf("\">") + 2, line.indexOf("</a>")).replaceAll(" [0-9].*", "").trim();
				 if(eventMap.contains(event))
					 continue;
				 eventMap.add(event);
				 Utils.bwWriteLine(seedBw, event);
			}
		}
		
		categoryIs.close();
		categoryBr.close();
		seedBw.close();
	}
}
