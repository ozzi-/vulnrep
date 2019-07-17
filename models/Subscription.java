package models;

import java.util.ArrayList;

public class Subscription {
	private String name;
	private double CVSS;
	private String apiKeyVulners;
	private String apiKeyWPVulnDB;

	
	private ArrayList<String> plugins = new ArrayList<String>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getCVSS() {
		return CVSS;
	}
	public void setCVSS(double cVSS) {
		CVSS = cVSS;
	}
	public ArrayList<String> getPlugins(){
		return plugins;
	}
	public void addPlugin(String plugin){
		plugins.add(plugin);
	}
	public void setPlugins(ArrayList<String> plugins){
		this.plugins=plugins;
	}
	public String getApiKeyVulners() {
		return apiKeyVulners;
	}
	public void setApiKeyVulners(String apiKey) {
		this.apiKeyVulners = apiKey;
	}
	public String getApiKeyWPVulnDB() {
		return apiKeyWPVulnDB;
	}
	public void setApiKeyWPVulnDB(String apiKeyWPVulnDB) {
		this.apiKeyWPVulnDB = apiKeyWPVulnDB;
	}
}
