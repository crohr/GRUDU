/****************************************************************************/
/* [Documentation Here!]                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KadeployPartitionPanel.java,v 1.1 2007/09/28 16:02:44 aamar Exp $
 * $Log: KadeployPartitionPanel.java,v $
 * Revision 1.1  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import diet.gridr.g5k.util.*;

public class KadeployPartitionPanel extends JPanel {

    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = -5615216967584657719L;
    
    /**
     * Cluter kadeploy partition editor
     */
    private JTextField [] kpartitions;
    
    public KadeployPartitionPanel(String[] partitions) {
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.25;
        this.kpartitions = new JTextField[G5kCluster.getClustersNumber()];
        for (int ix=0; ix< G5kCluster.getClustersNumber(); ix++) {
            c.gridx   = 0;
            c.gridy   = ix;
            JLabel l = new JLabel(G5kCluster.getNameForIndex(ix));
            this.add(l, c);
            c.gridx   = 1;
            c.gridy   = ix;
            this.kpartitions[ix] = new JTextField(partitions[ix]);
            this.add(this.kpartitions[ix], c);
        }
            
    }
    
    public void apply(boolean save){
        for (int ix=0; ix<G5kCluster.getClustersNumber(); ix++) {
            Config.setXDA(ix, this.kpartitions[ix].getText());
        }
        if (save)
            Config.save();
    }

}
