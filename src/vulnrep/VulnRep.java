package vulnrep;
import helpers.ArgumentParser;
import helpers.Email;
import helpers.HTML;
import helpers.HistoryHandler;
import helpers.Subscriptions;
import helpers.Vulnerabilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import models.HistoryBundle;
import models.Subscription;
import models.Vulnerability;

public class VulnRep {

	public static void main(String[] args) throws Exception {
	
		Date maxAgeDate = ArgumentParser.parseMaxAge(args);
		ArrayList<Subscription> subscriptions = Subscriptions.loadSubscriptions();
		HistoryBundle hb = HistoryHandler.getHistory();
		
		ArrayList<Vulnerability> searchResults = Vulnerabilities.getVulnerabilities(maxAgeDate, subscriptions, hb);
		
		HistoryHandler.writeHistory(hb, hb.getDeleteAfter());
			
		String vulnHTML = HTML.generateVulnerabilityHTML(searchResults,subscriptions);
		System.out.println("");
		System.out.println(vulnHTML);
		
		try{
		    PrintWriter writer = new PrintWriter("report.html", "UTF-8");
		    writer.println(vulnHTML);
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Email.sendToRecipients(vulnHTML);
	}
}
