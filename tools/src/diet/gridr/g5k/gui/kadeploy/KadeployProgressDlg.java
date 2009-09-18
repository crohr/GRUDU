/****************************************************************************/
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KadeployProgressDlg.java,v 1.16 2007/11/19 15:16:20 dloureir Exp $
 * $Log: KadeployProgressDlg.java,v $
 * Revision 1.16  2007/11/19 15:16:20  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.15  2007/11/13 02:58:24  dloureir
 * Bug correction
 *
 * Revision 1.14  2007/11/12 20:16:13  dloureir
 * Adding the bug correction of the JFTP : The interface was blocked when synchronizing something on Grid'5000
 *
 * Revision 1.13  2007/11/07 11:35:01  dloureir
 * Some printlns are passed to the LoggingManager
 *
 * Revision 1.12  2007/09/28 16:03:02  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.11  2007/07/12 15:26:35  dloureir
 * Some typo correction and code cleanup
 *
 * Revision 1.10  2007/04/02 09:53:53  dloureir
 * Removing unused printouts and adding some logging
 *
 * Revision 1.9  2007/04/02 07:37:45  dloureir
 * Correcting a bug concerning the deployment on Bordeaux through the use of KaInfo
 *
 * Revision 1.8  2007/03/14 14:15:35  dloureir
 * Adding  a todo concerning the special behaviours for some sites
 *
 * Revision 1.7  2007/03/14 14:12:49  dloureir
 * Adding some special behaviours for Bordeaux, Orsay, and Lille
 *
 * Revision 1.6  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.5  2007/03/07 15:00:13  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.4  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.3  2007/02/27 13:38:14  dloureir
 * Adding logging to the kadeploy module
 *
 * Revision 1.2  2007/02/22 15:47:12  aamar
 * Changing long textarea and correcting bug with progression status.
 *
 * Revision 1.1  2007/02/22 15:11:55  aamar
 * .
 *
 ****************************************************************************/
package diet.gridr.g5k.gui.kadeploy;

import java.util.*;
import java.util.logging.Level;

import javax.swing.*;
import java.awt.*;

import java.io.*;

import com.trilead.ssh2.*;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;


/**
 * The progress dialog. This dialog window displays the allocated hosts, and
 * for each host a progress bar to show the deployment status
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 * TODO the special behaviours for Bordeaux, and Lille should be suppressed
 * for a better common solution for each site
 */

public class KadeployProgressDlg extends JFrame {

    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 342221661749004983L;

    /**
     * array containing the different boot steps of a deployment
     */
    public static final String[] BootSteps = {
        "<BootInit 0>",
        "<BootInit",
        "<PreInstall>",
        "<Transfert>",
        "<tar Transfert>",
        "<PostInstall>",
        "<BootEnv 0>",
        "<BootEnv",
        "<Completed>"
    };

    /**
     * the deployments to do
     */
    private Vector<KadeployDeployment>  myDeployments;

