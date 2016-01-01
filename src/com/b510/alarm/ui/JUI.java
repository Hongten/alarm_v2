/**
 * 
 */
package com.b510.alarm.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.StandardBorderPainter;
import org.jvnet.substance.button.ClassicButtonShaper;
import org.jvnet.substance.painter.StandardGradientPainter;
import org.jvnet.substance.skin.OfficeBlue2007Skin;
import org.jvnet.substance.theme.SubstanceAquaTheme;
import org.jvnet.substance.theme.SubstanceTheme;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;

/**
 * @author Hongten
 * @created 31 Dec, 2015
 */
public class JUI extends JFrame {

	private static final long serialVersionUID = -1897575774699775089L;

	static Logger logger = Logger.getLogger(JUI.class);

	String title;

	public SubstanceTheme getTheme() {
		return new SubstanceAquaTheme();
	}

	/**
	 * Set the page UI. including the theme, skin, watermark.etc.
	 */
	public void setJUI() {
		try {
			UIManager.setLookAndFeel(new SubstanceLookAndFeel());
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			SubstanceLookAndFeel.setCurrentTheme(new SubstanceAquaTheme());
			SubstanceLookAndFeel.setSkin(new OfficeBlue2007Skin());
			SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());
			SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
			SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
			SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}

	public JUI(String title) {
		this.title = title;
		setJUI();
	}

	public void init() {

	}

}
