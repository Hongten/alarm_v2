package com.b510.alarm.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.apache.log4j.Logger;

import com.b510.alarm.common.Common;
import com.b510.alarm.core.CountdownBarThread;
import com.b510.alarm.core.PlayerThread;
import com.b510.alarm.util.AlarmUtil;
import com.b510.alarm.util.TrayUtil;

/**
 * @author Hongten
 * @created 31 Dec, 2015
 */
@SuppressWarnings("rawtypes")
public class MainUI extends AlarmUI {

	private static final long serialVersionUID = 6609482987267647944L;

	static Logger logger = Logger.getLogger(MainUI.class);

	// All panels
	JPanel jPanelBars, jPanelBottom, jPanelTop, jPanelSetting;
	// All buttons
	JButton jButtonCancel, jButtonListening, jButtonStop, jButton_add_1, jButton_add_30, jButton_add_5, jButton_sub_1, jButton_sub_30, jButton_sub_5;
	// All comboBoxes
	JComboBox jComboBoxHour, jComboBoxMinute, jComboBoxSecond, jComboBoxRing;
	// All labels
	JLabel jLabelCurrentTime, jLabelSettingFast, jLabelRing, jLabelRingTime, jLabelTimer, jLabelTotalTime;
	// All progressBars
	JProgressBar jProgressBarHour, jProgressBarMinute, jProgressBarSecond, jProgressBarTimer;
	// menubar
	private JMenuBar jMenuBar;
	// All menus.
	private JMenu jMenuFile;
	// All sub-menus(include separator)
	private JSeparator line;
	private JMenuItem miniItem, exitItem, aboutItem;

	String h, m, s;

	// mini window status. Ctrl + M can set the mini/normal window.
	public static boolean MINI_STATUS = true;

	CountdownBarThread countdownBarThread;
	PlayerThread playerThread;
	Timer countdown, startTimer, timeUpTimer;
	int time, currentCountdown;

	// Tray tool
	private TrayUtil tray;

	/**
	 * Creates new form MainUI
	 */
	public MainUI(String title) {
		super(title);
		this.setTitle(title);
	}

	// exit system
	public void existSystem() {
		logger.debug("Exit System.....");
		System.exit(0);
	}

	/*
	 * the action listener
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		actionForFileMenuItem(e);
		actionForButtons(e);
		actionForHMSComboBoxes(e);
	}

	private void actionForHMSComboBoxes(ActionEvent e) {
		// jComboBoxHour, jComboBoxMinute, jComboBoxSecond
		if (e.getSource() == jComboBoxHour) {
			logger.debug("hour change");
			valueJudgment();
		} else if (e.getSource() == jComboBoxMinute) {
			logger.debug("minute change");
			valueJudgment();
		} else if (e.getSource() == jComboBoxSecond) {
			logger.debug("second change");
			valueJudgment();
		}
	}

	/**
	 * add action listener for all menus. e.g. when you click the '+1' menu,
	 * then execute add 1 minute operation.
	 */
	private void actionForButtons(ActionEvent e) {
		if (e.getSource() == jButtonCancel) {
			cancelAction();
		} else if (e.getSource() == jButtonListening) {
			logger.debug("listening sound.");
			playAction();
		} else if (e.getSource() == jButtonStop) {
			logger.debug("stop sound.");
			stopAction();
		} else if (e.getSource() == jButton_add_1) {
			logger.debug("+ 1 munite.");
			add1Action();
		} else if (e.getSource() == jButton_add_5) {
			logger.debug("+ 5 minutes.");
			add5Action();
		} else if (e.getSource() == jButton_add_30) {
			logger.debug("+ 30 minutes.");
			add30Action();
		} else if (e.getSource() == jButton_sub_1) {
			sub1Action();
			logger.debug("- 1 minute.");
		} else if (e.getSource() == jButton_sub_5) {
			logger.debug("- 5 minutes.");
			sub5Action();
		} else if (e.getSource() == jButton_sub_30) {
			logger.debug("- 30 minutes.");
			sub30Action();
		}

	}