    /**
     * Default constructor
     *
     * @param parent parent frame
     * @param deployments the deployments to operate
     */
    public KadeployProgressDlg(KadeployDlg parent, Vector<KadeployDeployment> deployments) {
        super("Kadeploy progression");
        this.myDeployments = deployments;
        DeploymentProgression [] deploymentProgs = new DeploymentProgression[deployments.size()];
        for (int ix=0; ix<this.myDeployments.size(); ix++) {
            deploymentProgs[ix] = new DeploymentProgression(this.myDeployments.get(ix).getName(),
                    this.myDeployments.get(ix).getCluster());
            String [] hosts = this.myDeployments.get(ix).getHosts();
            for (int jx=0; jx<hosts.length; jx++) {
                deploymentProgs[ix].addHost(hosts[jx]);
            } // end for jx
        } // end for ix
        JScrollPane pane = new JScrollPane(new DeployProgressPanel(deploymentProgs));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pane, BorderLayout.CENTER);
        pack();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "KadeployProgressDlg", "KadeployProgressDlg Frame launched");
    }

    /**
     * Class representing the panel for the deployment progression
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     *
     */
    public class DeployProgressPanel extends JPanel implements KadeployListener {

        /**
         * Serial version ID
         */
        private static final long serialVersionUID = 5451852263512758244L;

        /**
         * Deployments map
         */
        private HashMap<String, DeploymentProgressionPanel> myDeployments;

        /**
         * Log panels map
         */
        private HashMap<String, LogPanel> myLogPanels;

        /**
         * Thread used to deploy
         */
        private HashMap<String, SimulThread> myThreads;

        /**
         * Default constructor
         *
         * @param deployments array of deployment progressions
         */
        public DeployProgressPanel(DeploymentProgression [] deployments) {
            // TO DO
            // Several solutions
            // 1. GridLayout (2xn, 3xn, 4xn, ..)
            // 2. GridBag or Box Layout
            // 3. Tabbed dialog
            // 4. Unique log
            // *******
            // Tabbed dialog
            // Main Tab : progression bars
            // Secondary tabs : log (out & err output)
            // *******
            this.myDeployments = new HashMap<String, DeploymentProgressionPanel>();
            this.myLogPanels   = new HashMap<String, LogPanel>();
            this.myThreads = new HashMap<String, SimulThread>();
            setLayout(new BorderLayout());
            JPanel progressBarPanel = new JPanel();
            progressBarPanel.setLayout(new BoxLayout(progressBarPanel, BoxLayout.Y_AXIS));
            JTabbedPane tabPane = new JTabbedPane();
            for (int i=0; i<deployments.length; i++){
                if (deployments[i].isToDeploy()) {
                    DeploymentProgressionPanel panel = new DeploymentProgressionPanel(deployments[i]);
                    progressBarPanel.add(panel);
                    this.myDeployments.put(deployments[i].getName(), panel);
                    LogPanel logPanel = new LogPanel(deployments[i].getName());
                    this.myLogPanels.put(deployments[i].getName(), logPanel);
                } // end if isToDeploy
            } // end for i
            JScrollPane pane = new JScrollPane(progressBarPanel);
            tabPane.add("Deployment progression", pane);
            Iterator<LogPanel> p = this.myLogPanels.values().iterator();
            while (p.hasNext()) {
                LogPanel log = p.next();
                JScrollPane pane2 = new JScrollPane(log);
                tabPane.add(log.getName(), pane2);
            } // end while hasNext

            add(tabPane, BorderLayout.CENTER);

            // Create the threads
            for (int i=0; i<deployments.length; i++) {
                if (deployments[i].isToDeploy()) {
                    SimulThread thread = new SimulThread(deployments[i].getName(),
                            deployments[i].getCluster(),
                            deployments[i].getHosts());
                    thread.start();
                    this.myThreads.put(deployments[i].getName(), thread);
                }
            } // end for
        }

        /**
         * Method adding an error line for a certain deployment
         *
         * @param deploymentName name of the deployment
         * @param line error line to add
         */
        public void addError(String deploymentName, String line) {
            LogPanel log = this.myLogPanels.get(deploymentName);
            if (log == null) {
                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "addError", deploymentName + " not found!!!");
                return;
            }
            log.addError(line);
        }

        /**
         * Method adding an output line for a certain deployment
         *
         * @param deploymentName name of the deployment
         * @param line output line to add
         */
        public void addOutput(String deploymentName, String line) {
            LogPanel log = this.myLogPanels.get(deploymentName);
            if (log == null) {
                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "addOutput", deploymentName + " not found!!!");
                return;
            }
            log.addOutput(line);
        }

        /**
         * Method setting the parameter node as done
         *
         * @param node the node to set as done
         */
        public void setAsDone(String node) {
            // TODO Auto-generated method stub

        }

        /**
         * Method setting the progression of a certain deployment to a certain step
         *
         * @param deploymentName name of the deployment
         * @param step step of the progression
         */
        public void setProgression(String deploymentName, int step) {
            DeploymentProgressionPanel panel = this.myDeployments.get(deploymentName);
            if (panel == null) {
                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression",deploymentName + " not found!!!");
                return;
            }
            panel.getProgressBar().setValue(step+1);
            if (step < BootSteps.length)
                panel.getProgressBar().setString(BootSteps[step]);
            if (step >= BootSteps.length) {
                SimulThread thread = this.myThreads.get(deploymentName);
                if (thread != null) {
                    LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression","The thread " + deploymentName + " will be interrupted");
                    thread.interrupt();
                }
            }
        }

        /**
         * Thread used for the simulation
         *
         * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
         *
         */
        public class SimulThread extends Thread {
            /**
             * deployment name
             */
            private String myDeploymentName;
            /**
             * cluster name
             */
            private int myCluster;
            /**
             * hosts of the deployment
             */
            private String [] myHosts;
            /**
             * Default constructor
             *
             * @param deploymentName deployment name
             * @param cluster cluster index
             * @param hosts hosts of the deployment
             */
            public SimulThread(String deploymentName, int cluster, String [] hosts) {
                this.myDeploymentName = deploymentName;
                this.myCluster = cluster;
                this.myHosts = hosts;
            }

            /**
             * method launched when the thread is started
             */
            public void run() {
                // Create the deployment file with hosts
                String hostsFileName = System.getProperty("user.home") + "/.diet/scratch/" + 
                    this.myDeploymentName;
                File hostsFile = new File(hostsFileName);
                try {
                    FileWriter writer = new FileWriter(hostsFile);
                    for (int ix=0; ix<myHosts.length; ix++) {
                        writer.write(this.myHosts[ix] + Config.newline);
                    }
                    writer.close();
                }
                catch (IOException e) {
                    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression","Error while creating the hosts files " + hostsFile);
                    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression",e);
                    return;
                }
                LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "Writing the file containing the nodes for the deployment : " + hostsFile);
                try {
                    SCPClient scpClt = new SCPClient(KadeployDlg.getConnection());
                    
                    scpClt.put(hostsFile.getAbsolutePath(), "$HOME/.diet/");
                    
                    LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, 
                            this.getClass().getName(), "run", "putting the file on the remote machine");
                    Session sess = KadeployDlg.getConnection().openSession();
                    sess.execCommand("chmod a+r $HOME/.diet/" + this.myDeploymentName);
                    sess.close();

                    sess = KadeployDlg.getConnection().openSession();
                    sess.execCommand("scp $HOME/.diet/" + this.myDeploymentName +
                            " " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
                            ":.diet");
                    sess.close();

                    sess = KadeployDlg.getConnection().openSession();

//                    if(this.myCluster == G5kSite.siteBordeaux){
//
//                        String cmd = "ssh " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                            " \"chmod 777 .diet && kainfo -f $HOME/.diet/" + this.myDeploymentName + "\"";
//                        sess.execCommand(cmd);
//                        // -----------
//                        InputStream stdout = new StreamGobbler(sess.getStdout());
//                        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
//                        sess.getStdin();
//                        while (true) {
//                            String line = br.readLine();
//                            if (line == null)
//                                break;
//                            addOutput(this.myDeploymentName, line);
//                            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "output : " + line);
//                        } // end while
//                        br.close();
//                        stdout.close();
//                        InputStream stderr = new StreamGobbler(sess.getStderr());
//                        br = new BufferedReader(new InputStreamReader(stderr));
//                        while (true) {
//                            String line = br.readLine();
//                            if (line == null)
//                                break;
//                            addError(this.myDeploymentName, line);
//                        } // end while
//                        br.close();
//                        stderr.close();
//                        sess.close();
//                        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "KaInfo executed on Bordeaux. Right for nodes retrieved");
//                        sess = KadeployDlg.getConnection().openSession();
//
//                        cmd = "ssh " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                            " ls " + "$HOME/.diet/" +this.myDeploymentName + "_?da3";
//                        sess.execCommand(cmd);
//                        stdout = new StreamGobbler(sess.getStdout());
//                        br = new BufferedReader(new InputStreamReader(stdout));
//                        sess.getStdin();
//                        ArrayList<String> lineList = new ArrayList<String>();
//                        while (true) {
//                            String line = br.readLine();
//                            if (line == null)
//                                break;
//                            lineList.add(line);
//                        }
//                        String[] files = new String[lineList.size()];
//                        for(int i = 0 ; i < files.length ; i ++){
//                            files[i] = lineList.get(i);
//                            addOutput(this.myDeploymentName, files[i]);
//                            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "output : " + files[i]);
//
//                        }
//                        br.close();
//                        stdout.close();
//                        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "Files containing the nodes where the user as the rights to deploy");
//                        String xda = null;
//                        int index = 0;
//                        for(int ii = 0 ; ii < lineList.size() ; ii ++){
//
//                            index = files[ii].indexOf(this.myDeploymentName);
//                            xda = files[ii].substring(index+ this.myDeploymentName.length()+1);
//
//                            // -- We must know what are the nodes for this partition to deploy
//                            sess.close();
//
//                            sess = KadeployDlg.getConnection().openSession();
//
//                            cmd = "ssh " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                                " cat " + "$HOME/.diet/" +this.myDeploymentName + "_"+xda;
//                            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "  ** " + cmd);
//                            sess.execCommand(cmd);
//                            stdout = new StreamGobbler(sess.getStdout());
//                            br = new BufferedReader(new InputStreamReader(stdout));
//                            sess.getStdin();
//                            ArrayList<String> lineListTemp = new ArrayList<String>();
//                            while (true) {
//                                String line = br.readLine();
//                                if (line == null)
//                                    break;
//                                lineListTemp.add(line);
//                            }
//                            br.close();
//                            stdout.close();
//                            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "Nodes for "+this.myDeploymentName + "_"+xda+" retrieved");
//                            // -- we sort out the hosts for this partition
//                            ArrayList<String> hostsForThisPartition = new ArrayList<String>();
//                            for(int i1 = 0   ; i1 < myHosts.length ; i1 ++){
//                                for(int i2 = 0 ; i2 < lineListTemp.size() ; i2 ++){
//                                    if(myHosts[i1].equalsIgnoreCase(lineListTemp.get(i2))){
//                                        hostsForThisPartition.add(myHosts[i1]);
//                                        break;
//                                    }
//                                }
//                            }
//                            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "List containing the nodes for this partition : " + xda);
//                            // -- we write the new file for the deployment
//                            try {
//                                FileWriter writer = new FileWriter(hostsFile);
//                                for (int ix=0; ix<hostsForThisPartition.size(); ix++) {
//                                    writer.write(hostsForThisPartition.get(ix) + Config.newline);
//                                }
//                                writer.close();
//                            }
//                            catch (IOException e) {
//                                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression","Error while creating the hosts files " + hostsFile);
//                                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression",e);
//                                return;
//                            }
//                            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "File of nodes written");
//                            scpClt = new SCPClient(KadeployDlg.getConnection());
//                            scpClt.put(hostsFile.getAbsolutePath(), "$HOME/.diet");
//
//                            sess = KadeployDlg.getConnection().openSession();
//                            sess.execCommand("chmod a+r $HOME/.diet/" + this.myDeploymentName);
//                            sess.close();
//
//                            sess = KadeployDlg.getConnection().openSession();
//                            sess.execCommand("scp $HOME/.diet/" + this.myDeploymentName +
//                                    " " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                            ":.diet");
//                            sess.close();
//                            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "File of nodes send to the remote machine");
//                            // -- we realize the deployment
//                            cmd =
//                                "ssh " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                                " \"kadeploy -e " +
//                                KernelPanel.getSelectedKernel(this.myCluster) +
//                                " -f "  + "$HOME/.diet/" +
//                                "" + this.myDeploymentName +
//                                " -p " + xda + "\"";
//                            sess.close();
//
//                            sess = KadeployDlg.getConnection().openSession();
//
//                            sess.execCommand(cmd);
//                            stdout = new StreamGobbler(sess.getStdout());
//                            br = new BufferedReader(new InputStreamReader(stdout));
//                            sess.getStdin();
//                            setProgression(this.myDeploymentName, 0);
//                            while (true) {
//                                String line = br.readLine();
//                                if (line == null)
//                                    break;
//                                for (int i=0; i<BootSteps.length; i++) {
//                                    if (line.indexOf(BootSteps[i]) != -1) {
//                                        LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "### Step " + i);
//                                        setProgression(this.myDeploymentName, i);
//                                    } // end if startsWith
//                                } // end for
//                                addOutput(this.myDeploymentName, line);
//                                LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "output : " + line);
//                            } // end while
//                            br.close();
//                            stdout.close();
//                            stderr = new StreamGobbler(sess.getStderr());
//                            br = new BufferedReader(new InputStreamReader(stderr));
//                            while (true) {
//                                String line = br.readLine();
//                                if (line == null)
//                                    break;
//                                addError(this.myDeploymentName, line);
//                            } // end while
//                            br.close();
//                            stderr.close();
//
//                        }
//                        // -----------
//                    }
            //        else{ // Site != BORDEAUX !!
                        String cmd = null;
                        String xda = "";
                        if(!Config.getXDA(this.myCluster).equalsIgnoreCase("")) xda = " -p " + Config.getXDA(this.myCluster);
                        cmd =
                            "ssh -tt " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
                            " \"kadeploy -e " +
                            KernelPanel.getSelectedKernel(this.myCluster) +
                            //			" -m " + this.myHost +
                            " -f " + "$HOME/.diet/" +
                                    "" + this.myDeploymentName +
                            xda + "\"";
                        LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", " ** " + cmd);
                        sess.execCommand(cmd);
                        InputStream stdout = new StreamGobbler(sess.getStdout());
                        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                        sess.getStdin();
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            for (int i=0; i<BootSteps.length; i++) {
                                if (line.indexOf(BootSteps[i]) != -1) {
                                    LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "### Step " + i);
                                    setProgression(this.myDeploymentName, i);
                                } // end if startsWith
                            } // end for
                            addOutput(this.myDeploymentName, line);
                            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "output : " + line);
                        } // end while

                        InputStream stderr = new StreamGobbler(sess.getStderr());
                        br = new BufferedReader(new InputStreamReader(stderr));
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            addError(this.myDeploymentName, line);
                        } // end while

                        sess.close();
