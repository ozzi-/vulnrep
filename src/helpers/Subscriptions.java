package helpers;

import java.io.InputStream;
import java.util.ArrayList;

import vulnrep.VulnRep;
import models.Subscription;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Subscriptions {
	public static ArrayList<Subscription> loadSubscriptions() {
		ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();
		InputStream stream2 = null;
		JsonArray pluginArr = null;
		try {
			stream2 = VulnRep.class.getClass().getResourceAsStream("/subscriptions.json");
			String subscriptionsString = Convert.convertStreamToString(stream2);
			stream2.close();
			JsonElement jsonSubscriptions = new JsonParser().parse(subscriptionsString);
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
			System.out.print("Error loading subscriptions: ");
			System.out.println(e.getMessage());
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
