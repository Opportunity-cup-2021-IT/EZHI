package ru.hedgehog.multid.predictiveanalytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Anikushin Roman
 */
public class Utils {

	private static final SimpleDateFormat format = new SimpleDateFormat("dd LLLL yyyy HH:mm", new Locale("ru"));

	public static Date parseDate(String string) {
		try {
			return format.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public static String formatDate(Date date) {
		return format.format(date);
	}
}
