/****************************************************************************/
/* This class corresponds to a handler that allows the logging of the       */
/* informations in a JTextPane for the LoggingFrame                         */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: LoggingPanelHandler.java,v 1.3 2007/03/08 12:17:10 dloureir Exp $
 * $Log: LoggingPanelHandler.java,v $
 * Revision 1.3  2007/03/08 12:17:10  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.2  2007/03/05 16:06:34  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.util.handlers;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import diet.logging.LoggingManager;
import diet.logging.util.formatter.LoggingUtil;

/**
 * This class corresponds to a handler that allows the logging of the
 * informations in a JTextPane for the LoggingFrame
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class LoggingPanelHandler extends Handler {

	/**
     * Method defining that a LogRecord is Loggable
     *
     *
	 * @see java.util.logging.Handler#isLoggable(java.util.logging.LogRecord)
	 */
	@Override
	public boolean isLoggable(LogRecord record) {
		// TODO Auto-generated method stub
		return getFilter().isLoggable(record);
	}

    /**
     * LoggingPanel where the LogRecords will be written
     */
	JTextPane loggingPanel = null;
    /**
     * Document of the JTextPane
     */
	DefaultStyledDocument document = null;
	public static final String PLAIN_STYLE = "plain";
	public static final String DATEANDTIME_STYLE = "dateAndTime";
	public static final String LEVEL_STYLE = "level";
	public static final String MODULE_STYLE = "module";
	public static final String CLASSANDMETHOD_STYLE = "classAndMethod";
	public static final String MESSAGE_STYLE = "message";

	/**
     * Default constructor of the LoggingPanel Handler
     *
	 */
	public LoggingPanelHandler() {
		// TODO Auto-generated constructor stub
		super();
		loggingPanel = new JTextPane();
		loggingPanel.setEditable(false);
		loggingPanel.setAutoscrolls(true);
		document = new DefaultStyledDocument();
		loggingPanel.setDocument(document);
		initializingStyles();
	}

    /**
     * Method initializing the styles for the JTextPane
     *
     */
	private void initializingStyles(){
		Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		// default style
		Style plain = loggingPanel.addStyle(PLAIN_STYLE, defaultStyle);
		// date Style : plain + bold
		Style dateAndTime = loggingPanel.addStyle(DATEANDTIME_STYLE, plain);
		StyleConstants.setForeground(dateAndTime, Color.BLACK);
		StyleConstants.setBold(dateAndTime, true);
		// level Style : plain + underlined
		Iterator<Level> levelIterator = LoggingUtil.colorLevels.keySet().iterator();
		while(levelIterator.hasNext()){
			Color aColor = LoggingUtil.getColorFromLevel(levelIterator.next());
			Style level = loggingPanel.addStyle(LEVEL_STYLE+"_" + aColor.toString(), plain);
			StyleConstants.setUnderline(level, true);
			StyleConstants.setForeground(level, aColor);
			// module style : plain + bold
			Style module = loggingPanel.addStyle(MODULE_STYLE+"_" + aColor.toString(), plain);
			StyleConstants.setBold(module, true);
			StyleConstants.setForeground(module, aColor);
			// classAndSource style : plain + italic
			Style classAndMethod = loggingPanel.addStyle(CLASSANDMETHOD_STYLE+"_" + aColor.toString(), plain);
			StyleConstants.setItalic(classAndMethod,true);
			StyleConstants.setForeground(classAndMethod, aColor);
			// message style : plain
			Style message = loggingPanel.addStyle(MESSAGE_STYLE+"_" + aColor.toString(), plain);
			StyleConstants.setForeground(message, aColor);
		}

	}

    /**
     * Method returning the JTextPane
     *
     * @return JTextPane of the Handler
     */
	public JTextPane getJTextPane(){
		return loggingPanel;
	}


	/**
     * Method closing the handler
     * ( here it is a dummy method beacause there is nothing to close ...)
     *
	 * @see java.util.logging.Handler#close()
	 */
	@Override
	public void close() throws SecurityException {

	}

	/**
     * Method flushing the JTextPane
     *
     * @see java.util.logging.Handler#flush()
	 */
	@Override
	public void flush() {
		// TODO Auto-generated method stub
		try{
			document.insertString(document.getLength(), "\n", loggingPanel.getStyle("plain"));
			loggingPanel.setCaretPosition(document.getLength());
		}
		catch(Exception e){
			LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "flush", e);
		}
	}

	/**
     * Method publishing a LogRecord to this handler
     *
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(LogRecord record) {
		if(record.getLevel().intValue() >= this.getLevel().intValue()){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			String dateAndTime = formatter.format(new Date(record.getMillis()));
			String level = record.getLevel().getName();
			String className = record.getSourceClassName();
			String methodName = record.getSourceMethodName();
			String message = record.getMessage();
			String module = record.getLoggerName();
			Color levelColor = LoggingUtil.getColorFromLevel(record.getLevel());
			try{
				String levelColorString = levelColor.toString();
				document.insertString(document.getLength(), "["+dateAndTime+"]", loggingPanel.getStyle(DATEANDTIME_STYLE));
				document.insertString(document.getLength(), " ", loggingPanel.getStyle(PLAIN_STYLE));
				document.insertString(document.getLength(), level, loggingPanel.getStyle(LEVEL_STYLE+"_"+levelColor.toString()));
				document.insertString(document.getLength(), " ", loggingPanel.getStyle(PLAIN_STYLE));
				document.insertString(document.getLength(), module, loggingPanel.getStyle(MODULE_STYLE+"_"+levelColor.toString()));
				document.insertString(document.getLength(), " ", loggingPanel.getStyle(PLAIN_STYLE));
				document.insertString(document.getLength(), className + " " + methodName, loggingPanel.getStyle(CLASSANDMETHOD_STYLE+"_"+levelColor.toString()));
				document.insertString(document.getLength(), " ", loggingPanel.getStyle(PLAIN_STYLE));
				document.insertString(document.getLength(), ":: " + message, loggingPanel.getStyle(MESSAGE_STYLE+"_"+levelColor.toString()));
			}catch(Exception e){
				LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "publish", e);
			}
			flush();
		}
	}

}
