package vulnrep;
import helpers.ArgumentParser;
import helpers.Email;
import helpers.ErrorReporter;
import helpers.HTML;
import helpers.HistoryHandler;
import helpers.IO;
import helpers.Subscriptions;
import helpers.Vulnerabilities;

import java.util.ArrayList;
import java.util.Date;

import models.HistoryBundle;
import models.Subscription;
import models.Vulnerability;

public class VulnRep {

	public static void main(String[] args) {
		
		String currentDirectory = IO.getCurrentDirectory();
		HistoryBundle hb = null;
		ArrayList<Vulnerability>searchResults = null;
		
		// Setup
		Date maxAgeDate = ArgumentParser.parseMaxAge(args);
		ArrayList<Subscription> subscriptions = Subscriptions.loadSubscriptions(currentDirectory);
		if(!ErrorReporter.failed) {
			hb = HistoryHandler.getHistory(currentDirectory);			
		}
		String vulnHTML = "";
		
		// Run
		if(!ErrorReporter.failed) {
			searchResults = Vulnerabilities.getVulnerabilities(maxAgeDate, subscriptions, hb);
		}
		if(!ErrorReporter.failed) {
			HistoryHandler.writeHistory(hb, hb.getDeleteAfter(),currentDirectory);			
		}
		if(!ErrorReporter.failed) {
			vulnHTML = HTML.generateVulnerabilityHTML(searchResults,subscriptions);			
		}
		System.out.println("");
		System.out.println(vulnHTML);
		
		// Send and write report
		Email.sendToRecipients(vulnHTML,currentDirectory);
		IO.writeReport(vulnHTML);
	}
}