package helpers;

import java.io.File;
import java.net.URL;

import vulnrep.VulnRep;

public class BuildChecker {
	public static void checkForJSONFiles(){
		try{
			URL resourceUrlH = VulnRep.class.getClass().getResource("/history.json");
			URL resourceUrlR = VulnRep.class.getClass().getResource("/email.json");
			URL resourceUrlS = VulnRep.class.getClass().getResource("/subscriptions.json");
			@SuppressWarnings("unused")
			File file = new File(resourceUrlH.toURI());
			file = new File(resourceUrlR.toURI());
			file = new File(resourceUrlS.toURI());
		}catch(Exception e){
			System.err.println("Unfinished build. Please remove all JSON files from the JAR and place it next to the JAR.");
			System.exit(-1);
		}		
	}
}
