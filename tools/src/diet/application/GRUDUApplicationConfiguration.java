/****************************************************************************/
/* This class corresponds to the main element of the application wide       */
/* configuration for the GRUDU context                           		    */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: GRUDUApplicationConfiguration.java,v 1.3 2007/10/04 12:13:09 dloureir Exp $
 * $Log: GRUDUApplicationConfiguration.java,v $
 * Revision 1.3  2007/10/04 12:13:09  dloureir
 * The Application Configuration is now related to the Settings information
 *
 * Revision 1.2  2007/07/12 12:40:07  dloureir
 * Some javadoc and the correct headers
 *
 ****************************************************************************/
package diet.application;

import java.util.HashMap;

/**
 * This class corresponds to the main element of the application wide
 * configuration for the GRUDU context
 * 
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class GRUDUApplicationConfiguration 
	extends ApplicationConfiguration 
	implements GRUDUApplicationProperties {

	/* (non-Javadoc)
	 * @see diet.application.ApplicationConfiguration#initializeSpecificSettingsPanelList()
	 */
	@Override
	protected void initializeSpecificSettingsPanelList() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Default constructor
	 */
	protected GRUDUApplicationConfiguration(){
		myConfigurationFile = GRUDUApplicationPropertiesFile;
	}
	
	/**
	 * <p>Implementation of the initializeConfiguration method of the super class.</p>
	 * <p>This method initializes the properties of the GRUDU configuration
	 * file with their default values.</p>
	 * 
	 * @see diet.application.ApplicationConfiguration#initializeConfiguration()
	 */
	@Override
	public void initializeConfiguration() {
		configurationProperties = new HashMap<String, String>();
		configurationProperties.put(PROPERTY_VERSION,DEFAULT_VALUE_VERSION);
		configurationProperties.put(PROPERTY_TIPOFTHEDAY_FILEOFTIPS, DEFAULT_VALUE_TIPOFTHEDAY_FILEOFTIPS);
		configurationProperties.put(PROPERTY_TIPOFTHEDAY_SHOWONSTARTUP, DEFAULT_VALUE_TIPSOFTHEDAY_SHOWONSTARTUP);
		saveConfiguration();
	}
}