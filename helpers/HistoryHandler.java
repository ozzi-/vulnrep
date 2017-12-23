package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import vulnrep.VulnRep;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.History;
import models.HistoryBundle;

public class HistoryHandler {

	public static void writeHistory(HistoryBundle hb, int deleteAfterDays)
			throws URISyntaxException, FileNotFoundException, IOException {
		URL resourceUrl = VulnRep.class.getClass().getResource("/history.json");
		File file = new File(resourceUrl.toURI());
		OutputStream output = new FileOutputStream(file);
		ArrayList<History> historyList = hb.getHistoryList();
		JsonObject history = new JsonObject();
		JsonArray datasets = new JsonArray();
		for (History historyObject : historyList) {
			JsonObject dataset = new JsonObject();
			dataset.addProperty("id", historyObject.getId());

			dataset.addProperty("date", historyObject.getDateString());
			datasets.add(dataset);
		}
		history.add("history", datasets);

		JsonObject deleteAfter = new JsonObject();
		deleteAfter.addProperty("days", deleteAfterDays);
		history.add("deleteAfter", deleteAfter);

		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
		String historyJSONString = gson.toJson(history);

		output.write(historyJSONString.getBytes(Charset.forName("UTF-8")));
		output.close();
	}

	public static HistoryBundle getHistory() {
		int deleteAfterDays = 0;
		ArrayList<History> historyList = new ArrayList<History>();
		HashSet<String> historySet=new HashSet<String>();  

		InputStream stream3 = null;
		int skipCounter = 0;

		try {
			stream3 = VulnRep.class.getClass().getResourceAsStream("/history.json");
			String historyString = Convert.convertStreamToString(stream3);

			JsonElement jsonHistory = new JsonParser().parse(historyString);
			JsonElement deleteAfterElement = jsonHistory.getAsJsonObject().get("deleteAfter");
			deleteAfterDays = deleteAfterElement.getAsJsonObject().get("days").getAsInt();

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -deleteAfterDays);
			Date deleteDate = calendar.getTime();

			JsonElement historyElement = jsonHistory.getAsJsonObject().get("history");
			JsonArray historyArray = historyElement.getAsJsonArray();
			for (JsonElement historyJSON : historyArray) {
				History history = new History();
				history.setId(historyJSON.getAsJsonObject().get("id").getAsString());
				history.setDate(historyJSON.getAsJsonObject().get("date").getAsString());
				if (!history.getDate().before(deleteDate)) {
					historyList.add(history);
					historySet.add(history.getId());
				} else {
					skipCounter++;
				}
			}
		} catch (Exception e) {
			System.out.print("Error loading history: ");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Number of entries in history list: " + historyList.size());
		System.out.println("Number of old entries removed from history list: " + skipCounter);
		HistoryBundle hb = new HistoryBundle(historyList,historySet,deleteAfterDays);
		return hb;
	}
}
