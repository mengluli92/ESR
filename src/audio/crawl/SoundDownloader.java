package audio.crawl;

import java.net.*;
import java.io.*;

public class SoundDownloader {

	final String SoundDir = "F:/Java Work Space/ESR/data/corpus";
	
	public boolean httpDownload(String soundConcept, int soundIdx, String soundFileUrlStr) throws Exception{
        URL soundFileUrl = new URL(soundFileUrlStr);
        URLConnection conn = soundFileUrl.openConnection();
        InputStream soundFileIs = conn.getInputStream();
        String soundExt = soundFileUrlStr.substring(soundFileUrlStr.lastIndexOf('.'));
        FileOutputStream soundFileFs = new FileOutputStream(SoundDir + "/" + soundConcept + soundIdx + soundExt);

        byte[] buffer = new byte[1204];
        int bytesRead;
        while ((bytesRead = soundFileIs.read(buffer)) != -1) {
            soundFileFs.write(buffer, 0, bytesRead);
        }
        return true;
    }
}
