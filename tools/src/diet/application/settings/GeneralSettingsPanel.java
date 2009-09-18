/**
 * 
 */
package diet.application.settings;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import diet.application.ApplicationConfiguration;
import diet.application.ApplicationProperties;

/**
 * @author david
 *
 */
public class GeneralSettingsPanel 
extends SettingsPanelImplementation {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 4038519070786315914L;

	private JPanel versionJPanel = null;
	private JPanel totdJPanel    = null;
	private JPanel exitOpPanel   = null;
	private JCheckBox totdJCheckBox = null;
	private JCheckBox minimizeOnClose = null;
	
	/* (non-Javadoc)
	 * @see diet.util.gui.SettingsPanelInterface#getSettingsPanelNodeText()
	 */
	public String getSettingsPanelNodeText() {
		// TODO Auto-generated method stub
		return "General";
	}

	/* (non-Javadoc)
	 * @see diet.util.gui.SettingsPanelInterface#getSettingsPanelTitle()
	 */
	public String getSettingsPanelTitle() {
		// TODO Auto-generated method stub
		return "General settings of " + ApplicationConfiguration.getApplicationContext();
	}
	
	public GeneralSettingsPanel(){
		initialize();
	}

	public void initialize(){
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.add(getVersionJPanel());
		this.add(getTOTDJPanel());
		this.add(createCloseBehaviourPanel());
		this.add(Box.createVerticalGlue());
		
	}
	
	public JPanel getVersionJPanel(){
		if(versionJPanel == null){
			versionJPanel = new JPanel();
			versionJPanel.setLayout(new BoxLayout(versionJPanel,BoxLayout.X_AXIS));
			JLabel versionJLabel = new JLabel("Version of " + ApplicationConfiguration.getApplicationContext());
			JLabel versionValueJLabel = new JLabel(ApplicationConfiguration.getInstance().getProperty(ApplicationConfiguration.PROPERTY_VERSION));
			versionJPanel.add(versionJLabel);
			versionJPanel.add(Box.createHorizontalGlue());
			versionJPanel.add(versionValueJLabel);
		}
		return versionJPanel;
	}
	
	public JPanel getTOTDJPanel(){
		if(totdJPanel == null){
			totdJPanel = new JPanel();
			totdJPanel.setLayout(new BoxLayout(totdJPanel,BoxLayout.X_AXIS));
			JLabel totdJLabel = new JLabel("Tips of the day showed on start up");
			totdJCheckBox = new JCheckBox();
			totdJCheckBox.setSelected(Boolean.parseBoolean(ApplicationConfiguration.getInstance().getProperty(ApplicationProperties.PROPERTY_TIPOFTHEDAY_SHOWONSTARTUP)));
			totdJPanel.add(totdJLabel);
			totdJPanel.add(Box.createHorizontalGlue());
			totdJPanel.add(totdJCheckBox);
		}
		return totdJPanel;
	}

	public JPanel createCloseBehaviourPanel() {
	    if (this.exitOpPanel == null) {
	        this.exitOpPanel = new JPanel();
	        this.exitOpPanel.setLayout(new BoxLayout(this.exitOpPanel, BoxLayout.X_AXIS));
	        this.minimizeOnClose = new JCheckBox();
	        this.minimizeOnClose.setSelected(Boolean.parseBoolean(ApplicationConfiguration.getInstance().getProperty(ApplicationProperties.PROPERTY_CLOSE_OPERATION)));
	        this.exitOpPanel.add(new JLabel("Minimize on close (change takes effect next start)"));
	        this.exitOpPanel.add(Box.createHorizontalGlue());
	        this.exitOpPanel.add(this.minimizeOnClose);
	        
	    }
	    return this.exitOpPanel;
	}
	/* (non-Javadoc)
	 * @see diet.util.gui.SettingsPanelInterface#saveSettings()
	 */
	public void saveSettings() {
	    ApplicationConfiguration appCfg = ApplicationConfiguration.getInstance();
		appCfg.setProperty(ApplicationConfiguration.PROPERTY_TIPOFTHEDAY_SHOWONSTARTUP, 
		        Boolean.toString(totdJCheckBox.isSelected()));
		appCfg.setProperty(ApplicationConfiguration.PROPERTY_CLOSE_OPERATION, 
		        Boolean.toString(this.minimizeOnClose.isSelected()));
	}
	
}
