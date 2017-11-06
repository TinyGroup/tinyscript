package org.tinygroup.tinyscript.function.date;

public enum DateEnum {
	YEAR(1), MONTH(2), WEEK(3), DAY(5), HOUR(10), MINUTE(12), SECOND(13), WEEKDAY(7);

	private int calendarId;
	

	private DateEnum(int calendarId) {
		this.calendarId = calendarId;
	}

	public int getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(int calendarId) {
		this.calendarId = calendarId;
	}
}
