/**
 * 
 */
package com.b510.alarm.client;

import com.b510.alarm.common.Common;
import com.b510.alarm.ui.AlarmUI;

/**
 * The entrance for the this application.<br>
 * @author Hongten
 * @created 31 Dec, 2015
 */
public class Client {
	
	public static void main(String[] args) {
		AlarmUI alarmUI = new AlarmUI(Common.ALARM);
		alarmUI.init();
	}
}
