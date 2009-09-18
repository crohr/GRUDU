/****************************************************************************/
/* G5K utility class                                                        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kUtil.java,v 1.8 2007/11/19 15:12:25 dloureir Exp $
 * $Log: G5kUtil.java,v $
 * Revision 1.8  2007/11/19 15:12:25  dloureir
 * Changing the version of the ssh library
 *
 * Revision 1.7  2007/11/07 15:27:47  dloureir
 * Changing the way the remote installation is done
 *
 * Revision 1.6  2007/11/05 18:01:27  dloureir
 * Adding a join for the diet directories initialization
 *
 * Revision 1.5  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.4  2007/07/06 13:51:52  dloureir
 * Correction of a bug : if the configuration cannot be written (on Grid 5000 for example) the installer will now not let the user go further.
 *
 * Revision 1.3  2007/07/06 13:34:26  dloureir
 * Correcting the way the xml files in the .diet directory are created by the installer
 *
 * Revision 1.2  2007/07/06 12:50:48  dloureir
 * New version for the use of the new variable management in the DIET_DashBoard
 *
 * Revision 1.4  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.3  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.2  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.1  2007/02/22 14:11:36  aamar
 * Initial revision.
 *
 ****************************************************************************/
package com.izforge.izpack.panels.diet_dashboard_utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.panels.DIETDashBoardInstallationPanel;
import com.izforge.izpack.panels.g5k_utils.G5kCfg;
import com.izforge.izpack.panels.g5k_utils.G5kSite;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * G5K utility class
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class G5kUtil {
	
	WaitingFrame waitFrame = null;
	
	private InstallerFrame parentPanel = null;
	
	private DIETDashBoardInstallationPanel dietinstallationPanel = null;
    /**
     * Create the .diet directory in home.
     *
     * @param conn the ssh connection
     */
    private void initDietDir(){
    	System.out.println("Starting the initialization of the Remote DIET architecture");
        waitFrame = new WaitingFrame(DIETDashBoardInstallationPanel.getText(DIETDashBoardInstallationPanel.WAITING_FRAME_TITLE),DIETDashBoardInstallationPanel.getText(DIETDashBoardInstallationPanel.WAITING_FRAME_DESCRIPTION_TEXT) + " ...",G5kSite.getSitesNumber()+1,true);
        System.out.println("Creation of the Frame");
        waitFrame.launch(DIETDashBoardInstallationPanel.getText(DIETDashBoardInstallationPanel.WAITING_FRAME_CONNECTION_TEXT));
        System.out.println("SDisplaying the frame");
    	System.out.println("starting the intialization of the DIET directories");
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("host", G5kCfg.get(G5kCfg.PREFERREDACCESPOINT));
        properties.put("username", G5kCfg.get(G5kCfg.USERNAME));
        properties.put("publickey",G5kCfg.get(G5kCfg.SSHKEYFILE));
        properties.put("passphrase", G5kCfg.getSSHKey());
        try{
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
            	System.out.println("Authentication succeed");
                //JOptionPane.showMessageDialog(null, "Authentication succeed");
            }

            /* Create a session */

            Session sess = conn.openSession();

           sess.requestPTY("toto",
                    80, 80,
                    16, 16,
                    null);

            sess.startShell();
            sess.close();
            //JOptionPane.showMessageDialog(null, "Connection succeed");
            waitFrame.incrementProgressBar();
            System.out.println("Starting remote architecture creation");
            // Create the .diet/bin directory if doesn't exist
            for(int i = 0 ; i < G5kSite.getSitesNumber(); i ++){
                sess = conn.openSession();
                System.out.println("Session open on : " + G5kSite.getSiteForIndex(i));
                waitFrame.setStatusText(DIETDashBoardInstallationPanel.getText(DIETDashBoardInstallationPanel.WAITING_FRAME_WORKING_ON_SITE) + G5kSite.getSiteForIndex(i) + " ...");
                System.out.println("Creating remote architecture ...");
                sess.execCommand("ssh "+G5kSite.getInternalFrontals()[i]+" \"mkdir -p $HOME/.diet/bin ; mkdir -p $HOME/.diet/scratch \"");
                System.out.println("Creating remote architecture ... DONE");
                Thread.sleep(500);
                System.out.println("Creating remote architecture ... DONE + 500");
                waitFrame.incrementProgressBar();
                sess.close();
                System.out.println("Session close on : " + G5kSite.getSiteForIndex(i));
            }
            //!sess.close();
            System.out.println("Ending remote architecture creation");
            parentPanel.unlockNextButton();
            dietinstallationPanel.setValidated(true);
        }
        catch (Exception e) {
            System.err.println("Connection failed : " + e.getMessage());
            e.printStackTrace();
            dietinstallationPanel.setValidated(false);
        }
        waitFrame.dispose();
    } // end void initDietDir
    
//    public void initializeRemoteDiet(InstallerFrame aPanel,DIETDashBoardInstallationPanel anOtherPanel){
//    	parentPanel = aPanel;
//    	dietinstallationPanel = anOtherPanel;
//    	System.out.println("Starting the initialization");
//    	Thread th = new Thread(new Runnable(){
//    		public void run(){
//    			System.out.println("running the initialization");
//    			initDietDir();
//    		}
//    	});
//    	th.start();
//    	try{th.join();}catch(Exception e){e.printStackTrace();}
//    }

}