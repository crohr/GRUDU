package com.izforge.izpack.panels.grudu_utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import com.izforge.izpack.panels.GruduInstallationPanel;
import com.izforge.izpack.panels.g5k_utils.G5kCfg;
import com.izforge.izpack.panels.g5k_utils.G5kSite;

public class G5kCfgUser extends JPanel implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 8310328480628331762L;
	private JTextField userField;
	private JTextField sshFileField;
	private JPasswordField passwordField;
	private JComboBox preferredAccesPointComboBox;

	private JButton browse_btn;

	public G5kCfgUser() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		JLabel user_label       = new JLabel(GruduInstallationPanel.getText(GruduInstallationPanel.USERNAME));
		JLabel ssh_label        = new JLabel(GruduInstallationPanel.getText(GruduInstallationPanel.SSH_KEY_FILE));
		JLabel pass_label       = new JLabel(GruduInstallationPanel.getText(GruduInstallationPanel.PASSPHRASE));
		JLabel preferredAccesPointLabel = new JLabel(GruduInstallationPanel.getText(GruduInstallationPanel.PREFERRED_ACCES_POINT));

		Vector<String> vectorElements = new Vector<String>();
		for(int i = 0 ;  i < G5kSite.getSitesNumber() ; i ++){
			vectorElements.add(G5kSite.getSiteForIndex(i));
		}
		this.preferredAccesPointComboBox = new JComboBox(vectorElements);
		this.userField = new JTextField();
		this.sshFileField = new JTextField();
		this.passwordField = new JPasswordField();

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
		this.sshFileField.setToolTipText(GruduInstallationPanel.getText(GruduInstallationPanel.SSH_KEY_FILE_TOOLTIP));
		this.browse_btn = new JButton(GruduInstallationPanel.getText(GruduInstallationPanel.BROWSE));
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
	}

	protected void fillValues() {
		this.userField.setText(G5kCfg.get(G5kCfg.USERNAME));
		this.sshFileField.setText(G5kCfg.get(G5kCfg.SSHKEYFILE));
		this.passwordField.setText(G5kCfg.getSSHKey());
		this.preferredAccesPointComboBox.setSelectedItem(G5kCfg.get(G5kCfg.PREFERREDACCESPOINT));
	}

	public void apply() {
		G5kCfg.set(G5kCfg.USERNAME, this.userField.getText());
		G5kCfg.set(G5kCfg.SSHKEYFILE, this.sshFileField.getText());
		G5kCfg.setSSHKey(new String(this.passwordField.getPassword()));
		G5kCfg.set(G5kCfg.PREFERREDACCESPOINT, G5kSite.getExternalFrontals()[this.preferredAccesPointComboBox.getSelectedIndex()]);
        G5kCfg.save();
	}


	public void actionPerformed(ActionEvent event) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileHidingEnabled(false);
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				this.sshFileField.setText(chooser.getSelectedFile().getAbsolutePath());
			}
	}
}
