/****************************************************************************/
/* Class representing the panel for the deployment progression              */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: DeploymentProgressionPanel.java,v 1.5 2007/07/12 15:26:35 dloureir Exp $
 * $Log: DeploymentProgressionPanel.java,v $
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

import diet.logging.LoggingManager;

import java.awt.*;
import java.util.logging.Level;

/**
 * Class representing the panel for the deployment progression
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class DeploymentProgressionPanel extends JPanel {
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = -7660079097490982282L;
    /**
     * deployment progression
     */
    private DeploymentProgression myDeploymentProgression;
    /**
     * deployment label
     */
    private JLabel myDeploymentLabel;
    /**
     * progressing bar of the deployment
     */
    private JProgressBar myProgressBar;
    /**
     * button for the halting of the deployment
     */
    private JButton myHaltBtn;
    /**
     * Default constructor
     *
     * @param deploymentP deployment progression
     */
    public DeploymentProgressionPanel(DeploymentProgression deploymentP) {
        this.myDeploymentProgression = deploymentP;
        this.myDeploymentLabel = new JLabel(this.myDeploymentProgression.getName());
        this.myProgressBar = new JProgressBar(0, KadeployProgressDlg.BootSteps.length);
        this.myProgressBar.setStringPainted(true);
        this.myHaltBtn = new JButton("halt");
        setLayout(new FlowLayout());
        add(this.myDeploymentLabel);
        add(this.myProgressBar);
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "DeploymentProgressionPanel", "DeploymentProgressionPanel created");
    }

    /**
     * Method returning the deployment progression
     *
     * @return deployment progression
     */
    public DeploymentProgression getDeploymentProgression() {
        return this.myDeploymentProgression;
    }

    /**
     * Method returning the label of the deployment
     *
     * @return label of the deployment
     */
    public JLabel getLabel() {
        return this.myDeploymentLabel;
    }

    /**
     * Method returning the progression bar of the deployment
     *
     * @return a JProgressBar
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
