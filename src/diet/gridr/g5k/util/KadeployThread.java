/****************************************************************************/
/* KaDeploy executing thread.                                               */
/* Deploy a specific environment on a specif node                           */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KadeployThread.java,v 1.6 2007/11/19 15:16:20 dloureir Exp $
 * $Log: KadeployThread.java,v $
 * Revision 1.6  2007/11/19 15:16:20  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.5  2007/07/12 15:42:12  dloureir
 * Some typo corrections.
 *
 * Revision 1.4  2007/05/22 07:47:11  aamar
 * Correct header
 *
 ****************************************************************************/

package diet.gridr.g5k.util;

import java.io.*;
import java.util.logging.Level;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;
import diet.logging.LoggingManager;

/**
 * KaDeploy executing thread.
 * Deploy a specific environment on a specif node
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class KadeployThread extends Thread {
    /**
     * Connection of the KadeployThread to communicate remotely
     */
    private Connection conn;
    /**
     * String describing the oar host
     */
    private String oarHost;
    /**
     * String describing the name of the environment
     */
    private String envName;
    /**
     * String describing the node on which we are deploying
     */
    private String node;
    /**
     * Partition on which we are deploying
     */
    private String partition;
    /**
     * a listener for a deployment
     */
    private KadeployListener myListener;
    /**
     * default constructor of a KadeployThread
     *
     * @param conn connection
     * @param oarHost oar host
     * @param listener listener for the deployment
     * @param envName name of the environment
     * @param node name of the node on which we are deploying
     * @param partition partition on which we are deploying
     */
    public KadeployThread(Connection conn, String oarHost,
            KadeployListener listener,
            String envName, String node, String partition) {
        this.conn = conn;
        this.oarHost = oarHost;
        this.myListener = listener;
        this.envName = envName;
        this.node = node;
        this.partition = partition;
    }

    /**
     * Start the deployment of a KaDeploy environment and notify the listener about deployment progress.
     * The different steps are :
     * <BootInit 0>
     * <BootInit 1>
     * <PreInstall>
     * <Transfert>
     * <tar Transfert>
     * <PostInstall>
     *
     */

    public static final String[] BootSteps = {
        "<BootInit 0>",
        "<BootInit 1>",
        "<PreInstall>",
        "<Transfert>",
        "<tar Transfert>",
        "<PostInstall>"
    };

    /**
     * Method running the deployment
     *
     */
    public void run() {
        // Cmd syntax
        // kadeploy -e environment_name -m node -p partition_device
        String deployCmd =                                  // Reservation cmd
            "ssh -o ConnectTimeout=10 " + this.oarHost +
            " \"kadeploy -e " + this.envName +
            " -m " + this.node +
            " -p " + this.partition +  "\"";
        try {
            Session sess = conn.openSession();
            sess.execCommand(deployCmd);

            InputStream stdout = new StreamGobbler(sess.getStdout());

            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

            String line = null;
            while ( true ) {
                line = br.readLine();
                if (line == null)
                    break;
                for (int i=0; i<BootSteps.length; i++) {
                    if (line.startsWith(BootSteps[i])) {
                        if (this.myListener != null)
                            this.myListener.setProgression(this.node, i+1);
                    } // end if startsWith
                } // end for
            } // end while

        }
        catch (Exception e) {
        	LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", e);
        }
    }
}
