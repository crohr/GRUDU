/****************************************************************************/
/* This class coresponds to the panel for the information about the user    */
/*such as the username, the ssh key file etc ...                            */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kUserPanel.java,v 1.5 2007/10/30 10:25:23 dloureir Exp $
 * $Log: G5kUserPanel.java,v $
 * Revision 1.5  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.4  2007/07/09 16:21:31  dloureir
 * New panels for the IzPack installer and modified panels for the DIETDashBoard and GRUDU configuration
 *
 * Revision 1.3  2007/07/06 13:34:26  dloureir
 * Correcting the way the xml files in the .diet directory are created by the installer
 *
 * Revision 1.2  2007/07/06 12:50:48  dloureir
 * New version for the use of the new variable management in the DIET_DashBoard
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
package com.izforge.izpack.panels.diet_dashboard_utils;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import com.izforge.izpack.panels.g5k_utils.G5kCfg;
import com.izforge.izpack.panels.g5k_utils.G5kSite;

/**
 * This class coresponds to the panel for the information about the user
 * such as the username, the ssh key file etc ...
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kUserPanel extends JPanel implements ActionListener {
    /**
     * serialVersionUID defines the version for the serialisation of
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
     * ComboBox for the selection of the preferred acces point
     */
	private JComboBox preferredAccesPointComboBox;
    /**
     * browse button
     */
	private JButton browse_btn;
    /**
     * checkbox for the use of unique dirs
     */
	private JCheckBox useUniqueDirsCheckBox;
    /**
     * checkbox for the saving of the stderr
     */
    private JCheckBox saveStdErrCheckBox;
    /**
     * checkbox for the saving of the stdout
     */
    private JCheckBox saveStdOutCheckBox;
    /**
     * spinner for the debug level
     */
	private JSpinner debugSpinner;
    /**
     * CheckBox used to specify if the logging in goDIET shoud be used
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
			vectorElements.add(G5kSite.getSiteForIndex(i));
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

			c.weightx = 0.25;
			c.gridx = 0;
			c.gridy = 2;
			c.insets = new Insets(10,10, 10,10);
			add(scratch_label, c);

			c.weightx = 1.0;
			c.gridx = 1;
			c.gridy = 2;
			add(this.goDietScratchField, c);

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
		//c.gridwidth = GridBagConstraints.REMAINDER;
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

		fillValues();
	}

    /**
     * Method filling the values of the configuration
     *
     */
	protected void fillValues() {
		this.userField.setText(G5kCfg.get(G5kCfg.USERNAME));
		this.sshFileField.setText(G5kCfg.get(G5kCfg.SSHKEYFILE));
		this.passwordField.setText(G5kCfg.getSSHKey());
		this.preferredAccesPointComboBox.setSelectedItem(G5kCfg.get(G5kCfg.PREFERREDACCESPOINT));
		
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

    /**
     * Method applying the changes made
     *
     */
	public void apply() {
		G5kCfg.set(G5kCfg.USERNAME, this.userField.getText());
		G5kCfg.set(G5kCfg.SSHKEYFILE, this.sshFileField.getText());
		G5kCfg.setSSHKey(new String(this.passwordField.getPassword()));
		G5kCfg.set(G5kCfg.PREFERREDACCESPOINT, G5kSite.getExternalFrontals()[this.preferredAccesPointComboBox.getSelectedIndex()]);
		G5kCfg.set(G5kCfg.GODIETSCRATCH, this.goDietScratchField.getText());
		G5kCfg.set(G5kCfg.GODIETDEBUG, ((Integer)this.debugSpinner.getValue()).toString());
		G5kCfg.set(G5kCfg.GODIETUSEUNIQUEDIRS, useUniqueDirsCheckBox.isSelected()?"yes":"no");
		G5kCfg.set(G5kCfg.GODIETSAVESTDOUT, saveStdOutCheckBox.isSelected()?"yes":"no");
		G5kCfg.set(G5kCfg.GODIETSAVESTDERR, saveStdErrCheckBox.isSelected()?"yes":"no");
		G5kCfg.set(G5kCfg.LOGGINGINGODIET,useLoggingInGoDIET.isSelected()?"yes":"no");

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
				this.sshFileField.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}
}