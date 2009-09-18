/****************************************************************************/
/* This class corresponds to the animated panel used to frame allowing the  */
/* configuration of the G5k Config                                          */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kCfgDlg.java,v 1.9 2007/07/18 14:07:47 dloureir Exp $
 * $Log: G5kCfgDlg.java,v $
 * Revision 1.9  2007/07/18 14:07:47  dloureir
 * The configuration of GRUDU has now only two buttons for the saving of the modified (or not) configuration and the correction of a little bug : the JList of theUserPanel is composed of external acces frontales whereas it was not the case before (it was site's names).
 *
 * Revision 1.8  2007/06/26 15:06:13  dloureir
 * The ways the environment variables are stored has been modified to be compliant with GoDIET (the last version). You can now supply all the variables you want for each sites.
 *
 * Revision 1.7  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.*;

import diet.logging.LoggingManager;

/**
 * This class corresponds to the animated panel used to frame allowing the
 * configuration of the G5k Config
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kCfgDlg extends JFrame {

    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
	private static final long serialVersionUID = -8399748725859582143L;

	/**
     * tab panel of the Grid 5000 site config
     */
	private G5kCfgTabPane myTabPane;

	private JPanel jButtonPanel = null;
	
	private JButton saveButton = null;
	
	private JButton cancelButton = null;
	
    /**
     * Default constructor of the G5kCfgDlg
     *
     * @param parent parent frame
     */
	public G5kCfgDlg() {
		this.setTitle("Grid 5000 reservation tool configuration");
		this.myTabPane = new G5kCfgTabPane();
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		this.getContentPane().add(this.myTabPane);
		this.getContentPane().add(Box.createVerticalGlue());
		this.getContentPane().add(getButtonJPanel());
		this.setSize(480,700);
		this.setResizable(false);
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kCfgDlg","G5k Config Dialog Frame initialized");
	}
	
	private JPanel getButtonJPanel(){
		if(jButtonPanel == null){
			jButtonPanel = new JPanel();
			jButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			jButtonPanel.setLayout(new BoxLayout(jButtonPanel,BoxLayout.X_AXIS));
			saveButton = new JButton("Save");
			saveButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					myTabPane.apply();
				}
			});
			cancelButton = new JButton("Close");
			cancelButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					dispose();
				}
			});
			jButtonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
			jButtonPanel.add(saveButton);
			jButtonPanel.add(Box.createHorizontalStrut(10));
			jButtonPanel.add(cancelButton);
		}
		return jButtonPanel;
	}

    /**
     * Method launching the Dialog frame
     *
     * @return an integer corresponding to the status of the dialog frame
     */
	public int exec() {
		LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "exec","G5k Config Dialog Frame execution");
		setVisible(true);
		return 0;
	}

    /**
     * Method applying the modification realized in this dialog frame
     *
     */
	public void apply() {
		this.myTabPane.apply();
	}

}