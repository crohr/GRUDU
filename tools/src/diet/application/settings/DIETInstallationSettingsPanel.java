/**
 * 
 */
package diet.application.settings;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import diet.application.ApplicationConfiguration;
import diet.application.DDBApplicationProperties;

/**
 * @author david
 *
 */
public class DIETInstallationSettingsPanel extends SettingsPanelImplementation {
	
	private JPanel dietInstallationPanel = null;
	private JTextField dietInstallationPathTextField = null;
	
	
	public DIETInstallationSettingsPanel(){
		initialize();
	}
	
	private void initialize(){
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(getDIETInstallationPanel());
		add(getEmptyPanel());
	}
	
	private JPanel getEmptyPanel(){
		JPanel emptyJPanel = new JPanel();
		emptyJPanel.add(Box.createVerticalGlue());
		return emptyJPanel;
	}
	
	private JPanel getDIETInstallationPanel(){
		if(dietInstallationPanel == null){
			dietInstallationPanel = new JPanel();
			dietInstallationPanel.setMaximumSize(new Dimension(100000,20));
			dietInstallationPanel.setLayout(new BoxLayout(dietInstallationPanel,BoxLayout.X_AXIS));
			JLabel dietInstallationPathLabel = new JLabel("Path to the local DIET installation");
			String value = ApplicationConfiguration.getInstance().getProperty(DDBApplicationProperties.PROPERTY_DIET_INSTALLATION_PATH);
			dietInstallationPathTextField = new JTextField(value);
			dietInstallationPathTextField.setEditable(false);
			JButton dietInstallationPathBrowseButton = new JButton("Browse");
			dietInstallationPathBrowseButton.addActionListener(new ActionListener(){

				/* (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFileChooser fileChooser = new JFileChooser();
		            fileChooser.setFileHidingEnabled(false);
		            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int result = fileChooser.showOpenDialog(null);
		            if (result == JFileChooser.APPROVE_OPTION) {
		                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
		                dietInstallationPathTextField.setText(fileName);
		            }
				}
				
			});
			dietInstallationPanel.add(dietInstallationPathLabel);
			dietInstallationPanel.add(Box.createHorizontalGlue());
			dietInstallationPanel.add(dietInstallationPathTextField);
			dietInstallationPanel.add(dietInstallationPathBrowseButton);
		}
		return dietInstallationPanel;
	}
		
	/* (non-Javadoc)
	 * @see diet.util.gui.SettingsPanelInterface#getSettingsPanelNodeText()
	 */
	public String getSettingsPanelNodeText() {
		// TODO Auto-generated method stub
		return "DIET installation";
	}

	/* (non-Javadoc)
	 * @see diet.util.gui.SettingsPanelInterface#getSettingsPanelTitle()
	 */
	public String getSettingsPanelTitle() {
		// TODO Auto-generated method stub
		return "DIET Installation configuration";
	}

	/* (non-Javadoc)
	 * @see diet.util.gui.SettingsPanelInterface#saveSettings()
	 */
	public void saveSettings() {
		// TODO Auto-generated method stub
		ApplicationConfiguration.getInstance().setProperty(DDBApplicationProperties.PROPERTY_DIET_INSTALLATION_PATH, dietInstallationPathTextField.getText());
	}

}
