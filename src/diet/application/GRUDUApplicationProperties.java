/****************************************************************************/
/* <p>This file defines the main information for the Application such as:   */
/* <ul>																		*/
/* <li>The tags for the information concerning the application. This tags   */
/* concerns:																*/
/* 		<ul>																*/
/* 		<li>the version of the GRUDU</li>				         			*/
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
/* $Id: GRUDUApplicationProperties.java,v 1.3 2007/07/17 10:12:20 dloureir Exp $
 * $Log: GRUDUApplicationProperties.java,v $
 * Revision 1.3  2007/07/17 10:12:20  dloureir
 * Update of the version of GRUDU.
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
 * 		<li>the version of the GRUDU</li>
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
public interface GRUDUApplicationProperties extends ApplicationProperties {

	/**
	 * Name of the Application properties file for the DIET_DashBoard
	 */
	public static String GRUDUApplicationPropertiesFile = System.getProperty("user.home") + System.getProperty("file.separator") + ".diet" + System.getProperty("file.separator") + "GRUDUApplicationProperties.xml";

	/*
	 * Default values for the application properties
	 */
	/**
	 * Default value for the version
	 */
	public static String DEFAULT_VALUE_VERSION = "1.1.0";
	/**
	 * Default value for the tipOfTheDayShowOnStartup property
	 */
	public static String DEFAULT_VALUE_TIPSOFTHEDAY_SHOWONSTARTUP = "true";
	/**
	 * Default value for the tipOfTheDayFileOfTips property
	 */
	public static String DEFAULT_VALUE_TIPOFTHEDAY_FILEOFTIPS = "languages"+System.getProperty("file.separator") + "totd" + System.getProperty("file.separator") + "defaultGRUDUFileOfTips_eng.xml";
	
}
