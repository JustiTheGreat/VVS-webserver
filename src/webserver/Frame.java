package webserver;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

public class Frame implements ActionListener, MyConstants {
	private final JFrame jFrame = new JFrame();
    private final BorderLayout borderLayout = new BorderLayout();
    private final JPanel leftPanel = new JPanel();
    private final JPanel rightPanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private final TitledBorder leftBorder = new TitledBorder("WebServer Info");
    private final TitledBorder rightBorder = new TitledBorder("WebServer Control");
    private final TitledBorder bottomBorder = new TitledBorder("WebServer Configuration");   
    private final JLabel jLabel1 = new JLabel();
    private final JLabel jLabel2 = new JLabel();
    private final JLabel jLabel3 = new JLabel();
    private final JLabel jLabel4 = new JLabel();
    private final JLabel jLabel5 = new JLabel();
    private final JLabel jLabel6 = new JLabel();
    private final JButton jButton = new JButton();
    private final JCheckBox jCheckBox = new JCheckBox();
    private final JLabel jLabel7 = new JLabel();
    private final JLabel jLabel8 = new JLabel();
    private final JLabel jLabel9 = new JLabel();
    private final JSpinner jSpinner = new JSpinner();
    private final JTextField jTextField1 = new JTextField();
    private final JTextField jTextField2 = new JTextField();
    
    //private WebServer webServer = null;
	
