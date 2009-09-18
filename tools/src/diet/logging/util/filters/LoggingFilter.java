/****************************************************************************/
/* This class corresponds to a filter for the Logging                       */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: LoggingFilter.java,v 1.2 2007/03/05 16:06:33 dloureir Exp $
 * $Log: LoggingFilter.java,v $
 * Revision 1.2  2007/03/05 16:06:33  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.util.filters;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * This class corresponds to a filter for the Logging
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class LoggingFilter implements Filter {

    /**
     * Level of Log
     */
	private Level minimumLevel = Level.FINEST;

	/**
     * Method returning the minimum level of log
     *
	 * @return the minimumLevel of log
	 */
	public Level getMinimumLevel() {
		return minimumLevel;
	}



	/**
     * Method setting the minimum level of log
     *
	 * @param minimumLevel the minimumLevel to set
	 */
	public void setMinimumLevel(Level minimumLevel) {
		this.minimumLevel = minimumLevel;
	}

	/**
	 * Default constructor of the class
	 */
	public LoggingFilter() {
		// TODO Auto-generated constructor stub
	}



	/**
     * Method defining if the LogRecord is Loggable
     *
	 * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
	 */
	public boolean isLoggable(LogRecord record) {
//		if(record.getLevel().intValue() > minimumLevel.intValue())	return true;
//		else return false;
		return true;
	}

}
