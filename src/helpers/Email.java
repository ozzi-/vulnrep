package helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Email {

	public static void sendToRecipients(String vulnHTML, String currentDirectory) {
		int counter=0;
		try {
			String recipientsString = IO.readFile(currentDirectory+"email.json");
			JsonElement jsonRecipients = JsonParser.parseString(recipientsString);
			JsonElement recipientsElement = jsonRecipients.getAsJsonObject().get("recipients");
			JsonElement senderElement = jsonRecipients.getAsJsonObject().get("sender");
			String address = senderElement.getAsJsonObject().get("address").getAsString();
			String host = senderElement.getAsJsonObject().get("host").getAsString();
			int port = senderElement.getAsJsonObject().get("port").getAsInt();
			String user = senderElement.getAsJsonObject().get("user").getAsString();
			String password = senderElement.getAsJsonObject().get("password").getAsString();
			String subject = senderElement.getAsJsonObject().get("subject").getAsString();
			subject = subject.replace("<d>", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			
			JsonArray recipients = recipientsElement.getAsJsonArray();
			for (JsonElement recipient : recipients) {
				send(recipient.getAsString(),address, subject,"Please find the report attached as HTML", vulnHTML, "report.html", host, port, user, password);
				counter ++;
			}
			System.out.println(counter+" Mail(s) sent");
		} catch (Exception e) {
			ErrorReporter.handleError("Error loading email.json or sending email "+e.getMessage(),e);
		}
	}

	private static void send(String to, String from, String subject, String messageText, String attachment, String attachmentName, String mailHost, int port, String user, String password) {
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", mailHost);
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.user", user);
		properties.setProperty("mail.password", password);
		properties.setProperty("mail.smtp.port", String.valueOf(port));
		properties.setProperty("mail.smtp.starttls.enable", "true");
		
		System.out.println("sending mail from "+from+" to "+to+" with subject "+subject+" via server "+mailHost+":"+port+" and user "+user);
		
	    Session session = Session.getInstance(properties, new SmtpAuthenticator(user, password));
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(messageText);

		    BodyPart messageBodyPart = new MimeBodyPart();
		    messageBodyPart.setText(messageText);
		    Multipart multipart = new MimeMultipart();
		    multipart.addBodyPart(messageBodyPart);
		    messageBodyPart = new MimeBodyPart();
		    DataSource source = new ByteArrayDataSource(attachment, "text/html; charset=UTF-8");
		    messageBodyPart.setDataHandler(new DataHandler(source));
		    messageBodyPart.setFileName(attachmentName);
		    multipart.addBodyPart(messageBodyPart);
		    message.setContent(multipart);

			Transport.send(message);
			
		} catch (Exception e) {
			ErrorReporter.handleError("Error sending email: "+e.getMessage(),e);
		}
	}
}

