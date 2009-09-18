/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.sf.jftp.gui.base;

import net.sf.jftp.JFtp;
import net.sf.jftp.config.Settings;
import net.sf.jftp.diet.Config;
import net.sf.jftp.diet.G5kCfg;
import net.sf.jftp.diet.G5kSite;
import net.sf.jftp.diet.WaitingFrame;
import net.sf.jftp.gui.framework.*;
import net.sf.jftp.gui.hostchooser.SftpHostChooser;
import net.sf.jftp.gui.tasks.AdvancedOptions;
import net.sf.jftp.gui.tasks.Displayer;
import net.sf.jftp.gui.tasks.HttpBrowser;
import net.sf.jftp.gui.tasks.HttpDownloader;
import net.sf.jftp.gui.tasks.LastConnections;
import net.sf.jftp.gui.tasks.ProxyChooser;
import net.sf.jftp.net.*;
import net.sf.jftp.net.rsync.RsyncController;
import net.sf.jftp.system.logging.Log;
import net.sf.jftp.tools.*;
import net.sf.jftp.util.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.lang.Integer;

import java.util.*;

import javax.swing.*;


//***
public class AppMenuBar extends JMenuBar implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4986915753072760563L;
	public static JCheckBoxMenuItem fadeMenu = new JCheckBoxMenuItem("Enable Status Animation",
            Settings.getEnableStatusAnimation());
    public static JCheckBoxMenuItem askToDelete = new JCheckBoxMenuItem("Confirm Remove",
            Settings.getAskToDelete());
    public static JCheckBoxMenuItem debug = new JCheckBoxMenuItem("Verbose Console Debugging",
            Settings.getEnableDebug());
    public static JCheckBoxMenuItem disableLog = new JCheckBoxMenuItem("Disable Log",
            Settings.getDisableLog());
    public static JMenuItem clearItems = new JMenuItem("Clear Finished Items");
    private JFtp jftp;
    JMenu file = new JMenu("File");
    JMenu opt = new JMenu("Options");
    
    JMenuItem sftpCon = new JMenuItem("Connect to G5k...");
    JMenuItem exit = new JMenuItem("Exit");
    JMenuItem rsync = new JMenuItem("Rsync");
    //*** information on each of the last connections
    RsyncController controller = null;
    //BUGFIX
    String[][] cons = new String[jftp.CAPACITY][JFtp.CONNECTION_DATA_LENGTH];
    String[] lastConData = new String[jftp.CAPACITY];
    public AppMenuBar(JFtp jftp)
    {
        this.jftp = jftp;

        exit.addActionListener(this);
        sftpCon.addActionListener(this);
        fadeMenu.addActionListener(this);
        askToDelete.addActionListener(this);
        debug.addActionListener(this);
        disableLog.addActionListener(this);

        clearItems.addActionListener(JFtp.dList);
        clearItems.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
                                                         ActionEvent.ALT_MASK));
        
        rsync.addActionListener(this);
        opt.add(rsync);

        //*** setMnemonics(); was here
        //*** BELOW, ADDITIONS FOR THE FILE MENU ARE PUT IN PUBLIC METHOD
        resetFileItems();

        add(file);
        add(opt);
    }

    //*** Where changes to the file menu are made (initialization done here too)
    public void resetFileItems()
    {
        file.removeAll();
        file.add(sftpCon);

//        //*** ADDITION OF THE REMEMBERED CONNECTIONS
//        boolean connectionsExist = false;
//
        try
        {
            //*** get the information on the last connections
            cons = new String[jftp.CAPACITY][jftp.CONNECTION_DATA_LENGTH];

            cons = LastConnections.readFromFile(jftp.CAPACITY);

            String protocol;

            String htmp;

            String utmp;

            String conNumber;
            String usingLocal = new String("");
            Integer conNumberInt;

            lastConData[0] = new String("");
            //***
            for(int i = 0; i < jftp.CAPACITY; i++)
            {
                if(!(cons[i][0].equals("null")))
                {
                    protocol = cons[i][0];
                    htmp = cons[i][1];
                    utmp = cons[i][2];

                    int j = 3;

                    while(!(cons[i][j].equals(LastConnections.SENTINEL)))
                    {
                        j++;
                    }

                    //usingLocal is always last piece of data!
                    usingLocal = cons[i][j - 1];

                    if(usingLocal.equals("true"))
                    {
                        usingLocal = "(in local tab)";
                    }

                    else
                    {
                        usingLocal = "";
                    }

                    conNumberInt = new Integer(i + 1);
                    conNumber = conNumberInt.toString();
                    lastConData[i] = new String(conNumber + " " + protocol +
                                                ": " + htmp + " " + usingLocal);
                }
            }
        }
        catch(Exception ex)
        {
            Log.debug("WARNING: Remembered connections broken.");
            ex.printStackTrace();
        }
        setMnemonics();
    }

    //resetFileItems
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == exit)
        {
            jftp.windowClosing(null); // handles everything
        }
        
        else if(e.getSource().equals(rsync)){
        	if(controller == null){
        		controller = new RsyncController();
        		controller.show();
        	}
        	else{
        		controller.show();
        	}
        }
        else if((e.getSource() == sftpCon) && (!jftp.uiBlocked))
        {
            final ArrayList<Integer> sitesToContact = new ArrayList<Integer>();
            for(int i = 0; i < G5kSite.getSitesNumber() ; i++){
            	if(Config.isSiteEnable(i)){
            		sitesToContact.add(i);
            	}
            }

            final WaitingFrame myFrame =  new WaitingFrame("Operation in progress","Connection to Grid'5000 sites, please wait...",sitesToContact.size(),true);
            myFrame.launch(null);
            Thread th = new Thread(new Runnable(){
                public void run(){
                    myFrame.setStatusText("Initializing Grid'5000 configuration ...");
                    G5kCfg.initCfg();
                    Config.init();
                    myFrame.incrementProgressBar();
                    for(int i:sitesToContact){
                        myFrame.setStatusText("Connecting to " + G5kSite.getSiteForIndex(i));
                        boolean status = StartConnection.startCon(
                                G5kCfg.get(G5kCfg.USERNAME),
                                G5kCfg.get(G5kCfg.SSHKEYFILE),
                                G5kCfg.getSSHKey(),
                                G5kSite.getInternalFrontals()[i],
                                G5kCfg.get(G5kCfg.PREFERREDACCESPOINT), false);
                        myFrame.incrementProgressBar();
                    }
                    myFrame.dispose();
                }
            });
            th.start();
        }
