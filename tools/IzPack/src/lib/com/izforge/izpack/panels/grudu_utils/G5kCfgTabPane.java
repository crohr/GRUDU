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
/* $Id: G5kCfgTabPane.java,v 1.5 2007/10/30 10:25:22 dloureir Exp $
 * $Log: G5kCfgTabPane.java,v $
 * Revision 1.5  2007/10/30 10:25:22  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.4  2007/07/09 16:21:31  dloureir
 * New panels for the IzPack installer and modified panels for the DIETDashBoard and GRUDU configuration
 *
 * Revision 1.3  2007/07/06 13:34:25  dloureir
 * Correcting the way the xml files in the .diet directory are created by the installer
 *
 * Revision 1.2  2007/07/06 12:50:48  dloureir
 * New version for the use of the new variable management in the DIET_DashBoard
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
package com.izforge.izpack.panels.grudu_utils;

import javax.swing.*;

import com.izforge.izpack.panels.g5k_utils.Config;
import com.izforge.izpack.panels.g5k_utils.G5kCfg;

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
    private static final long serialVersionUID = -6410711599420986488L;

    /**
     * The user parameters panel
     */
    private G5kCfgUser myUserPanel;

    /**
     * The site enabling panel
     */
    private G5kCfgMisc  miscPanel;

    /**
     * The default constructor.
     *
     * @param parent the dialog window reference
     */
    public G5kCfgTabPane() {

    	this.myUserPanel = new G5kCfgUser();
    	add(this.myUserPanel);
    	setTitleAt(0, "User configuration");

    	this.miscPanel = new G5kCfgMisc();
    	add(this.miscPanel);
    	setTitleAt(1, "Misc");
   }

    /**
     * Method used to apply the changed realized in the panels of the tabpane
     *
     */
    public void apply() {
    	this.myUserPanel.apply();
    	this.miscPanel.apply(true);
    	Config.save();
		if (!G5kCfg.save()){
			JOptionPane.showMessageDialog(null, "ERROR while writing the XML file",
					"ERROR", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
		}
    }
    
	public void fillValues(){
		myUserPanel.fillValues();
	}
}
