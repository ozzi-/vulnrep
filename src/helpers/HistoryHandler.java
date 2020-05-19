package helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


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

	public static void writeHistory(HistoryBundle hb, int deleteAfterDays, String currentDirectory) {
		File file;
		try {
			file = new File(currentDirectory+"history.json");
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
		} catch (Exception e) {
			ErrorReporter.handleError("Error getting writing history: "+e.getMessage(),e);
		}
	}

	public static HistoryBundle getHistory(String currentDirectory) {
		if(!ErrorReporter.failed) {
			
		}
		int deleteAfterDays = 0;
		ArrayList<History> historyList = new ArrayList<History>();
		HashSet<String> historySet=new HashSet<String>();  
		int skipCounter = 0;

		try {
			String historyString = IO.readFile(currentDirectory+"history.json");
			
			JsonElement jsonHistory = JsonParser.parseString(historyString);
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
			ErrorReporter.handleError("Error loading history: "+e.getMessage(),e);
		}
		System.out.println("Number of entries in history list: " + historyList.size());
		System.out.println("Number of old entries removed from history list: " + skipCounter);
		HistoryBundle hb = new HistoryBundle(historyList,historySet,deleteAfterDays);
		return hb;
	}
}
