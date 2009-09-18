/**
 *
 */
package com.izforge.izpack.panels.grudu_utils;

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
 * @author david
 *
 */
public class WaitingFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JProgressBar jProgressBar = null;

	private JLabel jLabel2 = null;

	private String principalText = null;  //  @jve:decl-index=0:

	private String frameTitle = null;

	private String statusText = "";

	private int maximumValueForProgressBar = 0;

	private boolean withStatus = false;

	public void incrementProgressBar(){
		jProgressBar.setValue(jProgressBar.getValue()+1);
	}

	/**
	 * @param statusText the statusText to set
	 */
	public void setStatusText(String statusText) {
		this.statusText = statusText;
		jLabel1.setText(statusText);  // Generated
	}

	/**
	 * This is the default constructor
	 */
	public WaitingFrame(String frameTitle, String principalText, int maximumValueForProgressBar,boolean withStatus) {
		super();
		this.principalText = principalText;
		this.frameTitle = frameTitle;
		this.maximumValueForProgressBar = maximumValueForProgressBar;
		this.withStatus = withStatus;
		initialize();
	}

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

	public static void main(String args[]){
		final WaitingFrame myFrame =  new WaitingFrame("Waiting frame test","Testing the frame, please wait...",10,true);
		myFrame.launch("Testing ...");
		Thread th = new Thread(new Runnable(){
			public void run(){
				try{
					for(int i = 0 ; i < 10 ; i ++){
						Thread.sleep(1000);
						myFrame.setStatusText("Testing : index = " + i);
						myFrame.incrementProgressBar();
					}
					myFrame.incrementProgressBar();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		th.start();
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
