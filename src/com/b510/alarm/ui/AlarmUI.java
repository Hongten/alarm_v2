/**
 * 
 */
package com.b510.alarm.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

/**
 * The <code>AlarmUI</code> class extends <code>JUI</code> and implements <code>ActionListener</code>.
 * @author Hongten
 * @created 31 Dec, 2015
 */
public class AlarmUI extends JUI implements ActionListener {
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(AlarmUI.class);
	
	private MainUI mainUI;

	public AlarmUI(String title) {
		super(title);
		logger.debug("title = " + title);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	public void init() {
		if (null == mainUI) {
			mainUI = new MainUI(title);
		}
		mainUI.init();
	}
}
