package helpers;

import java.util.ArrayList;

import models.Subscription;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Subscriptions {
	public static ArrayList<Subscription> loadSubscriptions(String currentDirectory) {
		ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();
		JsonArray pluginArr = null;
		try {
			String subscriptionsString = IO.readFile(currentDirectory+"subscriptions.json");
			JsonElement jsonSubscriptions = JsonParser.parseString(subscriptionsString);
			String apiKeyVulners = jsonSubscriptions.getAsJsonObject().get("apikeyVulners").getAsString();
			String apiKeyWPVulnDB= jsonSubscriptions.getAsJsonObject().get("apikeyWPVulnDB").getAsString();
			
			JsonElement subscriptionElement = jsonSubscriptions.getAsJsonObject().get("entries");
			JsonArray subscriptionArray = subscriptionElement.getAsJsonArray();
			System.out.print("Subscribed to: ");
			for (JsonElement subscriptionJSON : subscriptionArray) {
				Subscription subscription = new Subscription();
				subscription.setName(subscriptionJSON.getAsJsonObject().get("name").getAsString());
				if(subscription.getName().equals("custom_wpvulndb_plugin")){
					pluginArr = subscriptionJSON.getAsJsonObject().get("plugins").getAsJsonArray();
					for (JsonElement plugin : pluginArr) {
						subscription.addPlugin(plugin.getAsString());
					}
				}else{
					subscription.setCVSS(subscriptionJSON.getAsJsonObject().get("cvss").getAsDouble());
					System.out.print(subscription.getName()+", ");
				}
				subscription.setApiKeyVulners(apiKeyVulners);
				subscription.setApiKeyWPVulnDB(apiKeyWPVulnDB);
				subscriptions.add(subscription);
			}
			System.out.println();
		} catch (Exception e) {
			ErrorReporter.handleError("Error loading subscriptions: "+e.getMessage(),e);
		}
		int pluginCount = 0;
		if(pluginArr!=null){
			System.out.println("Subscribed to WP plugins: "+pluginArr.toString());
			pluginCount = pluginArr.size();
		}
		System.out.println("Number of subscriptions: " + (subscriptions.size()+pluginCount));

		return subscriptions;
	}
}
