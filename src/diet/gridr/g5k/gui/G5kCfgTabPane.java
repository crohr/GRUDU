/****************************************************************************/
/* This class corresponds to the Tab panel containing the different tab for */
/* the configuration of the Grid5000 sites                                  */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kCfgTabPane.java,v 1.15 2007/11/05 15:45:14 aamar Exp $
 * $Log: G5kCfgTabPane.java,v $
 * Revision 1.15  2007/11/05 15:45:14  aamar
 * Restoring save function for sites properties
 *
 * Revision 1.14  2007/10/12 13:58:21  dloureir
 * The modifications done in all configuration panels are now applied in the configuration file
 *
 * Revision 1.13  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.12  2007/07/18 14:07:47  dloureir
 * The configuration of GRUDU has now only two buttons for the saving of the modified (or not) configuration and the correction of a little bug : the JList of theUserPanel is composed of external acces frontales whereas it was not the case before (it was site's names).
 *
 * Revision 1.11  2007/07/11 15:40:25  dloureir
 * Adding an application package for the application wide properties and two panels for the Tips of the Day for DDB and GRUDU
 *
 * Revision 1.10  2007/05/02 12:50:58  dloureir
 * Adding the support of the batch schedulers for the SeD in the Wizzard (through an updated XMLGoDIETgenerator version) and the log option for GoDIET
 *
 * Revision 1.9  2007/03/07 15:00:14  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.8  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.util.logging.Level;

import javax.swing.*;

import diet.application.ApplicationConfiguration;
import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

/**
 * This class corresponds to the Tab panel containing the different tab for
 * the configuration of the Grid5000 sites
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kCfgTabPane extends JTabbedPane {
	
	/**
     * Serial version ID
     */
	private static final long serialVersionUID = 8444669827671751804L;
    /**
     * The panel of G5K sites
     */
    private G5kCfgPanel sitesPanel[];

    /**
     * The user parameters panel
     */
    private G5kUserPanel myUserPanel;

    /**
     * The site enabling panel
     */
    private G5kCfgMisc  miscPanel;

    /**
     * Kadeploy parititions panel 
     */
    private KadeployPartitionPanel kpPanel;
    
    /**
     * The default constructor.
     *
     * @param parent the dialog window reference
     */
    public G5kCfgTabPane() {

    	this.myUserPanel = new G5kUserPanel();
    	add(this.myUserPanel);
    	setTitleAt(0, "User configuration");

    	this.miscPanel = new G5kCfgMisc();
    	add(this.miscPanel);
    	setTitleAt(1, "Misc");
    	
    	this.kpPanel = new KadeployPartitionPanel(Config.getXDAs());
    	add(this.kpPanel);
    	setTitleAt(2, "Kadeploy Paritition");
    	
    	if ( ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.DIETDASHBOARD_CONTEXT)){
            this.sitesPanel = new G5kCfgPanel[G5kSite.getSitesNumber()];
            for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
                this.sitesPanel[ix] =
                    new G5kCfgPanel(G5kConstants.sites.get(ix).getName(), ix);
                add(this.sitesPanel[ix]);
                setTitleAt(ix+3, G5kConstants.sites.get(ix).getName());
            }
    	}
    	LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kCfgTabPane", "G5k Config Tab Panel initialized");
    }

    /**
     * Method used to apply the changed realized in the panels of the tabpane
     *
     */
    public void apply() {
    	this.myUserPanel.apply(true);
    	this.miscPanel.apply(true);
    	if(ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.DIETDASHBOARD_CONTEXT)){
            for (int ix=0; ix<this.sitesPanel.length; ix++) {
                this.sitesPanel[ix].apply();
            }
            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "apply", "G5k Config updated");
    	}
    	G5kCfg.save();
    	this.kpPanel.apply(true);
    }
}
