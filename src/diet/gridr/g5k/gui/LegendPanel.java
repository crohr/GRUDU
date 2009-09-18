/****************************************************************************/
/* This class corresponds to the legend panel                               */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: LegendPanel.java,v 1.8 2007/10/05 15:19:40 dloureir Exp $
 * $Log: LegendPanel.java,v $
 * Revision 1.8  2007/10/05 15:19:40  dloureir
 * Addition of the button bar from the l2fprod commons for further addition of plugins (such as the ganglia plugin)
 *
 * Revision 1.7  2007/07/12 14:43:05  dloureir
 * Some typo corrections
 *
 * Revision 1.6  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

import diet.logging.LoggingManager;

import java.util.logging.Level;

/**
 * This class corresponds to the legend panel
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class LegendPanel extends JPanel {

    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
	private static final long serialVersionUID = 1L;
    /**
     * panel with the legend text
     */
	private JPanel jPanel = null;
    /**
     * empty cluster panel
     */
	private JPanel jPanel1 = null;
    /**
     * running reservation panel
     */
	private JPanel jPanel2 = null;
    /**
     * not enabled cluster panel
     */
	private JPanel jPanel3 = null;
    /**
     * legend text label
     */
	private JLabel jLabel = null;
    /**
     * label cluster for the empty cluster panel
     */
	private JLabel jLabel1 = null;
    /**
     * label text for the empty reservation panel
     */
	private JLabel jLabel2 = null;
    /**
     * label cluster for the running reservation panel
     */
	private JLabel jLabel3 = null;
    /**
     * label text for the running reservation panel
     */
	private JLabel jLabel4 = null;
    /**
     *label cluster for the not enabled cluster panel
     */
	private JLabel jLabel5 = null;
    /**
     * label text for the not enabled cluster panel
     */
	private JLabel jLabel6 = null;
    /**
     * panel for waiting reserving cluster
     */
	private JPanel jPanel4 = null;
    /**
     * label cluster for waiting reserving cluster
     */
	private JLabel jLabel7 = null;
    /**
     * label text for waiting reserving cluster
     */
	private JLabel jLabel8 = null;

	/**
	 * This is the default constructor
	 */
	public LegendPanel() {
		super();
		initialize();
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "LegendPanel", "Legend Panel initialized");
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
//		Dimension dim = new Dimension(500,140);
//		this.setSize(dim);
//		this.setPreferredSize(dim);
//		this.setMaximumSize(dim);
//		this.setMinimumSize(dim);
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.add(getJPanel(), null);
		this.add(Box.createVerticalStrut(5));
		this.add(getJPanel1(), null);
		this.add(Box.createVerticalStrut(5));
		this.add(getJPanel2(), null);
		this.add(Box.createVerticalStrut(5));
		this.add(getJPanel3(), null);
		this.add(Box.createVerticalStrut(5));
		this.add(getJPanel4(), null);
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel = new JLabel();
			jLabel.setText("Legend :");
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.X_AXIS));
			jPanel.add(jLabel, null);
			jPanel.add(Box.createHorizontalGlue());
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
			jLabel2 = new JLabel();
			jLabel2.setText("<html>a cluster that is enabled and where you have no reservations</html>");
			jLabel1 = new JLabel();
			jLabel1.setText("cluster");
			jLabel1.setOpaque(true);
			jLabel1.setBackground(Color.white);
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(jPanel1,BoxLayout.X_AXIS));
			jPanel1.add(jLabel1, null);
			jPanel1.add(Box.createHorizontalStrut(10));
			jPanel1.add(jLabel2, null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jLabel4 = new JLabel();
			jLabel4.setText("<html>a cluster that is enabled and where you have one or more running reservations</html>");
			jLabel3 = new JLabel();
			jLabel3.setText("cluster");
			jLabel3.setOpaque(true);
			jLabel3.setBackground(Color.GREEN);
			jLabel3.setForeground(Color.RED);
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BoxLayout(jPanel2,BoxLayout.X_AXIS));
			jPanel2.add(jLabel3, null);
			jPanel2.add(Box.createHorizontalStrut(10));
			jPanel2.add(jLabel4, null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jLabel6 = new JLabel();
			jLabel6.setText("<html>a cluster that is not enabled</html>");
			jLabel5 = new JLabel();
			jLabel5.setText("cluster");
			jLabel5.setOpaque(true);
			jLabel5.setBackground(Color.LIGHT_GRAY);
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BoxLayout(jPanel3,BoxLayout.X_AXIS));
			jPanel3.add(jLabel5, null);
			jPanel3.add(Box.createHorizontalStrut(10));
			jPanel3.add(jLabel6, null);
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
			jPanel4 = new JPanel();
			jPanel4.setLayout(new BoxLayout(jPanel4,BoxLayout.X_AXIS));
			jLabel7 = new JLabel();
			jLabel7.setText("<html>a cluster that is enabled and where you have one or more waiting reservations</html>");
			jLabel8 = new JLabel();
			jLabel8.setText("cluster");
			jLabel8.setOpaque(true);
			jLabel8.setBackground(Color.YELLOW);
			jLabel8.setForeground(Color.RED);
			jPanel4.add(jLabel8, null);
			jPanel4.add(Box.createHorizontalStrut(10));
			jPanel4.add(jLabel7, null);
		}
		return jPanel4;
	}
}