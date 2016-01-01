/**
 * 
 */
package com.b510.alarm.util;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.b510.alarm.common.Common;
import com.b510.alarm.ui.MainUI;

/**
 * @author Hongten
 * @created 1 Jan, 2016
 */
public class TrayUtil extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = -2948244948496624898L;

	static Logger logger = Logger.getLogger(TrayUtil.class);

	private Image icon;
	private TrayIcon trayIcon;
	private SystemTray systemTray;
	private PopupMenu popupMenu = new PopupMenu();

	private MenuItem miniItem;
	private MenuItem restoreItem;
	private MenuItem exitItem;

	private MainUI mainUI;

	public TrayUtil(MainUI mainUI) {
		this.mainUI = mainUI;
		initTray();
	}

	/**
	 * init tray
	 */
	public void initTray() {
		if (SystemTray.isSupported()) {
			generateTrayIcon();
			generatePopuppMenu();
			systemTray = SystemTray.getSystemTray();
			try {
				systemTray.add(trayIcon);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * tray icon setting
	 */
	private void generateTrayIcon() {
		icon = new ImageIcon(this.getClass().getClassLoader().getResource(Common.IMAGE_TRAY)).getImage();
		trayIcon = new TrayIcon(icon, Common.OPEN_MAIN_PANEL, popupMenu);
		trayIcon.addMouseListener(this);
	}

	/**
	 * all tray menus will be initialized
	 */
	private void generatePopuppMenu() {
		miniItem = new MenuItem(Common.MINI);
		miniItem.addActionListener(this);
		popupMenu.add(miniItem);

		restoreItem = new MenuItem(Common.RESTORE);
		restoreItem.addActionListener(this);
		popupMenu.add(restoreItem);

		popupMenu.addSeparator();

		exitItem = new MenuItem(Common.EXIT);
		exitItem.addActionListener(this);
		popupMenu.add(exitItem);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if ((e.getClickCount() == 1 || e.getClickCount() == 2) && e.getButton() != MouseEvent.BUTTON3) {
			if (mainUI.isVisible()) {
				mainUI.setVisible(false);
			} else {
				mainUI.setVisible(true);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == miniItem) {
			// show the main user interface and exchange the normal/mini window.
			mainUI.setVisible(true);
			if (MainUI.MINI_STATUS) {
				mainUI.setSize(Common.WINDOW_WIDTH, Common.WINDOW_MINI_HEIGHT);
				mainUI.setMiniItemText(Common.NORMAL);
				setMiniitemLabel(Common.NORMAL);
			} else {
				mainUI.setSize(Common.WINDOW_WIDTH, Common.WINDOW_HEIGHT);
				mainUI.setMiniItemText(Common.MINI);
				setMiniitemLabel(Common.MINI);
			}
			MainUI.MINI_STATUS = !MainUI.MINI_STATUS;
		} else if (e.getSource() == restoreItem) {
			// show main user interface
			mainUI.setVisible(true);
		} else if (e.getSource() == exitItem) {
			mainUI.existSystem();
		}
	}

	public void setMiniitemLabel(String text) {
		miniItem.setLabel(text);
	}
}
