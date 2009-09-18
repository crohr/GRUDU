/****************************************************************************/
/* KaDeploy log panel                                                      */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: LogPanel.java,v 1.7 2007/07/12 15:26:35 dloureir Exp $
 * $Log: LogPanel.java,v $
 * Revision 1.7  2007/07/12 15:26:35  dloureir
 * Some typo correction and code cleanup
 *
 * Revision 1.6  2007/03/14 12:14:59  dloureir
 * Correcting minor bug related to scrolling when some text is appended to the LogPanel
 *
 * Revision 1.5  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.4  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.3  2007/02/22 15:47:12  aamar
 * Changing long textarea and correcting bug with progression status.
 *
 * Revision 1.2  2007/02/22 15:14:18  aamar
 * Correct header.
 *
 ****************************************************************************/
package diet.gridr.g5k.gui.kadeploy;

import java.util.logging.Level;

import javax.swing.*;

import diet.logging.LoggingManager;

/**
 * KaDeploy log panel
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class LogPanel extends JPanel {
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Deployment name
     */
    private String myName;

    /**
     * Standard output text area
     */
    private JTextArea outputArea;
    /**
     * Standard output text area
     */
    private JTextArea errorArea;
    /**
     * Default constructor
     *
     * @param deploymentName deployment name
     */
    public LogPanel(String deploymentName) {
        this.myName = deploymentName;
        // Init the components
        this.outputArea   = new JTextArea(20,80);
        this.outputArea.setEditable(false);
        this.errorArea    = new JTextArea(20,80);
        this.errorArea.setEditable(false);
        JScrollPane pane1 = new JScrollPane(this.outputArea);
        JScrollPane pane2 = new JScrollPane(this.errorArea);
        // Set the layout
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        add(pane1);
        add(pane2);
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "LogPanel", "LogPanel created");
    } // end constructor

    /**
     * Method returning the name of the deployment
     *
     * @return the name of the deployment
     */
    public String getName() {
        return this.myName;
    } // end getName

    /**
     * Method adding an output line
     *
     * @param text the output line
     */
    public void addOutput(String text) {
        this.outputArea.append(text + diet.gridr.g5k.util.Config.newline);
        this.outputArea.setCaretPosition(outputArea.getDocument().getLength());
    } // end addOutput

    /**
     * Method adding an error line
     *
     * @param text the error line
     */
    public void addError(String text) {
        this.errorArea.append(text+ diet.gridr.g5k.util.Config.newline);
        this.errorArea.setCaretPosition(errorArea.getDocument().getLength());
    } // end addError
}