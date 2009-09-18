/****************************************************************************/
/* SSH Agent class used to store the SSH passphrase in memory               */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: SSHAgent.java,v 1.2 2007/07/12 15:49:36 dloureir Exp $
 * $Log: SSHAgent.java,v $
 * Revision 1.2  2007/07/12 15:49:36  dloureir
 * Some javadoc and the correct header
 *
 ****************************************************************************/
package diet.gridr.g5k.util;

/**
 * SSH Agent class used to store the SSH passphrase in memory. This class is
 * declared as a singleton.
 * 
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class SSHAgent {

	/**
	 * The ssh key passphrase
	 */
	private String sshKey = null;
	
	/**
	 * the SSH Agent
	 */
	private static SSHAgent sshAgent = null;
	
	/**
	 * Private constructor
	 */
	private SSHAgent(){
		sshKey = "";
	}

	/**
	 * Method setting the SSH passphrase
	 * 
	 * @param sshKey the sshKey to set
	 */
	public synchronized void setSshKey(String sshKey) {
		this.sshKey = sshKey;
	}

	/**
	 * Method returning the SSH passphrase
	 * 
	 * @return the sshKey
	 */
	public synchronized String getSshKey() {
		return sshKey;
	}
	
	/**
	 * Method returning the SSHAgent
	 * 
	 * @return the SSH Agent
	 */
	public static SSHAgent getInstance(){
		if(sshAgent==null) sshAgent = new SSHAgent();
		return sshAgent;
	}
}