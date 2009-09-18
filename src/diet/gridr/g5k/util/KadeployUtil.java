/****************************************************************************/
/* Utility class for Kadeploy                                               */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KadeployUtil.java,v 1.11 2008/01/07 16:56:07 dloureir Exp $
 * $Log: KadeployUtil.java,v $
 * Revision 1.11  2008/01/07 16:56:07  dloureir
 * Adding the -ttt for sites when asking for tty
 *
 * Revision 1.10  2007/11/19 15:16:20  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.9  2007/10/17 15:51:45  dloureir
 * Adding a little "hack" for the support of the classic ssh usage with OAR2
 *
 * Revision 1.8  2007/07/12 15:42:12  dloureir
 * Some typo corrections.
 *
 * Revision 1.7  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.6  2007/03/07 15:00:13  dloureir
 * Correctings some bugs or the javadoc
 *
 * Revision 1.5  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.4  2007/02/27 14:10:07  dloureir
 * Adding sol cluster of sophia and removing some print outs
 *
 * Revision 1.3  2007/02/23 13:13:25  aamar
 * Changing getKadeployEnv method to return kadeploy users also.
 *
 * Revision 1.2  2007/02/14 13:24:29  aamar
 * Removing ^M.
 *
 * Revision 1.1  2007/02/14 13:23:20  aamar
 * Initial revision.
 *
 ****************************************************************************/

package diet.gridr.g5k.util;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;
import diet.logging.LoggingManager;

/**
 * Utility class for Kadeploy
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class KadeployUtil {
	/**
	 * DEFAULT queue
	 */
	public static final int default_queue = 0;

	/**
	 * KADEPLOY queue
	 */
	public static final int kadeploy_queue = 1;

	/**
	 * OAR queues names
	 */
	public static final String [] queues = {"default", "deploy","allow_classic_ssh"};

	/**
	 * Get the queue name
	 *
	 * @param index index of the queue we want
	 * @return the name of the queue
	 */
	public static String getQueue(int index) {
		if (index < 0 || index >= queues.length)
			return queues[0];
		return queues[index];
	}

	/**
	 * Deploy an environment
	 *
	 * @param conn       the ssh connection to the gateway
	 * @param oarHost    the OAR host
	 * @param listener   the Kadeploy "listener"
	 * @param envName    environment name
	 * @param node the   host name
	 * @param partition partition device
	 *
	 * @return the corresponding KadeployThread
	 */
	public static KadeployThread kadeploy(Connection conn, String oarHost, KadeployListener listener,
			String envName, String node, String partition) {
		KadeployThread thread = new KadeployThread(conn, oarHost, listener, envName, node, partition);
		thread.start();
		return thread;
	} // end kadeploy

	/**
	 * Get the existing environment on a cluster
	 *
	 * @param conn         the ssh connection to the gateway
	 * @param oarHost    the OAR host
	 *
	 * @return the corresponding KaenvironmentHelper
	 */
	public static KaenvironmentHelper getKadeployEnv(Connection conn, String oarHost) {
		// ssh oarHost "kaenvironments --environment"
		Vector<KadeployEnv> envs = new Vector<KadeployEnv>();
		Vector<String> users = new Vector<String>();
		try {
			Session sess = conn.openSession();
			sess.execCommand("ssh -ttt -o ConnectTimeout=5 " +
					oarHost+
			" \"kaenvironments --environment\"");
			InputStream stdout = new StreamGobbler(sess.getStdout());
			InputStream stderr = new StreamGobbler(sess.getStderr());

			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));

			boolean env_part = false;
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				if (env_part) {
					// pattern
					// Name  Version User Description
					line = line.trim();
					if (line.length() != 0) {
						String name = line.substring(0, line.indexOf(' '));
						line = line.substring(line.indexOf(' '));
						line = line.trim();
						String version = line.substring(0, line.indexOf(' '));
						line = line.substring(line.indexOf(' '));
						line = line.trim();
						String user = line.substring(0, line.indexOf(' '));
						line = line.substring(line.indexOf(' '));
						line = line.trim();
						String description = line;
						KadeployEnv kenv = new KadeployEnv(name, version, user, description);
						if(user.equalsIgnoreCase(G5kCfg.get(G5kCfg.USERNAME))) LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, KadeployEnv.class.getName(), "getKadeployEnv", kenv.toString());
						envs.add(kenv);
					}
				} // end if env


				if (line.contains("Name") &&
						line.contains("Version") &&
						line.contains("user") &&
						line.contains("Description")
				) {
					// read a line of '------'
					br.readLine();
					env_part = true;
				} // end if line.contains
			} // end while

			boolean users_part = false;
			while (true) {
				String line = brerr.readLine();
				if (line == null)
					break;
				if (users_part) {
					// pattern
					// [User]
					    // User
					line = line.trim();
					String user = "";
					if (line.length() != 0) {
						if (line.charAt(0) == '[') {
							user = line.substring(1, line.length() - 1);
						}
						else {
							user = line;
						}
						if(user.equalsIgnoreCase(G5kCfg.get(G5kCfg.USERNAME))) LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, KadeployUtil.class.getName(), "getKadeployEnv","Kadeploy user  |" + user + "|");
						users.add(user);
					}
				} // end if users
				if (line.contains("kadeploy users")) {
					// read a line of '------'
					brerr.readLine();
					users_part = true;
				} // end if line of '------'
			} // end while


			brerr.close();
			br.close();
			sess.close();

		}
		catch (Exception e) {
			LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, KadeployUtil.class.getName(), "getKadeployEnv", e);
		}
		KaenvironmentHelper kaenvironment = new KaenvironmentHelper(G5kCluster.getClusterByOar(oarHost),
				envs, users);

		return kaenvironment;
	}
}