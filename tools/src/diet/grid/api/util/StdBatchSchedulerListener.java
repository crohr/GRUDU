/****************************************************************************/
/* [Documentation Here!]                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: StdBatchSchedulerListener.java,v 1.1 2007/09/28 16:02:07 aamar Exp $
 * $Log: StdBatchSchedulerListener.java,v $
 * Revision 1.1  2007/09/28 16:02:07  aamar
 * Initial revision
 *
 ****************************************************************************/
package diet.grid.api.util;

public class StdBatchSchedulerListener implements BatchSchedulerListener {
    /**
     * New line
     */
    public static String newline = System.getProperty("line.separator");

    public void addOutput(String msg) {
        System.out.println(msg + newline);
    }
    
    public void addError(String msg) {
        System.err.println(msg + newline);
    }
}
