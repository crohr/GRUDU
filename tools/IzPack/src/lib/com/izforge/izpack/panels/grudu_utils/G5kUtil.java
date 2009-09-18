/****************************************************************************/
/* G5K utility class                                                        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kUtil.java,v 1.6 2007/11/19 15:12:24 dloureir Exp $
 * $Log: G5kUtil.java,v $
 * Revision 1.6  2007/11/19 15:12:24  dloureir
 * Changing the version of the ssh library
 *
 * Revision 1.5  2007/11/05 18:01:27  dloureir
 * Adding a join for the diet directories initialization
 *
 * Revision 1.4  2007/10/30 10:25:22  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.3  2007/07/06 13:51:52  dloureir
 * Correction of a bug : if the configuration cannot be written (on Grid 5000 for example) the installer will now not let the user go further.
 *
 * Revision 1.2  2007/07/06 13:34:25  dloureir
 * Correcting the way the xml files in the .diet directory are created by the installer
 *
 * Revision 1.1  2007/07/06 09:14:37  dloureir
 * Adding the adapted IzPack used for the compilation of GRUDU and the DIET_DashBoard in the tools directory
 *
 * Revision 1.2  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.1  2007/02/22 14:11:36  aamar
 * Initial revision.
 *
 ****************************************************************************/
package com.izforge.izpack.panels.grudu_utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.izforge.izpack.panels.GruduInstallationPanel;
import com.izforge.izpack.panels.g5k_utils.G5kCfg;
import com.izforge.izpack.panels.g5k_utils.G5kSite;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

public class G5kUtil {

	private boolean initializationDone = false;

    private WaitingFrame waitFrame = null;

    public boolean initializeRemoteDiet(){

        waitFrame = new WaitingFrame(GruduInstallationPanel.getText(GruduInstallationPanel.WAITING_FRAME_TITLE),GruduInstallationPanel.getText(GruduInstallationPanel.WAITING_FRAME_DESCRIPTION_TEXT) + " ...",G5kSite.getSitesNumber()+1,true);
        waitFrame.launch(GruduInstallationPanel.getText(GruduInstallationPanel.WAITING_FRAME_CONNECTION_TEXT));
        Thread th = new Thread(new Runnable(){
            public void run(){
                initializationDone = initDietDir();
            }
        });
        th.start();
        try{th.join();}catch(Exception e){e.printStackTrace();}
        return initializationDone;
    }

    /**
     * Create the .diet directory in home.
     *
     * @param conn the ssh connection
     */
    private boolean initDietDir() {
    	boolean initializationOfTheDietDir = false;
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("host", G5kCfg.get(G5kCfg.PREFERREDACCESPOINT));
        properties.put("username", G5kCfg.get(G5kCfg.USERNAME));
        properties.put("publickey",G5kCfg.get(G5kCfg.SSHKEYFILE));
        properties.put("passphrase", G5kCfg.getSSHKey());
        try {
            /* Create a connection instance */
            Connection conn = new Connection(properties.get("host"));

            /* Now connect */
            conn.connect();

            /* Authenticate.
             */
            boolean isAuthenticated = conn.authenticateWithPublicKey(properties.get("username"),
                    new File(properties.get("publickey")), properties.get("passphrase"));

            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            else{
                //JOptionPane.showMessageDialog(null, "Authentication succeed");
            }

            /* Create a session */

            Session sess = conn.openSession();

//           sess.requestPTY("toto",
//                    80, 80,
//                    16, 16,
//                    null);
//
//            sess.startShell();

            //JOptionPane.showMessageDialog(null, "Connection succeed");
            sess.close();
            waitFrame.incrementProgressBar();
            // Create the .diet/bin directory if doesn't exist
            for(int i = 0 ; i < G5kSite.getSitesNumber(); i ++){
                sess = conn.openSession();
                waitFrame.setStatusText(GruduInstallationPanel.getText(GruduInstallationPanel.WAITING_FRAME_WORKING_ON_SITE) + G5kSite.getSiteForIndex(i) + " ...");
                sess.execCommand("ssh "+G5kSite.getInternalFrontals()[i]+" \"mkdir -p $HOME/.diet/bin ; mkdir -p $HOME/.diet/scratch \"");
                Thread.sleep(500);
                waitFrame.incrementProgressBar();
                sess.close();
            }
            sess.close();
            waitFrame.dispose();
            initializationOfTheDietDir = true;
        }
        catch (Exception e) {
            System.err.println("Connection failed : " + e.getMessage());
            e.printStackTrace();
            waitFrame.dispose();
        }
        return initializationOfTheDietDir;
    } // end void initDietDir



}