//                    }
                }
                catch (Exception e) {
                    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", e);
                }

            } // end run

            public void run2() {
                // Create the deployment file with hosts
                String hostsFileName = System.getProperty("user.home") + "/.diet/scratch/" + 
                    this.myDeploymentName;
                File hostsFile = new File(hostsFileName);
                try {
                    FileWriter writer = new FileWriter(hostsFile);
                    for (int ix=0; ix<myHosts.length; ix++) {
                        writer.write(this.myHosts[ix] + Config.newline);
                    }
                    writer.close();
                }
                catch (IOException e) {
                    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression","Error while creating the hosts files " + hostsFile);
                    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression",e);
                    return;
                }
                LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "Writing the file containing the nodes for the deployment : " + hostsFile);
                try {
                    SCPClient scpClt = new SCPClient(KadeployDlg.getConnection());
                    if (this.myCluster != G5kSite.siteLille){
                        scpClt.put(hostsFile.getAbsolutePath(), "$HOME/.diet");
                    }
                    else{
                        scpClt.put(hostsFile.getAbsolutePath(), "$HOME");
                    }
                    LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "putting the file on the remote machine");
                    Session sess = KadeployDlg.getConnection().openSession();
                    if (this.myCluster != G5kSite.siteLille) {
                        sess.execCommand("chmod a+r $HOME/.diet/" + this.myDeploymentName);
                    }else{
                        sess.execCommand("chmod a+r $HOME/" + this.myDeploymentName);
                    }
                    sess.close();

                    sess = KadeployDlg.getConnection().openSession();
                    if (this.myCluster != G5kSite.siteLille){
                        sess.execCommand("scp $HOME/.diet/" + this.myDeploymentName +
                            " " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
                    ":.diet");
                    }
                    else{
                        sess.execCommand("scp $HOME/" + this.myDeploymentName +
                                " " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
                        ":~");
                        }
                    sess.close();

                    sess = KadeployDlg.getConnection().openSession();

