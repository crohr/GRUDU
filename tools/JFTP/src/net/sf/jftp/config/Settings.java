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
package net.sf.jftp.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Properties;


public class Settings
{
    private static Properties p = new Properties();
    public static final String propertyFilename = System.getProperty("user.home") +
                                                  File.separator +
                                                  ".diet/jftp/jftp.properties".replace('/',
                                                                                  File.separatorChar);

    static
    {
        try
        {
            p.load(new FileInputStream(propertyFilename));
        }
        catch(Exception e)
        {
            System.out.println("no property file loaded, using defaults... (" +
                               e + ")");
        }
    }

    public static String sshHostKeyVerificationFile = System.getProperty("user.home") +
                                                      File.separator + ".diet" +
                                                      File.separator + "jftp" +
                                                      File.separator +
                                                      ".ssh_hostfile";
    public static final String defaultWidth = "840";
    public static final String defaultHeight = "640";
    public static final String defaultX = "20";
    public static final String defaultY = "20";
    public static int maxConnections = 3;
    public static boolean enableResuming = false; // overridden by JFtp
    public static boolean enableUploadResuming = true;
    public static boolean askToResume = true;
    public static boolean reconnect = true;
    public static int uiRefresh = 500;
    public static int logFlushInterval = 2000; // obsolete
    public static boolean useLogFlusher = false; // obsolete
    public static int ftpTransferThreadPause = 2000;
    public static int smallSize = 0; //100000;
    public static int smallSizeUp = 0; //50000;
    public static boolean autoUpdate = false;
    public static boolean shortProgress = true;
    private static String defaultFtpPasvMode = "true";
    private static String defaultEnableDebug = "false";
    private static String enableMultiThreading = "true";
    private static String enableSmbMultiThreading = "true";
    private static String enableSftpMultiThreading = "true";
    private static String noUploadMultiThreading = "false";
    private static String storePasswords = "false";

    // 1: manual, 2: onclick, 0: off
    public static int runtimeCommands = 2;
    public static boolean askToRun = true;

    // currently changed by remotedir on-the-fly
    public static boolean showDateNoSize = false;
    public static boolean showLocalDateNoSize = false;

    // hides some messages like MODE, Type etc.
    public static boolean hideStatus = false;

    public static boolean showNewlineOption = false;

    // for DataConnection - lower means less buffer, more updates in the downloadmanager
    // i recommend to use values greater than 2048 bytes
    public static int bufferSize = 8192;

    // sends NOOPs to ensure that buffers are empty
    public static boolean safeMode = false;

    // enables some delays
    public static boolean enableFtpDelays = false;

    // title of the app
    public static final String title = "JFtp - The GRUDU module for File transfer on Grid'5000";

    // overridden title for insomniac client
    public static final String insomniacTitle = ">>> Insomniac client BETA 1 <<< Based on JFtp ";

    // override ui with the insomniac client configuration
    public static boolean isInsomniacClient = false;
    public static int refreshDelay = 250;
    public static boolean useDefaultDir = true;

    // may the windows be resized?
    public static boolean resize = true;
    public static boolean showFileSize = true;
    public static boolean sortDir = true;
    public static final int visibleFileRows = 15;
    public static int scrollSpeed = 9;
    public static int numFiles = 9;
    public static final int connectionTimeout = 30000;
    public static final int testTimeout = 5000;
    public static int statusMessageAfterMillis = 1000;
    public static final String defaultDir = "<default>";
    public static final String defaultWorkDir = System.getProperty("user.home");
    public static final String userHomeDir = System.getProperty("user.home");
    public static final String appHomeDir = userHomeDir +
                                            "/.diet/jftp".replace('/',
                                                              File.separatorChar);
    public static final String greeting = "Have a lot of fun...";
    public static final String bookmarks = appHomeDir +
                                           "bookmarks.txt".replace('/',
                                                                   File.separatorChar);
    public static final String rsyncOptions = appHomeDir+"rsyncOptions.xml".replace('/', File.separatorChar);
    public static final String ls_out = appHomeDir+".ls_out".replace('/', File.separatorChar);

    public static final String login_def = appHomeDir +
                                           ".login_default".replace('/',
                                                                    File.separatorChar);
    public static final String login = appHomeDir +
                                       ".login".replace('/', File.separatorChar);

    //*** The new files that I have created
    public static final String last_cons = appHomeDir +
                                           ".last_cons".replace('/',
                                                                File.separatorChar);
    public static final String login_def_sftp = appHomeDir +
                                                ".last_cons_sftp".replace('/',
                                                                          File.separatorChar);
    public static final String login_def_smb = appHomeDir +
                                               ".last_cons_smb".replace('/',
                                                                        File.separatorChar);
    public static final String login_def_nfs = appHomeDir +
                                               ".last_cons_nfs".replace('/',
                                                                        File.separatorChar);

    //***
    //*** added in version 1.44
    public static final String adv_settings = appHomeDir +
                                              ".adv_settings".replace('/',
                                                                      File.separatorChar);

