/**
 * 
 */
package com.b510.alarm.util;

import java.util.Calendar;

import com.b510.alarm.common.Common;

/**
 * @author Hongten
 * @created 31 Dec, 2015
 */
public class AlarmUtil {

	public static int secondOfResult;

	/**
	 * get the current time
	 */
	public static String now() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		String h = hour < 10 ? Common.ZEOR_STR : Common.BLANK;
		String m = min < 10 ? Common.ZEOR_STR : Common.BLANK;
		String s = sec < 10 ? Common.ZEOR_STR : Common.BLANK;
		String current = new String(h + hour + Common.COLOR + m + min + Common.COLOR + s + sec);
		return current;
	}

	/**
	 * get current hour
	 */
	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * get current minute
	 */
	public static int getMunite() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	/**
	 * get current second
	 */
	public static int getSecond() {
		return Calendar.getInstance().get(Calendar.SECOND);
	}

	public static int surplusTime(int hr, int minute, int second) {
		int h = getHour();
		int m = getMunite();
		int s = getSecond();
		int dh = 0;
		int dm = 0;
		int ds = 0;
		if (hr != 0) {
			dh = hr - 1;
		}
		if (minute != 0) {
			dm = minute - 1;
		}
		if (second != 0) {
			ds = second - 1;
		}
		int hour = dh - h;
		int min = dm - m;
		int sec = ds - s;
		if (hour == 0) {
			if (min == 0) {
				if (sec == 0) {
					// r = Common.TIME_IS_UP;
				}
				if (sec < 0) {
					hour += 23;
					min += 59;
					sec += 59;
				}
			}
			if (min < 0) {
				hour += 23;
				if (sec < 0) {
					min -= 1;
					sec += 59;
				}
				min += 60;
			}
			if (min >= 0) {
				if (sec < 0 || sec == 0) {
					min -= 1;
					sec += 59;
				}
				if (sec > 0) {
					// sec=sec;
				}
			}
		}
		if (hour < 0) {
			if (min <= 0) {
				if (sec <= 0) {
					hour -= 1;
					min += 59;
					sec += 59;
				}
			}
			if (min > 0) {
				if (sec <= 0) {
					min -= 1;
					sec += 59;
				}
			}
			hour += 24;
		}
		if (hour > 0) {
			if (min == 0) {
				if (sec <= 0) {
					hour -= 1;
					min += 59;
					sec += 59;
				}
			}
			if (min < 0) {
				if (sec < 0) {
					min -= 1;
					sec += 59;
				}
				min += 60;
				hour -= 1;
			}
			if (min > 0) {
				if (sec < 0 || sec == 0) {
					min -= 1;
					sec += 59;
				}
			}
		}

		if (sec == 30 && min == 0 && hour == 0) {
			secondOfResult = sec;
		}
		return hour * 60 * 60 + min * 60 + sec;
	}
}
