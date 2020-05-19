package helpers;

import java.util.ArrayList;
import java.util.Calendar;

import models.Subscription;
import models.Vulnerability;

public class HTML {
	public static String generateVulnerabilityHTML(ArrayList<Vulnerability> searchResults, ArrayList<Subscription> subscriptions) {
		String html = "";
		String oldSearchTerm = "";
		html += "Generated: "+Calendar.getInstance().getTime()+"<br>";
		html += "Checking for ";
		String htmlPost ="";
		for (Subscription subscription : subscriptions) {
			if(subscription.getName().equals("custom_wpvulndb_plugin")){
				htmlPost = " WordPress plugins: "+subscription.getPlugins();
			}else{
				html+=subscription.getName()+", ";				
			}
		}
		html+=htmlPost;
		html += "<br><hr>";
		for (Vulnerability vulnerability : searchResults) {
			if (!oldSearchTerm.equals(vulnerability.getSearchTerm())) {
				oldSearchTerm = vulnerability.getSearchTerm();
				html += "<hr><h1>" + vulnerability.getSearchTerm() + "</h1><hr>";
			}
			html += "<h2>" + vulnerability.getTitle() + "</h2>";
			html += "<b>CVSS " + vulnerability.getCvssAsString() + "</b> " + "<a href=\"" + vulnerability.getHref()
					+ "\">Direct Link</a> ";
			html += (vulnerability.getVhref()!=null?"<a href=\"" + vulnerability.getVhref() + "\">Vulners Link</a><br>":"");
			html += vulnerability.metricToHTML()+"<br>";
			html += vulnerability.getDescription() + "<br>";
			html += "<i>Published on: " + vulnerability.getPublished() + "</i><br><br>";
		}	
		return html;
	}
	
	public static String finishHTML(String vulnHTML) {	
		String vulnHTMLComplete="<!DOCTYPE html>\n<html><head><meta charset=\"UTF-8\"><title>Vulnerability Report</title><style rel=\"stylesheet\" type=\"text/css\">\n" +  
		"body {\n" + 
		" font-family: Arial, Helvetica, sans-serif;\n" + 
		" font-size: 13px;\n" + 
		"}</style></head><body>";
		
	    if(ErrorReporter.failed) {
	    	vulnHTMLComplete+="Error(s) occured:<br>\r\n";
	    	vulnHTMLComplete+="*****************<br>\r\n";
	    	vulnHTMLComplete+=ErrorReporter.errorMessage+ "<br>\r\n<br>\r\n";
	    }
	    return vulnHTMLComplete + vulnHTML +"</body></html>";
	}
}