//                    if(this.myCluster == G5kSite.siteBordeaux){
//
//                        String cmd = "ssh " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                            " \"chmod 777 .diet && kainfo -f $HOME/.diet/" + this.myDeploymentName + "\"";
//                        sess.execCommand(cmd);
//                        // -----------
//                        InputStream stdout = new StreamGobbler(sess.getStdout());
//                        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
//                        sess.getStdin();
//                        while (true) {
//                            String line = br.readLine();
//                            if (line == null)
//                                break;
//                            addOutput(this.myDeploymentName, line);
//                            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "output : " + line);
//                        } // end while
//                        br.close();
//                        stdout.close();
//                        InputStream stderr = new StreamGobbler(sess.getStderr());
//                        br = new BufferedReader(new InputStreamReader(stderr));
//                        while (true) {
//                            String line = br.readLine();
//                            if (line == null)
//                                break;
//                            addError(this.myDeploymentName, line);
//                        } // end while
//                        br.close();
//                        stderr.close();
//                        sess.close();
//                        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "KaInfo executed on Bordeaux. Right for nodes retrieved");
//                        sess = KadeployDlg.getConnection().openSession();
//
//                        cmd = "ssh " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                            " ls " + "$HOME/.diet/" +this.myDeploymentName + "_?da3";
//                        sess.execCommand(cmd);
//                        stdout = new StreamGobbler(sess.getStdout());
//                        br = new BufferedReader(new InputStreamReader(stdout));
//                        sess.getStdin();
//                        ArrayList<String> lineList = new ArrayList<String>();
//                        while (true) {
//                            String line = br.readLine();
//                            if (line == null)
//                                break;
//                            lineList.add(line);
//                        }
//                        String[] files = new String[lineList.size()];
//                        for(int i = 0 ; i < files.length ; i ++){
//                            files[i] = lineList.get(i);
//                            addOutput(this.myDeploymentName, files[i]);
//                            LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "output : " + files[i]);
//
//                        }
//                        br.close();
//                        stdout.close();
//                        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "Files containing the nodes where the user as the rights to deploy");
//                        String xda = null;
//                        int index = 0;
//                        for(int ii = 0 ; ii < lineList.size() ; ii ++){
//
//                            index = files[ii].indexOf(this.myDeploymentName);
//                            xda = files[ii].substring(index+ this.myDeploymentName.length()+1);
//
//                            // -- We must know what are the nodes for this partition to deploy
//                            sess.close();
//
//                            sess = KadeployDlg.getConnection().openSession();
//
//                            cmd = "ssh " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                                " cat " + "$HOME/.diet/" +this.myDeploymentName + "_"+xda;
//                            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "  ** " + cmd);
//                            sess.execCommand(cmd);
//                            stdout = new StreamGobbler(sess.getStdout());
//                            br = new BufferedReader(new InputStreamReader(stdout));
//                            sess.getStdin();
//                            ArrayList<String> lineListTemp = new ArrayList<String>();
//                            while (true) {
//                                String line = br.readLine();
//                                if (line == null)
//                                    break;
//                                lineListTemp.add(line);
//                            }
//                            br.close();
//                            stdout.close();
//                            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "Nodes for "+this.myDeploymentName + "_"+xda+" retrieved");
//                            // -- we sort out the hosts for this partition
//                            ArrayList<String> hostsForThisPartition = new ArrayList<String>();
//                            for(int i1 = 0   ; i1 < myHosts.length ; i1 ++){
//                                for(int i2 = 0 ; i2 < lineListTemp.size() ; i2 ++){
//                                    if(myHosts[i1].equalsIgnoreCase(lineListTemp.get(i2))){
//                                        hostsForThisPartition.add(myHosts[i1]);
//                                        break;
//                                    }
//                                }
//                            }
//                            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "List containing the nodes for this partition : " + xda);
//                            // -- we write the new file for the deployment
//                            try {
//                                FileWriter writer = new FileWriter(hostsFile);
//                                for (int ix=0; ix<hostsForThisPartition.size(); ix++) {
//                                    writer.write(hostsForThisPartition.get(ix) + Config.newline);
//                                }
//                                writer.close();
//                            }
//                            catch (IOException e) {
//                                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression","Error while creating the hosts files " + hostsFile);
//                                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "setProgression",e);
//                                return;
//                            }
//                            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "File of nodes written");
//                            scpClt = new SCPClient(KadeployDlg.getConnection());
//                            scpClt.put(hostsFile.getAbsolutePath(), "$HOME/.diet");
//
//                            sess = KadeployDlg.getConnection().openSession();
//                            sess.execCommand("chmod a+r $HOME/.diet/" + this.myDeploymentName);
//                            sess.close();
//
//                            sess = KadeployDlg.getConnection().openSession();
//                            sess.execCommand("scp $HOME/.diet/" + this.myDeploymentName +
//                                    " " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                            ":.diet");
//                            sess.close();
//                            LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "File of nodes send to the remote machine");
//                            // -- we realize the deployment
//                            cmd =
//                                "ssh " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
//                                " \"kadeploy -e " +
//                                KernelPanel.getSelectedKernel(this.myCluster) +
//                                " -f "  + "$HOME/.diet/" +
//                                "" + this.myDeploymentName +
//                                " -p " + xda + "\"";
//                            sess.close();
//
//                            sess = KadeployDlg.getConnection().openSession();
//
//                            sess.execCommand(cmd);
//                            stdout = new StreamGobbler(sess.getStdout());
//                            br = new BufferedReader(new InputStreamReader(stdout));
//                            sess.getStdin();
//                            setProgression(this.myDeploymentName, 0);
//                            while (true) {
//                                String line = br.readLine();
//                                if (line == null)
//                                    break;
//                                for (int i=0; i<BootSteps.length; i++) {
//                                    if (line.indexOf(BootSteps[i]) != -1) {
//                                        LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "### Step " + i);
//                                        setProgression(this.myDeploymentName, i);
//                                    } // end if startsWith
//                                } // end for
//                                addOutput(this.myDeploymentName, line);
//                                LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "output : " + line);
//                            } // end while
//                            br.close();
//                            stdout.close();
//                            stderr = new StreamGobbler(sess.getStderr());
//                            br = new BufferedReader(new InputStreamReader(stderr));
//                            while (true) {
//                                String line = br.readLine();
//                                if (line == null)
//                                    break;
//                                addError(this.myDeploymentName, line);
//                            } // end while
//                            br.close();
//                            stderr.close();
//
//                        }
//                        // -----------
//                    }else{
                        String cmd = null;
                        if (this.myCluster != G5kSite.siteLille){
                            cmd =
                            "ssh -tt " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
                            " \"kadeploy -e " +
                            KernelPanel.getSelectedKernel(this.myCluster) +
                            //          " -m " + this.myHost +
                            " -f " + "$HOME/.diet/" +
                                    "" + this.myDeploymentName +
                            " -p " + Config.getXDA(this.myCluster) + "\"";
                        }
                        else{
                            cmd =
                                "ssh -tt " + G5kSite.getInternalFrontalForIndex(this.myCluster) +
                                " \"kadeploy -e " +
                                KernelPanel.getSelectedKernel(this.myCluster) +
                                //          " -m " + this.myHost +
                                " -f " + "$HOME/" +
                                        "" + this.myDeploymentName +
                                " -p " + Config.getXDA(this.myCluster) + "\"";

                        }
                        sess.execCommand(cmd);

                        InputStream stdout = new StreamGobbler(sess.getStdout());
                        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                        sess.getStdin();
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            for (int i=0; i<BootSteps.length; i++) {
                                if (line.indexOf(BootSteps[i]) != -1) {
                                    LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "### Step " + i);
                                    setProgression(this.myDeploymentName, i);
                                } // end if startsWith
                            } // end for
                            addOutput(this.myDeploymentName, line);
                            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", "output : " + line);
                        } // end while

                        InputStream stderr = new StreamGobbler(sess.getStderr());
                        br = new BufferedReader(new InputStreamReader(stderr));
                        while (true) {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            addError(this.myDeploymentName, line);
                        } // end while

                        sess.close();
//                    }
                }
                catch (Exception e) {
                    LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "run", e);
                }

            } // end run

            /**
             * Method returning the output
             *
             * @return the output
             */
            public String[] getOutput() {
                return null;
            }

            /**
             * Method returning the errors
             *
             * @return the errors
             */
            public String[] getError() {
                return null;
            }
        } // end class SimulThread
    } // end class DeployProgressPanel
}