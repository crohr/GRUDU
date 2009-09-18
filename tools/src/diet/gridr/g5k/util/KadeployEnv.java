/****************************************************************************/
/* KaDeploy kernel environment class                                        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KadeployEnv.java,v 1.3 2007/07/12 15:42:12 dloureir Exp $
 * $Log: KadeployEnv.java,v $
 * Revision 1.3  2007/07/12 15:42:12  dloureir
 * Some typo corrections.
 *
 * Revision 1.2  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.1  2007/02/22 14:12:51  aamar
 * Initial revision.
 *
 ****************************************************************************/
package diet.gridr.g5k.util;

/**
 * KaDeploy kernel environment class
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class KadeployEnv {
    /**
     * Kernel name.
     */
    private String myName;

    /**
     * Kernel version.
     */
    private String myVersion;

    /**
     * User name.
     */
    private String myUser;

    /**
     * Kernel description.
     */
    private String myDescription;

    /**
     * Default constructor.
     *
     * @param name    kernel name
     * @param version kernel version (as in kaDeploy database)
     * @param user    user name (as in kaDeploy database)
     * @param description kernel description stored in kaDeploy database
     */
    public KadeployEnv(String name, String version, String user, String description) {
        this.myName = name;
        this.myVersion = version;
        this.myUser = user;
        this.myDescription = description;
    }

    /**
     * Method returning a string describing the object
     *
     * @return the string describing the object
     *
     */
    public String toString() {
        String str = "Envrionment : |" + this.myName        + "|\n";
        str += "    - version     : |" + this.myVersion     + "|\n";
        str += "    - user        : |" + this.myUser        + "|\n";
        str += "    - description : |" + this.myDescription + "|\n";
        return str;
    }

    /**
     * Method returning the name of the environment
     *
     * @return the nam of the environment
     */
    public String getName() {
        return this.myName;
    }
}
