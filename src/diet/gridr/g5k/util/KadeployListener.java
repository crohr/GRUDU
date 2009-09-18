/****************************************************************************/
/* KaDeploy Listener Interface                                              */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: KadeployListener.java,v 1.3 2007/07/12 15:42:12 dloureir Exp $
 * $Log: KadeployListener.java,v $
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
 * Kadeploy Listener Interface
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public interface KadeployListener {
    /**
     * Set the progression state of a deployment process
     *
     * @param node the host name
     * @param step the deployment step
     */
    public void setProgression(String node, int step);

    /**
     * Add output log of deployment process.
     *
     * @param node the host name
     * @param line the output line
     */
    public void addOutput(String node, String line);

    /**
     * Add error log of deployment process.
     *
     * @param node the host name
     * @param line the error output
     */
    public void addError(String node, String line);

    /**
     * Set the state of deployment of node as DONE.
     *
     * @param node the host name
     */
    public void setAsDone(String node);
}