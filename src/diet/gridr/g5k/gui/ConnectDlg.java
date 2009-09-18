/****************************************************************************/
/* This class is used to make the connection to the Grid5000 platform       */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ConnectDlg.java,v 1.15 2007/11/19 15:16:19 dloureir Exp $
 * $Log: ConnectDlg.java,v $
 * Revision 1.15  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.14  2007/07/16 14:20:45  dloureir
 * When the user has typed his/her passphrase pressing return validate and get connected to Grid'5000
 *
 * Revision 1.13  2007/07/13 15:34:41  dloureir
 * When the user types its ssh key pass phrase, it get connected by pressing "return"
 *
 * Revision 1.12  2007/07/12 12:51:43  dloureir
 * Adding some comments
 *
 * Revision 1.11  2007/07/11 08:35:03  dloureir
 * Adding an SSHAgent for the correction of bug 36. The ssh key is nomore written on the disk. It will be ask at every application startup and stored in memory during the execution.
 *
 * Revision 1.10  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.9  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.logging.Level;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

/**
 * This class is used to make the connection to the Grid5000 platform
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class ConnectDlg extends JDialog implements ActionListener, KeyListener {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
    private static final long serialVersionUID = 1L;
    /**
     * JComboBox containing the different Grid5000 sites
     */
    private JComboBox sitesCB;
    /**
     * TextField for the user name
     */
    private JTextField userField;
    /**
     * TextField for the ssh key file
     */
    private JTextField keyFileField;
    /**
     * Password field for the passphrase
     */
    private JPasswordField keyFilePassField;
    /**
     * host for the ssh connection
     */
    private String host;
    /**
     * user for the ssh connection
     */
    private String user;
    /**
     * passphrase for the ssh connection
     */
    private String keyFilePass;
    /**
     * SSH Key file for the ssh connection
     */
    private File keyFile;
    /**
     * Button used to browse for the SSH key file
     */
    private JButton browse_btn;
    /**
     * name of the SSH key file
     */
    private String keyFileName = null;
    /**
     * SSH connection
     */
    private Connection myConn = null;
    /**
     * ok button
     */
    private JButton ok_btn;
    /**
     * cancel button
     */
    private JButton cancel_btn;
    /**
     * HashMap containing the properties of the connection
     */
    protected static HashMap<String, String> properties = new HashMap<String, String>();

    /**
     * Default dummy constructor
     *
     */
    public ConnectDlg(){

    }
    /**
     * Private constructor of the Connection dialog frame
     *
     * @param frame parent frame
     */
    private ConnectDlg(JFrame frame) {
        super(frame, true);
        this.setLayout(new GridBagLayout());
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ConnectDlg", "Connection Dialog Frame loading");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel hostLabel = new JLabel("Host name");
        c.weightx = 0.25;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10,10,
                10,10);
        add(hostLabel, c);

        this.sitesCB = new JComboBox(G5kSite.getExternalFrontals());
        this.sitesCB.setSelectedItem(G5kCfg.get(G5kCfg.PREFERREDACCESPOINT));
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 1;
        add(this.sitesCB, c);

        JLabel userLabel = new JLabel("User name");
        c.weightx = 0.25;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(10,10,
                10,10);
        add(userLabel, c);

        this.userField = new JTextField(System.getProperty("user.name"));
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 2;
        add(this.userField, c);

        JLabel keyLabel = new JLabel("Private key file");
        c.weightx = 0.25;
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(10, 10, 10, 10);
        add(keyLabel, c);

        this.keyFileField = new JTextField(20);
        this.keyFileField.setEditable(false);
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 3;
        add(this.keyFileField, c);
        this.keyFileField.setToolTipText("The path must be an absolute path");

        this.browse_btn = new JButton("Browse");
        this.browse_btn.addActionListener(this);
        c.weightx = 1.0;
        c.gridx = 2;
        c.gridy = 3;
        add(this.browse_btn, c);

        JLabel passLabel = new JLabel("Key file password ");

        c.weightx = 0.25;
        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(10, 10, 10, 10);
        add(passLabel, c);

        this.keyFilePassField = new JPasswordField(20);
        keyFilePassField.addKeyListener(this);
        this.addKeyListener(this);
        c.weightx = 1.0;
        c.gridx = 1;
        c.gridy = 4;
        add(this.keyFilePassField, c);


        URL url = getClass().getResource("/resources/gridr/g5k/ok.png");
        if (url != null)
            this.ok_btn = new JButton(new ImageIcon(url));
        else
            this.ok_btn = new JButton("OK");
        this.ok_btn.addActionListener(this);
        url = getClass().getResource("/resources/gridr/g5k/cancel.png");
        if (url != null)
            this.cancel_btn = new JButton(new ImageIcon(url));
        else
            this.cancel_btn = new JButton("CANCEL");
        this.cancel_btn.addActionListener(this);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(this.ok_btn);
        panel.add(this.cancel_btn);
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.insets = new Insets(0,0,
                0,0);
        add(panel, c);
        this.setResizable(false);
        pack();
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ConnectDlg", "Connection Dialog Frame loading");

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "windowClosing", "Connection Dialog Frame is closing");
            }

        });
    }

    /**
     * Method implemented for the management of the events for this dialog frame
     *
     * @param event an event
     *
     */
    public void actionPerformed(ActionEvent event) {
    	/*
    	 * The browse button has been clicked
    	 */
        if (event.getSource() == this.browse_btn) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileHidingEnabled(false);
            int returnVal = chooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "You chose to open this file: " +
                        chooser.getSelectedFile().getName());
                this.keyFileField.setText(chooser.getSelectedFile().getAbsolutePath());
                this.keyFile = chooser.getSelectedFile();
            }
        }
        /*
         * The ok button has been clicked
         */
        if (event.getSource() == this.ok_btn) {
            connection();
        }
        /*
         * the cancel button has been clicked
         */
        if (event.getSource() == this.cancel_btn) {
            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "windowClosing", "Connection cancelled");
            dispose();
        }
    }
    
    private void connection(){
    	this.host = (String) this.sitesCB.getSelectedItem();
        this.user = this.userField.getText();
        Config.setUser(this.user);
        this.keyFileName = this.keyFileField.getText();
        this.keyFilePass = new String(this.keyFilePassField.getPassword());
        G5kCfg.setSSHKey(keyFilePass);
        properties.put("host", host);
        properties.put("username", user);
        properties.put("publickey",keyFileName);
        properties.put("passphrase", keyFilePass);
        try {
            /* Create a connection instance */
            Connection conn = new Connection(this.host);

            /* Now connect */
            conn.connect();
            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Connection realized");

            /* Authenticate.
             */
            boolean isAuthenticated = conn.authenticateWithPublicKey(this.user,
                    this.keyFile, this.keyFilePass);

            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");

            /* Create a session */

            Session sess = conn.openSession();
            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Session opened");

            sess.requestPTY("toto",
                    80, 80,
                    16, 16,
                    null);

            sess.startShell();
            LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Connection succeed");

            JOptionPane.showMessageDialog(this, "Connection succeed");

            this.myConn = conn;

            dispose();
        }
        catch (Exception e) {
            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", e);
            JOptionPane.showMessageDialog(this, "Connection failed");
            return;
        }
    }

    /**
     * Method launching the dialog frame
     *
     * @param frame parent frame
     * @return ssh connection created (if any)
     */
    public static Connection exec(JFrame frame) {
        ConnectDlg dlg  = new ConnectDlg(frame);
        dlg.user = G5kCfg.get(G5kCfg.USERNAME);
        dlg.keyFilePass = G5kCfg.getSSHKey();
        String kf = G5kCfg.get(G5kCfg.SSHKEYFILE);
        if (( kf != null) &&
                (!kf.equals(""))
        ) {
            dlg.keyFile = new File(kf);
            dlg.keyFileField.setText(dlg.keyFile.getAbsolutePath());
        }
        dlg.userField.setText(dlg.user);
        dlg.keyFilePassField.setText(dlg.keyFilePass);
        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, ConnectDlg.class.getName(), "exec", "Connection Dialog Frame configured");
        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
        return dlg.myConn;
    }

    /**
     * Method returning the ssh connection
     *
     * @return the ssh connection
     */
    public Connection getConnection() {
        return this.myConn;
    }

    /**
     * Method returning the SSH Key file name
     *
     * @return the keyFileName the name of the ssh key file
     */
    public String getKeyFileName() {
        return keyFileName;
    }

    /**
     * Method setting the SSH Key file name
     *
     * @param keyFileName the keyFileName to set
     */
    public void setKeyFileName(String keyFileName) {
        this.keyFileName = keyFileName;
    }
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			connection();
		}// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}
}