/****************************************************************************/
/* This class corresponds to the panel for the information about the user    */
/*such as the username, the ssh key file etc ...                            */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kUserPanel.java,v 1.16 2007/09/28 16:02:44 aamar Exp $
 * $Log: G5kUserPanel.java,v $
 * Revision 1.16  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.15  2007/07/18 14:07:47  dloureir
 * The configuration of GRUDU has now only two buttons for the saving of the modified (or not) configuration and the correction of a little bug : the JList of theUserPanel is composed of external acces frontales whereas it was not the case before (it was site's names).
 *
 * Revision 1.14  2007/07/12 14:33:49  dloureir
 * Removing an unused attribute and correction of some typo errors
 *
 * Revision 1.13  2007/07/11 15:40:25  dloureir
 * Adding an application package for the application wide properties and two panels for the Tips of the Day for DDB and GRUDU
 *
 * Revision 1.12  2007/07/11 08:35:03  dloureir
 * Adding an SSHAgent for the correction of bug 36. The ssh key is nomore written on the disk. It will be ask at every application startup and stored in memory during the execution.
 *
 * Revision 1.11  2007/05/02 15:19:39  aamar
 * Add Java_Home property
 *
 * Revision 1.10  2007/05/02 12:50:58  dloureir
 * Adding the support of the batch schedulers for the SeD in the Wizzard (through an updated XMLGoDIETgenerator version) and the log option for GoDIET
 *
 * Revision 1.9  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.*;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.*;

import diet.application.ApplicationConfiguration;
import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

/**
 * This class corresponds to the panel for the information about the user
 * such as the username, the ssh key file etc ...
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kUserPanel extends JPanel implements ActionListener {
    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
	private static final long serialVersionUID = 8310328480628331762L;
    /**
     * TextField for the selection of the user
     */
	private JTextField userField;
    /**
     * TextField for the selection of the SSH Key file
     */
	private JTextField sshFileField;
    /**
     * TextField for the selection of the passphrase
     */
	private JPasswordField passwordField;
    /**
     * TextField for the selection of the GoDIEt scratch directory
     */
	private JTextField goDietScratchField;
    /**
     * ComboBox for the selection of the preferred access point
     */
	private JComboBox preferredAccesPointComboBox;
    /**
     * browse button
     */
	private JButton browse_btn;
    /**
     * JCheckBox for the use of unique dirs
     */
	private JCheckBox useUniqueDirsCheckBox;
    /**
     * JCheckBox for the saving of the standard error
     */
    private JCheckBox saveStdErrCheckBox;
    /**
     * JCheckBox for the saving of the standard output
     */
    private JCheckBox saveStdOutCheckBox;
    /**
     * spinner for the debug level
     */
	private JSpinner debugSpinner;

    /**
     * JCheckBox used to specify if the logging in goDIET should be used
     */
    private JCheckBox useLoggingInGoDIET = null;

    /**
     * Default constructor
     *
     * @param parent parent frame
     */
	public G5kUserPanel() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		JLabel scratch_label    = new JLabel("GoDIET scratch ");
		JLabel user_label       = new JLabel("Username");
		JLabel ssh_label        = new JLabel("Private SSH key file");
		JLabel pass_label       = new JLabel("Password");
		JLabel uniqueDirs_label  = new JLabel("Unique Dirs ?");
		JLabel saveStdOut_label = new JLabel("Save stderr ?");
		JLabel saveStdErr_label = new JLabel("Save stdout ?");
		JLabel debug_label      = new JLabel("Debug level");
		JLabel preferredAccesPointLabel = new JLabel("Preferred acces point");
        JLabel useLoggingInGoDIETLabel = new JLabel("Logging in GoDIET");

		Vector<String> vectorElements = new Vector<String>();
		for(int i = 0 ;  i < G5kSite.getSitesNumber() ; i ++){
			vectorElements.add(G5kSite.getExternalFrontals()[i]);
		}
		this.preferredAccesPointComboBox = new JComboBox(vectorElements);
		this.goDietScratchField = new JTextField();
		this.userField = new JTextField();
		this.sshFileField = new JTextField();
		this.passwordField = new JPasswordField();

		this.useUniqueDirsCheckBox = new JCheckBox();
		this.saveStdErrCheckBox = new JCheckBox();
		this.saveStdOutCheckBox = new JCheckBox();

		this.debugSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
		this.useLoggingInGoDIET = new JCheckBox();
        this.useLoggingInGoDIET.setSelected(true);

		c.weightx = 0.25;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(10,10, 10,10);
		add(preferredAccesPointLabel, c);

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 1;
		add(this.preferredAccesPointComboBox, c);

		if(ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.DIETDASHBOARD_CONTEXT)){
			c.weightx = 0.25;
			c.gridx = 0;
			c.gridy = 2;
			c.insets = new Insets(10,10, 10,10);
			add(scratch_label, c);

			c.weightx = 1.0;
			c.gridx = 1;
			c.gridy = 2;
			add(this.goDietScratchField, c);
		}

		c.weightx = 0.25;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(10,10, 10,10);
		add(user_label, c);

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 3;
		add(this.userField, c);

		c.weightx = 0.25;
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(10,10, 10,10);
		add(ssh_label, c);

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 4;
		add(this.sshFileField, c);
		this.sshFileField.setToolTipText("The path must be an absolute path");
		this.browse_btn = new JButton("Browse");
		this.browse_btn.addActionListener(this);
		c.weightx = 1.0;
		c.gridx = 2;
		c.gridy = 4;
		add(this.browse_btn, c);

		c.weightx = 0.25;
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(10,10, 10,10);
		add(pass_label, c);

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 5;
		add(this.passwordField, c);

		if(ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.DIETDASHBOARD_CONTEXT) ){

			c.weightx = 0.25;
			c.gridx = 0;
			c.gridy = 6;
			c.insets = new Insets(10,10, 10,10);
			add(uniqueDirs_label, c);

			c.weightx = 1.0;
			c.gridx = 1;
			c.gridy = 6;
			add(useUniqueDirsCheckBox, c);

			c.weightx = 0.25;
			c.gridx = 0;
			c.gridy = 7;
			c.insets = new Insets(10,10, 10,10);
			add(saveStdOut_label, c);

			c.weightx = 1.0;
			c.gridx = 1;
			c.gridy = 7;
			add(saveStdOutCheckBox, c);

			c.weightx = 0.25;
			c.gridx = 0;
			c.gridy = 8;
			c.insets = new Insets(10,10, 10,10);
			add(saveStdErr_label, c);

			c.weightx = 1.0;
			c.gridx = 1;
			c.gridy = 8;
			add(saveStdErrCheckBox, c);

			c.weightx = 0.25;
			c.gridx = 0;
			c.gridy = 9;
			c.insets = new Insets(10,10, 10,10);
			add(debug_label, c);

			c.weightx = 1.0;
			c.gridx = 1;
			c.gridy = 9;
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.WEST;
			add(debugSpinner, c);
			c.fill = GridBagConstraints.HORIZONTAL;

            c.weightx = 0.25;
            c.gridx = 0;
            c.gridy = 10;
            c.insets = new Insets(10,10,10,10);
            add(useLoggingInGoDIETLabel,c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 10;
            add(useLoggingInGoDIET,c);

		}
		LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kUserPanel", "G5k User panel initialized");
		fillValues();
	}

    /**
     * Method filling the values of the configuration
     *
     */
	protected void fillValues() {
		G5kCfg.initCfg();
		this.userField.setText(G5kCfg.get(G5kCfg.USERNAME));
		this.sshFileField.setText(G5kCfg.get(G5kCfg.SSHKEYFILE));
		this.passwordField.setText(G5kCfg.getSSHKey());
		this.preferredAccesPointComboBox.setSelectedItem(G5kCfg.get(G5kCfg.PREFERREDACCESPOINT));
		if(ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.DIETDASHBOARD_CONTEXT)){

			this.goDietScratchField.setText(G5kCfg.get(G5kCfg.GODIETSCRATCH));

			String temp = G5kCfg.get(G5kCfg.GODIETUSEUNIQUEDIRS);
			useUniqueDirsCheckBox.setSelected( temp.equalsIgnoreCase("yes") ? true : false );
			temp = G5kCfg.get(G5kCfg.GODIETSAVESTDOUT);
			saveStdOutCheckBox.setSelected(temp.equalsIgnoreCase("yes") ? true : false );
			temp = G5kCfg.get(G5kCfg.GODIETSAVESTDERR);
			saveStdErrCheckBox.setSelected(temp.equalsIgnoreCase("yes") ? true : false );

			temp = G5kCfg.get(G5kCfg.GODIETDEBUG);

			if(temp.equalsIgnoreCase("")) debugSpinner.setValue(Integer.valueOf(0));
			else debugSpinner.setValue(Integer.valueOf(temp));
            temp = G5kCfg.get(G5kCfg.LOGGINGINGODIET);
            useLoggingInGoDIET.setSelected(temp.equalsIgnoreCase("yes")? true: false);
		}
		LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "fillValues", "G5k User panel configuration loaded");
	}

    /**
     * Method applying the changes made
     *
     */
	public void apply(boolean save) {
		G5kCfg.set(G5kCfg.USERNAME, this.userField.getText());
		G5kCfg.set(G5kCfg.SSHKEYFILE, this.sshFileField.getText());
		G5kCfg.setSSHKey(new String(this.passwordField.getPassword()));
		G5kCfg.set(G5kCfg.PREFERREDACCESPOINT, G5kSite.getExternalFrontals()[this.preferredAccesPointComboBox.getSelectedIndex()]);
		if(ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.DIETDASHBOARD_CONTEXT)){
			G5kCfg.set(G5kCfg.GODIETSCRATCH, this.goDietScratchField.getText());
			G5kCfg.set(G5kCfg.GODIETDEBUG, ((Integer)this.debugSpinner.getValue()).toString());
			G5kCfg.set(G5kCfg.GODIETUSEUNIQUEDIRS, useUniqueDirsCheckBox.isSelected()?"yes":"no");
			G5kCfg.set(G5kCfg.GODIETSAVESTDOUT, saveStdOutCheckBox.isSelected()?"yes":"no");
			G5kCfg.set(G5kCfg.GODIETSAVESTDERR, saveStdErrCheckBox.isSelected()?"yes":"no");
            G5kCfg.set(G5kCfg.LOGGINGINGODIET,useLoggingInGoDIET.isSelected()?"yes":"no");
		}
		if (save) {
		    if (!G5kCfg.save()){
		        JOptionPane.showMessageDialog(null, "ERROR while writing the XML file",
		                "ERROR", JOptionPane.INFORMATION_MESSAGE);
		        LoggingManager.log(Level.WARNING,LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "G5k User panel configuration couldn't be saved");
		    }
		    else{
		        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "G5k User panel configuration saved");
		    }
		}
		LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "apply", "G5k User panel configuration updated");
	}

	/**
     * Method implementing the event management
     *
     * @param event an event
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.browse_btn) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileHidingEnabled(false);
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				LoggingManager.log(Level.INFO,LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed","You chose to open this file: " +
						chooser.getSelectedFile().getName());
				this.sshFileField.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}
}
