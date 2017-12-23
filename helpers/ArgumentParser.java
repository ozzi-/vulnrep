package helpers;

import java.util.Calendar;
import java.util.Date;

import models.MaxAgeType;

public class ArgumentParser {
	public static Date parseMaxAge(String[] args) {
		MaxAgeType maxAgeType = MaxAgeType.day;
		int maxAge = 7;
		Date maxAgeDate;
		String maxageArg;

		if (args.length < 1) {
			System.out.println("No command line argument definind the vulnerability maximum age was found.");
			System.out.println("Using default of 7 days. (=java -jar vulndb 7d)");
			System.out.println("m = Minute, h = Hour, d = Day");
			maxageArg = "7d";
		} else {
			maxageArg = args[0];
		}

		String maxageTypeArg = maxageArg.substring(maxageArg.length() - 1);
		boolean invalidType = false;

		switch (maxageTypeArg) {
		case "d":
			maxAgeType = MaxAgeType.day;
			break;
		case "m":
			maxAgeType = MaxAgeType.minute;
			break;
		case "h":
			maxAgeType = MaxAgeType.hour;
			break;
		default:
			System.out.println("Invalid maximum age type >" + maxageTypeArg + "<");
			System.out.println("Defaulting to 7d");
			invalidType = true;
			break;
		}
		if (!invalidType) {
			try {
				maxAge = Integer.valueOf(maxageArg.substring(0, maxageArg.length() - 1));
			} catch (Exception e) {
				System.out.println("Cannot parse maximum age parameter");
				System.out.println("Defaulting to 7d");
			}
		}

		Calendar calendar = Calendar.getInstance();
		if (maxAgeType.equals(MaxAgeType.day)) {
			calendar.add(Calendar.DAY_OF_MONTH, -maxAge);
		} else if (maxAgeType.equals(MaxAgeType.hour)) {
			calendar.add(Calendar.HOUR_OF_DAY, -maxAge);
		} else if (maxAgeType.equals(MaxAgeType.minute)) {
			calendar.add(Calendar.MINUTE, -maxAge);
		}
		maxAgeDate = calendar.getTime();
		return maxAgeDate;
	}
}
