/****************************************************************************/
/* This class corresponds to a formatter that will give format the text log */
/* information to a plain text format                                       */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: PlainTextFormatter.java,v 1.2 2007/03/05 16:06:33 dloureir Exp $
 * $Log: PlainTextFormatter.java,v $
 * Revision 1.2  2007/03/05 16:06:33  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.util.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * This class corresponds to a formatter that will give format the text log
* information to a plain text format
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class PlainTextFormatter extends Formatter {

    /**
     * Module name
     */

	private String module = null;

    /**
     * Method returning the head of the plain text file that will be produced
     *
     * @see java.util.logging.Formatter#getHead(java.util.logging.Handler)
     */
	@Override
	public String getHead(Handler h) {
		String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		if(module != null) return date + " :: " +module+" Plain Text Log file\n\n";
		else return "";
	}

    /**
     * Default constructor of the PlainTextFormatter
     *
     * @param aModule name of the module associated to fhis formatter
     */
	public PlainTextFormatter(String aModule) {
		// TODO Auto-generated constructor stub
		if(aModule != null) module = aModule;
	}

    /**
     * Main method of the formatter; This method take the LogRecord and produced
     * a plain text readable line corresponding to the information to Log
     *
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
	@Override
	public String format(LogRecord record) {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss-SSS");
		String dateAndTime = "["+formatter.format(new Date(record.getMillis()))+"] ";
		String level = record.getLevel().getName();
		String className = record.getSourceClassName();
		String methodName = record.getSourceMethodName();
		String message = record.getMessage();
		String module = record.getLoggerName();
		String result = dateAndTime + level.toUpperCase() + " " + module + " " +className + " " + methodName + " :: " + message;
		return result + "\n";
	}

}
