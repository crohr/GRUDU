/****************************************************************************/
/* This class corresponds to the main element of the application wide       */
/* configuration.                                                           */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ApplicationConfiguration.java,v 1.5 2007/11/05 11:02:19 dloureir Exp $
 * $Log: ApplicationConfiguration.java,v $
 * Revision 1.5  2007/11/05 11:02:19  dloureir
 * Creation of the .diet directory if it does not exist
 *
 * Revision 1.4  2007/10/12 13:50:28  dloureir
 * Changing the location of the settings and totd packages for compilation purpose
 *
 * Revision 1.3  2007/10/04 12:13:09  dloureir
 * The Application Configuration is now related to the Settings information
 *
 * Revision 1.2  2007/07/12 12:40:07  dloureir
 * Some javadoc and the correct headers
 *
 ****************************************************************************/
package diet.application;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import diet.application.settings.SettingsPanelImplementation;
import diet.application.settings.SettingsPanelInterface;

/**
 * This class corresponds to the main element of the application wide
 * configuration.
 *   
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public abstract class ApplicationConfiguration implements ApplicationProperties {
	
	/**
	 * <p>String corresponding to the application context. In the framework of thies
	 * code it could be DIETDASHBOARD_CONTEXT for the DIET DashBoard and GRUDU_CONTEXT
	 * for ... GRUDU.</p>
	 * <p>The default application context is the DIET_DASHBOARD</p>
	 * 
	 */
	private static String applicationContext = ApplicationProperties.DIETDASHBOARD_CONTEXT;
	
	/**
	 * A string corresponding to the configuration file from which the properties
	 * are taken from 
	 */
	protected String myConfigurationFile = null;
	
	/**
	 * As this class is a singleton, here is the Application configuration that
	 * contains the properties
	 */
	private static ApplicationConfiguration myConfiguration = null;
	
	/**
	 * Root element of the XML file corresponding to the Application Configuration
	 */
	private Element rootElement = null;
	
	/**
	 * JDOM Document corresponding to the XML file
	 */
	private Document document = null;
	
	/**
	 * HashMap containing the configuration properties. The key is the attribute
	 * "name" of the property, and the value is the attribute "value" of the property.
	 */
	protected HashMap<String, String> configurationProperties = null;
	
	/**
	 * To avoid the application to have more than one context, the user
	 * can set the application context only once.
	 */
	private static boolean isApplicationContextSetted = false;
	
	protected static ArrayList<SettingsPanelImplementation> specificSettingsPanelList = new ArrayList<SettingsPanelImplementation>();
	
	/**
	 * Default constructor
	 */
	protected ApplicationConfiguration(){
		
	}
	
	/**
	 * This method sets the application context. It can only be called once. It
	 * initializes the application configuration by loading the properties from
	 * the xml configuration file.
	 * 
	 * @param context The application context. Must be DIETDASHBOARD_CONTEXT or
	 * GRUDU_CONTEXT.
	 */
	public static void setApplicationContext(String context){
		if(!isApplicationContextSetted){
			applicationContext = context;
			isApplicationContextSetted = true;
			if(applicationContext.equalsIgnoreCase(DIETDASHBOARD_CONTEXT)) myConfiguration = new DDBApplicationConfiguration();
			else myConfiguration = new GRUDUApplicationConfiguration();
			myConfiguration.loadConfiguration();
		}
		else{
			System.err.println("Application context has already be setted!\n Application context is " + applicationContext);
		}
	}
	
	/**
	 * This method returns the application context.
	 * 
	 * @return DIETDASHBOARD_CONTEXT or GRUDU_CONTEXT
	 */
	public static String getApplicationContext(){
		return applicationContext;
	}
	
	/**
	 * This method returns the Application Configuration instance
	 *  
	 * @return The ApplicationConfiguration
	 */
	public static ApplicationConfiguration getInstance(){
		if(myConfiguration == null){
			if(applicationContext.equalsIgnoreCase(DIETDASHBOARD_CONTEXT)) myConfiguration = new DDBApplicationConfiguration();
			else myConfiguration = new GRUDUApplicationConfiguration();
			myConfiguration.loadConfiguration();
		}
		return myConfiguration;
	}
	
	/**
	 * Method loading the configuration from the XML file
	 */
	public void loadConfiguration(){
		SAXBuilder saxBuilder = new SAXBuilder();
		try{
			document = saxBuilder.build(new File(myConfigurationFile));
			rootElement = document.getRootElement();
			List<Element> listOfProperties = rootElement.getChildren(TAG_PROPERTIES);
			Iterator<Element> iter = listOfProperties.iterator();
			configurationProperties = new HashMap<String, String>();
			while(iter.hasNext()){
				Element anElement = iter.next();
				String key = anElement.getAttributeValue(ATTRIBUTE_NAME_PROPERTIES);
				String value = anElement.getAttributeValue(ATTRIBUTE_VALUE_PROPERTIES);
				configurationProperties.put(key, value);
			}
			initializeSpecificSettingsPanelList();
		}
		catch(Exception e){
			initializeConfiguration();
			loadConfiguration();
		}
	}
	
	protected abstract void initializeSpecificSettingsPanelList();
	
	/**
	 * Abstract method initializing the configuration file and the configuration
	 * that should be implemented be the sub classes depending on which properties
	 * are mandatory.
	 */
	public abstract void initializeConfiguration();
	
	/**
	 * Method used to save the configuration of the application to the corresponding
	 * configuration file
	 */
	public void saveConfiguration(){
		File dietDirectory = new File(System.getProperty("user.home") + "/.diet");
		
		if(!dietDirectory.exists()) dietDirectory.mkdirs();
		rootElement = new Element(TAG_APPLICATION);
		document = new Document(rootElement);
		
		Iterator<String> iter = configurationProperties.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			String value = configurationProperties.get(key);
			Element anElement = new Element(TAG_PROPERTIES);
			anElement.setAttribute(ATTRIBUTE_NAME_PROPERTIES, key);
			anElement.setAttribute(ATTRIBUTE_VALUE_PROPERTIES, value);
			rootElement.addContent(anElement);
		}	
		try{
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, new FileOutputStream(new File(myConfigurationFile)));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method returning the property value corresponding to the property name.
	 * 
	 * @param key property name
	 * @return property value
	 */
	public synchronized String getProperty(String key){
		return configurationProperties.get(key);
	}
	
	/**
	 * Method setting a property with a name and a value
	 * 
	 * @param key the name of the property to set
	 * @param value the value of the property to set
	 */
	public synchronized void setProperty(String key, String value){
		configurationProperties.put(key, value);
	}
	
	public ArrayList<SettingsPanelImplementation> getSpecificSettingsPanelList(){
		return specificSettingsPanelList;
	}
	
}
