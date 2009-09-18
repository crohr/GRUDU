/****************************************************************************/
/* [Documentation Here!]                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: BatchSchedulerListener.java,v 1.1 2007/09/28 16:02:07 aamar Exp $
 * $Log: BatchSchedulerListener.java,v $
 * Revision 1.1  2007/09/28 16:02:07  aamar
 * Initial revision
 *
 ****************************************************************************/
package diet.grid.api.util;

public interface BatchSchedulerListener {

    void addOutput(String msg);
    
    void addError(String msg);
}
