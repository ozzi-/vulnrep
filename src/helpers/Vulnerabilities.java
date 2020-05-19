package helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import models.Header;
import models.History;
import models.HistoryBundle;
import models.Subscription;
import models.Vulnerability;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Vulnerabilities {
	public static ArrayList<Vulnerability> getVulnerabilities(Date maxAgeDate, ArrayList<Subscription> subscriptions,
			HistoryBundle hb) {
		ArrayList<Vulnerability> searchResults = new ArrayList<Vulnerability>();
		Date currentDate = Calendar.getInstance().getTime();

		for (Subscription subscription : subscriptions) {
			String apiKeyVulners = subscription.getApiKeyVulners();
			String apiKeyWPVulnDB = subscription.getApiKeyWPVulnDB();
					
			if(subscription.getName().equals("custom_wpvulndb_plugin")){
				ArrayList<String> plugins = subscription.getPlugins();
				for (String plugin : plugins) {
					Vulnerability item = new Vulnerability();
					String html;
					try {
						html = NW.getHTML("https://wpvulndb.com/api/v3/plugins/"+plugin,new Header("Authorization", "Token token="+apiKeyWPVulnDB));
						if(html!=null){
							JsonElement jsonElement = JsonParser.parseString(html);
							JsonObject jsonObject = jsonElement.getAsJsonObject().getAsJsonObject(plugin);
							JsonArray vulnerabilities = jsonObject.getAsJsonArray("vulnerabilities");
							for (JsonElement vulnerability : vulnerabilities) {
								JsonObject vuln = vulnerability.getAsJsonObject();
								item.setPublished(vuln.get("created_at").getAsString());
								item.setSearchTerm("WPVulnDB Plugins");
								item.setHref("https://wpvulndb.com/vulnerabilities/"+vuln.get("id").getAsString());
								item.setId(vuln.get("id").getAsString());
								item.setDescription("");
								item.setTitle(vuln.get("title").getAsString());
					
								if (maxAgeDate.before(item.getPublished()) && !hb.historyContainsID(item.getId()) && (item.getCvss()==0.0 || subscription.getCVSS()<=item.getCvss())) {
									searchResults.add(item);
									History history = new History();
									history.setId(item.getId());
									history.setDate(currentDate);
									hb.add(history); 
								}
							}
						}else{
							System.out.println("404 getting wpvulndb plugin "+plugin);
						}
					} catch (Exception e) {
						ErrorReporter.handleError("Error getting vulnerabilities on wpvulndb.com:  "+e.getMessage(),e);
					}
					
				}
			}else{
				String html;
				try {
					html = NW.getHTML("https://vulners.com/api/v3/search/lucene/?query=" + subscription.getName()+"&api_key="+apiKeyVulners, null);
					JsonElement jsonElement = JsonParser.parseString(html);
					JsonObject jsonObject = jsonElement.getAsJsonObject();
					if(jsonObject.get("result") != null && jsonObject.get("result").getAsString().equals("error")) {
						throw new Exception("Error returned from vulners api: "+jsonObject.toString());
					}
					JsonObject data = jsonObject.getAsJsonObject("data");
					JsonArray searchArray = data.getAsJsonArray("search");
					
					for (JsonElement searchResult : searchArray) {
						Vulnerability item = new Vulnerability();
						try {
							item.setDescription(searchResult.getAsJsonObject().get("flatDescription").getAsString());	
						}catch (NullPointerException e){
							item.setDescription("No description available.");
						}
						JsonObject source = searchResult.getAsJsonObject().getAsJsonObject("_source");
						item.setPublished(source.get("published").getAsString());
						item.setSearchTerm(subscription.getName());
						item.setTitle(source.get("title").getAsString());
						item.setHref(source.get("href").getAsString());
						item.setVhref(source.get("vhref").getAsString());
						item.setId("wpplugin_"+source.get("id").getAsString());
						JsonObject cvss = source.getAsJsonObject("cvss");
						if(cvss!=null) {
							item.setCvss(cvss.get("score").getAsDouble());						
						}
						item.parseAndAddMetrics(cvss.get("vector").getAsString());
						if (maxAgeDate.before(item.getPublished()) && !hb.historyContainsID(item.getId()) && (item.getCvss()==0.0 || subscription.getCVSS()<=item.getCvss())) {
							searchResults.add(item);
							History history = new History();
							history.setId(item.getId());
							history.setDate(currentDate);
							hb.add(history); 
						}
					}
				} catch (Exception e) {
					ErrorReporter.handleError("Error getting vulnerabilities on vulners.com:  "+e.getMessage(),e);
				}
			}
		}
		System.out.println("Number of found vulnerabilities: " + searchResults.size());
		return searchResults;
	}
}