    public Frame() {
        jFrame.getContentPane().setLayout(borderLayout);
        jFrame.setSize(500, 300);
        jFrame.setTitle(TITLE_WHILE_STOPPED);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        leftBorder.setTitleJustification(TitledBorder.LEFT);
        leftBorder.setTitlePosition(TitledBorder.TOP);
        rightBorder.setTitleJustification(TitledBorder.LEFT);
        rightBorder.setTitlePosition(TitledBorder.TOP);
        bottomBorder.setTitleJustification(TitledBorder.LEFT);
        bottomBorder.setTitlePosition(TitledBorder.TOP);
        
        leftPanel.setLayout(null);
        leftPanel.setPreferredSize(new Dimension(240, 150));
        jFrame.getContentPane().add(leftPanel, BorderLayout.WEST);
        leftPanel.setBorder(leftBorder);
        rightPanel.setLayout(null);
        rightPanel.setPreferredSize(new Dimension(240, 150));
        jFrame.getContentPane().add(rightPanel, BorderLayout.EAST);
        rightPanel.setBorder(rightBorder);
        bottomPanel.setLayout(null);
        bottomPanel.setPreferredSize(new Dimension(500, 150));
        jFrame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setBorder(bottomBorder);
        
        jLabel1.setText("Server status:");
        jLabel1.setBounds(20, 20, 120, 20);
        leftPanel.add(jLabel1);
        jLabel2.setText("Server address:");
        jLabel2.setBounds(20, 50, 120, 20);
        leftPanel.add(jLabel2);
        jLabel3.setText("Server listening port:");
        jLabel3.setBounds(20, 80, 120, 20);
        leftPanel.add(jLabel3);
        
        jLabel4.setText(NOT_RUNNING);
        jLabel4.setBounds(150, 20, 100, 20);
        leftPanel.add(jLabel4);
        jLabel5.setText(NOT_RUNNING);
        jLabel5.setBounds(150, 50, 100, 20);
        leftPanel.add(jLabel5);
        jLabel6.setText(NOT_RUNNING);
        jLabel6.setBounds(150, 80, 100, 20);
        leftPanel.add(jLabel6);
        
        jButton.setText(START_SERVER);
        jButton.setBounds(60, 30, 120, 40);
        jButton.setActionCommand(START_SERVER);
        jButton.addActionListener(this);
        rightPanel.add(jButton);
        
        jCheckBox.setText("Switch to maintenance mode");
        jCheckBox.setBounds(25, 80, 190, 20);
        jCheckBox.setEnabled(false);
        jCheckBox.setActionCommand(START_MAINTENANCE);
        jCheckBox.addActionListener(this);
        rightPanel.add(jCheckBox);
        
        jLabel7.setText(SERVER_LISTENING_ON_PORT);
        jLabel7.setBounds(20, 20, 140, 20);
        bottomPanel.add(jLabel7);
        jLabel8.setText(WEB_ROOT_DIRECTORY);
        jLabel8.setBounds(20, 50, 140, 20);
        bottomPanel.add(jLabel8);
        jLabel9.setText(MAINTENANCE_DIRECTORY);
        jLabel9.setBounds(20, 80, 140, 20);
        bottomPanel.add(jLabel9);
        
        jSpinner.setModel(new SpinnerNumberModel(INITIAL_PORT, MIN_PORT, MAX_PORT, 1));
        jSpinner.setBounds(200, 20, 100, 20);
        bottomPanel.add(jSpinner);
        
        jTextField1.setText(DEFAULT_ROOT_DIR);
        jTextField1.setBounds(200, 50, 200, 20);
        bottomPanel.add(jTextField1);
        jTextField2.setText(DEFAULT_MAINTENANCE_DIR);
        jTextField2.setBounds(200, 80, 200, 20);
        bottomPanel.add(jTextField2);
        
        jFrame.setVisible(true);
    }

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equals(START_SERVER)) {
			if(WebServer.setRootDirectory(jTextField1.getText())
					&& WebServer.setMaintenanceDirectory(jTextField2.getText())) {
				WebServer.setServerSocket((Integer) jSpinner.getValue());
				WebServer.setServerIsRunning(true);
				jTextField1.setForeground(BLACK);
				jTextField2.setForeground(BLACK);
				jFrame.setTitle(TITLE_WHILE_RUNNING);
				jLabel4.setText(RUNNING);
				try {
					jLabel5.setText(InetAddress.getLocalHost().getHostAddress());
				} catch (UnknownHostException e) {
					System.err.println("Unknon host");
					System.exit(-1);
				}
				jLabel6.setText(""+jSpinner.getValue());
				jButton.setText(STOP_SERVER);
				jButton.setActionCommand(STOP_SERVER);
				jCheckBox.setEnabled(true);
				jSpinner.setEnabled(false);
				jTextField1.setEnabled(false);
			} else {
				if(!WebServer.setRootDirectory(jTextField1.getText())) {
					jTextField1.setForeground(RED);
				}
				if(!WebServer.setMaintenanceDirectory(jTextField2.getText())) {
					jTextField2.setForeground(RED);
				}
			}
	    } else if (actionEvent.getActionCommand().equals(STOP_SERVER)) {
	    	WebServer.setServerIsRunning(false);
	    	WebServer.setServerIsInMaintenance(false);
	    	jFrame.setTitle(TITLE_WHILE_STOPPED);
			jLabel4.setText(NOT_RUNNING);
			jLabel5.setText(NOT_RUNNING);
			jLabel6.setText(NOT_RUNNING);
			jButton.setText(START_SERVER);
			jButton.setActionCommand(START_SERVER);
			jCheckBox.setSelected(false);
			jCheckBox.setEnabled(false);
			jSpinner.setEnabled(true);
			jTextField1.setEnabled(true);
			jTextField2.setEnabled(true);
	    } else if (actionEvent.getActionCommand().equals(START_MAINTENANCE)) {
	    	if(WebServer.setMaintenanceDirectory(jTextField2.getText())) {
	    		WebServer.setServerIsInMaintenance(true);
	    		jTextField2.setForeground(BLACK);
		    	jFrame.setTitle(TITLE_WHILE_MAINTAINING);
				jLabel4.setText(MAINTENANCE);
				jCheckBox.setActionCommand(STOP_MAINTENANCE);
				jTextField1.setEnabled(true);
				jTextField2.setEnabled(false);
	    	} else {
				jTextField2.setForeground(RED);
				jCheckBox.setSelected(false);
	    	}
	    } else if (actionEvent.getActionCommand().equals(STOP_MAINTENANCE)) {
	    	if(WebServer.setRootDirectory(jTextField1.getText())) {
	    		WebServer.setServerIsInMaintenance(false);
	    		jTextField1.setForeground(BLACK);
		    	jFrame.setTitle(TITLE_WHILE_RUNNING);
				jLabel4.setText(RUNNING);
				jCheckBox.setActionCommand(START_MAINTENANCE);
				jTextField1.setEnabled(false);
				jTextField2.setEnabled(true);
	    	} else {
				jTextField1.setForeground(RED);
				jCheckBox.setSelected(true);
	    	}
	    }
	}


}