//        else if(e.getSource() == stdback)
//        {
//            Settings.setProperty("jftp.useBackground", stdback.getState());
//            Settings.save();
//            JFtp.statusP.jftp.fireUpdate();
//        }
//        else if(e.getSource() == sshKeys)
//        {
//            Settings.setProperty("jftp.useSshKeyVerification",
//                                 sshKeys.getState());
//            Settings.save();
//            JFtp.statusP.jftp.fireUpdate();
//        }
        else if(e.getSource() == debug)
        {
            Settings.setProperty("jftp.enableDebug", debug.getState());
            Settings.save();
        }
        else if(e.getSource() == disableLog)
        {
            Settings.setProperty("jftp.disableLog", disableLog.getState());
            Settings.save();
        }
//        else if(e.getSource() == sftpThreads)
//        {
//            Settings.setProperty("jftp.enableSftpMultiThreading",
//                                 sftpThreads.getState());
//            Settings.save();
//        }
        else if(e.getSource() == fadeMenu)
        {
            Settings.setProperty("jftp.gui.enableStatusAnimation",
                                 fadeMenu.getState());
            Settings.save();
        }
        else if(e.getSource() == askToDelete)
        {
            Settings.setProperty("jftp.gui.askToDelete", askToDelete.getState());
            Settings.save();
        }

//        else if(e.getSource() == storePasswords)
//        {
//            boolean state = storePasswords.getState();
//
//            if(!state)
//            {
//                JOptionPane j = new JOptionPane();
//                int x = j.showConfirmDialog(storePasswords,
//                                            "You chose not to Save passwords.\n" +
//                                            "Do you want your old login data to be deleted?",
//                                            "Delete old passwords?",
//                                            JOptionPane.YES_NO_OPTION);
//
//                if(x == JOptionPane.YES_OPTION)
//                {
//                    File f = new File(Settings.login_def);
//                    f.delete();
//
//                    f = new File(Settings.login_def_sftp);
//                    f.delete();
//
//                    f = new File(Settings.login_def_nfs);
//                    f.delete();
//
//                    f = new File(Settings.login_def_smb);
//                    f.delete();
//
//                    f = new File(Settings.login);
//                    f.delete();
//
//                    f = new File(Settings.last_cons);
//                    f.delete();
//
//                    Log.debug("Deleted old login data files.\n" +
//                              "Please edit your bookmarks file manually!");
//                }
//            }
//
//            Settings.setProperty("jftp.security.storePasswords", state);
//            Settings.save();
//        }

        //*** END OF NEW LISTENERS
        else
        {
            String tmp = ((JMenuItem) e.getSource()).getLabel();

            UIManager.LookAndFeelInfo[] m = UIManager.getInstalledLookAndFeels();

            for(int i = 0; i < m.length; i++)
            {
                if(m[i].getName().equals(tmp))
                {
                    JFtp.statusP.jftp.setLookAndFeel(m[i].getClassName());
                    Settings.setProperty("jftp.gui.look", m[i].getClassName());
                    Settings.save();
                }
            }
        }
    }

    private void show(String file)
    {
        java.net.URL url = ClassLoader.getSystemResource(file);

        if(url == null)
        {
            url = HImage.class.getResource("/" + file);
        }

        Displayer d = new Displayer(url);
        JFtp.desktop.add(d, new Integer(Integer.MAX_VALUE - 11));
    }

    // by jake
    private void setMnemonics()
    {
        //*** I added accelerators for more menu items
        //*** (issue: should ALL accelerators have the CTRL modifier (so that
        //*** the ALT modifier is for mnemonics only?)
        //*** I added mnemonics for the main menu items
        file.setMnemonic('F');
        opt.setMnemonic('O');
//        view.setMnemonic('V');
//        tools.setMnemonic('T');
//        info.setMnemonic('I');

        //*** set accelerators for the remote connection window
        sftpCon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                                      ActionEvent.CTRL_MASK));


        //*** and here are some other menu mnemonics I thought I'd include:
        //*** (I'll add more if more are wanted)
        exit.setMnemonic('X');
//        readme.setMnemonic('R');
//        todo.setMnemonic('N');
//        changelog.setMnemonic('C');
//        hp.setMnemonic('H');
        clearItems.setMnemonic('F');


    }
}
