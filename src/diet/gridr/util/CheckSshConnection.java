/****************************************************************************/
/* Utility class to check ssh connection.                                   */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: CheckSshConnection.java,v 1.4 2007/07/12 15:56:54 dloureir Exp $
 * $Log: CheckSshConnection.java,v $
 * Revision 1.4  2007/07/12 15:56:54  dloureir
 * Some javadoc with the deprecated tag
 *
 * Revision 1.3  2007/05/22 07:52:39  aamar
 * Correct headers.
 *
 ****************************************************************************/
package diet.gridr.util;

/**
 * Utility class to check ssh connection.
 * 
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * 
 * @deprecated This class is now deprecated because it is no more used
 *
 */
public class CheckSshConnection {
	/**
	 * @deprecated
	 * 
	 * @param user
	 * @param host
	 * @return
	 */
    public static boolean check(String user, String host) {
	String [] cmdArray = { "ssh",
			      user+"@"+host,
			      "echo toto > /dev/null"};
	try {
	    Process child = Runtime.getRuntime().exec(cmdArray);
	    if (child.waitFor() != 0)  { // if the ssh failed  
		System.out.println("the ssh failed");
		return false;
	    }	
	}
	catch (Exception e) {
	    System.out.println(e.toString()); 
	    return false;
	}
	
	
	return true;
    }

    /**
     * @deprecated
     * 
     * @param user
     * @param host1
     * @param host2
     * @return
     */
    public static boolean check(String user, String host1, String host2) {
	String [] cmdArray = { "ssh",
			       user+"@"+host1,
			       "ssh "+ host2,
			      "echo toto > /dev/null"};
       try { 
	   Process child = Runtime.getRuntime().exec(cmdArray);
	   if (child.waitFor() != 0)  { // if the ssh failed  
	       System.out.println("the ssh failed");
	       return false;
	   }	
       }
       catch (Exception e) {
	   System.out.println(e.toString()); 
	   return false;
       }

       return true;
    }

}
