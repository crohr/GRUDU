/****************************************************************************/
/* This class corresponds to a LoggingUnit, the elements that defines the   */
/* level, the filter, the logger and the handlers used to log information.  */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: LoggingUnit.java,v 1.4 2007/03/05 16:06:34 dloureir Exp $
 * $Log: LoggingUnit.java,v $
 * Revision 1.4  2007/03/05 16:06:34  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.util;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import diet.logging.gui.LoggingUnitPropertiesPanel;
import diet.logging.util.filters.LoggingFilter;
import diet.logging.util.handlers.LoggingPanelHandler;


/**
 * This class corresponds to a LoggingUnit, the elements that defines the
* level, the logger and the handlers used to log information.
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class LoggingUnit {

    /**
     * The name of the LoggingUnit
     */
	private String name = null;
    /**
     * the filter for this LoggingUnit
     */
	private LoggingFilter filter = null;
    /**
     * List of Handlers where log information will be
     * redirected
     */
	private ArrayList<Handler> handlersList = null;
    /**
     * Logger that allows the logging of information
     */
	private Logger logger = null;
    /**
     * Parent LoggingUnit of this
     */
	private LoggingUnit parentLoggingUnit = null;
    /**
     * Default level of Logging
     */
	private Level level = Level.ALL;
    /**
     * LoggingPanel associated to this LoggingUnit
     */
    private JPanel loggingPanel = null;
    /**
     * LoggingUnitPropertiesPanel presenting information
     * about this LoggingUnit
     */
	private LoggingUnitPropertiesPanel propertiesPanel = null;

    /**
     * Method closing the LoggingUnit and its associated handlers
     *
     */
	public void close(){
		Iterator<Handler> handlers = handlersList.iterator();
		while(handlers.hasNext()){
			handlers.next().close();
		}
	}

	/**
     * Method returning the LoggingUnitPropertiesPanel
     *
	 * @return the propertiesPanel
	 */
	public LoggingUnitPropertiesPanel getPropertiesPanel() {
		return propertiesPanel;
	}

	/**
     * Method setting the LoggingUnitPropertiesPanel
     *
	 * @param propertiesPanel the propertiesPanel to set
	 */
	public void setPropertiesPanel(LoggingUnitPropertiesPanel propertiesPanel) {
		this.propertiesPanel = propertiesPanel;
	}

	/**
     * Method returning the level of this LoggingUnit
     *
	 * @return the level
	 */
	public Level getLevel() {
		return level;
	}

	/**
     * Method setting the level of this LoggingUnit
     *
	 * @param level the level to set
	 */
	public void setLevel(Level level) {
		logger.setLevel(level);
		this.level = level;
		Iterator<Handler> iter = handlersList.iterator();
		while(iter.hasNext()){
			Handler anHandler = iter.next();
			anHandler.setLevel(level);
		}
		propertiesPanel.updateview(level);

	}

    /**
     * Default constructor of this LoggingUnit
     *
     * @param aName the name of this LoggingUnit
     */
	public LoggingUnit(String aName){
		handlersList = new ArrayList<Handler>();
		name = aName;
		logger = Logger.getLogger(name);
		logger.setLevel(level);
		Handler[] listOfHandlers = logger.getHandlers();
		for(int i = 0 ; i < listOfHandlers.length ; i++){
			logger.removeHandler(listOfHandlers[i]);
		}
		propertiesPanel = new LoggingUnitPropertiesPanel(this);
	}

    /**
     * Method returning the LoggingPanelHandlerJTextPane
     *
     * @return the LoggingPanelHandlerJTextPane
     */
	public JTextPane getLoggingPanelHandlerJTextPane(){
		Iterator<Handler> iter = handlersList.iterator();
		while(iter.hasNext()){
			Handler anHandler = iter.next();
			if( anHandler instanceof LoggingPanelHandler) return ((LoggingPanelHandler)anHandler).getJTextPane();
		}
		return null;
	}

	/**
     * Method returning the ParentLoggingUnit of this
     * LoggingUnit
     *
	 * @return the parentLoggingUnit
	 */
	public LoggingUnit getParentLoggingUnit() {
		return parentLoggingUnit;
	}

	/**
     * Method setting the ParentLoggingUnit
     *
	 * @param parentLoggingUnit the parentLoggingUnit to set
	 */
	public void setParentLoggingUnit(LoggingUnit parentLoggingUnit) {
		this.parentLoggingUnit = parentLoggingUnit;
		logger.setParent(parentLoggingUnit.logger);
	}

    /**
     * Method adding a handler to the LoggingUnit
     *
     * @param anHandler
     */
	public void addHandler(Handler anHandler){
		handlersList.add(anHandler);
		anHandler.setFilter(filter);
		logger.addHandler(anHandler);
	}

    /**
     * Method removing a handler from this LoggingUnit
     *
     * @param anHandler
     */
	public void removeLoggerObject(Handler anHandler){
		handlersList.remove(anHandler);
		logger.removeHandler(anHandler);
	}

	/**
     * Method returning the filter of this LogginUnit
     *
	 * @return the filter
	 */
	public LoggingFilter getFilter() {
		return filter;
	}

	/**
     * Method setting the filter of this LoggingUnit
     *
	 * @param filter the filter to set
	 */
	public void setFilter(LoggingFilter filter) {
		this.filter = filter;
		logger.setFilter(filter);
		Iterator<Handler> iter = handlersList.iterator();
		while(iter.hasNext()){
			Handler anHandler = iter.next();
			anHandler.setFilter(filter);
		}
	}

    /**
     * Method returning the list of Handlers of this LoggingUnit
     *
     * @return an ArrayList of Handler
     */
	public ArrayList<Handler> getHandlersList() {
		return handlersList;
	}

    /**
     * Method setting the ArrayList of Handler
     *
     * @param handlersList the list of Handler
     */
	public void setHandlersList(ArrayList<Handler> handlersList) {
		this.handlersList = handlersList;
	}

	/**
     * Method returning the name of this LoggingUnit
     *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
     * Methode setting the name of the LoggingUnit
     *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Method that logs a message of a certain level, for a certain class and method
     *
     * @param levelOfLog level of log
     * @param aClass class from which we are logging
     * @param aMethod method from which we are logging
     * @param aMessage message to log
     */
	public void log(Level levelOfLog, String aClass, String aMethod, String aMessage){
		logger.logp(levelOfLog, aClass, aMethod, aMessage);
	}

    /**
     * Method defining if we have to use the parent handler
     *
     * @param aBool a value defining the if we have to use the parent handler
     */
	public void setUseParentHandlers(boolean aBool){
		logger.setUseParentHandlers(aBool);
	}

    /**
     * Method returning a vector of vector of String for the use in a table
     *
     * @return structure for a table
     */
	public Vector<Vector<String>> getHandlersForTable(){
		Vector<Vector<String>> vector = new Vector<Vector<String>>();
		if(parentLoggingUnit == null){
			Vector<String> vectorTemp0 = new Vector<String>();
			vectorTemp0.add("Console");
			vectorTemp0.add("Terminal");
			vectorTemp0.add("Plain Text");
			vector.add(vectorTemp0);
		}
		Vector<String> vectorTemp1 = new Vector<String>();
		vectorTemp1.add("File");
		vectorTemp1.add(System.getProperty("user.home") + ".diet/log/Log_"+name+".log");
		vectorTemp1.add("Plain Text");
		vector.add(vectorTemp1);
		Vector<String> vectorTemp2 = new Vector<String>();
		vectorTemp2.add("File");
		vectorTemp2.add(System.getProperty("user.home") + ".diet/log/Log_"+name+".html");
		vectorTemp2.add("HTML");
		vector.add(vectorTemp2);
		Vector<String> vectorTemp3 = new Vector<String>();
		vectorTemp3.add("Object");
		vectorTemp3.add("Logger " + name + " in LoggerFrame");
		vectorTemp3.add("Swing");
		vector.add(vectorTemp3);
		return vector;
	}

    /**
     * Method returning the LoggingPanel where the information are
     * stored for the LoggingFrame
     *
     * @param dim dimension of the LoggingPanel
     * @return a Panel for the LoggingFrame
     */
    public JPanel getLoggingPanel(Dimension dim){
        if(loggingPanel == null){
            loggingPanel = new JPanel();
            JTabbedPane jTabbedPane = new JTabbedPane();
            JTextPane jTextPane = this.getLoggingPanelHandlerJTextPane();
            JScrollPane scrollPane2 = new JScrollPane();
            scrollPane2.setMaximumSize(new Dimension((int)(dim.width-dim.width*0.25)-15,dim.height-75));
            scrollPane2.setPreferredSize(new Dimension((int)(dim.width-dim.width*0.25)-15,dim.height-75));
            scrollPane2.setMinimumSize(new Dimension((int)(dim.width-dim.width*0.25)-15,dim.height-75));
            jTextPane.setMaximumSize(new Dimension((int)(dim.width-dim.width*0.25)-15,dim.height-75));
            jTextPane.setMinimumSize(new Dimension((int)(dim.width-dim.width*0.25)-15,dim.height-75));
            jTextPane.setPreferredSize(new Dimension((int)(dim.width-dim.width*0.25)-15,dim.height-75));
            scrollPane2.setViewportView(jTextPane);
            scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jTextPane.setCaretPosition(jTextPane.getDocument().getLength());
            jTabbedPane.addTab("Logging window", scrollPane2);
            jTabbedPane.addTab("Preferences", new LoggingUnitPropertiesPanel(this));
            loggingPanel.add(jTabbedPane);
        }
        return loggingPanel;
    }

}
