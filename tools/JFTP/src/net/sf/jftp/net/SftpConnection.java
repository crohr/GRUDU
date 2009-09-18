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
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.sf.jftp.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.SwingUtilities;

import net.sf.jftp.diet.Config;
import net.sf.jftp.diet.G5kSite;
import net.sf.jftp.diet.WaitingFrame;
import net.sf.jftp.net.rsync.RsyncController;
import net.sf.jftp.system.StringUtils;
import net.sf.jftp.system.logging.Log;
import net.sf.jftp.util.OutilsZip;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class SftpConnection implements BasicConnection
{;
    private String path = "";
    private String pwd = "/";
    private Vector listeners = new Vector();
    private String[] files;
    private String[] size = new String[0];
    private int[] perms = null;
    private boolean connected = false;

    /*********************/

    private Connection connection = null;
    private String login = null;
    private String keyFile = null;
    private String passPhrase = null;
    private String frontale = null;
    private String preferredFrontale = null;
    private boolean isTheFrontaleThePrefferedFrontale = false;
//    private Session session = null;

    /*********************/

    public SftpConnection(String aLogin, String aKeyFile, String aPassPhrase, String aFrontale, String aPrefferedFrontale){
        login = aLogin;
        keyFile = aKeyFile;
        passPhrase = aPassPhrase;
        frontale = aFrontale;
        preferredFrontale = aPrefferedFrontale;
        int siteIndex = 0;
        for(int i = 0 ; i < G5kSite.getSitesNumber() ; i++){
            if(G5kSite.getExternalFrontals()[i].equalsIgnoreCase(preferredFrontale)){
                siteIndex = i;
                break;
            }
        }
        if(frontale.equalsIgnoreCase(G5kSite.getInternalFrontals()[siteIndex])) isTheFrontaleThePrefferedFrontale = true;
        Log.debug("SFTP connection created for the connection to "+ frontale);
    }

    public boolean login()
    {
        Log.debug("Trying to log on " + frontale +" ...");
        boolean resultOfTheLogin = false;

        try{
            Connection conn = new Connection(preferredFrontale);

            conn.connect();

            boolean isAuthenticated = conn.authenticateWithPublicKey(login,
                   new File(keyFile), passPhrase);

            if (isAuthenticated == false)
                throw new IOException("Authentication failed for "+ frontale);

            connection = conn;

            resultOfTheLogin = true;
            Log.debug("You are now logged on  " + frontale);
        }

        catch(Exception e){
            Log.debug("Unable to open a session on " + frontale);
            e.printStackTrace();
        }

        fireConnectionInitialized(this);
        return resultOfTheLogin;
    }

    public int removeFileOrDir(String file)
    {
        Log.debug("Removing " + toSFTP(file) + " on " + frontale + " ...");
        if(isTheFrontaleThePrefferedFrontale){
            Session session = null;
            try{
                try{
                    session = connection.openSession();
                }
                catch(Exception e){
                    e.printStackTrace();
                    login();
                    session = connection.openSession();
                }
                String fileToDelete = toSFTP(file);
                session.execCommand("rm -rf "+ fileToDelete +"\n");
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of " + frontale);
                }
                Log.debug("Removing " + toSFTP(file) + " on " + frontale + " ... DONE");
            }
            catch(Exception e){
                Log.debug("Unable to remove " + toSFTP(file) + " on " + frontale);
                return -1;
            }
            session.close();
        }
        else{
            Session session=null;
            try{
                try{
                    session = connection.openSession();
                }
                catch(Exception e){
                    e.printStackTrace();
                    login();
                    session = connection.openSession();
                }
                String fileToDelete = toSFTP(file);
                session.execCommand("ssh "+frontale+" \" rm -rf "+ fileToDelete +"\"");
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of " + frontale);
                }
                Log.debug("Removing " + toSFTP(file) + " on " + frontale + " ... DONE");
            }
            catch(Exception e){
                Log.debug("Unable to remove " + toSFTP(file) + " on " + frontale);
                e.printStackTrace();
            }
            session.close();
        }
        fireDirectoryUpdate();
       return 1;
    }

    public void sendRawCommand(String cmd)
    {
    }

    public void disconnect()
    {
        connection.close();
        connected = false;
        Log.debug("You are now disconnected from : " + frontale);
    }

    public boolean isConnected()
    {
        return connected;
    }

    public String getPWD()
    {
        if(pwd == null || pwd=="/"){
        Session session = null;
        try{
            try{
                session = connection.openSession();
            }
            catch(Exception e){
                e.printStackTrace();
                login();
                session = connection.openSession();
            }
            if(isTheFrontaleThePrefferedFrontale){
                session.execCommand("pwd\n");
            }
            else{
                session.execCommand("ssh "+frontale + " \"pwd\"\n");
            }
        }
        catch(Exception e){
            Log.debug("Unable to get the remote pwd of " + frontale);
            e.printStackTrace();
        }
        InputStream stdout = new StreamGobbler(session.getStdout());

        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
        try{
            while (true)
            {
                String line = br.readLine();
                if (line == null)
                    break;
                pwd=line+"/";
            }
        }
        catch(Exception e){
            Log.debug("Unable to read from stdout of " + frontale);
        }
        InputStream stderr = new StreamGobbler(session.getStderr());

        BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
        try{
            while (true)
            {
                String line = brErr.readLine();
                if (line == null)
                    break;
                Log.debug("stderr >>> " + line);
            }
        }
        catch(Exception e){
            Log.debug("Unable to read from stderr of " + frontale);
        }
        }

        return pwd;
    }

    public boolean mkdir(String dirName)
    {
        Log.debug("Creating directory " + toSFTP(dirName) + " on " + frontale+ " ...");
        try{
            Session session = null;
            try{
                session = connection.openSession();
            }
            catch(Exception e){
                e.printStackTrace();
                login();
                session = connection.openSession();
            }
            if(isTheFrontaleThePrefferedFrontale){
                session.execCommand("mkdir "+toSFTP(dirName)+"\n");
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                    Log.debug("Creating directory " + toSFTP(dirName) + " on " + frontale+ " ... DONE");
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of " + frontale);
                    fireDirectoryUpdate();
                    return false;
                }
                session.close();

            }
            else{
                String file = toSFTP(dirName);
                session.execCommand("ssh "+frontale + " \"mkdir "+file+"\"\n");
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of "+ frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                    Log.debug("Creating directory " + toSFTP(dirName) + " on " + frontale+ " ... DONE");
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of" + frontale);
                }
                session.close();
                Log.debug("Creating directory " + toSFTP(dirName) + " on " + frontale+ " ... DONE");
            }
        }
        catch(Exception e){
            Log.debug("Unable to create remote directory "+ toSFTP(dirName) + " on " + frontale);
            fireDirectoryUpdate();
            return false;
        }
        fireDirectoryUpdate();
        return true;
    }

    public void list() throws IOException
    {
    }

    public boolean chdir(String p)
    {
        return chdir(p, true);
    }

    public boolean chdir(String p, boolean refresh)
    {
        String tmp = toSFTP(p);
        Log.debug("Changing of directory from " + pwd + " to " + tmp + " on " + frontale + " ...");
        if(!tmp.endsWith("/"))
        {
            tmp = tmp + "/";
        }

        if(tmp.endsWith("../"))
        {
            return cdup();
        }
        Log.debug("Old pwd : "+ pwd);
        pwd= tmp;
        Log.debug("New pwd : "+ pwd);
        try{
            Session session = null;
            try{
                session = connection.openSession();
            }
            catch(Exception e){
                e.printStackTrace();
                login();
                session = connection.openSession();
            }
            if(isTheFrontaleThePrefferedFrontale){
                session.execCommand("cd "+ tmp +"\n");
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of " + frontale);
                }
                session.close();
                Log.debug("Changing of directory from " + pwd + " to " + tmp + " on " + frontale + " ... DONE");
            }
            else{
                session.execCommand("ssh "+frontale + " \"cd "+ tmp+"\"\n");
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of "+ frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of "+ frontale);
                }
                session.close();
                Log.debug("Changing of directory from " + pwd + " to " + tmp + " on " + frontale + " ... DONE");
            }
        }
        catch(Exception e){
            Log.debug("Unable to change of directory from " + pwd + " to " + tmp + " on " + frontale);
            e.printStackTrace();
        }

        sortLs();

        if(refresh)
        {
            fireDirectoryUpdate();
        }

        return true;
    }

    public boolean cdup()
    {
        String tmp = pwd;

        if(pwd.endsWith("/"))
        {
            tmp = pwd.substring(0, pwd.lastIndexOf("/"));
        }

        return chdir(tmp.substring(0, tmp.lastIndexOf("/") + 1));
    }

    public boolean chdirNoRefresh(String p)
    {
        return chdir(p, false);
    }

    public String getLocalPath()
    {
        return path;
    }

    public boolean setLocalPath(String p)
    {
        if(StringUtils.isRelative(p))
        {
            p = path + p;
        }

        p = p.replace('\\', '/');

        File f = new File(p);

        if(f.exists())
        {
            try
            {
                path = f.getCanonicalPath();
                path = path.replace('\\', '/');

                if(!path.endsWith("/"))
                {
                    path = path + "/";
                }

            }
            catch(IOException ex)
            {
                Log.debug("Error: can not get pathname (local)!");

                return false;
            }
        }
        else
        {
            Log.debug("(local) No such path: \"" + p + "\"");

            return false;
        }

        return true;
    }

    public String[] sortLs()
    {
        Log.debug("Retrieving files list of  " + pwd + " on " + frontale + " ..." );
        Session session = null;
        ArrayList<String> listOfFiles = new ArrayList<String>();
        try{
            try{
                session = connection.openSession();
            }
            catch(Exception e){
                e.printStackTrace();
                login();
                session = connection.openSession();
            }
            if(isTheFrontaleThePrefferedFrontale){
                session.execCommand("cd "+ pwd +"; ls -alrt\n");
            }
            else{
                session.execCommand("ssh "+frontale + " \"cd "+pwd+"; ls -alrt\"\n");
            }
            InputStream stdout = new StreamGobbler(session.getStdout());

            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

            try{
                while (true)
                {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    listOfFiles.add(line);
                }
            }
            catch(Exception e){
                Log.debug("Unable to read from stdout on "+ frontale);
            }
            InputStream stderr = new StreamGobbler(session.getStderr());

            BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
            try{
                while (true)
                {
                    String line = brErr.readLine();
                    if (line == null)
                        break;
                    Log.debug("stderr >>> " + line);
                }
            }
            catch(Exception e){
                Log.debug("Unable to read from stderr on " + frontale);
            }
            session.close();
            files = new String[listOfFiles.size()-1];
            size = new String[listOfFiles.size()-1];
            perms = new int[listOfFiles.size()-1];
            Iterator<String> iter = listOfFiles.iterator();
            int index = 0;
            StringTokenizer tokenizer = null;
            while(iter.hasNext()){
                String message = iter.next();
                tokenizer = new StringTokenizer(message, " \t");
                if(tokenizer.countTokens() < 6){}
                else{
                    String permission = tokenizer.nextToken();
                    if(permission.charAt(1)=='r') perms[index] = FtpConstants.R; // permission
                    else perms[index]=FtpConstants.DENIED;
                    tokenizer.nextElement(); // links
                    tokenizer.nextToken(); // group
                    tokenizer.nextElement(); // user
                    size[index] = tokenizer.nextToken(); //size
                    // on some different OSes the date and time can be expressed differently
                    String aToken = null;
                    while(tokenizer.hasMoreTokens()) aToken = tokenizer.nextToken();
                    files[index] = aToken;
                    if(permission.charAt(0) == 'd') files[index] +="/";
                    index++;
                }
            }
            Log.debug("Retrieving files list of  " + pwd + " on " + frontale + " ... DONE" );
        }
        catch(Exception e){
            Log.debug("Unable to retrieve files list of  " + pwd + " on " + frontale + " ..." );
        }
        return files;
    }

    public String[] sortSize()
    {
        return size;
    }

    public int[] getPermissions()
    {
        return perms;
    }

    public int handleUpload(String f)
    {
        upload(pwd+f);
        return 0;
    }

    public int handleDownload(String f)
    {
        download(pwd+f);
        return 0;
    }

    public int upload(String f)
    {
    	System.out.println("Fichier à transferer : " + f);
        // en entrée on a le chemin vers le fichier local
        String file = toSFTP(f);
        System.out.println("Fichier à transferer (toSFTP): " + file);
        String path = getLocalPath();
        String fileToTransfer = path + StringUtils.getDir(f);
        System.out.println("Fichier à transferer en local: " + file);
        if(fileToTransfer.endsWith("/")){
            fileToTransfer = fileToTransfer.substring(0,fileToTransfer.length()-1);
        }
        String fichierToTransfer = fileToTransfer;
        String out = null;
        if(file.endsWith("/")){
            try{
                fichierToTransfer = System.getProperty("user.home")+System.getProperty("file.separator")+"temporary_file.zip_JFTP";
                OutilsZip.tarGz(fileToTransfer, fichierToTransfer);
//                OutilsZip.zipDir(fileToTransfer, fichierToTransfer);
                System.out.println("Fichier à transferer : " + fichierToTransfer);
                file = file.substring(0,file.length()-1);
            }catch(Exception e){
                e.printStackTrace();
            }
            out = file;
        }else{
            out = file;
//            int lastIndexOfFileSeparator = file.lastIndexOf(System.getProperty("file.separator"));
//            out = file.substring(0, lastIndexOfFileSeparator);
        }
        System.out.println("On va transférer : " + fichierToTransfer + " vers : " + out);
        uploadDir(fichierToTransfer, out);
        fireActionFinished(this);

        return 0;
    }

    public int download(String f)
    {
        String file = toSFTP(f);
        downloadDir(file, getLocalPath() );
        fireActionFinished(this);

        return 0;
    }

    private void downloadDir(String dir, String out)
    {
    	int numberOfSteps = 2;
    	if(!isTheFrontaleThePrefferedFrontale){
    		numberOfSteps ++;
    	}
    	final WaitingFrame myFrame =  new WaitingFrame("Operation in progress","Downloading " + toSFTP(dir) + " to " + out + " on "+ frontale + " ...",numberOfSteps,true);
        myFrame.launch(null);
        myFrame.setStatusText("Opening a session for the download ...");
        
        Log.debug("Downloading " + toSFTP(dir) + " to " + out + " on "+ frontale + " ...");
        String initDir = dir;
        String basedir = null;
        try{
            Session session = null;
            try{
                session = connection.openSession();
            }
            catch(Exception e){
                e.printStackTrace();
                login();
                session = connection.openSession();
            }
            if(!isTheFrontaleThePrefferedFrontale){
            	myFrame.incrementProgressBar();
            	myFrame.setStatusText("Retrieving data from distant frontale to preferred frontale ...");
                String cmd = "scp -r "+frontale + ":"+toSFTP(dir)+ " /tmp";
                session.execCommand(cmd);
//                basedir = StringUtils.getFile(toSFTP(dir));
//                dir="/tmp/"+basedir;
                if(initDir.endsWith("/")){
                    basedir = StringUtils.getDir(toSFTPDir(dir));
                    dir="/tmp/"+basedir;
                }else{
                    basedir = StringUtils.getFile(toSFTP(dir));
                    dir="/tmp/"+basedir;
                }
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of " + frontale);
                }
                session.close();
            }
            if(dir.endsWith("/")){
                dir= dir.substring(0,dir.length()-1);
                basedir = StringUtils.getDir(dir);
                int lastIndexOffileSeparator = dir.lastIndexOf("/");
                String parentDir = dir.substring(0, lastIndexOffileSeparator);
                try{
                    session = connection.openSession();
                }
                catch(Exception e){
                    e.printStackTrace();
                    login();
                    session = connection.openSession();
                }
                session.execCommand("cd "+parentDir+" ; tar cvfz /tmp/temporaryFile.tgz_JFTP " + basedir);
                dir = "/tmp/temporaryFile.tgz_JFTP";
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        //Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of "+ frontale);
                }
                session.close();

            }
            myFrame.incrementProgressBar();
            myFrame.setStatusText("Retrieving data from the preferred frontale ...");
            SCPClient client = connection.createSCPClient();
            String temp1 = toSFTP(dir);
            String temp2 = out;
            client.get(temp1, temp2);
            if(initDir.endsWith("/")){
                try{
                    session = connection.openSession();
                }
                catch(Exception e){
                    e.printStackTrace();
                    login();
                    session = connection.openSession();
                }
                session.execCommand("rm -rf " +dir);
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of " + frontale);
                }
                session.close();
                try{
                    OutilsZip.getFilesFromTarGz(new File(out+"temporaryFile.tgz_JFTP"), new File(out));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            myFrame.incrementProgressBar();
            Log.debug("Downloading " + toSFTP(initDir) + " to " + toSFTP(out) + " on "+ frontale + " ... DONE");
        }
        catch(Exception e){
            Log.debug("Exception caught :" + e + "\n");
            Log.debug(e.getMessage());
        }
        myFrame.dispose();
    }

    private void uploadDir(String dir, String out)
    {
    	int numberOfSteps = 2;
    	if(dir.endsWith("zip_JFTP")) numberOfSteps ++;
    	if(!isTheFrontaleThePrefferedFrontale) numberOfSteps++;
    	final WaitingFrame myFrame =  new WaitingFrame("Operation in progress","Uploading " + toSFTP(dir) + " to " + out + " on "+ frontale + " ...",numberOfSteps,true);
        myFrame.launch(null);
        Log.debug("Uploading " + toSFTP(dir) + " to " + out + " on "+ frontale + " ...");
        try{
            Session session = null;
            try{
                session = connection.openSession();
            }
            catch(Exception e){
                e.printStackTrace();
                login();
                session = connection.openSession();
            }
            SCPClient client = connection.createSCPClient();
            String baseName = StringUtils.getDir(dir);
            if(!isTheFrontaleThePrefferedFrontale){
            	myFrame.setStatusText("Uploading the data to the preferred frontale ...");
                client.put(dir, "/tmp");
                myFrame.incrementProgressBar();
                int lastIndexOfFileSeparator = out.lastIndexOf("/");
                String outTemp = out.substring(0, lastIndexOfFileSeparator);
                myFrame.setStatusText("Uploading the data to the destination frontale ...");
                session.execCommand("scp -r /tmp/"+ baseName+ " " + frontale + ":"+toSFTPDir(outTemp) +"\n");
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of frontale");
                }
                session.close();
                myFrame.incrementProgressBar();
                if(dir.endsWith("zip_JFTP")){
                    try{
                        session = connection.openSession();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        login();
                        session = connection.openSession();
                    }
                    baseName = StringUtils.getFile(out);
                    myFrame.setStatusText("Unpacking the data on the destination frontale ...");
                    
                    session.execCommand("ssh "+ frontale +" \"cd "+ outTemp+"; tar xvfz "+outTemp+"/temporary_file.zip_JFTP ; rm -f temporary_file.zip_JFTP\"");
                    int lastFileSeparator = dir.lastIndexOf(System.getProperty("file.separator"));
                    dir = dir.substring(0, lastFileSeparator+1) + StringUtils.getFile(out);
                    stdout = new StreamGobbler(session.getStdout());

                    br = new BufferedReader(new InputStreamReader(stdout));
                    try{
                        while (true)
                        {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            Log.debug("stdout >>> " + line);
                        }
                    }
                    catch(Exception e){
                        Log.debug("Unable to read from stdout of " + frontale);
                    }
                   stderr = new StreamGobbler(session.getStderr());

                    brErr = new BufferedReader(new InputStreamReader(stderr));
                    try{
                        while (true)
                        {
                            String line = brErr.readLine();
                            if (line == null)
                                break;
                            Log.debug("stderr >>> " + line);
                        }
                    }
                    catch(Exception e){
                        Log.debug("Unable to read from stderr of " + frontale);
                    }
                }
                session.close();
                myFrame.incrementProgressBar();
                Log.debug("Uploading " + dir + " to " + out + " on "+ frontale + " ... DONE");
            }
            else{
                if(dir.endsWith("zip_JFTP")){
                    session.execCommand("mkdir "+ out);
                    session.close();
                    try{
                        session = connection.openSession();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        login();
                        session = connection.openSession();
                    }
                }
                myFrame.setStatusText("Uploading the data on the destination frontale ...");
                client.put(dir, "/tmp");
                rename("/tmp/"+baseName, out);
                myFrame.incrementProgressBar();
                if(dir.endsWith("zip_JFTP")){
                	myFrame.setStatusText("Unpacking the data on the destination frontale ...");
                    session.execCommand("cd "+ out+"; tar xvfz temporary_file.zip_JFTP; rm -f temporary_file.zip_JFTP");
                    int lastFileSeparator = dir.lastIndexOf(System.getProperty("file.separator"));
                    dir = dir.substring(0, lastFileSeparator+1) + StringUtils.getFile(out);
                    InputStream stdout = new StreamGobbler(session.getStdout());

                    BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                    try{
                        while (true)
                        {
                            String line = br.readLine();
                            if (line == null)
                                break;
                            //Log.debug("stdout >>> " + line);
                        }
                    }
                    catch(Exception e){
                        Log.debug("Unable to read from stdout of " + frontale);
                    }
                    InputStream stderr = new StreamGobbler(session.getStderr());

                    BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                    try{
                        while (true)
                        {
                            String line = brErr.readLine();
                            if (line == null)
                                break;
                            Log.debug("stderr >>> " + line);
                        }
                    }
                    catch(Exception e){
                        Log.debug("Unable to read from stderr of " + frontale);
                    }
                    myFrame.incrementProgressBar();
                    File fichierTemp = new File(System.getProperty("user.home")+ System.getProperty("file.separator") + "temporary_file.zip_JFTP");
                    fichierTemp.delete();
                }
                session.close();
                Log.debug("Uploading " + toSFTP(dir) + " to " + out + " on "+ frontale + " ... DONE");
                
            }

        }
        catch(Exception e){
            Log.debug("Exception caught :" + e + "\n");
            e.printStackTrace();
        }
        myFrame.dispose();
    }

    private String toSFTP(String f)
    {
        String file;

        if(f.startsWith("/"))
        {
            file = f;
        }
        else
        {
            file = getPWD() + f;
        }

        file = file.replace('\\', '/');

        return file;
    }

    private String toSFTPDir(String f)
    {
        String file;

        if(f.startsWith("/"))
        {
            file = f;
        }
        else
        {
            file = pwd + f;
        }

        file = file.replace('\\', '/');

        if(!file.endsWith("/"))
        {
            file = file + "/";
        }

        return file;
    }
    
    public void rsync(String fileToSynchronize){ 
    	Log.debug("Synchronizing " + toSFTP(fileToSynchronize)+ " from "+ frontale + " to others frontales...");
    	try{
    		final String theFileToSynchronize1 = fileToSynchronize;
    		final ArrayList<Integer> sitesToContact = new ArrayList<Integer>();
    		for(int i = 0; i < G5kSite.getSitesNumber() ; i++){
    			if(Config.isSiteEnable(i)) sitesToContact.add(i);
    		}
    		final WaitingFrame myFrame =  new WaitingFrame("Operation in progress","Synchronizing " + toSFTP(fileToSynchronize)+ " from "+ frontale + " to others frontales...",sitesToContact.size(),true);
    		myFrame.launch(null);
    		SwingUtilities.invokeLater(new Runnable(){
    			public void run(){

    				Thread rsyncThread = new Thread(new Runnable(){
    					public void run(){
    						try{
    							String  theFileToSynchronize = theFileToSynchronize1;
    							Session session = null;
    							RsyncController rsyncController = new RsyncController();
    							String optionsForRsync = rsyncController.getRsyncOptions();
    							if(isTheFrontaleThePrefferedFrontale){
    								Iterator<Integer> iter = sitesToContact.iterator();
    								while(iter.hasNext()){
    									try{
    										session = connection.openSession();
    									}
    									catch(Exception e){
    										e.printStackTrace();
    										login();
    										session = connection.openSession();
    									}
    									int siteIndex = iter.next();
    									if(!frontale.equalsIgnoreCase(G5kSite.getInternalFrontals()[siteIndex])){
    										myFrame.setStatusText("Synchronizing " + toSFTP(theFileToSynchronize)+ " from "+ frontale + " to " + G5kSite.getSiteForIndex(siteIndex) +" ...");
    										Log.debug("Synchronizing " + toSFTP(theFileToSynchronize)+ " from "+ frontale + " to " + G5kSite.getSiteForIndex(siteIndex) +" ...");
    										String syncSite = G5kSite.getSiteForIndex(siteIndex).toLowerCase()+".grid5000.fr";
    										if(theFileToSynchronize.endsWith(System.getProperty("file.separator"))){
    											theFileToSynchronize = theFileToSynchronize.substring(0,theFileToSynchronize.length()-1);
    										}
    										String remoteDirectoryWheretoSynchronize=toSFTP(theFileToSynchronize);
    										int lastFileSeparator = remoteDirectoryWheretoSynchronize.lastIndexOf(System.getProperty("file.separator"));
    										remoteDirectoryWheretoSynchronize = remoteDirectoryWheretoSynchronize.substring(0,lastFileSeparator);
    										String commandToExec = "rsync "+ optionsForRsync + " " +toSFTP(theFileToSynchronize) + " " +syncSite +":"+ remoteDirectoryWheretoSynchronize;
    										session.execCommand(commandToExec);
    										InputStream stdout = new StreamGobbler(session.getStdout());

    										BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
    										try{
    											while (true)
    											{
    												String line = br.readLine();
    												if (line == null)
    													break;
    												Log.debug("stdout >>> " + line);
    											}
    										}
    										catch(Exception e){
    											Log.debug("Unable to read from stdout of " + frontale);
    										}
    										InputStream stderr = new StreamGobbler(session.getStderr());

    										BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
    										try{
    											while (true)
    											{
    												String line = brErr.readLine();
    												if (line == null)
    													break;
    												Log.debug("stderr >>> " + line);
    											}
    										}
    										catch(Exception e){
    											Log.debug("Unable to read from stderr of "+ frontale);
    										}
    										Log.debug("Synchronizing " + toSFTP(theFileToSynchronize)+ " from "+ frontale + " to " + G5kSite.getSiteForIndex(siteIndex) +" ... DONE");
    										myFrame.incrementProgressBar();
    									}
    									session.close();
    								}
    							}
    							else{
    								Iterator<Integer> iter = sitesToContact.iterator();
    								while(iter.hasNext()){
    									try{
    										session = connection.openSession();
    									}
    									catch(Exception e){
    										e.printStackTrace();
    										login();
    										session = connection.openSession();
    									}
    									int siteIndex = iter.next();
    									if(!frontale.equalsIgnoreCase(G5kSite.getInternalFrontals()[siteIndex])){
    										myFrame.setStatusText("Synchronizing " + toSFTP(theFileToSynchronize)+ " from "+ frontale + " to " + G5kSite.getSiteForIndex(siteIndex) +" ...");
    										Log.debug("Synchronizing " + toSFTP(theFileToSynchronize)+ " from "+ frontale + " to " + G5kSite.getSiteForIndex(siteIndex) +" ...");
    										String syncSite = G5kSite.getSiteForIndex(siteIndex).toLowerCase()+".grid5000.fr";
    										if(theFileToSynchronize.endsWith(System.getProperty("file.separator"))){
    											theFileToSynchronize = theFileToSynchronize.substring(0,theFileToSynchronize.length()-1);
    										}
    										String remoteDirectoryWheretoSynchronize=toSFTP(theFileToSynchronize);
    										int lastFileSeparator = remoteDirectoryWheretoSynchronize.lastIndexOf(System.getProperty("file.separator"));
    										remoteDirectoryWheretoSynchronize = remoteDirectoryWheretoSynchronize.substring(0,lastFileSeparator);
    										String commandToExec = "ssh "+frontale+" \"rsync "+ optionsForRsync + " " + toSFTP(theFileToSynchronize) + " " +syncSite +":"+ remoteDirectoryWheretoSynchronize + "\"";
    										session.execCommand(commandToExec);
    										InputStream stdout = new StreamGobbler(session.getStdout());

    										BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
    										try{
    											while (true)
    											{
    												String line = br.readLine();
    												if (line == null)
    													break;
    												Log.debug("stdout >>> " + line);
    											}
    										}
    										catch(Exception e){
    											Log.debug("Unable to read from stdout of " + frontale);
    										}
    										InputStream stderr = new StreamGobbler(session.getStderr());

    										BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
    										try{
    											while (true)
    											{
    												String line = brErr.readLine();
    												if (line == null)
    													break;
    												Log.debug("stderr >>> " + line);
    											}
    										}
    										catch(Exception e){
    											Log.debug("Unable to read from stderr of "+ frontale);
    										}
    										Log.debug("Synchronizing " + toSFTP(theFileToSynchronize)+ " from "+ frontale + " to " + G5kSite.getSiteForIndex(siteIndex) +" ... DONE");
    										myFrame.incrementProgressBar();
    									}
    									session.close();
    								}
    								Log.debug("Synchronizing " + toSFTP(theFileToSynchronize)+ " from "+ frontale + " to others frontales...");
    							}
    							myFrame.dispose();
    						}
    						catch(Exception e){
    							Log.debug(e.getLocalizedMessage());
    							Log.debug(e.toString());
    						}
    					}
    				});
    				rsyncThread.start();
//  				try{rsyncThread.join();}catch(Exception e){e.printStackTrace();};
    			}
    		});
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public boolean rename(String oldName, String newName)
    {
        Log.debug("Renaming " + toSFTP(oldName) + " to " + toSFTP(newName) + " on "+ frontale + " ...");
        try{
            Session session = null;
            String myOldName = toSFTP(oldName);
            String myNewName = toSFTP(newName);
            try{
                session = connection.openSession();
            }
            catch(Exception e){
                e.printStackTrace();
                login();
                session = connection.openSession();
            }
            if(isTheFrontaleThePrefferedFrontale){

                session.execCommand("mv "+myOldName+" "+myNewName+"\n");
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of "+ frontale);
                }
                session.close();
                Log.debug("Renaming " + toSFTP(oldName) + " to " + toSFTP(newName) + " on "+ frontale + " ... DONE");
            }
            else{
                session.execCommand("ssh "+frontale + " \"mv "+myOldName+" "+myNewName+"\"\n");
                InputStream stdout = new StreamGobbler(session.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                try{
                    while (true)
                    {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.debug("stdout >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stdout of " + frontale);
                }
                InputStream stderr = new StreamGobbler(session.getStderr());

                BufferedReader brErr = new BufferedReader(new InputStreamReader(stderr));
                try{
                    while (true)
                    {
                        String line = brErr.readLine();
                        if (line == null)
                            break;
                        Log.debug("stderr >>> " + line);
                    }
                }
                catch(Exception e){
                    Log.debug("Unable to read from stderr of " + frontale);
                }
                session.close();
                Log.debug("Renaming " + toSFTP(oldName) + " to " + toSFTP(newName) + " on "+ frontale + " ... DONE");
            }

        }
        catch(Exception e){
            Log.debug("Unable to rename " + toSFTP(oldName) + " to " + toSFTP(newName) + " on "+ frontale + " ...");
        }
        fireDirectoryUpdate();
        return true;
    }

    public void addConnectionListener(ConnectionListener l)
    {
        listeners.add(l);
    }

    public void setConnectionListeners(Vector l)
    {
        listeners = l;
    }

    /** remote directory has changed */
    public void fireDirectoryUpdate()
    {
        if(listeners == null)
        {
            return;
        }
        else
        {
            for(int i = 0; i < listeners.size(); i++)
            {
                ((ConnectionListener) listeners.elementAt(i)).updateRemoteDirectory(this);
            }
        }
    }

    public void fireActionFinished(SftpConnection con)
    {
        if(listeners == null)
        {
            return;
        }
        else
        {
            for(int i = 0; i < listeners.size(); i++)
            {
                ((ConnectionListener) listeners.elementAt(i)).actionFinished(con);
            }
        }
    }

    public int upload(String file, InputStream i)
    {
        return 0;
    }

    public InputStream getDownloadInputStream(String file)
    {
        return null;
    }

    public Date[] sortDates()
    {
        return null;
    }

    /**
     * Connection is there and user logged in
     *
     * @param con The FtpConnection calling the event
     */
     public void fireConnectionInitialized(SftpConnection con)
     {
         if(listeners == null)
         {
             return;
         }
         else
         {
             for(int i = 0; i < listeners.size(); i++)
             {
                 ((ConnectionListener) listeners.elementAt(i)).connectionInitialized(con);
             }
         }
     }
}
