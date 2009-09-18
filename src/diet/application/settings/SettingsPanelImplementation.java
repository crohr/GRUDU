/**
 * 
 */
package diet.application.settings;

import javax.swing.JPanel;


/**
 * @author david
 *
 */
public abstract class SettingsPanelImplementation
extends JPanel
implements SettingsPanelInterface {

	/* (non-Javadoc)
	 * @see diet.util.gui.SettingsPanelInterface#getSettingsPanelNodeText()
	 */
	public abstract String getSettingsPanelNodeText();

	/* (non-Javadoc)
	 * @see diet.util.gui.SettingsPanelInterface#getSettingsPanelTitle()
	 */
	public abstract String getSettingsPanelTitle();

	/* (non-Javadoc)
	 * @see diet.util.gui.SettingsPanelInterface#saveSettings()
	 */
	public abstract void saveSettings();
}
