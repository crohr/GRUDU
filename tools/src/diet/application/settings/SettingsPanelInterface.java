/****************************************************************************/
/* This interface specifies two methods that should be implemented by all   */
/* classes that represent a Settings Panel                                  */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: SettingsPanelInterface.java,v 1.1 2007/10/12 13:50:28 dloureir Exp $
 * $Log: SettingsPanelInterface.java,v $
 * Revision 1.1  2007/10/12 13:50:28  dloureir
 * Changing the location of the settings and totd packages for compilation purpose
 *
 * Revision 1.3  2007/07/17 10:15:39  dloureir
 * Adding a saveSettings method to implement
 *
 * Revision 1.2  2007/03/05 13:18:57  dloureir
 * Adding documentation
 *
 ****************************************************************************/
package diet.application.settings;

/**
 * This interface specifies two methods that should be implemented by all
 * classes that represent a Settings Panel
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public interface SettingsPanelInterface {

    /**
     * Methods giving back the title of the panel
     *
     * @return the title of the panel
     */
	public String getSettingsPanelTitle();

    /**
     * Methods giving back the text of the node in the settings tree
     *
     * @return the text to print on the settings tree
     */
	public String getSettingsPanelNodeText();

	/**
	 * Methods saving the settings for that panel
	 */
	public void saveSettings();
	
}
