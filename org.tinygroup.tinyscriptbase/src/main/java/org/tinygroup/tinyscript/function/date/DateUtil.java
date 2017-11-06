package org.tinygroup.tinyscript.function.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
	private static final List<String> weekDays = new ArrayList<String>();
	static {
		weekDays.add("sunday");
		weekDays.add("monday");
		weekDays.add("tuesday");
		weekDays.add("wednesday");
		weekDays.add("thursday");
		weekDays.add("friday");
		weekDays.add("saturday");
	}

	public static String getWeekday(int index) {
		return weekDays.get(index);
	}

	public static int getWeekDay(String weekDay) {
		return weekDays.indexOf(weekDay);
	}

	public static int countWeekDiff(Calendar c1, Calendar c2, String startWeekDay) {
		int diffWeeks = 0;
		startWeekDay = startWeekDay == null ? "sunday" : startWeekDay;
		if (c1.get(Calendar.DAY_OF_WEEK) > 1 + getWeekDay(startWeekDay)) {
			c1.set(Calendar.DATE, c1.get(Calendar.DATE) + 8 - c1.get(Calendar.DAY_OF_WEEK) + getWeekDay(startWeekDay));
			if (c1.getTimeInMillis() <= c2.getTimeInMillis()) {
				diffWeeks = 1;
			}
		} else if (c1.get(Calendar.DAY_OF_WEEK) < 1 + getWeekDay(startWeekDay)) {
			c1.set(Calendar.DATE, c1.get(Calendar.DATE) + 1 - c1.get(Calendar.DAY_OF_WEEK) + getWeekDay(startWeekDay));
			if (c1.getTimeInMillis() <= c2.getTimeInMillis()) {
				diffWeeks = 1;
			}
		}
		if (c1.getTimeInMillis() > c2.getTimeInMillis()) {
			return diffWeeks;
		}
		int daysDiff = (int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / (1000 * 3600 * 24));
		return daysDiff / 7 + diffWeeks;
	}
	
	public static Date convertDateByString(String s, String rule) throws ParseException {
		DateFormat format;
		if (rule != null) {
			format = new SimpleDateFormat(rule);
		} else {
			format = new SimpleDateFormat();
		}
		return format.parse(s);
	}

	public static String dateToString(Date date, String rule) {
		DateFormat dateFormat;
		if (rule != null) {
			dateFormat = new SimpleDateFormat(rule);
		} else {
			dateFormat = new SimpleDateFormat();
		}
		return dateFormat.format(date);
	}
}
