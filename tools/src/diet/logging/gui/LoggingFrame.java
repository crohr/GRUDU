/****************************************************************************/
/* This class allows the user to see the logging of information for each    */
/* defined LoggingUnit                                                      */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: LoggingFrame.java,v 1.4 2007/03/08 12:17:10 dloureir Exp $
 * $Log: LoggingFrame.java,v $
 * Revision 1.4  2007/03/08 12:17:10  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.3  2007/03/05 16:06:30  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.gui;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.logging.Level;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import diet.logging.LoggingManager;
import diet.util.gui.DietGuiTool;

/**
 * This class allows the user to see the logging of information for each
 * defined LoggingUnit
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class LoggingFrame extends JFrame implements DietGuiTool {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
    private static final long serialVersionUID = 1L;
    /**
     * main panel ef the application
     */
    private JPanel jPanel = null;
    /**
     * panel containing the split panel
     */
	private JPanel jPanel1 = null;
    /**
     * split panel with the list of available loggingUint on the left
     * and the log information on the right
     */
	private JSplitPane jSplitPane = null;
    /**
     * Panel containing the tree of LoggingUnit
     */
	private JPanel jPanel3 = null;
    /**
     * Panel containing the information on the selected LoggingUnit
     */
	private JPanel jPanel4 = null;
    /**
     * CardLayout for the switching between LoggingUnit LogPanel
     */
	private CardLayout carder = null;
    /**
     * Tree of LoggingUnits
     */
	private JTree jTree = null;
    /**
     * Root node of the tree
     */
	private DefaultMutableTreeNode rootNode = null;
    /**
     * Scroll panel for the logging information
     */
	JScrollPane scrollPane1 = null;
    /**
     * LoggingManager from which we extract the LoggingUnits and the informations
     */
	LoggingManager logManager = null;

    /**
     * Method allowing the launching of the frame
     */
	public void go(){
		logManager = LoggingManager.getInstance();
		setSize(840,400);
		setPreferredSize(new Dimension(840,400));
		setMinimumSize(new Dimension(840,400));
		setMaximumSize(new Dimension(840,400));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Logging frame");
		initialize();
		addWindowStateListener(new WindowAdapter(){

			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowStateChanged(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowStateChanged(WindowEvent e) {
				// TODO Auto-generated method stub
				jSplitPane.setDividerLocation(0.25);
			}

			public void windowClosing(WindowEvent e){
				LoggingManager.log(Level.INFO, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "windowClosing", "Logging Frame is closing");
			}

		});

		setVisible(true);
		pack();
		jSplitPane.setDividerLocation(0.25);
	}

    /**
     * Method initializing this
     *
     */
	public void initialize(){
        this.setContentPane(getJPanel());  // Generated
        validate();
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			try {
				jPanel = new JPanel();
				jPanel.setLayout(new BorderLayout());  // Generated
				jPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
				jPanel.add(getJPanel1(), BorderLayout.CENTER);  // Generated
			} catch (java.lang.Throwable e) {
				LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "getJPanel", new Exception(e));
			}
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			try {
				jPanel1 = new JPanel();
				jPanel1.setLayout(new BorderLayout());  // Generated
				jPanel1.add(getJSplitPane(), BorderLayout.NORTH);  // Generated
			} catch (java.lang.Throwable e) {
                LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "getJPanel1", new Exception(e));
			}
		}
		return jPanel1;
	}

	/**
	 * This method initializes jSplitPane
	 *
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			try {
				jSplitPane = new JSplitPane();
				jSplitPane.setLeftComponent(getJPanel3());  // Generated
				jSplitPane.setRightComponent(getJPanel4());  // Generated
				jSplitPane.setDividerLocation(0.25);
			} catch (java.lang.Throwable e) {
                LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "getJSplitPane", new Exception(e));
            }
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jPanel3
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			try {
				jPanel3 = new JPanel();
				jPanel3.setLayout(new BorderLayout());  // Generated
				scrollPane1 = new JScrollPane();
				scrollPane1.setViewportView(getJTree());
                scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				jPanel3.add(scrollPane1, BorderLayout.NORTH);  // Generated
			} catch (java.lang.Throwable e) {
                LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "getJPanel3", new Exception(e));
			}
		}
		return jPanel3;
	}

	/**
	 * This method initializes jPanel4
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			try {
				jPanel4 = new JPanel();
				carder = new CardLayout();
				jPanel4.setLayout(carder);  // Generated
				Iterator<String> iter = logManager.getLoggingUnitMap().keySet().iterator();
				jPanel4.add(logManager.getRootLoggingUnit().getName(),logManager.getRootLoggingUnit().getLoggingPanel(this.getSize()));
				while(iter.hasNext()){
					String aName = iter.next();
					jPanel4.add(aName, logManager.getLoggingUnitMap().get(aName).getLoggingPanel(this.getSize()));
                    validate();
				}
				carder.show(jPanel4, logManager.getRootLoggingUnit().getName());
                validate();
			} catch (java.lang.Throwable e) {
                LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "getJPanel4", new Exception(e));
            }
		}
		return jPanel4;
	}

	/**
	 * This method initializes jTree
	 *
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			try {

				rootNode = new DefaultMutableTreeNode(logManager.getRootLoggingUnit().getName());
				Iterator<String> iter =  logManager.getLoggingUnitMap().keySet().iterator();
				while(iter.hasNext()){
						String aName = iter.next();
						if(!aName.equalsIgnoreCase(rootNode.toString())){
							DefaultMutableTreeNode aNode = new DefaultMutableTreeNode(aName);
							rootNode.add(aNode);
						}
				}
				jTree = new JTree(rootNode);

				jTree.setMinimumSize(new Dimension((int)(this.getSize().width*0.25),this.getSize().height));
                validate();
				jTree.addTreeSelectionListener(new TreeSelectionListener(){

					/* (non-Javadoc)
					 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
					 */
					public void valueChanged(TreeSelectionEvent e) {
						DefaultMutableTreeNode aNode = (DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();
						carder.show(jPanel4, aNode.toString());
                        validate();
					}
				});
			} catch (java.lang.Throwable e) {
                LoggingManager.log(Level.WARNING, LoggingManager.LOGGINGTOOL, this.getClass().getName(), "getJTree", new Exception(e));
			}
		}
		return jTree;
	}
}