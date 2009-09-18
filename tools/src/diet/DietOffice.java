/****************************************************************************/
/* DIET_DASH BOARD & GRUDU MAIN CLASS                                       */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: DietOffice.java,v 1.30 2007/11/29 21:00:32 dloureir Exp $
 * $Log: DietOffice.java,v $
 * Revision 1.30  2007/11/29 21:00:32  dloureir
 * Changing the class used to check whether if the workflow have been selected during the installation
 *
 * Revision 1.29  2007/10/12 14:02:19  dloureir
 * changing the imports
 *
 * Revision 1.28  2007/10/09 10:33:40  aamar
 * Adding DashBoard close operation behaviour (minimize/exit)
 *
 * Revision 1.27  2007/07/17 10:14:56  dloureir
 * Adding the settings frame and a correction for the module checking.
 *
 * Revision 1.26  2007/07/16 14:19:22  dloureir
 * The tests for the existence in the classpath of the libraries corresponding to the modules for the DIEt_DashBoard
 *
 * Revision 1.25  2007/07/12 09:20:17  dloureir
 * Changing the text of the JButton of GRUDU
 *
 * Revision 1.24  2007/07/11 15:40:27  dloureir
 * Adding an application package for the application wide properties and two panels for the Tips of the Day for DDB and GRUDU
 *
 * Revision 1.23  2007/05/14 14:22:55  dloureir
 * Adding the help for grudu and changing the link for the help of the DashBoard
 *
 * Revision 1.22  2007/03/08 12:27:09  dloureir
 * Deletion of the printStackTrace for a new log function
 *
 * Revision 1.21  2007/02/24 19:20:00  aamar
 * Remove some warnings
 *
 * Revision 1.20  2007/02/22 15:21:17  dloureir
 * Correcting minor bug
 *
 * Revision 1.19  2007/02/22 15:18:02  aamar
 * Some changes.
 *
 ****************************************************************************/
package diet;

/**
 * @author Abdelkader Amar
 * @author David Loureiro
 *
 */

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.*;

import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import com.birosoft.liquid.LiquidLookAndFeel;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;

