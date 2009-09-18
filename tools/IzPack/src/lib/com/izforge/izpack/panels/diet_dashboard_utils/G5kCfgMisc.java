/****************************************************************************/
/* Grid5000 configuration class                                             */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kCfgMisc.java,v 1.5 2007/10/30 10:25:23 dloureir Exp $
 * $Log: G5kCfgMisc.java,v $
 * Revision 1.5  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.14  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.13  2007/07/18 14:07:47  dloureir
 * The configuration of GRUDU has now only two buttons for the saving of the modified (or not) configuration and the correction of a little bug : the JList of theUserPanel is composed of external acces frontales whereas it was not the case before (it was site's names).
 *
 * Revision 1.12  2007/07/12 12:55:03  dloureir
 * A typo correction
 *
 * Revision 1.11  2007/05/22 07:33:21  aamar
 * Correct header.
 *
 ****************************************************************************/
package  com.izforge.izpack.panels.diet_dashboard_utils;

import javax.swing.*;

import com.izforge.izpack.panels.g5k_utils.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


/**
 * Grid5000 configuration class
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 */

public class G5kCfgMisc extends JPanel{

    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
	private static final long serialVersionUID = -5293991270401888668L;

	/**
     * Checkboxes to show and control if the cluster is enabled
     */
    protected JCheckBox  [] enabledSites = new JCheckBox[G5kSite.getSitesNumber()];

    /**
     * KaDeploy partitions
     */
    protected JTextField [] kadeployPartitions = new JTextField[G5kSite.getSitesNumber()];

    /**
     * Batch schedulers
     */
    protected JComboBox [] batchSchedulers = new JComboBox[G5kSite.getSitesNumber()];

    protected String [] batchSchedulersIds;
    
    /**
     * Default constructor
     *
     * @param parent the dialog window reference
     */
    public G5kCfgMisc() {
        init();
        initComponents();
    }
    
    protected void init() {
        this.batchSchedulersIds = BatchSchedulerMgr.getSchedulersIDs();
    }

    /**
     * Method initializing the components of the G5kCfg tab for misc values
     *
     */
    protected void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.25;
        c.gridx   = 2;
        c.gridy   = 1;
        add(new JLabel("Kadeploy partition"), c);
        for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
            c.weightx = 0.25;
            c.gridx = 0;
            c.gridy = ix+2;
            c.insets = new Insets(5, 5, 5, 5);
            add(new JLabel(G5kConstants.sites.get(ix).getName()), c);
            
            c.weightx = 0.25;
            c.gridx = 1;
            c.gridy = ix+2;
            this.enabledSites[ix] = new JCheckBox();
            if (Config.isSiteEnable(ix))
                this.enabledSites[ix].setSelected(true);
            add(this.enabledSites[ix], c);
            
            c.weightx = 0.25;
            c.gridx = 2;
            this.kadeployPartitions[ix] = new JTextField();
            this.kadeployPartitions[ix].setText(Config.getXDA(ix));
            add(this.kadeployPartitions[ix], c);

            c.gridx = 3;
            this.batchSchedulers[ix] = new JComboBox(this.batchSchedulersIds);
            String schedID = Config.getBatchScheduler(ix);
            for (int jx=0; jx<this.batchSchedulersIds.length; jx++) {
                if (schedID.equals(this.batchSchedulersIds[jx])) {
                    this.batchSchedulers[ix].setSelectedIndex(jx);
                    break;
                }
            }
            add(this.batchSchedulers[ix], c);
        } // end for
    } // end initComponents
    
    public void apply(boolean save){
        for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
            Config.setEnable(ix, this.enabledSites[ix].isSelected());
            Config.setBatchScheduler(ix, this.batchSchedulers[ix].getSelectedItem()+"");
        }
        if (save)
            Config.save();
    }
}
