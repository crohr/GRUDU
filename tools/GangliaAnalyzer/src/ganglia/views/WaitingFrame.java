/****************************************************************************/
/* This class corresponds to a waiting frame with some status informations  */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: WaitingFrame.java,v 1.1 2007/10/05 16:00:14 dloureir Exp $
 * $Log: WaitingFrame.java,v $
 * Revision 1.1  2007/10/05 16:00:14  dloureir
 * tools for the ganglia plugin
 *
 * Revision 1.7  2007/03/26 20:26:41  dloureir
 * Adding an infinite progress bar to the waiting frame
 *
 * Revision 1.6  2007/03/23 16:43:58  dloureir
 * Removing import creating an error in the WaitingFrame
 *
 * Revision 1.5  2007/03/08 12:18:17  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.4  2007/03/05 13:18:56  dloureir
 * Adding documentation
 *
 ****************************************************************************/
package ganglia.views;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.swing.JProgressBar;

/**
 * This class corresponds to a waiting frame with some status informations
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class WaitingFrame extends JFrame {

    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
     */
	private static final long serialVersionUID = 1L;

    /**
     * Content pane of the frame
     */
	private JPanel jContentPane = null;

    /**
     * Main panel of the wainting frame
     */
	private JPanel jPanel = null;
	/**
     * Panel of the principal text
	 */
	private JPanel jPanel1 = null;
	/**
     * Status text panel
	 */
	private JPanel jPanel2 = null;
    /**
     * label of the principal text
     */
	private JLabel jLabel = null;
	/**
     * evolving status text label
	 */
	private JLabel jLabel1 = null;
	/**
     * progress bar showing the evolution
	 */
	private JProgressBar jProgressBar = null;
	/**
     * label for the status text
	 */
	private JLabel jLabel2 = null;
	/**
     * principal text String
	 */
	private String principalText = null;  //  @jve:decl-index=0:
	/**
     * frame title
	 */
	private String frameTitle = null;
	/**
     * default status text
	 */
	private String statusText = "";
	/**
     * maximum value for the progress bar
	 */
	private int maximumValueForProgressBar = 0;
	/**
     * boolean value defining if the status should be shown
	 */
	private boolean withStatus = false;

    /**
     * Method incrementing the progress bar value
     *
     */
	public void incrementProgressBar(){
		jProgressBar.setValue(jProgressBar.getValue()+1);
	}

	/**
     * Method setting the status text of the waiting frame
     *
	 * @param statusText the statusText to set
	 */
	public void setStatusText(String statusText) {
		this.statusText = statusText;
		jLabel1.setText(statusText);  // Generated
	}

	/**
     * Default constructor of the waiting frame.
     *
     * @param frameTitle frame title
     * @param principalText principal text of the frame
     * @param maximumValueForProgressBar maximum value for the progressbar
     * @param withStatus boolean value defining if the status should be shown
	 */
	public WaitingFrame(String frameTitle, String principalText, int maximumValueForProgressBar,boolean withStatus) {
		super();
		this.principalText = principalText;
		this.frameTitle = frameTitle;
		this.maximumValueForProgressBar = maximumValueForProgressBar;
		this.withStatus = withStatus;
		initialize();
	}

    /**
     * Method launching the frame with the initial status text given in parameter
     *
     * @param initialStatusText initial status text
     */
	public void launch(String initialStatusText){
		statusText = initialStatusText;
		this.setVisible(true);
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 150);
		this.setContentPane(getJContentPane());
		this.setTitle(frameTitle);
		this.setLocationRelativeTo(null);
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
			jContentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			jContentPane.add(getJPanel(), BorderLayout.CENTER);  // Generated
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            jContentPane.add(progressBar,BorderLayout.SOUTH);
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
				gridBagConstraints.gridx = 0;  // Generated
				gridBagConstraints.gridy = 0;  // Generated
				gridBagConstraints.weightx=1.0;
				gridBagConstraints.weighty=1.0;
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.gridx = 0;  // Generated
				gridBagConstraints1.gridy = 1;  // Generated
				gridBagConstraints1.weightx=1.0;
				gridBagConstraints1.weighty=1.0;
				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.gridx = 0;  // Generated
				gridBagConstraints2.gridy = 2;  // Generated
				gridBagConstraints2.weightx=2.0;
				gridBagConstraints2.weighty=1.0;
                gridBagConstraints2.fill=GridBagConstraints.BOTH;
				gridBagConstraints.anchor = GridBagConstraints.WEST;
				gridBagConstraints1.anchor = GridBagConstraints.WEST;
				jPanel = new JPanel();
				jPanel.setLayout(new GridBagLayout());  // Generated
				jPanel.add(getJPanel1(), gridBagConstraints);  // Generated
				if(withStatus)jPanel.add(getJPanel2(), gridBagConstraints1);  // Generated
				jPanel.add(getJProgressBar(), gridBagConstraints2);  // Generated
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
				jLabel = new JLabel();
				jLabel.setText(principalText);  // Generated
				jPanel1 = new JPanel();
				jPanel1.setLayout(new BoxLayout(jPanel1,BoxLayout.X_AXIS));  // Generated
				jPanel1.add(jLabel);  // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
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
			try {
				jLabel2 = new JLabel();
				jLabel2.setText("Status : ");  // Generated
				jLabel1 = new JLabel();
				jLabel1.setText(statusText);  // Generated
				jPanel2 = new JPanel();
				jPanel2.setLayout(new BoxLayout(jPanel2,BoxLayout.X_AXIS));  // Generated
				jPanel2.add(jLabel2, null);  // Generated
				jPanel.add(Box.createHorizontalGlue());
				jPanel2.add(jLabel1);  // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jPanel2;
	}

	/**
	 * This method initializes jProgressBar
	 *
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			try {
				jProgressBar = new JProgressBar(0,maximumValueForProgressBar);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jProgressBar;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