import diet.util.gui.GuiThread;
import diet.vizdiet.VizDIETLoader;
import diet.wf.designer.Designer;
import diet.wf.designer.CfgValues;
import diet.wf.log.gui.WfLogSrvFrame;
import diet.wizzard.jframe.WizzardFrame;
import diet.application.ApplicationConfiguration;
import diet.application.settings.SettingsFrame;
import diet.application.totd.DDBTipOfTheDay;
import diet.designer.gui.DietDesigner;
import diet.gridr.g5k.gui.*;
import diet.logging.LoggingManager;
import diet.logging.gui.LoggingFrame;
import diet.mapping.gui.MappingToolFrame;
import diet.deploy.gui.DietDeploy;
public class DietOffice extends JFrame implements ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * The interface commands
     */
    protected final String wfDesigner_cmd         = "WF_DESIGNER";
    protected final String wfLog_cmd              = "WF_LOG";
    protected final String dietDesigner_cmd       = "DIET_DESIGNER";
    protected final String dietDeploy_cmd         = "DIET_DEPLOY";
    protected final String dietMapping_cmd        = "DIET_MAPPING";
    protected final String vizDIET_cmd			  = "VIZDIET";
    protected final String xmlGoDietGenerator_cmd = "XMLGODIETGENERATOR";
    protected final String settings_cmd           = "SETTINGS";
    protected final String resources_cmd          = "RESOURCES";
    protected final String help_cmd               = "HELP";
    protected final String about_cmd              = "ABOUT";
    protected final String g5k_cmd				  = "G5K";
    protected final String logging_cmd			  = "LOGGING";
    /**
     * The workflow designer button
     */
    private JMenuItem wfDesigner_btn = null;
    /**
     * The workflow log service button
     */
    private JMenuItem wfLog_btn = null;
    /**
     * The diet designer button
     */
    private JMenuItem dietDesigner_btn = null;
    /**
     * The diet deployement button
     */
    private JMenuItem dietDeploy_btn = null;
    /**
     * The resources reservation button
     */
    private JMenuItem g5k_btn = null;
    /**
     * The settings button
     */
    private JMenuItem settings_btn = null;
    /**
     * The help button
     */
    private JMenuItem help_btn = null;
    /**
     * The about button
     */
    private JMenuItem about_btn = null;
    /**
     * The XMLGoDietGenerator wizzard button
     *
     */
    private JMenuItem xmlgodietgenerator_wizzard_btn = null;
    /**
     * the mapping tool button
     *
     */
    private JMenuItem mapping_btn = null;
    /**
     * the button for vizDIET
     */
    private JMenuItem vizdiet_btn = null;

    private JMenuItem logging_btn;
    private JMenuItem loggingMenu = null;

    private JMenu workFlowMenu =null;
    private JMenu dietMenu = null;
    private JMenu resourceMenu = null;
    private JMenu optionsMenu = null;
    private JMenuBar myMenuBar = null;

    private JPopupMenu trayPopupMenu = null;
    private JMenuItem dietTrayMenu = null;
    private JMenuItem workFlowTrayMenu = null;
    private JMenuItem resourceTrayMenu = null;
    private JMenuItem optionsTrayMenu = null;
    private JMenuItem quitTrayMenu = null;

    private JMenuItem dietDeployTrayMenu = null;
    private JMenuItem dietDesignerTrayMenu = null;
    private JMenuItem mappingToolTrayMenu = null;
    private JMenuItem vizdietTrayMenu = null;
    private JMenuItem xmlgodietgeneratorTrayMenu = null;
    private JMenuItem wfDesignerTrayMenu = null;
    private JMenuItem wfLogTrayMenu = null;
    private JMenuItem g5kTrayMenu = null;
    private JMenuItem settingsTrayMenu = null;
    private JMenuItem helpTrayMenu = null;
    private JMenuItem aboutTrayMenu = null;
    private JMenuItem maximizeTrayMenu = null;
    private JMenuItem minimizeTrayMenu = null;

    private LoggingManager logManager = null;


    public DietOffice() {
	super("DIET DashBoard");
	logManager = LoggingManager.getInstance();

	//
	// For each module that could be considered as an extra part of the DIET_DashBoard
	// we try to load a class from one of the libraries mandatory for the considered
	// module. If the class could be loaded, then we add the icon or the menuitem
	// else we do not add the icon or the menuitem.
	//

	// Create the buttons
	ImageIcon icon = null;
	boolean classLoadingSuccess = false;
	try{
		Class aClass = Class.forName("org.netbeans.graph.vmd.VMDDocumentRenderer");
		classLoadingSuccess = true;
	}
	catch(Exception e){}
	if(classLoadingSuccess){
		// wf designer
		icon = new ImageIcon(getClass().getResource("/resources/wfDesigner.png"));
		if (icon != null)
			this.wfDesigner_btn = new JMenuItem("<html><center>Workflow<br>designer</center></html>",
					icon);
		else
			this.wfDesigner_btn = new JMenuItem("<html><center>Workflow<br>designer</center></html>");
		this.wfDesigner_btn.addActionListener(this);
		this.wfDesigner_btn.setActionCommand(wfDesigner_cmd);
		// adding tooltip text to the button
		this.wfDesigner_btn.setToolTipText("Allows you to graphically design your own workflow considering the available services");
		logManager.addLoggingUnit(LoggingManager.WFDESIGNER);
		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "Adding WorkFlow Designer Module");

		// wf log service
		icon = new ImageIcon(getClass().getResource("/resources/wfLog.png"));
		if (icon != null)
			this.wfLog_btn = new JMenuItem("<html><center>Workflow<br>log service</center></html>",
					icon);
		else
			this.wfLog_btn = new JMenuItem("<html><center>Workflow<br>log service</center></html>");
		this.wfLog_btn.addActionListener(this);
		wfLog_btn.setActionCommand(wfLog_cmd);
		//	 adding tooltip text to the button
		this.wfLog_btn.setToolTipText("Allows you to see the workflow progress in real time");

		// WorkFlowMenu
		icon = new ImageIcon(getClass().getResource("/resources/wf.png"));
		if(icon != null){
			workFlowMenu = new JMenu("<html><center>WorkFlow</center></html>");
			workFlowMenu.setIcon(icon);
		}
		else{
			workFlowMenu = new JMenu("<html><center>WorkFlow</center></html>");
		}
		logManager.addLoggingUnit(LoggingManager.WFLOGSERVICE);
		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "Adding WorkFlow LogService Module");
	}
	// diet designer
	icon = new ImageIcon(getClass().getResource("/resources/dietDesigner.png"));
	if (icon != null)
	    this.dietDesigner_btn = new JMenuItem("<html><center>Diet<br>designer</center></html>",
						icon);
	else
	    this.dietDesigner_btn = new JMenuItem("<html><center>Diet<br>designer</center></html>");
	this.dietDesigner_btn.addActionListener(this);
	this.dietDesigner_btn.setActionCommand(dietDesigner_cmd);
	//	 adding tooltip text to the button
	this.dietDesigner_btn.setToolTipText("Allows you to graphically design your own DIET architecture");
	logManager.addLoggingUnit(LoggingManager.DIETDESIGNERTOOL);
	LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "Adding Diet Designer Module");
	// diet deployment
	icon = new ImageIcon(getClass().getResource("/resources/dietDeploy.png"));
	if (icon != null)
	    this.dietDeploy_btn = new JMenuItem("<html><center>Diet<br>deploy tool</center></html>",
					      icon);
	else
	    this.dietDeploy_btn = new JMenuItem("<html><center>Diet<br>deploy tool</center></html>");
	this.dietDeploy_btn.addActionListener(this);
	dietDeploy_btn.setActionCommand(dietDeploy_cmd);
	//	 adding tooltip text to the button
	this.dietDeploy_btn.setToolTipText("Allows you to deploy your DIET hierarchy on the ressources you have reserved");
	logManager.addLoggingUnit(LoggingManager.DIETDEPLOYTOOL);
	LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "Adding Diet Deploy tool Module");
	// mapping tool
	icon = new ImageIcon(getClass().getResource("/resources/map.png"));
	if(icon != null){
		this.mapping_btn = new JMenuItem("<html><center>Mapping<br>Tool</center></html>",icon);
	}else{
		this.mapping_btn = new JMenuItem("<html><center>Mapping<br>Tool</center></html>");
	}
	this.mapping_btn.addActionListener(this);
	this.mapping_btn.setActionCommand(dietMapping_cmd);
	logManager.addLoggingUnit(LoggingManager.DIETMAPPINGTOOL);
	LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "Adding Diet Mapping Tool Module");

	classLoadingSuccess = false;
	try{
		Class aClass = Class.forName("Main.VizDIET");
		classLoadingSuccess = true;
	}
	catch(Exception e){

	}
	if(classLoadingSuccess){
		// vizDIET
		icon = new ImageIcon(getClass().getResource("/resources/VizDIET.png"));
		if(icon != null){
			this.vizdiet_btn = new JMenuItem("<html><center>VizDIET</center></html>",icon);
		}else{
			this.vizdiet_btn = new JMenuItem("<html><center>VizDIET</center></html>");
		}
		this.vizdiet_btn.addActionListener(this);
		this.vizdiet_btn.setActionCommand(vizDIET_cmd);
		logManager.addLoggingUnit(LoggingManager.VIZDIET);
		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "Adding VizDIET Module");

	}

	classLoadingSuccess = false;

	try{
		Class aClass = Class.forName("launcher.XmlGoDietGenerator");
		classLoadingSuccess = true;
	}
	catch(Exception e){}
	if(classLoadingSuccess){
		// XMLGoDietGenerator wizzard
		icon = new ImageIcon(getClass().getResource("/resources/XMLGoDietGeneratorWizzard.png"));
		if(icon != null){
			this.xmlgodietgenerator_wizzard_btn = new JMenuItem("<html><center>XMLGoDietGenerator<br>wizzard</center></html>",icon);
		}else{
			this.xmlgodietgenerator_wizzard_btn = new JMenuItem("<html><center>XMLGoDietGenerator<br>wizzard</center></html>");
		}
		this.xmlgodietgenerator_wizzard_btn.addActionListener(this);
		this.xmlgodietgenerator_wizzard_btn.setActionCommand(xmlGoDietGenerator_cmd);
		logManager.addLoggingUnit(LoggingManager.XMLGODIETGENERATOR);
		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "Adding XMLGoDIETGenerator Module");

	}
	// resources allocation
	icon = new ImageIcon(getClass().getResource("/resources/g5k.png"));
	if (icon != null)
	    this.g5k_btn = new JMenuItem("<html><center>GRUDU</center></html>",
					     icon);
	else
	    this.g5k_btn = new JMenuItem("<html><center>GRUDU</center></html>");
	this.g5k_btn.addActionListener(this);
	this.g5k_btn.setActionCommand(g5k_cmd);
	//	 adding tooltip text to the button
	this.g5k_btn.setToolTipText("Allows you to reserve some ressources for future DIET deployments");
	logManager.addLoggingUnit(LoggingManager.RESOURCESTOOL);
	LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "Adding Resources Tool Module");

	// settings
	icon = new ImageIcon(getClass().getResource("/resources/settings.png"));
	if (icon != null)
	    this.settings_btn = new JMenuItem("<html><center>Settings</center></html>", icon);
	else
	    this.settings_btn = new JMenuItem("<html><center>Settings</center></html>");
	this.settings_btn.addActionListener(this);
	this.settings_btn.setActionCommand(settings_cmd);
	//	 adding tooltip text to the button
	this.settings_btn.setToolTipText("Allows you to define the necessary settings used to reserve, deploy and observe your platforms and Workflows");
	// help
	icon = new ImageIcon(getClass().getResource("/resources/help.png"));
	if (icon != null){
	    this.help_btn = new JMenuItem("<html><center>Help</center></html>",icon);
	}
	else
	    this.help_btn = new JMenuItem("<html><center>Help</center></html>");
	this.help_btn.addActionListener(this);
	this.help_btn.setActionCommand(help_cmd);
	// about
	icon = new ImageIcon(getClass().getResource("/resources/about.png"));
	if (icon != null){
	    this.about_btn = new JMenuItem("",icon);
	}else
	    this.about_btn = new JMenuItem("<html><center>About</center></html>");
	this.about_btn.addActionListener(this);
	this.about_btn.setActionCommand(about_cmd);
	//	 adding tooltip text to the button
	this.about_btn.setToolTipText("The credits of the DIET_DashBoard");

	// adding loggingMenu
    icon = new ImageIcon(getClass().getResource("/resources/Logging.png"));
    if (icon != null){
        this.logging_btn = new JMenuItem("<html><center>Logging</center></html>",icon);
    }else
        this.logging_btn = new JMenuItem("<html><center>Logging</center></html>");

	this.logging_btn.addActionListener(this);
	this.logging_btn.setActionCommand(logging_cmd);

	// menus of the JMenuBar

	// dietMenu
	icon = new ImageIcon(getClass().getResource("/resources/DIET.png"));
	if(icon != null){
		dietMenu = new JMenu("<html><center>Diet</center></html>");
		dietMenu.setIcon(icon);
	}
	else{
		dietMenu = new JMenu("<html><center>Diet</center></html>");
	}
	// resourceMenu
	icon = new ImageIcon(getClass().getResource("/resources/resources.png"));
	if(icon != null){
		resourceMenu = new JMenu("<html><center>Resources</center></html>");
		resourceMenu.setIcon(icon);
	}
	else{
		resourceMenu = new JMenu("<html><center>Resources</center></html>");
	}
	// optionMenu
	icon = new ImageIcon(getClass().getResource("/resources/options.png"));
	if(icon != null){
		optionsMenu = new JMenu("<html><center>Options</center></html>");
		optionsMenu.setIcon(icon);
	}
	else{
		optionsMenu = new JMenu("<html><center>Options</center></html>");
	}

	myMenuBar = new JMenuBar();
	this.dietMenu.add(this.dietDesigner_btn);
	this.dietMenu.add(this.dietDeploy_btn);
	this.dietMenu.add(this.mapping_btn);
	if(xmlgodietgenerator_wizzard_btn != null)this.dietMenu.add(this.xmlgodietgenerator_wizzard_btn);
	if(vizdiet_btn != null) this.dietMenu.add(this.vizdiet_btn);
	myMenuBar.add(dietMenu);
	if(workFlowMenu != null){
		this.workFlowMenu.add(this.wfDesigner_btn);
		this.workFlowMenu.add(this.wfLog_btn);
		myMenuBar.add(workFlowMenu);
	}
	this.resourceMenu.add(this.g5k_btn);
	myMenuBar.add(resourceMenu);

	myMenuBar.add(optionsMenu);
	optionsMenu.add(settings_btn);
	optionsMenu.add(help_btn);
	optionsMenu.add(logging_btn);
	myMenuBar.add(optionsMenu);
	myMenuBar.add(this.about_btn);
	getContentPane().add(myMenuBar);
	LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "MenuBar initialized");
	pack();
	setResizable(false);
	initTray();

	this.addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent e){
			LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "windowClosing", "DIET_DashBoard MenuBar is closing");
		}
	});
	//init();
    }

    private void initTray(){
    	// Create the buttons
    	ImageIcon icon = null;
    	boolean classLoadingSuccess = false;
    	try{
    		Class aClass = Class.forName("org.netbeans.graph.vmd.VMDDocumentRenderer");
    		classLoadingSuccess = true;
    	}
    	catch(Exception e){}
    	if(classLoadingSuccess){
    		// wf designer
    		icon = new ImageIcon(getClass().getResource("/resources/wfDesignerTray.png"));
    		if (icon != null)
    			this.wfDesignerTrayMenu = new JMenuItem("<html><center>Workflow<br>designer</center></html>",
    					icon);
    		else
    			this.wfDesignerTrayMenu = new JMenuItem("<html><center>Workflow<br>designer</center></html>");
    		this.wfDesignerTrayMenu.addActionListener(this);
    		wfDesignerTrayMenu.setActionCommand(wfDesigner_cmd);
    		// adding tooltip text to the button
    		this.wfDesignerTrayMenu.setToolTipText("Allows you to graphically design your own workflow considering the available services");
    		// wf log service
    		icon = new ImageIcon(getClass().getResource("/resources/wfLogTray.png"));
    		if (icon != null)
    			this.wfLogTrayMenu = new JMenuItem("<html><center>Workflow<br>log service</center></html>",
    					icon);
    		else
    			this.wfLogTrayMenu = new JMenuItem("<html><center>Workflow<br>log service</center></html>");
    		this.wfLogTrayMenu.addActionListener(this);
    		wfLogTrayMenu.setActionCommand(wfLog_cmd);
    		//	 adding tooltip text to the button
    		this.wfLogTrayMenu.setToolTipText("Allows you to see the workflow progress in real time");

    		// WorkFlowMenu
    		icon = new ImageIcon(getClass().getResource("/resources/wfTray.png"));
    		if(icon != null){
    			workFlowTrayMenu = new JMenu("<html><center>WorkFlow</center></html>");
    			workFlowTrayMenu.setIcon(icon);
    		}
    		else{
    			workFlowTrayMenu = new JMenu("<html><center>WorkFlow</center></html>");
    		}
    	}
    	// diet designer
    	icon = new ImageIcon(getClass().getResource("/resources/dietDesignerTray.png"));
    	if (icon != null)
    	    this.dietDesignerTrayMenu = new JMenuItem("<html><center>Diet<br>designer</center></html>",
    						icon);
    	else
    	    this.dietDesignerTrayMenu = new JMenuItem("<html><center>Diet<br>designer</center></html>");
    	this.dietDesignerTrayMenu.addActionListener(this);
    	dietDesignerTrayMenu.setActionCommand(dietDesigner_cmd);
    	//	 adding tooltip text to the button
    	this.dietDesignerTrayMenu.setToolTipText("Allows you to graphically design your own DIET architecture");
    	// diet deployment
    	icon = new ImageIcon(getClass().getResource("/resources/dietDeployTray.png"));
    	if (icon != null)
    	    this.dietDeployTrayMenu = new JMenuItem("<html><center>Diet<br>deploy tool</center></html>",
    					      icon);
    	else
    	    this.dietDeployTrayMenu = new JMenuItem("<html><center>Diet<br>deploy tool</center></html>");
    	this.dietDeployTrayMenu.addActionListener(this);
    	dietDeployTrayMenu.setActionCommand(dietDeploy_cmd);
    	//	 adding tooltip text to the button
    	this.dietDeployTrayMenu.setToolTipText("Allows you to deploy your DIET hierarchy on the ressources you have reserved");
    	// mapping tool
    	icon = new ImageIcon(getClass().getResource("/resources/mapTray.png"));
    	if(icon != null){
    		this.mappingToolTrayMenu = new JMenuItem("<html><center>Mapping<br>Tool</center></html>",icon);
    	}else{
    		this.mappingToolTrayMenu = new JMenuItem("<html><center>Mapping<br>Tool</center></html>");
    	}
    	this.mappingToolTrayMenu.addActionListener(this);
    	mappingToolTrayMenu.setActionCommand(dietMapping_cmd);

    	classLoadingSuccess = false;
    	try{
    		Class aClass = Class.forName("Main.VizDIET");
    		classLoadingSuccess = true;
    	}
    	catch(Exception e){

    	}
    	if(classLoadingSuccess){
    		// vizDIET
    		icon = new ImageIcon(getClass().getResource("/resources/VizDIETTray.png"));
    		if(icon != null){
    			this.vizdietTrayMenu = new JMenuItem("<html><center>VizDIET</center></html>",icon);
    		}else{
    			this.vizdietTrayMenu = new JMenuItem("<html><center>VizDIET</center></html>");
    		}
    		this.vizdietTrayMenu.addActionListener(this);
    		this.vizdietTrayMenu.setActionCommand(vizDIET_cmd);
    	}

    	classLoadingSuccess = false;

    	try{
    		Class aClass = Class.forName("launcher.XmlGoDietGenerator");
    		classLoadingSuccess = true;
    	}
    	catch(Exception e){}
    	if(classLoadingSuccess){
    		// XMLGoDietGenerator wizzard
    		icon = new ImageIcon(getClass().getResource("/resources/XMLGoDietGeneratorWizzardTray.png"));
    		if(icon != null){
    			this.xmlgodietgeneratorTrayMenu = new JMenuItem("<html><center>XMLGoDietGenerator<br>wizzard</center></html>",icon);
    		}else{
    			this.xmlgodietgeneratorTrayMenu = new JMenuItem("<html><center>XMLGoDietGenerator<br>wizzard</center></html>");
    		}
    		this.xmlgodietgeneratorTrayMenu.addActionListener(this);
    		xmlgodietgeneratorTrayMenu.setActionCommand(xmlGoDietGenerator_cmd);
    	}
    	// resources allocation
    	icon = new ImageIcon(getClass().getResource("/resources/g5kTray.png"));
    	if (icon != null)
    	    this.g5kTrayMenu = new JMenuItem("<html><center>GRUDU</center></html>",
    					     icon);
    	else
    	    this.g5kTrayMenu = new JMenuItem("<html><center>GRUDU</center></html>");
    	this.g5kTrayMenu.addActionListener(this);
    	g5kTrayMenu.setActionCommand(g5k_cmd);
    	//	 adding tooltip text to the button
    	this.g5kTrayMenu.setToolTipText("Allows you to reserve some ressources for future DIET deployments");
    	// settings
    	icon = new ImageIcon(getClass().getResource("/resources/settingsTray.png"));
    	if (icon != null)
    	    this.settingsTrayMenu = new JMenuItem("<html><center>Settings</center></html>", icon);
    	else
    	    this.settingsTrayMenu = new JMenuItem("<html><center>Settings</center></html>");
    	this.settingsTrayMenu.addActionListener(this);
    	this.settingsTrayMenu.setActionCommand(settings_cmd);
    	//	 adding tooltip text to the button
    	this.settingsTrayMenu.setToolTipText("Allows you to define the necessary settings used to reserve, deploy and observe your platforms and Workflows");
    	// help
    	icon = new ImageIcon(getClass().getResource("/resources/helpTray.png"));
    	if (icon != null){
    	    this.helpTrayMenu = new JMenuItem("<html><center>Help</center></html>",icon);
    	}
    	else
    	    this.helpTrayMenu = new JMenuItem("<html><center>Help</center></html>");
    	this.helpTrayMenu.addActionListener(this);
    	helpTrayMenu.setActionCommand(help_cmd);
    	// about
    	icon = new ImageIcon(getClass().getResource("/resources/aboutTray.png"));
    	if (icon != null){
    	    this.aboutTrayMenu = new JMenuItem("",icon);
    	}else
    	    this.aboutTrayMenu = new JMenuItem("<html><center>About</center></html>");
    	this.aboutTrayMenu.addActionListener(this);
    	aboutTrayMenu.setActionCommand(about_cmd);
    	//	 adding tooltip text to the button
    	this.aboutTrayMenu.setToolTipText("The credits of the DIET_DashBoard");
    	//logging
        icon = new ImageIcon(getClass().getResource("/resources/LoggingTray.png"));
        if (icon != null){
            this.loggingMenu = new JMenuItem("<html><center>Logging</center></html>",icon);
        }else
            this.loggingMenu = new JMenuItem("<html><center>Logging</center></html>");
    	this.loggingMenu.addActionListener(this);
        this.loggingMenu.setActionCommand(logging_cmd);

    	// menus of the JMenuBar

    	// dietMenu
    	icon = new ImageIcon(getClass().getResource("/resources/DIETTray.png"));
    	if(icon != null){
    		dietTrayMenu = new JMenu("<html><center>Diet</center></html>");
    		dietTrayMenu.setIcon(icon);
    	}
    	else{
    		dietTrayMenu = new JMenu("<html><center>Diet</center></html>");
    	}
    	// resourceMenu
    	icon = new ImageIcon(getClass().getResource("/resources/resourcesTray.png"));
    	if(icon != null){
    		resourceTrayMenu = new JMenu("<html><center>Resources</center></html>");
    		resourceTrayMenu.setIcon(icon);
    	}
    	else{
    		resourceTrayMenu = new JMenu("<html><center>Resources</center></html>");
    	}
    	// optionMenu
    	icon = new ImageIcon(getClass().getResource("/resources/optionsTray.png"));
    	if(icon != null){
    		optionsTrayMenu = new JMenu("<html><center>Options</center></html>");
    		optionsTrayMenu.setIcon(icon);
    	}
    	else{
    		optionsTrayMenu = new JMenu("<html><center>Options</center></html>");
    	}

    	trayPopupMenu = new JPopupMenu("DIET_DashBoard");
    	dietTrayMenu.add(dietDesignerTrayMenu);
    	dietTrayMenu.add(dietDeployTrayMenu);
    	dietTrayMenu.add(mappingToolTrayMenu);
    	if(vizdietTrayMenu != null) dietTrayMenu.add(vizdietTrayMenu);
    	if(xmlgodietgeneratorTrayMenu != null) dietTrayMenu.add(xmlgodietgeneratorTrayMenu);
    	trayPopupMenu.add(dietTrayMenu);
    	if(workFlowTrayMenu != null){
    		workFlowTrayMenu.add(wfDesignerTrayMenu);
    		workFlowTrayMenu.add(wfLogTrayMenu);
    		trayPopupMenu.add(workFlowTrayMenu);
    	}
    	resourceTrayMenu.add(g5kTrayMenu);
    	trayPopupMenu.add(resourceTrayMenu);
    	optionsTrayMenu.add(settingsTrayMenu);
    	optionsTrayMenu.add(helpTrayMenu);
    	optionsTrayMenu.add(aboutTrayMenu);
    	optionsTrayMenu.add(loggingMenu);
    	trayPopupMenu.add(optionsTrayMenu);
    	trayPopupMenu.addSeparator();


    	icon = new ImageIcon(getClass().getResource("/resources/minimizeTray.png"));
    	if(icon != null){
    		minimizeTrayMenu = new JMenuItem("<html><center>Minimize</center></html>");
    		minimizeTrayMenu.setIcon(icon);
    	}
    	else{
    		minimizeTrayMenu = new JMenuItem("<html><center>Minimize</center></html>");
    	}
    	trayPopupMenu.add(minimizeTrayMenu);
    	minimizeTrayMenu.addActionListener(this);

    	icon = new ImageIcon(getClass().getResource("/resources/maximizeTray.png"));
    	if(icon != null){
    		maximizeTrayMenu = new JMenuItem("<html><center>Maximize</center></html>");
    		maximizeTrayMenu.setIcon(icon);
    	}
    	else{
    		maximizeTrayMenu = new JMenuItem("<html><center>Maximize</center></html>");
    	}
    	maximizeTrayMenu.addActionListener(this);
    	trayPopupMenu.add(maximizeTrayMenu);
    	trayPopupMenu.addSeparator();
    	icon = new ImageIcon(getClass().getResource("/resources/quitTray.png"));
    	if(icon != null){
    		quitTrayMenu = new JMenuItem("<html><center>Quit</center></html>");
    		quitTrayMenu.setIcon(icon);
    	}
    	else{
    		quitTrayMenu = new JMenuItem("<html><center>Quit</center></html>");
    	}
    	quitTrayMenu.addActionListener(this);
    	trayPopupMenu.add(quitTrayMenu);
    	ImageIcon inactiveIcon = new ImageIcon(getClass().getResource("/resources/DIET_DashBoard_Tray.png"));
    	// avoid the problems created with apple platforms (tray is not supported on mac os x)
    	if(!System.getProperty("os.name").toLowerCase().contains("mac")){
            ApplicationConfiguration appCfg = ApplicationConfiguration.getInstance();
            if (Boolean.parseBoolean(appCfg.getProperty(ApplicationConfiguration.PROPERTY_CLOSE_OPERATION))) {
                SystemTray tray = SystemTray.getDefaultSystemTray();
                TrayIcon trayIcon = new TrayIcon(inactiveIcon, "DIET_DashBoard", trayPopupMenu);
                trayIcon.setIconAutoSize(true);
                trayIcon.addActionListener(this);
                tray.addTrayIcon(trayIcon);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "DietOffice", "SysTray initialized");
                LoggingManager.log(Level.WARNING,LoggingManager.DIETDASHBOARD,this.getClass().getName(),"initTray","DefaultCloseOperation is 'Dispose on close'");
            }
            else {
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                LoggingManager.log(Level.WARNING,LoggingManager.DIETDASHBOARD,this.getClass().getName(),"initTray","DefaultCloseOperation is 'Exit on close'");                
            }
    	}
    	else{
    	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		LoggingManager.log(Level.WARNING,LoggingManager.DIETDASHBOARD,this.getClass().getName(),"initTray","DefaultCloseOperation is 'Exit on close'");

    	}
    }

    protected void init() {
    	CfgValues.read();
    }

    public void actionPerformed(ActionEvent event) {
    	if (event.getActionCommand().equalsIgnoreCase(wfDesigner_cmd)) {
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching Workflow Designer Tool");
    		Designer designer = new Designer ();
    		GuiThread thread = new GuiThread(designer);
    		thread.start();
    	}
    	if (event.getActionCommand().equalsIgnoreCase(this.wfLog_cmd)) {
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching Workflow Log Service");
    		WfLogSrvFrame wfLogSrv = new WfLogSrvFrame();
    		GuiThread thread = new GuiThread(wfLogSrv);
    		thread.start();
    	}
    	if (event.getActionCommand().equalsIgnoreCase(this.dietDesigner_cmd)) {
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching DIET Designer Tool");
    		DietDesigner dietDesigner = new DietDesigner();
    		GuiThread thread = new GuiThread(dietDesigner);
    		thread.start();
    	}
    	if (event.getActionCommand().equalsIgnoreCase(this.dietDeploy_cmd)) {
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching DIET Deploy Tool");
    		DietDeploy dietDeploy = new DietDeploy();
    		GuiThread thread = new GuiThread(dietDeploy);
    		thread.start();

    	}
    	if(event.getActionCommand().equalsIgnoreCase(this.xmlGoDietGenerator_cmd)) {
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching XMLGoDIETGenerator");
    		WizzardFrame wizzardFrame = new WizzardFrame();
    		GuiThread thread = new GuiThread(wizzardFrame);
    		thread.start();
    	}
    	if(event.getActionCommand().equalsIgnoreCase(this.dietMapping_cmd)){
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching DIET Mapping Tool");
    		MappingToolFrame frame = new MappingToolFrame();
    		GuiThread thread = new GuiThread(frame);
    		thread.start();
    	}
    	if(event.getActionCommand().equalsIgnoreCase(this.vizDIET_cmd)){
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching VizDIET");
    		VizDIETLoader loader = new VizDIETLoader();
    		GuiThread thread = new GuiThread(loader);
    		thread.start();
    	}
    	if (event.getActionCommand().equalsIgnoreCase(this.g5k_cmd)) {
    		SwingUtilities.invokeLater(new Runnable(){
    			public void run(){
    				LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching Resources Tool");
    				GuiThread thread = new GuiThread(new G5kRes());
    				thread.start();
    			}
    		});
    	}
    	if(event.getActionCommand().equalsIgnoreCase(this.logging_cmd)){
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching Logging Tool");
    		GuiThread thread = new GuiThread(new LoggingFrame());
    		thread.start();
    	}
    	if (event.getActionCommand().equalsIgnoreCase(this.settings_cmd)) {
    		SwingUtilities.invokeLater(new Runnable(){
    			public void run(){
    				LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "launching Application Settings frame");
    				GuiThread thread = new GuiThread(new SettingsFrame());
    				thread.start();
    			}
    		});
    	}
    	if (event.getActionCommand().equalsIgnoreCase(this.help_cmd)) {
//    		 Find the HelpSet file and create the HelpSet object:
    		HelpSet hs;
    		try {
    			URL hsURL = new File(System.getProperty("user.dir") +"/Help/DIET_DashBoard/DIET_DashBoard_Help.hs").toURL();
    			hs = new HelpSet(null, hsURL);
    		} catch (Exception ee) {
//  			Say what the exception really is
                LoggingManager.log(Level.WARNING, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed","HelpSet"+ ee);
                LoggingManager.log(Level.WARNING, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed","HelpSet "+ System.getProperty("user.dir") +"/Help/DIET_DashBoard/DIET_DashBoard_Help.hs" +" not found");
    			return;
    		}
//    		 Create a HelpBroker object:
    		HelpBroker hb = hs.createHelpBroker();
    		//hb.setLocation(new Point(0,220));
    		hb.setSize(new Dimension(800,600));
    		ActionEvent cshAction = new ActionEvent(new JLabel(),
    	            ActionEvent.ACTION_FIRST, null);
    		new CSH.DisplayHelpFromSource( hb ).actionPerformed(cshAction);
    	}
    	if (event.getActionCommand().equalsIgnoreCase(this.about_cmd)) {
    		AboutDlg dlg = new AboutDlg(this);
    		dlg.setVisible(true);
    	}
    	if(event.getSource() == this.quitTrayMenu){
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "closing application");
    		LoggingManager.close();
    		System.exit(0);
    	}
    	if(event.getSource() == this.minimizeTrayMenu){
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "minimizing application");
    		dispose();
    	}
    	if(event.getSource() == this.maximizeTrayMenu){
    		LoggingManager.log(Level.INFO, LoggingManager.DIETDASHBOARD, this.getClass().getName(), "actionPerformed", "maximizing application");
    		this.setVisible(true);
    	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
        	Locale.getDefault().setDefault(Locale.ENGLISH);
        	ApplicationConfiguration.setApplicationContext(ApplicationConfiguration.DIETDASHBOARD_CONTEXT);
            UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            LiquidLookAndFeel.setToolbarFlattedButtons(true);
            LiquidLookAndFeel.setToolbarButtonsFocusable(false);
            SplashScreenFrame.splashImageOnScreen("/resources/DietDashBoard.png");
            DietOffice mainWin = new DietOffice();
            mainWin.setVisible(true);
            new DDBTipOfTheDay();
        }
        catch (Exception e) {
            LoggingManager.log(Level.WARNING, LoggingManager.DIETDASHBOARD, DietOffice.class.getName(), "main", e);
        }
    }
}