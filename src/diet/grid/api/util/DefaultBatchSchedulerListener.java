/****************************************************************************/
/* [Documentation Here!]                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: DefaultBatchSchedulerListener.java,v 1.5 2007/11/07 10:39:25 dloureir Exp $
 * $Log: DefaultBatchSchedulerListener.java,v $
 * Revision 1.5  2007/11/07 10:39:25  dloureir
 * Correcting the call to the addOutput method of the listener
 *
 * Revision 1.4  2007/11/07 10:26:03  dloureir
 * Removing the logging of the output
 *
 * Revision 1.3  2007/10/08 14:53:57  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.2  2007/10/08 08:34:27  dloureir
 * Adding logging features to the default batch scheduler listener
 *
 * Revision 1.1  2007/09/28 16:02:07  aamar
 * Initial revision
 *
 ****************************************************************************/
package diet.grid.api.util;

import java.util.logging.Level;

import diet.logging.LoggingManager;

public class DefaultBatchSchedulerListener implements BatchSchedulerListener {
    
    public void addOutput(String msg) {
    	LoggingManager.log(Level.FINE, 
		        LoggingManager.RESOURCESTOOL, 
		        this.getClass().getName(), 
		        "addOutput", 
		        msg);
    }
    
    public void addError(String msg) {
    	LoggingManager.log(Level.WARNING, 
		        LoggingManager.RESOURCESTOOL, 
		        this.getClass().getName(), 
		        "addError", 
		        msg);
    }

}
