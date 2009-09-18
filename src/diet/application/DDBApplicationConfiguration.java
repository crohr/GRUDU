/****************************************************************************/
/* This class corresponds to the main element of the application wide       */
/* configuration for the DIET_DashBoard context                             */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: DDBApplicationConfiguration.java,v 1.5 2007/10/12 13:50:28 dloureir Exp $
 * $Log: DDBApplicationConfiguration.java,v $
 * Revision 1.5  2007/10/12 13:50:28  dloureir
 * Changing the location of the settings and totd packages for compilation purpose
 *
 * Revision 1.4  2007/10/06 09:35:08  aamar
 * Adding DashBoard close operation behaviour (minimize/exit)
 *
 * Revision 1.3  2007/10/04 12:13:09  dloureir
 * The Application Configuration is now related to the Settings information
 *
 * Revision 1.2  2007/07/12 12:40:07  dloureir
 * Some javadoc and the correct headers
 *
 ****************************************************************************/
package diet.application;

import java.util.HashMap;

import diet.application.settings.DIETInstallationSettingsPanel;


/**
 * This class corresponds to the main element of the application wide
 * configuration for the DIET_DashBoard context
 * 
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class DDBApplicationConfiguration 
	extends ApplicationConfiguration 
	implements DDBApplicationProperties {

	/* (non-Javadoc)
	 * @see diet.application.ApplicationConfiguration#initializeSpecificSettingsPanelList()
	 */
	@Override
	protected void initializeSpecificSettingsPanelList() {
		// TODO Auto-generated method stub
		specificSettingsPanelList.add(new DIETInstallationSettingsPanel());
	}

	/**
	 * Default constructor
	 */
	protected DDBApplicationConfiguration(){
		myConfigurationFile = DDBApplicationPropertiesFile;
	}

	/**
	 * <p>Implementation of the initializeConfiguration method of the super class.</p>
	 * <p>This method initializes the properties of the DIET DashBoard configuration
	 * file with their default values.</p>
	 * 
	 * @see diet.application.ApplicationConfiguration#initializeConfiguration()
	 */
	@Override
	public void initializeConfiguration() {
		// TODO Auto-generated method stub
		configurationProperties = new HashMap<String, String>();
		configurationProperties.put(PROPERTY_VERSION,DEFAULT_VALUE_VERSION);
		configurationProperties.put(PROPERTY_TIPOFTHEDAY_FILEOFTIPS, DEFAULT_VALUE_TIPOFTHEDAY_FILEOFTIPS);
		configurationProperties.put(PROPERTY_TIPOFTHEDAY_SHOWONSTARTUP, DEFAULT_VALUE_TIPSOFTHEDAY_SHOWONSTARTUP);
		configurationProperties.put(PROPERTY_DIET_INSTALLATION_PATH,DEFAULT_VALUE_DIET_INSTALLATION_PATH);
		configurationProperties.put(ApplicationProperties.PROPERTY_CLOSE_OPERATION, 
		        DDBApplicationProperties.DEFAULT_VALUE_DASHBOARD_MINIMIZE_ON_CLOSE);
		saveConfiguration();
	}
	
}
