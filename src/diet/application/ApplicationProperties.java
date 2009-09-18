/****************************************************************************/
/* This interface declares the application wide properties and the          */
/* necessary tags used to read and write to/from XML configuration files    */
/*																			*/
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ApplicationProperties.java,v 1.4 2007/10/06 09:35:08 aamar Exp $
 * $Log: ApplicationProperties.java,v $
 * Revision 1.4  2007/10/06 09:35:08  aamar
 * Adding DashBoard close operation behaviour (minimize/exit)
 *
 * Revision 1.3  2007/07/17 10:12:41  dloureir
 * Changing the names of the contexts.
 *
 * Revision 1.2  2007/07/12 12:40:07  dloureir
 * Some javadoc and the correct headers
 *
 ****************************************************************************/
package diet.application;

/**
 * This interface declares the application wide properties and the necessary 
 * tags used to read and write to/from XML configuration files
 * 
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public interface ApplicationProperties {

	/*
	 * Tags for the application properties file
	 */
	
	/**
	 * rootElement tag
	 */
	public static String TAG_APPLICATION = "application";
	/**
	 * version property name
	 */
	public static String PROPERTY_VERSION = "version";
	/**
	 * language property name
	 */
	public static String PROPERTY_LANGUAGE = "language";
	/**
	 * properties tag
	 */
	public static String TAG_PROPERTIES = "properties";
	/**
	 * properties attribute name
	 */
	public static String ATTRIBUTE_NAME_PROPERTIES = "name";
	/**
	 * properties attribute value
	 */
	public static String ATTRIBUTE_VALUE_PROPERTIES = "value";
	/**
	 * Tip Of The Day ShowOnStartup property
	 */
	public static String PROPERTY_TIPOFTHEDAY_SHOWONSTARTUP = "tipOfTheDayShowOnStartup";
	/**
	 * Tip Of The Day file of tips property
	 */
	public static String PROPERTY_TIPOFTHEDAY_FILEOFTIPS = "tipOfTheDayFileOfTips";

	/**
	 * Close operation behaviour
	 */
	public static String PROPERTY_CLOSE_OPERATION = "minimizeOnClose";
	/*
	 * Contexts for the application
	 */
	
	/**
	 * String corresponding to the DIET DASHBOARD context
	 */
	public static String DIETDASHBOARD_CONTEXT = "DIET DashBoard";
	/**
	 * String corresponding to the GRUDU context
	 */
	public static String GRUDU_CONTEXT = "GRUDU";
	
}
