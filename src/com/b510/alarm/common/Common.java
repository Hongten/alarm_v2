/**
 * 
 */
package com.b510.alarm.common;

/**
 * All variables will be here.
 * 
 * @author Hongten
 * @created 31 Dec, 2015
 */
public class Common {

	public static String ALARM = "Alarm";
	public static String EMPTY = "";
	public static String BLANK = " ";
	public static String ADD = "+";
	public static String SUB = "-";
	public static String COLOR = ":";
	public static String ZEOR_STR = "0";

	// UI Size
	public static final int WINDOW_WIDTH = 428;
	public static final int WINDOW_HEIGHT = 525;
	public static final int WINDOW_MINI_HEIGHT = 197;

	// Dialog messages and titles
	public static String CONFIRM_EXIT = "Confirm Exit";
	public static String EXIT_SYSTEM = "Exit Alarm?";
	public static String CONFIRM_MINIMIZE = "Minimize to tray?";
	public static String NOTICE = "Notice";
	public static String TIME_IS_UP = "Time is Up!";

	// Menu in MainUI
	public static String FILE = "File";

	// button and menu description
	public static String EXIT = "Exit";
	public static String MINI = "Mini";
	public static String NORMAL = "Normal";
	public static String ABOUT = "About " + ALARM;

	// tray
	public static final String OPEN_MAIN_PANEL = "Open main panel";
	public static final String RESTORE = "Restore";

	// KeyStroke
	public static char N = 'N';
	public static char W = 'W';
	public static char M = 'M';
	public static char A = 'A';

	// buttons text
	public static String ONE = "1";
	public static String FIVE = "5";
	public static String THIRTH = "30";
	public static String ADD_1 = ADD + ONE;
	public static String ADD_5 = ADD + FIVE;
	public static String ADD_30 = ADD + THIRTH;
	public static String SUB_1 = SUB + ONE;
	public static String SUB_5 = SUB + FIVE;
	public static String SUB_30 = SUB + THIRTH;
	public static String LISTENING = "Listening";
	public static String RING = "Ring";
	public static String STOP = "Stop";
	public static String CANCEL = "Cancel";
	public static String LISTENING_RING = LISTENING + BLANK + RING;
	public static String STOP_RING = STOP + BLANK + RING;
	public static String CANCEL_ALARM = CANCEL + BLANK + ALARM;

	// label text
	public static String FAST = "Fast";
	public static String SETTING = "Setting";
	public static String TIME = "Time";
	public static String FIRST_SETTING = FAST + BLANK + SETTING + BLANK + COLOR;
	public static String RING_SETTING = RING + BLANK + COLOR;
	public static String RING_TIME = RING + BLANK + TIME + BLANK + COLOR;
	public static String DEFAULT_TIME = ZEOR_STR + ZEOR_STR + COLOR + ZEOR_STR + ZEOR_STR + COLOR + ZEOR_STR + ZEOR_STR;

	// Bar tool tip text
	public static String HR = BLANK + "hr";
	public static String MIN = BLANK + "min";
	public static String SEC = BLANK + "sec";

	// combobox text
	public static String CLOSE = "Close";
	public static String[] HOURS_NUM = new String[] { "Close", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };
	public static String[] MINUTE_NUM = new String[] { "Close", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };
	public static String[] SECOND_NUM = new String[] { "Close", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };
	public static String[] SOUND_NUM = new String[] { "Bandari-Childhood Memory", "Bandari-New Morning", "Bandari-Snowdreams", "Bandari-The Foggy Dew", "Bandari-Your Smile" };

	// resources

	// image resource path
	public static final String BASE_IMAGE_PATH = "com/b510/alarm/resources/images/";
	public static final String IMAGE_TRAY = BASE_IMAGE_PATH + "mytray.png";
	public static String[] ALARM_SOUNDS = new String[] { "src/com/b510/alarm/resources/sounds/Bandari-Childhood Memory.mp3", "src/com/b510/alarm/resources/sounds/Bandari-New Morning.mp3", "src/com/b510/alarm/resources/sounds/Bandari-Snowdreams.mp3", "src/com/b510/alarm/resources/sounds/Bandari-The Foggy Dew.mp3", "src/com/b510/alarm/resources/sounds/Bandari-Your Smile.mp3" };

	//about information
	public static final String ABOUT_INFO =
			"<html><head><style>"
			+ "table {"
			+"width:100%;}"
			+"table, td {"
			+"border: 1px solid black;}"
			+ "</style>"
			+ "</head>"
	
			+ "<body>"
			+ "<br>"
				+ "<table id='t01'>"
					+ "<tr>"
				    	+ "<td>Application Name</td>"
				    	+ "<td>Alarm</td>"		
				    + "</tr>"
				    + "<tr>"
				    	+ "<td>Version</td>"
				    	+ "<td>2.0</td>"	
				    + "</tr>"
				    + "<tr>"
				    	+ " <td>Author</td>"
				    	+ " <td>Hongten</td>"		
				    + "</tr>"
				    + " <tr>"
				  		+ " <td>Home Page</td>"
				  		+ "<td><a href='http://www.cnblogs.com/hongten' target='_blank'><font color='#880000'><b>http://www.cnblogs.com/hongten</b></font></a></td>"	
				    + "</tr>"
				    + " <tr>"
			  			+ " <td>github</td>"
			  			+ "<td><a href='https://github.com/Hongten' target='_blank'><font color='#880000'><b>https://github.com/Hongten</b></font></a></td>"	
			  		+ "</tr>"
				+ "</table>"

		+ "</body>" + "</html>";
}
