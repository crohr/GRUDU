/****************************************************************************/
/* This class is the main element for logging                               */
/* Through the use of this class you can log information concerning the way */
/* it evolves.                                                              */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: LoggingManager.java,v 1.8 2007/07/11 15:41:30 dloureir Exp $
 * $Log: LoggingManager.java,v $
 * Revision 1.8  2007/07/11 15:41:30  dloureir
 * Some modification correpsonding to the addition of the application wide properties
 *
 * Revision 1.7  2007/03/08 12:17:09  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.6  2007/03/05 16:06:34  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import diet.application.ApplicationConfiguration;
import diet.logging.util.LoggingUnit;
import diet.logging.util.filters.LoggingFilter;
import diet.logging.util.formatter.HTMLFormatter;
import diet.logging.util.formatter.PlainTextFormatter;
import diet.logging.util.handlers.LoggingPanelHandler;
import diet.logging.util.handlers.MyConsoleHandler;
import diet.util.gui.WaitingFrame;

/**
 * This class is the main element for logging
 * Through the use of this class you can log information concerning the way
 * it evolves.
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class LoggingManager {

    /**
     * Parameter for the logging of the DIET Deploy Tool
     */
	public static final String DIETDEPLOYTOOL = "DIETDeployTool";
    /**
     * Parameter for the logging of DIET Designer Tool
     */
	public static final String DIETDESIGNERTOOL = "DIETDesignerTool";
    /**
     * Parameter for the logging of the DIET Mapping Tool
     */
	public static final String DIETMAPPINGTOOL = "DIETMappingTool";
    /**
     * Parameter for the logging of the XMLGoDIETGenerator
     */
	public static final String XMLGODIETGENERATOR = "XMLGoDIETGenerator";
    /**
     * Parameter for the logging of the VizDIET
     */
	public static final String VIZDIET = "VizDIET";
    /**
     * Parameter for the logging of the WorkFlow Designer
     */
	public static final String WFDESIGNER = "WFDesigner";
    /**
     * Parameter for the logging of the WorkFlow Log Service
     */
	public static final String WFLOGSERVICE = "WFLogService";
    /**
     * Parameter for the logging of the Resources tool
     */
	public static final String RESOURCESTOOL = "ResourcesTool";
    /**
     * Parameter for the logging of the Logging Tool
     */
	public static final String LOGGINGTOOL = "LoggingTool";
    /**
     * Parameter for the logging of the DIET_DashBoard
     */
	public static final String DIETDASHBOARD = "DIET_DashBoard";
	/**
     * HashMap containing the loggingUnit (does not contained the root logging Unit)
	 */
	private HashMap<String, LoggingUnit> loggingUnitMap = null;
    /**
     * Root Logging Unit
     * It corresponds to the LoggingUnit of the DIET_DashBoard when the
     * platformType is RUN_WITH_DASHBOARD of to the LoggingUnit of the
     * Resources Tool when the platformType is RUN_STANDALONE
     */
	private LoggingUnit rootLoggingUnit = null;
    /**
     * Log directory that will contain the log files
     */
	private static File logDirectory = initializeLogDirectory();
    /**
     * LoggingManager that will realize the logging
     */
	private static LoggingManager loggingManager = null;

	/**
     * Method returning the root LoggingUnit
     *
	 * @return the rootLoggingUnit
	 */
	public  LoggingUnit getRootLoggingUnit() {
		if(rootLoggingUnit == null){
			initializedRootLoggingUnit();
		}
		return rootLoggingUnit;
	}

    /**
     * Method allowing the addition of a LoggingUnit from a
     * identifier
     *
     * @param anIdentifier
     */
	public void addLoggingUnit(String anIdentifier){
		LoggingManager.log(Level.CONFIG, LOGGINGTOOL, this.getClass().getName(), "addLoggingUnit", "adding " + anIdentifier + " logger");
		LoggingManager.log(Level.CONFIG, anIdentifier, this.getClass().getName(), "addLoggingUnit", "adding " + anIdentifier + " logger");
		loggingUnitMap.put(anIdentifier, getLoggingUnit(this, anIdentifier));
	}

	/**
     * Method returning the file corresponding to the
     * Log directry
     *
	 * @return the logDirectory
	 */
	public static File getLogDirectory() {
		return logDirectory;
	}

    /**
     * Method returning the LoggingUnit HashMap
     *
     * @return Hashmap containing the Logging Units
     */
	public HashMap<String, LoggingUnit> getLoggingUnitMap(){
		return loggingUnitMap;
	}

    /**
     * Defualt private constructor disabling any construction of a
     * LoggingManager outside this class and its getInstance methods
     *
     */
	private LoggingManager(){

	}

    /**
     * Method initalizing the log directory
     *
     * @return the file corresponding to the logDirectory where
     * log information will be stored
     */
	private static File initializeLogDirectory(){
		File aLogDirectory = new File(System.getProperty("user.home") + "/.diet/log");
        if(aLogDirectory.exists()){
            if(aLogDirectory.isFile()){
                System.err.println("ERROR : " + aLogDirectory.getAbsolutePath() + "already exists and is not a directory !!!");
                boolean exists = true;
                int i = 0;
                while(exists){
                    aLogDirectory = new File(System.getProperty("user.home") + "/.diet/log_ " + i);
                    if(!aLogDirectory.exists()) break;
                }
                System.err.println("WARNING : Log Directory has been changed to : " + aLogDirectory.getAbsolutePath()  );
            }else{
                File[] listOfFiles = aLogDirectory.listFiles();
                for(int i = 0 ; i < listOfFiles.length ; i ++){
                    if(!listOfFiles[i].delete()) System.err.println("Unable to delete the file : " + listOfFiles[i]);
                }
            }
        }else{
            aLogDirectory.mkdirs();
        }
		return aLogDirectory;
	}

    /**
     * Method initializing the LoggingManager
     *
     * @param modulesToLog the ArrayList should contain the identifier of the
     * Modules you want to get log from
     */
	private void initializeLoggingManager(ArrayList<String> modulesToLog){
		loggingUnitMap = new HashMap<String, LoggingUnit>();
		addLoggingUnit(LOGGINGTOOL);
		initializedRootLoggingUnit();
		log(Level.CONFIG,LOGGINGTOOL,this.getClass().getName(),"initializeLoggingManager","rootLoggingUnit initialized");
		if(modulesToLog != null){
			initializedModulesLoggingUnits(modulesToLog);
		}
		log(Level.CONFIG,LOGGINGTOOL,this.getClass().getName(),"initializeLoggingManager","Logging Manager initialized");
	}