    //***
    public static String rsyncImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/rsync.png";
    public static final String readme = "resources/gridr/g5k/jftp/docs/readme";
    public static final String changelog = "resources/gridr/g5k/jftp/docs/CHANGELOG";
    public static final String todo = "resources/gridr/g5k/jftp/docs/TODO";
    public static final String nfsinfo = "resources/gridr/g5k/jftp/resources/gridr/g5k/jftp/docs/doc/nfsinfo";
    public static String iconImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/20x20/ComputerIn.gif";
    public static String hostImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/20x20/ComputerIn.gif";
    public static String closeImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/20x20/Error.gif";
    public static String infoImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/20x20/Inform.gif";
    public static String listImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/20x20/List.gif";
    public static String deleteImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/DeleteDocument.gif";
    public static String rmdirImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/DeleteFolder.gif";
    public static String mkdirImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/NewFolder.gif";
    public static String refreshImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Undo.gif";
    public static String cdImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Open.gif";
    public static String cmdImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/ExecuteProject.gif";
    public static String downloadImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Left.gif";
    public static String uploadImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Right.gif";
    public static String fileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Document.gif";
    public static String dirImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Folder.gif";
    public static String codeFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/List.gif";
    public static String textFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/DocumentDraw.gif";
    public static String execFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/ExecuteProject.gif";
    public static String audioFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/cd.gif";
    public static String videoFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/CameraFlash.gif";
    public static String htmlFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/World2.gif";
    public static String zipFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/DataStore.gif";
    public static String imageFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Camera.gif";
    public static String presentationFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/DocumentDiagram.gif";
    public static String spreadsheetFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/DatePicker.gif";
    public static String bookFileImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Book.gif";
    public static String copyImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Copy.gif";
    public static String openImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/World2.gif";
    public static String sftpImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/NewEnvelope.gif";
    public static String nfsImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/TrafficGreen.gif";
    public static String webdavImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/DataStore.gif";
    public static String linkImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Right.gif";
    public static String typeImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/20x20/Type.gif";
    public static String clearImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Undo.gif";
    public static String resumeImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/GreenCircle.gif";
    public static String pauseImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/RedCircle.gif";
    public static String background = "resources/gridr/g5k/jftp/images/back.jpg";
    public static boolean isStandalone = true;
    public static String saveImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Save.gif";
    public static String cdUpImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Exit.gif";
    public static String nextRSSImage = "resources/gridr/g5k/jftp/images/org/javalobby/icons/16x16/Forward.gif";
    public static String hiddenPassword = "<%hidden%>";

    public static Object setProperty(String key, String value)
    {
        return p.setProperty(key, value);
    }

    public static String getProperty(String key) {
    	return ""+p.getProperty(key);
    }

    public static Object setProperty(String key, int value)
    {
        return p.setProperty(key, Integer.toString(value));
    }

    public static Object setProperty(String key, boolean value)
    {
        String val = "false";

        if(value)
        {
            val = "true";
        }

        return p.setProperty(key, val);
    }

    public static void save()
    {
        try
        {
            new File(System.getProperty("user.home") + File.separator +
                     ".diet/jftp").mkdir();
            p.store(new FileOutputStream(propertyFilename), "jftp.properties");
        }
        catch(Exception e)
        {
            System.out.println("Cannot save properties...");

            //e.printStackTrace();
        }
    }

    public static int getMaxConnections()
    {
        return maxConnections;
    }

    public static String getSocksProxyHost()
    {
        String what = p.getProperty("jftp.socksProxyHost", "");

        return what;
    }

    public static String getSocksProxyPort()
    {
        String what = p.getProperty("jftp.socksProxyPort", "");

        return what;
    }

    public static boolean getUseBackground()
    {
        String what = p.getProperty("jftp.useBackground", "true");

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getEnableSshKeys()
    {
        String what = p.getProperty("jftp.useSshKeyVerification", "false");

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getEnableResuming()
    {
        String what = p.getProperty("jftp.enableResuming", "true");

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getEnableDebug()
    {
        String what = p.getProperty("jftp.enableDebug", defaultEnableDebug);

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getDisableLog()
    {
        String what = p.getProperty("jftp.disableLog", "false");

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getEnableStatusAnimation()
    {
        String what = p.getProperty("jftp.gui.enableStatusAnimation", "true");

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getAskToDelete()
    {
        String what = p.getProperty("jftp.gui.askToDelete", "true");

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static String getLookAndFeel()
    {
        return p.getProperty("jftp.gui.look", null);
    }

    public static boolean getEnableMultiThreading()
    {
        String what = p.getProperty("jftp.enableMultiThreading",
                                    enableMultiThreading);

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getEnableSmbMultiThreading()
    {
        String what = p.getProperty("jftp.enableSmbMultiThreading",
                                    enableSmbMultiThreading);

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getEnableSftpMultiThreading()
    {
        String what = p.getProperty("jftp.enableSftpMultiThreading",
                                    enableSftpMultiThreading);

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getNoUploadMultiThreading()
    {
        String what = p.getProperty("jftp.noUploadMultiThreading",
                                    noUploadMultiThreading);

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getFtpPasvMode()
    {
        String what = p.getProperty("jftp.ftpPasvMode", defaultFtpPasvMode);

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getUseDefaultDir()
    {
        String what = p.getProperty("jftp.useDefaultDir", "true");

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean getEnableRSS()
    {
        String what = p.getProperty("jftp.enableRSS", "false");

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static String getRSSFeed()
    {
        String what = p.getProperty("jftp.customRSSFeed",
                                    "http://slashdot.org/rss/slashdot.rss");

        return what;
    }

    public static java.awt.Dimension getWindowSize()
    {
        int width = Integer.parseInt(p.getProperty("jftp.window.width",
                                                   defaultWidth));
        int height = Integer.parseInt(p.getProperty("jftp.window.height",
                                                    defaultHeight));

        return new java.awt.Dimension(width, height);
    }

    public static java.awt.Point getWindowLocation()
    {
        int x = Integer.parseInt(p.getProperty("jftp.window.x", defaultX));
        int y = Integer.parseInt(p.getProperty("jftp.window.y", defaultY));

        return new java.awt.Point(x, y);
    }

    public static int getSocketTimeout()
    {
        return 3000;
    }

    public static boolean getStorePasswords()
    {
        String what = p.getProperty("jftp.security.storePasswords",
                                    storePasswords);

        if(what.trim().equals("false"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
