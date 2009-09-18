/****************************************************************************/
/* This class represents the panel containing the available kernels         */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KernelPanel.java,v 1.8 2007/09/28 16:03:02 aamar Exp $
 * $Log: KernelPanel.java,v $
 * Revision 1.8  2007/09/28 16:03:02  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.7  2007/07/12 15:26:35  dloureir
 * Some typo correction and code cleanup
 *
 * Revision 1.6  2007/03/07 15:00:13  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.5  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.4  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.3  2007/02/23 13:14:44  aamar
 * Changing the manipulated kadeploy environment objects from Kadeploy to
 * KaenvironmentHelper.
 *
 * Revision 1.2  2007/02/22 15:14:18  aamar
 * Correct header.
 *
 ****************************************************************************/
package diet.gridr.g5k.gui.kadeploy;

import java.awt.*;
import javax.swing.*;

import java.util.*;
import java.util.logging.Level;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

/**
 * This class represents the panel containing the available kernels
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class KernelPanel extends JPanel {

    /**
     * Serial version ID
     */
    private static final long serialVersionUID = -8665090119336275218L;
    /**
     * array of JComboBox for the kernels
     */
    private static JComboBox [] kernelCombo;
    /**
     * array of cluster labels
     */
    private JLabel    [] siteLabel;

    /**
     * Default constructor
     *
     * @param parent parent panel
     * @param kadeployEnvs KaDeploy environments
     *
     */
    public KernelPanel(MainPanel parent, Map<Integer, KaenvironmentHelper> kadeployEnvs) {
        /*
         * TODO Change the layout to BoxLayout
         * */
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        kernelCombo = new JComboBox[100];
        this.siteLabel =  new JLabel[100];

        c.insets = new Insets(5, 5, 5, 5);
        Iterator<Integer> p = kadeployEnvs.keySet().iterator();
        while (p.hasNext()) {
            Integer key = p.next();
            KaenvironmentHelper environment = kadeployEnvs.get(key);
            Vector<KadeployEnv> envs = environment.getEnvs();
            c.gridx = 0;
            c.gridy = key.intValue();
            add(this.siteLabel[key.intValue()] = new JLabel(G5kSite.getSiteForIndex(key.intValue())), c);
            c.gridx = 1;
            KadeployEnv [] envArray = new KadeployEnv[envs.size()];
            envs.toArray(envArray);
            String [] envNames = new String[envArray.length];
            for (int jx=0; jx<envNames.length; jx++)
                envNames[jx] = envArray[jx].getName();
            add(kernelCombo[key.intValue()] = new JComboBox(envNames), c);
        }
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "KernelPanel", "KernelPanel created");
    }

    /**
     * Method returning the kernel of the selected cluster
     *
     * @param cluster cluster index
     * @return the kernel of the cluster given in parameter
     */
    public static String getSelectedKernel(int cluster) {
        if (cluster >=0 && cluster < kernelCombo.length)
            return (String)(kernelCombo[cluster].getSelectedItem());
        return null;
    }
}