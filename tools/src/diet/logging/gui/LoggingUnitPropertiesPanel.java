/****************************************************************************/
/* This class corresponds to a Panel presenting the main properties of the  */
/* LoggingUnit                                                              */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: LoggingUnitPropertiesPanel.java,v 1.3 2007/03/05 16:06:30 dloureir Exp $
 * $Log: LoggingUnitPropertiesPanel.java,v $
 * Revision 1.3  2007/03/05 16:06:30  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.gui;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import diet.logging.LoggingManager;
import diet.logging.util.LoggingUnit;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JTable;
import javax.swing.JButton;

/**
 * This class corresponds to a Panel presenting the main properties of the
 * LoggingUnit
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class LoggingUnitPropertiesPanel extends JPanel {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
	private static final long serialVersionUID = 1L;
    /**
     * Label logging unit name
     */
	private JLabel jLabel = null;
    /**
     * Label handlers text
     */
	private JLabel jLabel1 = null;
    /**
     * Table of handlers
     */
	private JTable jTable = null;
    /**
     * Panel containing the table
     */
	private JPanel jPanel = null;
    /**
     * Label of the level
     */
	private JLabel jLabel2 = null;
    /**
     * Button for changing the level of the LoggingUnit
     */
	private JButton jButton = null;
    /**
     * LoggingUnit from which we get information
     */
	private LoggingUnit loggingUnit = null;
    /**
     * Panel containing the table
     */
	private JPanel jPanel1 = null;

    /**
     * Default constructor of the LoggingUnitPopertiesPanel
     *
     */
	public LoggingUnitPropertiesPanel() {
		super();
		loggingUnit = LoggingManager.getLoggingUnit(LoggingManager.getInstance(), LoggingManager.DIETDEPLOYTOOL);
		initialize();
	}

    /**
     * Second constructor of the LoggingUnitPropertiesPanel
     * witha  LoggingUnit from which we extract data
     *
     * @param aLoggingUnit
     */
	public LoggingUnitPropertiesPanel(LoggingUnit aLoggingUnit) {
		super();
		loggingUnit = aLoggingUnit;
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;  // Generated
		gridBagConstraints2.gridy = 0;  // Generated
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.BOTH;  // Generated
		gridBagConstraints3.gridy = 2;  // Generated
		gridBagConstraints3.weightx = 2.0;  // Generated
		gridBagConstraints3.weighty = 1.0;  // Generated
		gridBagConstraints3.gridx = 1;  // Generated
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;  // Generated
		gridBagConstraints1.gridy = 2;  // Generated
		gridBagConstraints1.anchor = GridBagConstraints.NORTH;
		jLabel1 = new JLabel();
		jLabel1.setText("Handlers : ");  // Generated
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;  // Generated
		gridBagConstraints.gridy = 0;  // Generated
		jLabel = new JLabel();
		jLabel.setText("Level : ");  // Generated
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.add(jLabel, gridBagConstraints);  // Generated
		this.add(jLabel1, gridBagConstraints1);  // Generated
		this.add(getJTable(), gridBagConstraints3);  // Generated
		this.add(getJPanel(), gridBagConstraints2);  // Generated
	}

    /**
     * Method updating the view of the Panel
     *
     * @param aLevel aLevel
     */
	public void updateview(Level aLevel){
		jLabel2.setText(aLevel.getName());
	}

	/**
	 * This method initializes jTable
	 *
	 * @return javax.swing.JTable
	 */
	private JPanel getJTable() {
		if (jPanel1 == null) {
			try {
				jPanel1 = new JPanel();
				jPanel1.setLayout(new BoxLayout(jPanel1,BoxLayout.Y_AXIS));
				Vector<String> temp = new Vector<String>();
				temp.add("Handler");
				temp.add("Output");
				temp.add("Type");
				jTable = new JTable(loggingUnit.getHandlersForTable(),temp);
				jPanel1.add(jTable.getTableHeader());
				jPanel1.add(jTable);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jPanel1;
	}


	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			try {
				jLabel2 = new JLabel();
				jLabel2.setText(loggingUnit.getLevel().getName());  // Generated
				jPanel = new JPanel();
				jPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10, 10));
				jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.X_AXIS));  // Generated
				jPanel.add(jLabel2, null);  // Generated
				jPanel.add(Box.createHorizontalGlue());
				jPanel.add(getJButton(), null);  // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jPanel;
	}


	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			try {
				jButton = new JButton("Change");
				jButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						new ChangingLevelFrame(loggingUnit);
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jButton;
	}

}
