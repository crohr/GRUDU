/****************************************************************************/
/* Panel for the progression of a host                                      */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: HostProgressionPanel.java,v 1.5 2007/07/12 15:26:35 dloureir Exp $
 * $Log: HostProgressionPanel.java,v $
 * Revision 1.5  2007/07/12 15:26:35  dloureir
 * Some typo correction and code cleanup
 *
 * Revision 1.4  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.3  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.2  2007/02/22 15:14:18  aamar
 * Correct header.
 *
 ****************************************************************************/
package diet.gridr.g5k.gui.kadeploy;

import javax.swing.*;

/**
 * Panel for the progression of a host
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class HostProgressionPanel extends JPanel {
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = -7660079097490982282L;
    /**
     * Host progression
     */
    private HostProgression myHostProgression;
    /**
     * label of the host progression
     */
    private JLabel myHostLabel;
    /**
     * ProgressBar of the progression
     */
    private JProgressBar myProgressBar;
    /**
     * Button for the halting
     */
    private JButton myHaltBtn;
    /**
     * Default constructor
     *
     * @param hostP host progression
     */
    public HostProgressionPanel(HostProgression hostP) {
        this.myHostProgression = hostP;
        this.myHostLabel = new JLabel(this.myHostProgression.getHost());
        this.myProgressBar = new JProgressBar(0, KadeployProgressDlg.BootSteps.length);
        this.myProgressBar.setStringPainted(true);
        this.myHaltBtn = new JButton("halt");
    }

    /**
     * Method returning the host progression
     *
     * @return the host progression
     */
    public HostProgression getHost() {
        return this.myHostProgression;
    }

    /**
     * Method returning the host label
     *
     * @return the host label
     */
    public JLabel getLabel() {
        return this.myHostLabel;
    }

    /**
     * Method returning the progressionBar
     *
     * @return the progressionBar
     */
    public JProgressBar getProgressBar() {
        return this.myProgressBar;
    }

    /**
     * Method returning the halt button
     *
     * @return the halt button
     */
    public JButton getHaltBtn() {
        return this.myHaltBtn;
    }
}