/****************************************************************************/
/* This class corresponds to a console handler                              */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: MyConsoleHandler.java,v 1.4 2007/03/08 12:17:10 dloureir Exp $
 * $Log: MyConsoleHandler.java,v $
 * Revision 1.4  2007/03/08 12:17:10  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.3  2007/03/05 16:06:34  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.util.handlers;

import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import diet.logging.LoggingManager;

/**
 * This class corresponds to a console handler
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class MyConsoleHandler extends ConsoleHandler {

	/**
     * Method publishing the LogRecord to the native FileDescriptor
     *
	 * @see java.util.logging.ConsoleHandler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(LogRecord record) {
		// TODO Auto-generated method stub
		if(record.getLevel().intValue()>= this.getLevel().intValue()){
            String theMessage = record.getMessage();
			String msg = getFormatter().format(record);
            if(theMessage.startsWith("\n") ||
                    theMessage.startsWith("\t") || theMessage.startsWith(" ")) return;
			if(record.getLevel().intValue()> Level.INFO.intValue()){
			    try{
			        BufferedWriter bf = new BufferedWriter(new FileWriter(FileDescriptor.err));
			        bf.write(msg);
                    bf.flush();
			    }
                catch(Exception e){
                    LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "publish", e);
                }
			}
			else{
                try{
                    BufferedWriter bf = new BufferedWriter(new FileWriter(FileDescriptor.out));
                    bf.write(msg);
                    bf.flush();
                }
                catch(Exception e){
                    LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "publish", e);
                }
			}
		}
	}

	/**
	 * Default constructor
	 */
	public MyConsoleHandler() {
	}

}
