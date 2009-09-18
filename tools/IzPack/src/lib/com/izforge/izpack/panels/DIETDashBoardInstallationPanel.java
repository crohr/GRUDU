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
import com.izforge.izpack.panels.g5k_utils.Config;
import com.izforge.izpack.panels.g5k_utils.G5kCfg;
import com.izforge.izpack.panels.g5k_utils.G5kSite;
import com.izforge.izpack.panels.diet_dashboard_utils.G5kCfgTabPane;
import com.izforge.izpack.panels.diet_dashboard_utils.G5kUtil;
import com.izforge.izpack.panels.diet_dashboard_utils.WaitingFrame;



/**
 * @author david
 *
 */
public class DIETDashBoardInstallationPanel extends IzPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private G5kCfgTabPane myTabPane;
    private JButton jButton = null;
    private boolean isValidated = false;

    public static HashMap<String, String> langpackHashMap = null;

    public static String PROCEED_CONFIGURATION = "DIETDashBoardInstallationPanel.proceed_configuration";
    public static String KADEPLOY_PARTITION = "DIETDashBoardInstallationPanel.kadeploy_partition";
    public static String NONE = "DIETDashBoardInstallationPanel.none";
    public static String USER_CONFIGURATION = "DIETDashBoardInstallationPanel.user_configuration";
    public static String MISC = "DIETDashBoardInstallationPanel.misc";
    public static String USERNAME = "DIETDashBoardInstallationPanel.username";
    public static String SSH_KEY_FILE = "DIETDashBoardInstallationPanel.ssh_key_file";
    public static String PASSPHRASE = "DIETDashBoardInstallationPanel.passphrase";
    public static String PREFERRED_ACCES_POINT = "DIETDashBoardInstallationPanel.preferred_acces_point";
    public static String SSH_KEY_FILE_TOOLTIP = "DIETDashBoardInstallationPanel.ssh_key_file_tooltip";
    public static String BROWSE = "DIETDashBoardInstallationPanel.browse";
    public static String WAITING_FRAME_TITLE = "DIETDashBoardInstallationPanel.waiting_frame_title";
    public static String WAITING_FRAME_DESCRIPTION_TEXT = "DIETDashBoardInstallationPanel.waiting_frame_description_text";
    public static String WAITING_FRAME_CONNECTION_TEXT = "DIETDashBoardInstallationPanel.waiting_frame_connection_text";
    public static String WAITING_FRAME_WORKING_ON_SITE = "DIETDashBoardInstallationPanel.working_on_site";
    public static String GODIET_RUNTIME_SCRATCH = "DIETDashBoardInstallationPanel.godiet_runtime_scratch";
    public static String IMPORT = "DIETDashBoardInstallationPanel.import";
    public static String IMPORT_TOOLTIP = "DIETDashBoardInstallationPanel.import_tooltip";
    public static String ERROR_SITE_SELECTION_MESSAGE = "DIETDashBoardInstallationPanel.error_site_selection_message";
    public static String GODIET_SCRATCH = "DIETDashBoardInstallationPanel.godiet_scratch";
    public static String UNIQUE_DIRS = "DIETDashBoardInstallationPanel.unique_dirs";
    public static String SAVE_STDERR = "DIETDashBoardInstallationPanel.save_stderr";
    public static String SAVE_STDOUT = "DIETDashBoardInstallationPanel.save_stdout";
    public static String DEBUG_LEVEL = "DIETDashBoardInstallationPanel.debug_level";

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
        langpackHashMap.put(WAITING_FRAME_TITLE,parent.langpack.getString(WAITING_FRAME_TITLE)==WAITING_FRAME_TITLE?"Remote diet directories creation":parent.langpack.getString(WAITING_FRAME_TITLE));
        langpackHashMap.put(WAITING_FRAME_DESCRIPTION_TEXT,parent.langpack.getString(WAITING_FRAME_DESCRIPTION_TEXT)==WAITING_FRAME_DESCRIPTION_TEXT?"Installing necessary files on Grid5000 frontals":parent.langpack.getString(WAITING_FRAME_DESCRIPTION_TEXT));
        langpackHashMap.put(WAITING_FRAME_CONNECTION_TEXT,parent.langpack.getString(WAITING_FRAME_CONNECTION_TEXT)==WAITING_FRAME_CONNECTION_TEXT?"Opening connection":parent.langpack.getString(WAITING_FRAME_CONNECTION_TEXT));
        langpackHashMap.put(WAITING_FRAME_WORKING_ON_SITE,parent.langpack.getString(WAITING_FRAME_WORKING_ON_SITE)==WAITING_FRAME_WORKING_ON_SITE?"Working on site":parent.langpack.getString(WAITING_FRAME_WORKING_ON_SITE));
        langpackHashMap.put(GODIET_RUNTIME_SCRATCH,parent.langpack.getString(GODIET_RUNTIME_SCRATCH)==GODIET_RUNTIME_SCRATCH?"GoDIET runtime scratch":parent.langpack.getString(GODIET_RUNTIME_SCRATCH));
        langpackHashMap.put(IMPORT,parent.langpack.getString(IMPORT)==IMPORT?"import":parent.langpack.getString(IMPORT));
        langpackHashMap.put(IMPORT_TOOLTIP,parent.langpack.getString(IMPORT_TOOLTIP)==IMPORT_TOOLTIP?"Import the settings of an other site":parent.langpack.getString(IMPORT_TOOLTIP));
        langpackHashMap.put(ERROR_SITE_SELECTION_MESSAGE,parent.langpack.getString(ERROR_SITE_SELECTION_MESSAGE)==ERROR_SITE_SELECTION_MESSAGE?"No site selected or the selected site was not found":parent.langpack.getString(ERROR_SITE_SELECTION_MESSAGE));
        langpackHashMap.put(GODIET_SCRATCH,parent.langpack.getString(GODIET_SCRATCH)==GODIET_SCRATCH?"GoDIEt scratch":parent.langpack.getString(GODIET_SCRATCH));
        langpackHashMap.put(UNIQUE_DIRS,parent.langpack.getString(UNIQUE_DIRS)==UNIQUE_DIRS?"Use unique dirs":parent.langpack.getString(UNIQUE_DIRS));
        langpackHashMap.put(SAVE_STDERR,parent.langpack.getString(SAVE_STDERR)==SAVE_STDERR?"Save stdErr":parent.langpack.getString(SAVE_STDERR));
        langpackHashMap.put(SAVE_STDOUT,parent.langpack.getString(SAVE_STDOUT)==SAVE_STDOUT?"Save stdOut":parent.langpack.getString(SAVE_STDOUT));
        langpackHashMap.put(DEBUG_LEVEL,parent.langpack.getString(DEBUG_LEVEL)==DEBUG_LEVEL?"Debug level":parent.langpack.getString(DEBUG_LEVEL));
    }


    public static String getText(String key){
    	String value = langpackHashMap.get(key);
        return value;
    }

    /**
     * @param parent
     * @param idata
     */
    public DIETDashBoardInstallationPanel(InstallerFrame parent, InstallData idata) {
        super(parent, idata, new IzPanelLayout(FILL_OUT_COLUMN_WIDTH));
        initializeLangPackHashMap();
        //setLayout(new BorderLayout());
        this.myTabPane = new G5kCfgTabPane();
        this.add(this.myTabPane,NEXT_LINE);
        this.add(this.getJButton(),NEXT_LINE);
        // At end of layouting we should call the completeLayout method also they do nothing.
        getLayoutHelper().completeLayout();

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
        myTabPane.fillValues();
    }

    private JButton getJButton() {
    	if (jButton == null) {
    		try {
    			//ImageIcon icon = new ImageIcon(this.getClass().getResource("run.png"));
    			//if(icon==null) jButton = new JButton("Ok");
    			//else{
    			jButton = new JButton(getText(PROCEED_CONFIGURATION));
    			//}
    			jButton.addActionListener(new ActionListener(){
    				public void actionPerformed(ActionEvent e){
    					SwingUtilities.invokeLater(new Runnable(){
    						public void run(){
    							Thread th = new Thread(new Runnable(){
    								public void run(){
    									myTabPane.apply();
    			    					System.out.println("Starting the initialization of the Remote DIET architecture");
    			    			        WaitingFrame waitFrame = new WaitingFrame(DIETDashBoardInstallationPanel.getText(DIETDashBoardInstallationPanel.WAITING_FRAME_TITLE),DIETDashBoardInstallationPanel.getText(DIETDashBoardInstallationPanel.WAITING_FRAME_DESCRIPTION_TEXT) + " ...",G5kSite.getSitesNumber()+1,true);
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
    					
    					//new G5kUtil().initializeRemoteDiet(parent,DIETDashBoardInstallationPanel.this);
    				}
    			});
    		} catch (java.lang.Throwable e) {
    			e.printStackTrace();
    		}
    	}
        return jButton;
    }
    public void setValidated(boolean validation){
    	isValidated = validation;
    }

}
