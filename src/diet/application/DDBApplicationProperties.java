/****************************************************************************/
/* <p>This file defines the main information for the Application such as:   */
/* <ul>																		*/
/* <li>The tags for the information concerning the application. This tags   */
/* concerns:																*/
/* 		<ul>																*/
/* 		<li>the version of the DIET DashBoard</li>							*/
/* 		<li>the language use for the internationalization of the application*/
/*      (when the internationalization will be implemented)					*/
/* 		<li>etc ...</li>													*/
/* 		</ul></li>															*/
/* <li>The default values for this information</li>							*/
/* </ul>																	*/
/*																			*/
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: DDBApplicationProperties.java,v 1.5 2007/11/22 14:59:36 dloureir Exp $
 * $Log: DDBApplicationProperties.java,v $
 * Revision 1.5  2007/11/22 14:59:36  dloureir
 * Changing the version of the DashBoard
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

/**
 * <p>This file defines the main information for the Application such as:
 * <ul>
 * <li>The tags for the information concerning the application. This tags concerns:
 * 		<ul>
 * 		<li>the version of the DIET DashBoard</li>
 * 		<li>the language use for the internationalization of the application (when
 * 		the internationalization will be implemented)
 * 		<li>etc ...</li>
 * 		</ul></li>
 * <li>The default values for this information</li>
 * </ul>
 * 
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public interface DDBApplicationProperties extends ApplicationProperties {
	
	/**
	 * Name of the Application properties file for the DIET_DashBoard
	 */
	public static String DDBApplicationPropertiesFile = System.getProperty("user.home") + System.getProperty("file.separator") + ".diet" + System.getProperty("file.separator") + "DDBApplicationProperties.xml";

	/*
	 * Default values for the application properties
	 */
	/**
	 * Default value for the version
	 */
	public static String DEFAULT_VALUE_VERSION = "1.0.0";
	/**
	 * Default value for the tipOfTheDayShowOnStartup property
	 */
	public static String DEFAULT_VALUE_TIPSOFTHEDAY_SHOWONSTARTUP = "true";
	/**
	 * Default value for the tipOfTheDayFileOfTips property
	 */
	public static String DEFAULT_VALUE_TIPOFTHEDAY_FILEOFTIPS = "languages"+System.getProperty("file.separator") + "totd" + System.getProperty("file.separator") + "defaultDDBFileOfTips_eng.xml";
	
	public static String DEFAULT_VALUE_DIET_INSTALLATION_PATH = "/usr/local/diet";
	
	public static String PROPERTY_DIET_INSTALLATION_PATH = "dietInstallationPath";
	
	public static String DEFAULT_VALUE_DASHBOARD_MINIMIZE_ON_CLOSE = "true";
	
}
