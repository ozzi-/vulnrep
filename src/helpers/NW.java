package helpers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import models.Header;

public class NW {
	public static String getHTML(String urlToRead, Header header) throws Exception  {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		if(header!=null) {
			conn.setRequestProperty(header.getKey(), header.getValue());			
		}
		BufferedReader rd;
		String line;

		try {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (FileNotFoundException e) {
			return null;
		} catch (Exception e) {
			System.err.println("Could not HTTP GET to '"+urlToRead+"' due to: "+e.getMessage()+" - "+e.getCause());
			if(header!=null) {
				System.err.println("Header: "+header.getKey()+" - "+header.getValue());
			}
			throw e;
		}
	}
}
