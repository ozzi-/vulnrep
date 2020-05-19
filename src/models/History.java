package models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class History {
	private String id;
	private Date date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public String getDateString() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return format.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDate(String date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			this.date = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