	/**
	 * stop sound
	 */
	private void stopAction() {
		if (playerThread != null) {
			playerThread.stopPlaying();
		}
	}

	/**
	 * listening sound
	 */
	void playAction() {
		// stopping the sound before listening.
		stopAction();
		int soudIndex = jComboBoxRing.getSelectedIndex();
		String soudPath = Common.ALARM_SOUNDS[soudIndex];
		File file = new File(soudPath);

		if (playerThread != null) {
			playerThread.stopPlaying();
			playerThread = null;
		}

		playerThread = PlayerThread.startPlaying(file);
	}

	/**
	 * cancel alarm
	 */
	private void cancelAction() {
		logger.debug("cancel alarm.");
		//stop the sound first.
		stopAction();
		if (startTimer != null) {
			startTimer.stop();
		}
		if (timeUpTimer != null) {
			timeUpTimer.stop();
		}
		if (countdownBarThread != null) {
			countdownBarThread.setStop(true);
		}
		jButtonListening.setEnabled(true);
		jButtonStop.setEnabled(true);

		jComboBoxHour.setSelectedIndex(0);
		jComboBoxMinute.setSelectedIndex(0);
		jComboBoxSecond.setSelectedIndex(0);
		time = 0;
		currentCountdown = 0;

		// wait 1 minute
		try {
			Thread.sleep(2000);
			jProgressBarHour.setValue(0);
			jProgressBarMinute.setValue(0);
			jProgressBarSecond.setValue(0);
			if (countdownBarThread != null) {
				countdownBarThread.setCurrent(0);
				jProgressBarTimer.setValue(countdownBarThread.getCurrent());
				jLabelTimer.setText(refreshTimer(countdownBarThread.getCurrent()));
			}
			jProgressBarTimer.setValue(0);
			jLabelTotalTime.setText(Common.DEFAULT_TIME);
			jLabelTimer.setText(Common.DEFAULT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * add 1 minute
	 */
	private void add1Action() {
		isHMSZero();
		if (jComboBoxMinute.getSelectedIndex() + 1 > 60) {
			jComboBoxMinute.setSelectedIndex(1);
			if (jComboBoxHour.getSelectedIndex() > 23) {
				jComboBoxHour.setSelectedIndex(1);
			} else {
				jComboBoxHour.setSelectedIndex(jComboBoxHour.getSelectedIndex() + 1);
			}
		} else {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() + 1);
		}
		valueJudgment();
	}

	/**
	 * add 5 minutes
	 */
	private void add5Action() {
		isHMSZero();
		if (jComboBoxMinute.getSelectedIndex() + 5 > 60) {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() - 55);
			if (jComboBoxHour.getSelectedIndex() > 23) {
				jComboBoxHour.setSelectedIndex(1);
			} else {
				jComboBoxHour.setSelectedIndex(jComboBoxHour.getSelectedIndex() + 1);
			}
		} else {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() + 5);
		}
		valueJudgment();
	}

