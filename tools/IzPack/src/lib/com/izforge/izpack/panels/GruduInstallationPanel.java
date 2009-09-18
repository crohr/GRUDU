/**
 *
 */
package com.izforge.izpack.panels;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.installer.IzPanel;
import com.izforge.izpack.panels.grudu_utils.WaitingFrame;
import com.izforge.izpack.panels.g5k_utils.Config;
import com.izforge.izpack.panels.g5k_utils.G5kCfg;
import com.izforge.izpack.panels.g5k_utils.G5kSite;
import com.izforge.izpack.panels.grudu_utils.G5kCfgTabPane;



/**
 * @author david
 *
 */
public class GruduInstallationPanel extends IzPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private G5kCfgTabPane myTabPane;
    private JButton jButton = null;
    private boolean isValidated = false;
    public static HashMap<String, String> langpackHashMap = null;

    public static String PROCEED_CONFIGURATION = "GruduInstallationPanel.proceed_configuration";
    public static String KADEPLOY_PARTITION = "GruduInstallationPanel.kadeploy_partition";
    public static String NONE = "GruduInstallationPanel.none";
    public static String USER_CONFIGURATION = "GruduInstallationPanel.user_configuration";
    public static String MISC = "GruduInstallationPanel.misc";
    public static String USERNAME = "GruduInstallationPanel.username";
    public static String SSH_KEY_FILE = "GruduInstallationPanel.ssh_key_file";
    public static String PASSPHRASE = "GruduInstallationPanel.passphrase";
    public static String PREFERRED_ACCES_POINT = "GruduInstallationPanel.preferred_acces_point";
    public static String SSH_KEY_FILE_TOOLTIP = "GruduInstallationPanel.ssh_key_file_tooltip";
    public static String BROWSE = "GruduInstallationPanel.browse";
    public static String WAITING_FRAME_TITLE = "GruduInstallationPanel.waiting_frame_title";
    public static String WAITING_FRAME_DESCRIPTION_TEXT = "GruduInstallationPanel.waiting_frame_description_text";
    public static String WAITING_FRAME_CONNECTION_TEXT = "GruduInstallationPanel.waiting_frame_connection_text";
    public static String WAITING_FRAME_WORKING_ON_SITE = "GruduInstallationPanel.working_on_site";


    private void initializeLangPackHashMap(){
        langpackHashMap = new HashMap<String, String>();
        langpackHashMap.put(PROCEED_CONFIGURATION, parent.langpack.getString(PROCEED_CONFIGURATION)==PROCEED_CONFIGURATION?"Write configuration":parent.langpack.getString(PROCEED_CONFIGURATION));
        langpackHashMap.put(KADEPLOY_PARTITION,parent.langpack.getString(KADEPLOY_PARTITION)==KADEPLOY_PARTITION?"Kadeploy partition":parent.langpack.getString(KADEPLOY_PARTITION));
        langpackHashMap.put(NONE,parent.langpack.getString(NONE)==NONE?"None":parent.langpack.getString(NONE));
        langpackHashMap.put(USER_CONFIGURATION,parent.langpack.getString(USER_CONFIGURATION)==USER_CONFIGURATION?"User configuration":parent.langpack.getString(USER_CONFIGURATION));
        langpackHashMap.put(MISC,parent.langpack.getString(MISC)==MISC?"Misc":parent.langpack.getString(MISC));
        langpackHashMap.put(USERNAME,parent.langpack.getString(USERNAME)==USERNAME?"Username":parent.langpack.getString(USERNAME));
        langpackHashMap.put(SSH_KEY_FILE,parent.langpack.getString(SSH_KEY_FILE)==SSH_KEY_FILE?"Private SSH Key File":parent.langpack.getString(SSH_KEY_FILE));
        langpackHashMap.put(PASSPHRASE,parent.langpack.getString(PASSPHRASE)==PASSPHRASE?"Passphrase":parent.langpack.getString(PASSPHRASE));
        langpackHashMap.put(PREFERRED_ACCES_POINT,parent.langpack.getString(PREFERRED_ACCES_POINT)==PREFERRED_ACCES_POINT?"Preferred acces point":parent.langpack.getString(PREFERRED_ACCES_POINT));
        langpackHashMap.put(SSH_KEY_FILE_TOOLTIP,parent.langpack.getString(SSH_KEY_FILE_TOOLTIP)==SSH_KEY_FILE_TOOLTIP?"The path must be an absolute path":parent.langpack.getString(SSH_KEY_FILE_TOOLTIP));
        langpackHashMap.put(BROWSE,parent.langpack.getString(BROWSE)==BROWSE?"Browse":parent.langpack.getString(BROWSE));
        langpackHashMap.put(WAITING_FRAME_TITLE,parent.langpack.getString(WAITING_FRAME_TITLE)==WAITING_FRAME_TITLE?"Remote diet directories creation": parent.langpack.getString(WAITING_FRAME_TITLE));
        langpackHashMap.put(WAITING_FRAME_DESCRIPTION_TEXT,parent.langpack.getString(WAITING_FRAME_DESCRIPTION_TEXT)==WAITING_FRAME_DESCRIPTION_TEXT?"Installing necessary files on Grid5000 frontals":parent.langpack.getString(WAITING_FRAME_DESCRIPTION_TEXT));
        langpackHashMap.put(WAITING_FRAME_CONNECTION_TEXT, parent.langpack.getString(WAITING_FRAME_CONNECTION_TEXT)==WAITING_FRAME_CONNECTION_TEXT?"Opening connection":parent.langpack.getString(WAITING_FRAME_CONNECTION_TEXT));
        langpackHashMap.put(WAITING_FRAME_WORKING_ON_SITE, parent.langpack.getString(WAITING_FRAME_WORKING_ON_SITE)==WAITING_FRAME_WORKING_ON_SITE?"Working on site": parent.langpack.getString(WAITING_FRAME_WORKING_ON_SITE));
    }

    public static String getText(String key){
        return langpackHashMap.get(key);
    }

    /**
     * @param parent
     * @param idata
     */
    public GruduInstallationPanel(InstallerFrame parent, InstallData idata) {
        super(parent, idata, new IzPanelLayout());
        initializeLangPackHashMap();
        // TODO Auto-generated constructor stub
    }

    public boolean isValidated(){
        if(isValidated) parent.unlockNextButton();
        else parent.lockNextButton();
        return isValidated;
    }

    public void panelActivate(){

        parent.lockNextButton();
        File dietdirectory = new File(System.getProperty("user.home")+"/.diet");
        dietdirectory.mkdirs();
        File dietBin = new File(dietdirectory.getAbsolutePath() + "/bin");
        dietBin.mkdirs();
        File dietScratch = new File(dietdirectory.getAbsolutePath() + "/scratch");
        dietScratch.mkdirs();
        File dietLog = new File(dietdirectory.getAbsolutePath() + "/log");
        dietLog.mkdirs();
        G5kCfg.initCfg();
        Config.init();
        //setLayout(new BorderLayout());
        this.myTabPane = new G5kCfgTabPane();
        this.add(this.myTabPane,NEXT_LINE);
        this.add(this.getJButton(),NEXT_LINE);
        // At end of layouting we should call the completeLayout method also they do nothing.
        getLayoutHelper().completeLayout();
    }

    private JButton getJButton() {
        if (jButton == null) {
            try {
                //ImageIcon icon = new ImageIcon(this.getClass().getResource("run.png"));
                //if(icon==null) jButton = new JButton("Ok");
                //else{
                    jButton = new JButton(getString(PROCEED_CONFIGURATION));
                //}
                jButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        myTabPane.apply();
    					SwingUtilities.invokeLater(new Runnable(){
    						public void run(){
    							Thread th = new Thread(new Runnable(){
    								public void run(){
    									myTabPane.apply();
    			    					System.out.println("Starting the initialization of the Remote DIET architecture");
    			    			        WaitingFrame waitFrame = new WaitingFrame(GruduInstallationPanel.getText(GruduInstallationPanel.WAITING_FRAME_TITLE),GruduInstallationPanel.getText(GruduInstallationPanel.WAITING_FRAME_DESCRIPTION_TEXT) + " ...",G5kSite.getSitesNumber()+1,true);
    			    			        System.out.println("Creation of the Frame");
    			    			        waitFrame.launch(GruduInstallationPanel.getText(GruduInstallationPanel.WAITING_FRAME_CONNECTION_TEXT));
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
    			    			                waitFrame.setStatusText(GruduInstallationPanel.getText(GruduInstallationPanel.WAITING_FRAME_WORKING_ON_SITE) + G5kSite.getSiteForIndex(i) + " ...");
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
    			    			            parent.unlockNextButton();
    			    			            isValidated = true;
    			    			        }
    			    			        catch (Exception ex) {
    			    			            System.err.println("Connection failed : " + ex.getMessage());
    			    			            ex.printStackTrace();
    			    			            isValidated = false;
    			    			        }
    			    			        waitFrame.dispose();
    								}
    							});
    							th.start();
    						}
    					});
                    }
                });
            } catch (java.lang.Throwable e) {
                e.printStackTrace();
            }
        }
        return jButton;
    }

}
