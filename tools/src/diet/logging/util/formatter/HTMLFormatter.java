/****************************************************************************/
/* This class corresponds to a formatter that will give format the text log */
/* information to a HTML format                                             */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: HTMLFormatter.java,v 1.3 2007/03/05 16:06:33 dloureir Exp $
 * $Log: HTMLFormatter.java,v $
 * Revision 1.3  2007/03/05 16:06:33  dloureir
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
 * information to a HTML format
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class HTMLFormatter extends Formatter{

    /**
     * Module name
     */
	private String module = null;

	/**
     * Method returning the head of the HTML file that will be produced
     *
	 * @see java.util.logging.Formatter#getHead(java.util.logging.Handler)
	 */
	@Override
	public String getHead(Handler h) {
		String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
		String head = "<html>" +
				"<head>" +
				"<title>Log file from the DIET_DashBoard -- module = " + module +
				"</title>" +
				"</head>" +
				"<body>" +
				"<h1>"+date + " :: " + module +" HTML Log file</h1><br>";
		return head;

	}

	/**
     * Method returning the tail of the HTML file that will be produced
     *
	 * @see java.util.logging.Formatter#getTail(java.util.logging.Handler)
	 */
	@Override
	public String getTail(Handler h) {
		String tail = "</body>" +
				"</html>";
		return tail;
	}

    /**
     * Default constructor of the HTMLFormatter
     *
     * @param moduleName name of the module associated to fhis formatter
     */
	public HTMLFormatter(String moduleName) {
		module = moduleName;
	}

	/**
     * Main method of the formatter; This method take the LogRecord and produced
     * a HTML readable line corresponding to the information to Log
     *
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord record) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String dateAndTime = "<b>["+formatter.format(new Date(record.getMillis()))+"]</b>  ";
		String level = "<font color=\""+ LoggingUtil.getHTMLColorFromLevel(record.getLevel())+"\"><u>" + record.getLevel().getName() + "</u> ";
		String module = "<b>" + record.getLoggerName() + "</b> ";
		String className = "<i>" + record.getSourceClassName() + " ";
		String methodName = record.getSourceMethodName()+"</i> ";
        String messageAdapted = record.getMessage().replace("\n", "</br>");
		String message = " :: " + messageAdapted + "</font>";
		String result = dateAndTime + level + module + className + methodName + message + "<br>";
		return result;
	}

}