	/**
	 * add 30 minutes
	 */
	private void add30Action() {
		isHMSZero();
		if (jComboBoxMinute.getSelectedIndex() + 30 > 60) {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() - 30);
			if (jComboBoxHour.getSelectedIndex() > 23) {
				jComboBoxHour.setSelectedIndex(1);
			} else {
				jComboBoxHour.setSelectedIndex(jComboBoxHour.getSelectedIndex() + 1);
			}
		} else {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() + 30);
		}
		valueJudgment();
	}

	/**
	 * sub 1 minute
	 */
	private void sub1Action() {
		isHMSZero();
		if (jComboBoxMinute.getSelectedIndex() - 1 == 0) {
			jComboBoxMinute.setSelectedIndex(60);
			if (jComboBoxHour.getSelectedIndex() == 0) {
				jComboBoxHour.setSelectedIndex(24);
			} else {
				jComboBoxHour.setSelectedIndex(jComboBoxHour.getSelectedIndex() - 1);
			}
		} else {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() - 1);
		}
		valueJudgment();
	}

	/**
	 * sub 5 minutes
	 */
	private void sub5Action() {
		isHMSZero();
		if (jComboBoxMinute.getSelectedIndex() - 5 < 0) {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() + 55);
			if (jComboBoxHour.getSelectedIndex() == 0) {
				jComboBoxHour.setSelectedIndex(24);
			} else {
				jComboBoxHour.setSelectedIndex(jComboBoxHour.getSelectedIndex() - 1);
			}
		} else {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() - 5);
		}
		valueJudgment();
	}

	/**
	 * sub 30 minutes
	 */
	private void sub30Action() {
		isHMSZero();
		if (jComboBoxMinute.getSelectedIndex() - 30 < 0) {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() + 30);
			if (jComboBoxHour.getSelectedIndex() == 0) {
				jComboBoxHour.setSelectedIndex(24);
			} else {
				jComboBoxHour.setSelectedIndex(jComboBoxHour.getSelectedIndex() - 1);
			}
		} else {
			jComboBoxMinute.setSelectedIndex(jComboBoxMinute.getSelectedIndex() - 30);
		}
		valueJudgment();
	}

	private void valueJudgment() {
		h = jComboBoxHour.getSelectedItem().toString();
		m = jComboBoxMinute.getSelectedItem().toString();
		s = jComboBoxSecond.getSelectedItem().toString();
		hsmCheck();
	}

	private void hsmCheck() {
		if (!Common.CLOSE.equals(h) && !Common.CLOSE.equals(m) && !Common.CLOSE.equals(s)) {
			jButtonCancel.setEnabled(true);
			jButtonListening.setEnabled(false);
			jButtonStop.setEnabled(false);
			startCountdown();
		}
	}

	private void isHMSZero() {
		if (jComboBoxHour.getSelectedIndex() == 0) {
			jComboBoxHour.setSelectedIndex(AlarmUtil.getHour() + 1);
		}
		if (jComboBoxMinute.getSelectedIndex() == 0) {
			jComboBoxMinute.setSelectedIndex(AlarmUtil.getMunite() + 1);
		}
		if (jComboBoxSecond.getSelectedIndex() == 0) {
			jComboBoxSecond.setSelectedIndex(AlarmUtil.getSecond());
		}
	}

	private void startCountdown() {
		time = AlarmUtil.surplusTime(jComboBoxHour.getSelectedIndex(), jComboBoxMinute.getSelectedIndex(), jComboBoxSecond.getSelectedIndex());
		countdownBarThread = new CountdownBarThread(time);
		countdownBarThread.setCurrent(0);
		new Thread(countdownBarThread).start();

		jProgressBarTimer.setMinimum(0);
		jProgressBarTimer.setMaximum(time);
		jLabelTotalTime.setText(refreshTimer(time));
		// if you are listening the soud, then will be stoped.
		stopAction();

		startTimer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (countdownBarThread != null) {
					jProgressBarTimer.setValue(countdownBarThread.getCurrent());
					jLabelTimer.setText(refreshTimer(countdownBarThread.getCurrent()));
					if (!countdownBarThread.stop) {
						currentCountdown = AlarmUtil.surplusTime(jComboBoxHour.getSelectedIndex(), jComboBoxMinute.getSelectedIndex(), jComboBoxSecond.getSelectedIndex());
						int hr = getHour(currentCountdown);
						int min = getMinute(currentCountdown);
						int sec = getSecond(currentCountdown);
						jProgressBarHour.setValue(hr);
						jProgressBarMinute.setValue(min);
						jProgressBarSecond.setValue(sec);
						jProgressBarHour.setToolTipText(hr + Common.HR);
						jProgressBarMinute.setToolTipText(min + Common.MIN);
						jProgressBarSecond.setToolTipText(sec + Common.SEC);
					}
				}
			}
		});
		startTimer.start();
		// time up timer
		timeUpTimer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (countdownBarThread != null) {
					timeUp();
				}
			}
		});
		timeUpTimer.start();
	}

	private String refreshTimer(int current) {
		int hr = current / 3600;
		int min = (current - ((current / 3600) * 3600)) / 60;
		int sec = current % 60;
		String hour = Common.BLANK;
		String minute = Common.BLANK;
		String second = Common.BLANK;
		hour = hr <= 9 ? Common.ZEOR_STR + hr : Common.EMPTY + hr;
		minute = min <= 9 ? Common.ZEOR_STR + min : Common.EMPTY + min;
		second = sec <= 9 ? Common.ZEOR_STR + sec : Common.EMPTY + sec;
		return hour + Common.COLOR + minute + Common.COLOR + second;
	}

	private int getHour(int current) {
		return current / 3600;
	}

	private int getMinute(int current) {
		return (current - ((current / 3600) * 3600)) / 60;
	}

	private int getSecond(int current) {
		return current % 60;
	}

	/**
	 * time up
	 */
	public void timeUp() {
		int h = AlarmUtil.getHour();
		int m = AlarmUtil.getMunite();
		int s = AlarmUtil.getSecond();
		int dh = 0;
		int dm = 0;
		int ds = 0;
		if (jComboBoxHour.getSelectedIndex() != 0) {
			dh = jComboBoxHour.getSelectedIndex() - 1;
		}
		if (jComboBoxMinute.getSelectedIndex() != 0) {
			dm = jComboBoxMinute.getSelectedIndex() - 1;
		}
		if (jComboBoxSecond.getSelectedIndex() != 0) {
			ds = jComboBoxSecond.getSelectedIndex() - 1;
		}
		int hour = dh - h;
		int min = dm - m;
		int sec = ds - s;
		if (hour == 0 && min == 0 && sec == 0) {
			this.setVisible(true);
			timeUpAction();
			playAction();
		}
	}

	/**
	 * time up action
	 */
	private void timeUpAction() {
		logger.debug("time up action.");
		if (startTimer != null) {
			startTimer.stop();
		}
		if (countdownBarThread != null) {
			countdownBarThread.setStop(true);
		}
		jButtonListening.setEnabled(true);
		jButtonStop.setEnabled(true);

		jComboBoxHour.setSelectedIndex(0);
		jComboBoxMinute.setSelectedIndex(0);
		jComboBoxSecond.setSelectedIndex(0);
		time = 0;
		currentCountdown = 0;

		// wait 1 minute
		try {
			Thread.sleep(2000);
			jProgressBarHour.setValue(0);
			jProgressBarMinute.setValue(0);
			jProgressBarSecond.setValue(0);
			if (countdownBarThread != null) {
				countdownBarThread.setCurrent(0);
				jProgressBarTimer.setValue(countdownBarThread.getCurrent());
				jLabelTimer.setText(refreshTimer(countdownBarThread.getCurrent()));
			}
			jProgressBarTimer.setValue(0);
			jLabelTotalTime.setText(Common.DEFAULT_TIME);
			jLabelTimer.setText(Common.DEFAULT_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * add action listener for all menus. e.g. when you click the 'Mini' menu,
	 * then execute the mini operation.
	 */
	private void actionForFileMenuItem(ActionEvent e) {
		if (e.getSource() == miniItem) {
			// Ctrl + M can change the mini/normal window.
			if (MINI_STATUS) {
				setSize(Common.WINDOW_WIDTH, Common.WINDOW_MINI_HEIGHT);
				setMiniItemText(Common.NORMAL);
				tray.setMiniitemLabel(Common.NORMAL);
			} else {
				setSize(Common.WINDOW_WIDTH, Common.WINDOW_HEIGHT);
				setMiniItemText(Common.MINI);
				tray.setMiniitemLabel(Common.MINI);
			}
			MINI_STATUS = !MINI_STATUS;
		} else if (e.getSource() == aboutItem) {
			// show about information
			JOptionPane.showMessageDialog(this, Common.ABOUT_INFO, Common.ABOUT, JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == exitItem) {
			// exit system
			existSystem();
		}

	}
	
	public void setMiniItemText(String text){
		miniItem.setText(text);
	}

	// =====================================
	// ======== init all components ========
	// =====================================
	public void init() {
		// init panels
		initPanels();
		// init buttons
		initButtons();
		// init comboBox
		initComboBoxes();
		// init labels
		initLabels();
		// init progressBars
		initProgressBars();
		// init menu
		initMeun();
		// show all components
		initComponents();

		initItSelf();
	}

	/**
	 * init itself
	 */
	private void initItSelf() {
		this.setResizable(false);
		// setting window location
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {
				setVisible(false);
			}

			@Override
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(MainUI.this, Common.CONFIRM_MINIMIZE, Common.NOTICE, JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					// hide the window
					setVisible(false);
				} else {
					// exit system
					existSystem();
				}
			}
		});

		// Loading the tray when the application start.
		if (null == tray) {
			tray = new TrayUtil(MainUI.this);
		} else {
			tray.initTray();
		}
	}

	/**
	 * init panels
	 */
	private void initPanels() {
		jPanelTop = new JPanel();
		jPanelBottom = new JPanel();
		jPanelBars = new JPanel();
		jPanelSetting = new JPanel();
	}

	/**
	 * init buttons
	 */
	private void initButtons() {
		jButton_add_1 = new JButton();
		jButton_add_5 = new JButton();
		jButton_add_30 = new JButton();
		jButton_sub_1 = new JButton();
		jButton_sub_5 = new JButton();
		jButton_sub_30 = new JButton();
		jButtonListening = new JButton();
		jButtonStop = new JButton();
		jButtonCancel = new JButton();

		jButton_add_1.setText(Common.ADD_1);
		jButton_add_1.addActionListener(this);
		jButton_add_5.setText(Common.ADD_5);
		jButton_add_5.addActionListener(this);
		jButton_add_30.setText(Common.ADD_30);
		jButton_add_30.addActionListener(this);
		jButton_sub_1.setText(Common.SUB_1);
		jButton_sub_1.addActionListener(this);
		jButton_sub_5.setText(Common.SUB_5);
		jButton_sub_5.addActionListener(this);
		jButton_sub_30.setText(Common.SUB_30);
		jButton_sub_30.addActionListener(this);
		jButtonListening.setText(Common.LISTENING_RING);
		jButtonListening.addActionListener(this);
		jButtonStop.setText(Common.STOP_RING);
		jButtonStop.addActionListener(this);
		jButtonCancel.setText(Common.CANCEL_ALARM);
		jButtonCancel.addActionListener(this);
	}

	/**
	 * init comboBoxes
	 */
	@SuppressWarnings("unchecked")
	private void initComboBoxes() {
		jComboBoxHour = new JComboBox();
		jComboBoxMinute = new JComboBox();
		jComboBoxSecond = new JComboBox();
		jComboBoxRing = new JComboBox();

		jComboBoxHour.setModel(new DefaultComboBoxModel(Common.HOURS_NUM));
		jComboBoxHour.addActionListener(this);
		jComboBoxMinute.setModel(new DefaultComboBoxModel(Common.MINUTE_NUM));
		jComboBoxMinute.addActionListener(this);
		jComboBoxSecond.setModel(new DefaultComboBoxModel(Common.SECOND_NUM));
		jComboBoxSecond.addActionListener(this);
		jComboBoxRing.setModel(new DefaultComboBoxModel(Common.SOUND_NUM));
	}

	/**
	 * init labels
	 */
	private void initLabels() {
		jLabelRingTime = new JLabel();
		jLabelCurrentTime = new JLabel();
		jLabelTimer = new JLabel();
		jLabelTotalTime = new JLabel();
		jLabelSettingFast = new JLabel();
		jLabelRing = new JLabel();

		jLabelCurrentTime.setFont(new java.awt.Font("Tahoma", 0, 48));
		jLabelCurrentTime.setForeground(new java.awt.Color(255, 0, 0));
		jLabelCurrentTime.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelCurrentTime.setText(AlarmUtil.now());

		jLabelTimer.setText(Common.DEFAULT_TIME);
		jLabelTotalTime.setText(Common.DEFAULT_TIME);

		jLabelRing.setForeground(new java.awt.Color(255, 0, 0));
		jLabelRing.setText(Common.RING_SETTING);

		jLabelSettingFast.setForeground(new java.awt.Color(255, 0, 0));
		jLabelSettingFast.setText(Common.FIRST_SETTING);

		jLabelRingTime.setForeground(new java.awt.Color(255, 0, 0));
		jLabelRingTime.setText(Common.RING_TIME);

		countdown = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jLabelCurrentTime.setText(AlarmUtil.now());
			}
		});
		countdown.start();
	}

	/**
	 * init progressBars
	 */
	private void initProgressBars() {
		jProgressBarTimer = new JProgressBar();
		jProgressBarHour = new JProgressBar();
		jProgressBarMinute = new JProgressBar();
		jProgressBarSecond = new JProgressBar();

		jProgressBarTimer.setValue(0);

		jProgressBarHour.setMaximum(24);
		jProgressBarHour.setMinimum(0);
		jProgressBarHour.setOrientation(1);
		jProgressBarHour.setToolTipText("20 hr");
		jProgressBarHour.setValue(0);

		jProgressBarMinute.setMaximum(60);
		jProgressBarMinute.setMinimum(0);
		jProgressBarMinute.setOrientation(1);
		jProgressBarMinute.setToolTipText("20 min");
		jProgressBarMinute.setValue(0);

		jProgressBarSecond.setMaximum(60);
		jProgressBarSecond.setMinimum(0);
		jProgressBarSecond.setOrientation(1);
		jProgressBarSecond.setToolTipText("30 sec");
		jProgressBarSecond.setValue(0);
	}

	/**
	 * init menu
	 */
	private void initMeun() {
		jMenuBar = new JMenuBar();
		jMenuFile = new JMenu();

		jMenuFile.setText(Common.FILE);
		jMenuBar.add(jMenuFile);

		miniItem = new JMenuItem(Common.MINI);
		miniItem.setAccelerator(KeyStroke.getKeyStroke(Common.M, InputEvent.CTRL_MASK));
		miniItem.addActionListener(this);
		jMenuFile.add(miniItem);

		line = new JSeparator();
		jMenuFile.add(line);

		aboutItem = new JMenuItem(Common.ABOUT);
		aboutItem.setAccelerator(KeyStroke.getKeyStroke(Common.A, InputEvent.CTRL_MASK));
		aboutItem.addActionListener(this);
		jMenuFile.add(aboutItem);

		line = new JSeparator();
		jMenuFile.add(line);

		exitItem = new JMenuItem(Common.EXIT);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(Common.W, InputEvent.CTRL_MASK));
		exitItem.addActionListener(this);
		jMenuFile.add(exitItem);

		line = new JSeparator();
		jMenuFile.add(line);

		setJMenuBar(jMenuBar);
	}

	// ===================================================
	// == the user interface of the application as below layout.
	// == WARNING: Do NOT modify this code.
	// ===================================================
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {
		jPanelTopLayout();
		jPanelBarsLayout();
		jPanelSettingLayout();
		jPanelBottomLayout();
		mainLayout();
		pack();
	}

	/**
	 * WARNING: Do NOT modify this code.
	 */
	private void mainLayout() {
		GroupLayout mainLayout = new GroupLayout(getContentPane());
		getContentPane().setLayout(mainLayout);
		mainLayout.setHorizontalGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(mainLayout.createSequentialGroup().addContainerGap().addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jPanelTop, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanelBottom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
		mainLayout.setVerticalGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(mainLayout.createSequentialGroup().addContainerGap().addComponent(jPanelTop, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(11, 11, 11).addComponent(jPanelBottom, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
	}

	/**
	 * WARNING: Do NOT modify this code.
	 */
	private void jPanelTopLayout() {
		GroupLayout jPanelTopLayout = new GroupLayout(jPanelTop);
		jPanelTop.setLayout(jPanelTopLayout);
		jPanelTopLayout.setHorizontalGroup(jPanelTopLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelTopLayout.createSequentialGroup().addContainerGap().addGroup(jPanelTopLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jLabelCurrentTime, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jProgressBarTimer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanelTopLayout.createSequentialGroup().addComponent(jLabelTimer).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabelTotalTime))).addContainerGap()));
		jPanelTopLayout.setVerticalGroup(jPanelTopLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelTopLayout.createSequentialGroup().addContainerGap().addComponent(jLabelCurrentTime).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jProgressBarTimer, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanelTopLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jLabelTimer).addComponent(jLabelTotalTime)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
	}

	/**
	 * WARNING: Do NOT modify this code.
	 */
	private void jPanelBottomLayout() {
		GroupLayout jPanelBottomLayout = new GroupLayout(jPanelBottom);
		jPanelBottom.setLayout(jPanelBottomLayout);
		jPanelBottomLayout.setHorizontalGroup(jPanelBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jPanelBottomLayout.createSequentialGroup().addContainerGap().addComponent(jPanelBars, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jPanelSetting, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
		jPanelBottomLayout.setVerticalGroup(jPanelBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jPanelBottomLayout.createSequentialGroup().addContainerGap().addGroup(jPanelBottomLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(jPanelSetting, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanelBars, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
	}

	/**
	 * WARNING: Do NOT modify this code.
	 */
	private void jPanelBarsLayout() {
		GroupLayout jPanelBarsLayout = new GroupLayout(jPanelBars);
		jPanelBars.setLayout(jPanelBarsLayout);
		jPanelBarsLayout.setHorizontalGroup(jPanelBarsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelBarsLayout.createSequentialGroup().addContainerGap().addComponent(jProgressBarHour, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jProgressBarMinute, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jProgressBarSecond, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanelBarsLayout.setVerticalGroup(jPanelBarsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelBarsLayout.createSequentialGroup().addContainerGap().addGroup(jPanelBarsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jProgressBarHour, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jProgressBarMinute, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jProgressBarSecond, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
	}

	/**
	 * WARNING: Do NOT modify this code.
	 */
	private void jPanelSettingLayout() {
		GroupLayout jPanelSettingLayout = new GroupLayout(jPanelSetting);
		jPanelSetting.setLayout(jPanelSettingLayout);
		jPanelSettingLayout.setHorizontalGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jPanelSettingLayout.createSequentialGroup().addContainerGap().addGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(jButtonCancel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.LEADING, jPanelSettingLayout.createSequentialGroup().addGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jComboBoxHour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabelRingTime).addComponent(jLabelSettingFast).addComponent(jButton_add_1, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE).addComponent(jButton_sub_1)).addGap(18, 18, 18).addGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelSettingLayout.createSequentialGroup().addComponent(jComboBoxMinute, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE).addGap(18, 18, Short.MAX_VALUE).addComponent(jComboBoxSecond, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)).addGroup(jPanelSettingLayout.createSequentialGroup().addGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelSettingLayout.createSequentialGroup().addComponent(jButton_sub_5).addGap(18, 18, 18).addComponent(jButton_sub_30)).addGroup(jPanelSettingLayout.createSequentialGroup().addComponent(jButton_add_5).addGap(18, 18, 18).addComponent(jButton_add_30))).addGap(0, 0, Short.MAX_VALUE)))).addComponent(jComboBoxRing, GroupLayout.Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.LEADING, jPanelSettingLayout.createSequentialGroup().addComponent(jLabelRing).addGap(0, 0, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, jPanelSettingLayout.createSequentialGroup().addComponent(jButtonListening, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jButtonStop, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))).addContainerGap()));
		jPanelSettingLayout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[] { jButton_add_1, jButton_add_30, jButton_add_5, jButton_sub_1, jButton_sub_30, jButton_sub_5, jComboBoxHour, jComboBoxMinute, jComboBoxSecond });
		jPanelSettingLayout.setVerticalGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanelSettingLayout.createSequentialGroup().addContainerGap().addComponent(jLabelRingTime).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jComboBoxHour, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jComboBoxMinute, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jComboBoxSecond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabelSettingFast).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButton_add_1).addComponent(jButton_add_5).addComponent(jButton_add_30)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButton_sub_1).addComponent(jButton_sub_5).addComponent(jButton_sub_30)).addGap(18, 18, 18).addComponent(jLabelRing).addGap(18, 18, 18).addComponent(jComboBoxRing, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(jPanelSettingLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButtonListening).addComponent(jButtonStop)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButtonCancel).addContainerGap()));
	}
}