//    /**
//     * Method giving you back a LoggingManager initialized with only the root directory
//     * defined and initialized with the RUN_WITH_DASHBOARD
//     *
//     * @return an instance of the LoggingManager
//     */
//	public static LoggingManager getInstance(){
//		if(loggingManager == null) initialize(null,Platform.RUN_WITH_DASHBOARD);
//		return loggingManager;
//	}

    /**
     * Method giving you back a LoggingManager initialized with only the root directory
     * defined and initialized with the application context
     *
     * @return an instance of the LoggingManager
     */
	public static LoggingManager getInstance(){
		if(loggingManager == null) initialize(null);
		return loggingManager;
	}

    /**
     * Method closing the LoggingManager
     *
     */
	public static void close(){
		int value = loggingManager.loggingUnitMap.size() + 7;
		final WaitingFrame myFrame =  new WaitingFrame("Operation in progress","Closing application, please wait...",value,false);
		myFrame.launch(null);
		Thread th = new Thread(new Runnable(){
			public void run(){
				Iterator<String> iter = loggingManager.loggingUnitMap.keySet().iterator();
				while(iter.hasNext()){
					String name = iter.next();
					LoggingManager.log(Level.CONFIG, LOGGINGTOOL, LoggingManager.class.getName(), "close", "closing "+ name + " logger");
					LoggingManager.log(Level.CONFIG, name, LoggingManager.class.getName(), "close", "closing "+ name + " logger");
					(loggingManager.loggingUnitMap.get(name)).close();
					myFrame.incrementProgressBar();
				}
				LoggingManager.log(Level.CONFIG, LOGGINGTOOL, LoggingManager.class.getName(), "close", "closing General logger");
                String identifier = ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.DIETDASHBOARD_CONTEXT)?DIETDASHBOARD:RESOURCESTOOL;
                LoggingManager.log(Level.CONFIG, identifier, LoggingManager.class.getName(), "close", "closing "+ identifier + " logger");
				loggingManager.rootLoggingUnit.close();
				myFrame.incrementProgressBar();
				File fileTodelete = new File(logDirectory.getAbsolutePath()+"/Log_"+identifier+".log");
				fileTodelete.delete();
				myFrame.incrementProgressBar();
				fileTodelete = new File(logDirectory.getAbsolutePath()+"/Log_"+identifier+".html");
				fileTodelete.delete();
				myFrame.incrementProgressBar();
				fileTodelete = new File(logDirectory.getAbsolutePath()+"/Log_"+identifier+".log.lck");
				fileTodelete.delete();
				myFrame.incrementProgressBar();
				fileTodelete = new File(logDirectory.getAbsolutePath()+"/Log_"+identifier+".html.lck");
				fileTodelete.delete();
				myFrame.incrementProgressBar();
				fileTodelete = new File(logDirectory.getAbsolutePath()+"/Log_"+identifier+".log.1");
				fileTodelete.renameTo(new File(logDirectory.getAbsolutePath()+"/Log_"+identifier+".log"));
				myFrame.incrementProgressBar();
				fileTodelete = new File(logDirectory.getAbsolutePath()+"/Log_"+identifier+".html.1");
				fileTodelete.renameTo(new File(logDirectory.getAbsolutePath()+"/Log_"+identifier+".html"));
				myFrame.incrementProgressBar();
				myFrame.dispose();
			}
		});
		th.start();
	}

	/**
	 * Method initializing the LoggingManager with the modulesToLog (corresponding to
     * the modules that should be logged) and the platformTypeInt (corresponding to
     * the platform that should be logged)
	 */
	public static void initialize(ArrayList<String> modulesToLog) {

		if(loggingManager == null) loggingManager = new LoggingManager();
		loggingManager.initializeLoggingManager(modulesToLog);
	}

    /**
     * Method initializing the LoggingUnits avec l'ArrayList modulestoLog
     *
     * @param modulesToLog ArrayList containing the identifiers of the modules
     * to log
     */
	private void initializedModulesLoggingUnits(ArrayList<String> modulesToLog){
		if(rootLoggingUnit == null) initializedRootLoggingUnit();
		Iterator<String> iter = modulesToLog.iterator();
		while(iter.hasNext()){
			String anIndentifier = iter.next();
			loggingUnitMap.put(anIndentifier, getLoggingUnit(this,anIndentifier));
			log(Level.CONFIG,LOGGINGTOOL,this.getClass().getName(),"initializedModulesLoggingUnits","Logging Module "+ anIndentifier +"initialized");
		}
	}

    /**
     * Main method for the logging of informations
     *
     * @param levelOfLog level of Log
     * @param module module form which we are logging information
     * @param aClass the class from which we are logging information
     * @param aMethod the method from which we are logging information
     * @param aMessage a log message
     */
	public static void log(Level levelOfLog, String module, String aClass, String aMethod, String aMessage){

		Iterator<String> iter = loggingManager.loggingUnitMap.keySet().iterator();
		while(iter.hasNext()){
			String name = iter.next();
			if(name.equalsIgnoreCase(module)){
				loggingManager.loggingUnitMap.get(name).log(levelOfLog, aClass, aMethod, aMessage);
			}
		}
	}

    /**
     * Main method for the logging of informations
     *
     * @param levelOfLog level of Log
     * @param module module form which we are logging information
     * @param aClass the class from which we are logging information
     * @param aMethod the method from which we are logging information
     * @param anException an Exception to print
     */
	public static void log(Level levelOfLog, String module, String aClass, String aMethod, Exception anException){
	    String message = "";
        try {
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        anException.printStackTrace(pw);
	        message = sw.toString();
            pw.close();
            sw.close();
	    }
	    catch(Exception e2) {
	        message = anException.getMessage();
	    }
	    Iterator<String> iter = loggingManager.loggingUnitMap.keySet().iterator();
	    while(iter.hasNext()){
	        String name = iter.next();
            if(name.equalsIgnoreCase(module)){
                loggingManager.loggingUnitMap.get(name).log(levelOfLog, aClass, aMethod, message);
            }
        }
    }

    /**
     * Method initializing the root LoggingUnit
     *
     */
	private void initializedRootLoggingUnit(){
		String identifier = ApplicationConfiguration.getApplicationContext().equalsIgnoreCase(ApplicationConfiguration.DIETDASHBOARD_CONTEXT)?DIETDASHBOARD:RESOURCESTOOL;
		rootLoggingUnit = new LoggingUnit(identifier);
		rootLoggingUnit.setUseParentHandlers(false);
        rootLoggingUnit.setLevel(Level.ALL);
		MyConsoleHandler consoleHandler = new MyConsoleHandler();
		consoleHandler.setFormatter(new PlainTextFormatter(null));
        consoleHandler.setLevel(Level.INFO);
		rootLoggingUnit.addHandler(consoleHandler);
		try{
			FileHandler fileHandler = new FileHandler(logDirectory.getAbsolutePath() + "/Log_"+identifier+".html");
			fileHandler.setFormatter(new HTMLFormatter(identifier));
			rootLoggingUnit.addHandler(fileHandler);
			FileHandler PlainTextFileHandler = new FileHandler(LoggingManager.getLogDirectory().getAbsolutePath() + "/Log_"+identifier+".log");
			PlainTextFileHandler.setFormatter(new PlainTextFormatter(identifier));
			rootLoggingUnit.addHandler(PlainTextFileHandler);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		LoggingPanelHandler panelHandler = new LoggingPanelHandler();
		rootLoggingUnit.addHandler(panelHandler);
		LoggingFilter rootFilter = new LoggingFilter();
		rootLoggingUnit.setFilter(rootFilter);
		loggingUnitMap.put(rootLoggingUnit.getName(), rootLoggingUnit);
	}

    /**
     * Method returning a LoggingUnit  from aLoggingManager and an identifier
     *
     * @param aLoggingManager a LoggingManager
     * @param aLoggingUnitIdentifier an identifier
     * @return the LoggingUnit
     */
	public static LoggingUnit getLoggingUnit(LoggingManager aLoggingManager, String aLoggingUnitIdentifier){
		LoggingUnit aLoggingUnit = new LoggingUnit(aLoggingUnitIdentifier);
		aLoggingUnit.setLevel(Level.ALL);
		LoggingFilter filter = new LoggingFilter();
		try{
			FileHandler HTMLFileHandler = new FileHandler(LoggingManager.getLogDirectory().getAbsolutePath() + "/Log_"+aLoggingUnitIdentifier+".html");
			HTMLFileHandler.setFormatter(new HTMLFormatter(aLoggingUnitIdentifier));
			aLoggingUnit.addHandler(HTMLFileHandler);

			FileHandler PlainTextFileHandler = new FileHandler(LoggingManager.getLogDirectory().getAbsolutePath() + "/Log_"+aLoggingUnitIdentifier+".log");
			PlainTextFileHandler.setFormatter(new PlainTextFormatter(aLoggingUnitIdentifier));
			aLoggingUnit.addHandler(PlainTextFileHandler);
			LoggingPanelHandler panelHandler = new LoggingPanelHandler();
			aLoggingUnit.addHandler(panelHandler);

			aLoggingUnit.setFilter(filter);
			aLoggingUnit.setParentLoggingUnit(aLoggingManager.getRootLoggingUnit());
		}
		catch(Exception e){
			LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, LoggingManager.LOGGINGTOOL, "getLoggingUnit", e);
		}
		return aLoggingUnit;
	}

}
