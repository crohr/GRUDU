/****************************************************************************/
/* This class allows the user to change the Level of logging                */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ChangingLevelFrame.java,v 1.2 2007/03/05 16:06:30 dloureir Exp $
 * $Log: ChangingLevelFrame.java,v $
 * Revision 1.2  2007/03/05 16:06:30  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.gui;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

import diet.logging.util.LoggingUnit;
import diet.logging.util.formatter.LoggingUtil;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

/**
 * This class allows the user to change the Level of logging
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class ChangingLevelFrame extends JFrame {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
	private static final long serialVersionUID = 1L;
	/**
     * content pane of the frame
	 */
	private JPanel jContentPane = null;
	/**
     * main panel of the frame
	 */
	private JPanel jPanel = null;
    /**
     * validation panel of the frame
     */
	private JPanel jPanel1 = null;
	/**
     * validate button
	 */
	private JButton jButton = null;
	/**
     * cancel button
	 */
	private JButton jButton1 = null;
	/**
     * LoggingUnit of which you want to modify the level
	 */
	private LoggingUnit loggingUnit = null;
	/**
     * JComboBox for the modification of the level
	 */
	private JComboBox jComboBox = null;

    /**
     * Default constructor of the class
     *
     * @param aLoggingUnit LoggingUnit of which we may want to
     * modify the level of logging
     */
	public ChangingLevelFrame(LoggingUnit aLoggingUnit) {
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
		this.setSize(200, 100);
		this.setContentPane(getJContentPane());
		this.setTitle("Changing Level Frame");
		this.setVisible(true);
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.CENTER);  // Generated
			jContentPane.add(getJPanel1(), BorderLayout.SOUTH);  // Generated
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			try {
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.fill = GridBagConstraints.BOTH;  // Generated
				gridBagConstraints.weighty = 1.0;  // Generated
				gridBagConstraints.weightx = 1.0;  // Generated
				jPanel = new JPanel();
				jPanel.add(getJComboBox(), gridBagConstraints);  // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
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
				jPanel1.setLayout(new BoxLayout(jPanel1,BoxLayout.X_AXIS));  // Generated
				jPanel1.add(Box.createHorizontalGlue());
				jPanel1.add(getJButton());  // Generated
				jPanel1.add(Box.createHorizontalStrut(10));
				jPanel1.add(getJButton1());  // Generated
				jPanel1.add(Box.createHorizontalGlue());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jPanel1;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			try {
				jButton = new JButton("Validate");
				jButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						loggingUnit.setLevel(Level.parse((String)jComboBox.getSelectedItem()));
						dispose();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			try {
				jButton1 = new JButton("Cancel");
				jButton1.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jButton1;
	}

	/**
	 * This method initializes jComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			try {
				jComboBox = new JComboBox(LoggingUtil.availableLevels);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jComboBox;
	}

}
