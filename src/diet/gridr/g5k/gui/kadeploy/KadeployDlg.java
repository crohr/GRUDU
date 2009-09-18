/****************************************************************************/
/* KaDeploy Dialog class                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KadeployDlg.java,v 1.9 2007/11/19 15:16:20 dloureir Exp $
 * $Log: KadeployDlg.java,v $
 * Revision 1.9  2007/11/19 15:16:20  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.8  2007/09/28 16:03:02  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.7  2007/07/12 15:26:35  dloureir
 * Some typo correction and code cleanup
 *
 * Revision 1.6  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.5  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.4  2007/02/23 13:14:44  aamar
 * Changing the manipulated kadeploy environment objects from Kadeploy to
 * KaenvironmentHelper.
 *
 * Revision 1.3  2007/02/22 15:41:49  aamar
 * Changing the KadeployDlg base class.
 *
 * Revision 1.2  2007/02/22 15:14:18  aamar
 * Correct header.
 *
 ****************************************************************************/

package diet.gridr.g5k.gui.kadeploy;

import javax.swing.*;

import com.trilead.ssh2.Connection;

import java.awt.*;
import java.util.*;
import java.util.logging.Level;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

import diet.grid.api.GridJob;

/**
 * KaDeploy Dialog class
 *
 * @author  AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class KadeployDlg extends JFrame {
    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 5901887841902420210L;

    /**
     * The main panel
     */
    MainPanel myMainPanel;

    /**
     * The ssh connection to the gateway
     */
    private static Connection myConn;


    /**
     * Default constructor
     *
     */
    public KadeployDlg(Connection conn,
            Map<OarJobKey, GridJob> kadeployJobs,
            Map<Integer, KaenvironmentHelper> kadeployEnvs) {
        super("Deploy your system with Kadeploy");
        myConn = conn;
        this.myMainPanel = new MainPanel(this, kadeployJobs, kadeployEnvs);
        getContentPane().add(this.myMainPanel, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "Kadeploydlg", "Kadeploydlg Frame created");
    }

    /**
     * Synchronized method that returns the connection
     *
     * @return the SSH connection
     */
    public synchronized static Connection getConnection() {
        return myConn;
    }

    /**
     * The tree panel. This panel display the clusters and their allocated
     * hosts. Each node has a CheckBox to control its status
     */
    public class TreePanel extends JPanel {

        /**
         * Serial version ID
         */
        private static final long serialVersionUID = 1L;
    }

    /**
     * The parameters panel. This panel displays the selection parameters and
     * for each cluster the available images
     */
    public class ParamPanel extends JPanel {

        /**
         * Serial version ID
         */
        private static final long serialVersionUID = 1L;
    }
}