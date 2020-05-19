package helpers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import vulnrep.VulnRep;

public class IO {

	public static String readFile(String path) throws Exception {
		byte[] encoded;
		encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, "UTF-8");
	}
	
	public static String getCurrentDirectory() {
		URL url = VulnRep.class.getProtectionDomain().getCodeSource().getLocation();
        String separator =java.nio.file.FileSystems.getDefault().getSeparator();
        
		String path = url.getPath();
		if(System.getProperty("os.name").toLowerCase().contains("windows") && (path.startsWith("/") || path.startsWith("\\"))){
			path=path.substring(1);
		}
		if(path.toLowerCase().endsWith(".jar")) {
		    int index=path.lastIndexOf(separator);
		    path = path.substring(0,index)+separator;
		}
		return path;
	}
	
	public static void writeReport(String vulnHTML) {
		try{
		    PrintWriter writer = new PrintWriter("report.html", "UTF-8");
		    if(ErrorReporter.failed) {
		    	writer.println("Error(s) occured:");
		    	writer.println("*****************");
		    	writer.println(ErrorReporter.errorMessage);
		    	writer.println("---");
		    }
		    writer.println(vulnHTML);
		    writer.close();
		} catch (IOException e) {
			ErrorReporter.handleError("Error writing report.html: "+e.getMessage(),e);
		}
	}
}