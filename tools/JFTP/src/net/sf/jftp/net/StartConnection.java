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

//TODO: Add SFTP port number here (convert potmp to a string and pass it
package net.sf.jftp.net;

import com.trilead.ssh2.Connection;
import net.sf.jftp.JFtp;
import net.sf.jftp.config.Settings;
import net.sf.jftp.diet.G5kSite;
import net.sf.jftp.gui.tasks.LastConnections;
import net.sf.jftp.system.logging.Log;


// This class is used to initiate connections of all types (FTP, SFTP, SMB, NFS
// are currently supported.) Any time the user tries to open a connection using
// any protocol, this class is the intermediary between the GUI and the actual
// connection establishing classes. This puts much common functionality into
// one method (so in creating this I did some code cleanup.)
public class StartConnection
{
    public static FtpConnection con = null;
//    public static com.sshtools.j2ssh.configuration.SshConnectionProperties properties = new com.sshtools.j2ssh.configuration.SshConnectionProperties();
    public static String keyfile = null;

//    public static void setSshProperties(com.sshtools.j2ssh.configuration.SshConnectionProperties props)
//    {
//        properties = props;
//    }

    //data sent to startCon: protocol: (ie. FTP, SFTP, etc.)
    //                       htmp: hostname
    //                       utmp: username
    //                       ptmp: password
    //	                 potmp: port
    //			 dtmp: domain
    // (null data is sent if it is ever not applicable)
    //maybe it should just take an array of strings instead? (What's
    //stored in the array can then be determined by reading the 1st
    //entry, which is the protocol name)
    public static boolean startCon(String aLogin, String aKeyFile, String aPassPhrase, String aFrontale, String aPrefferedFrontale,
                                   boolean useLocal)
    {

        SftpConnection con;

        con = new SftpConnection(aLogin, aKeyFile, aPassPhrase, aFrontale, aPrefferedFrontale);

        int siteIndex = 0;
        for(int i = 0; i < G5kSite.getSitesNumber() ; i++){
            if(G5kSite.getInternalFrontals()[i].equalsIgnoreCase(aFrontale)){
                siteIndex = i;
                break;
            }
        }


        if(con.login())
        {

            if(useLocal)
            {
                JFtp.statusP.jftp.addLocalConnection(G5kSite.getSiteForIndex(siteIndex), con);
            }
            else
            {
                JFtp.statusP.jftp.addConnection(G5kSite.getSiteForIndex(siteIndex), con);
            }

            if(con.chdir(con.getPWD()) || con.chdir("/"))
            {
                ;
            }

            return true;
        }
        //return true only if all has executed as expected
        return true;
    }

    //startFtpCon
    private static void updateFileMenu(String[] searchValue)
    {
        int position;

        position = LastConnections.findString(searchValue, JFtp.CAPACITY);

        //bugfix: now a 2D array
        String[][] newVals = new String[JFtp.CAPACITY][JFtp.CONNECTION_DATA_LENGTH];

        if(position >= 0)
        {
            newVals = LastConnections.moveToFront(position, JFtp.CAPACITY);
        }
        else
        {
            newVals = LastConnections.prepend(searchValue, JFtp.CAPACITY, true);
        }
    }

    //updateFileMenu
}


//StartConnection
