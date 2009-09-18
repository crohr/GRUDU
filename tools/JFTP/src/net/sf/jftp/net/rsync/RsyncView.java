/**
 * 
 */
package net.sf.jftp.net.rsync;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

import java.awt.BorderLayout;
import java.awt.CheckboxGroup;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author david
 *
 */
public class RsyncView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JPanel jPanel3 = null;
	private JPanel jPanel4 = null;
	private JPanel jPanel5 = null;
	private JPanel jPanel6 = null;
	private JPanel jPanel7 = null;
	private JPanel jPanel8 = null;
	private JPanel jPanel9 = null;
	private JPanel jPanel10 = null;
	private JPanel jPanel11 = null;
	private JPanel jPanel12 = null;
	private JPanel jPanel13 = null;
	private JPanel jPanel14 = null;
	private JPanel jPanel15 = null;
	private JPanel jPanel16 = null;
	private JPanel jPanel17 = null;
	private JPanel jPanel18 = null;
	private JPanel jPanel19 = null;
	private JPanel jPanel20 = null;
	private JCheckBox jCheckBox = null;
	private JLabel jLabel = null;
	private JCheckBox jCheckBox1 = null;
	private JLabel jLabel1 = null;
	private JCheckBox jCheckBox2 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel4 = null;
	private JLabel jLabel5 = null;
	private JCheckBox jCheckBox6 = null;
	private JLabel jLabel6 = null;
	private JCheckBox jCheckBox7 = null;
	private JLabel jLabel7 = null;
	private JCheckBox jCheckBox8 = null;
	private JLabel jLabel8 = null;
	private JLabel jLabel9 = null;
	private JCheckBox jCheckBox10 = null;
	private JLabel jLabel10 = null;
	private JPanel jPanel21 = null;
	private JTextField jTextField = null;
	private JLabel jLabel11 = null;
	private JTextField jTextField1 = null;
	private JCheckBox checkboxZ = null;
	private JCheckBox checkboxA = null;
	private JCheckBox jRadioButton = null;
	private JCheckBox jRadioButton1 = null;
	private JCheckBox jRadioButton2 = null;
	private RsyncController controller = null; 

	
	
	public RsyncView(RsyncController aController){
		controller = aController;
		initialize();
	}
	
	public void initialize(){
		this.setTitle("Rsync options");
		this.setLayout(new BorderLayout());
		this.setContentPane(getJPanel());
		this.pack();
		
	}
	
	public void showView(){
		this.setVisible(true);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getJPanel1(), BorderLayout.SOUTH);
			jPanel.add(getJPanel2(), BorderLayout.CENTER);
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
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(jPanel1,BoxLayout.X_AXIS));
			jPanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jPanel.add(Box.createHorizontalGlue());
			jPanel1.add(getJButton(), null);
			jPanel1.add(Box.createHorizontalStrut(10));
			jPanel1.add(getJButton1(), null);
			jPanel.add(Box.createHorizontalGlue());
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
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BoxLayout(jPanel2,BoxLayout.Y_AXIS));
			jPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
			jPanel2.add(getJPanel3(), null);
			jPanel2.add(getJPanel4(), null);
			jPanel2.add(getJPanel5(), null);
			jPanel2.add(getJPanel6(), null);
			jPanel2.add(getJPanel7(), null);
			jPanel2.add(Box.createVerticalGlue(),null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton("Apply");
			jButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					controller.setStatesFromView(getStateFromView());
					dispose();
				}
			});
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
			jButton1 = new JButton("Cancel");
			jButton1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					dispose();
				}
			});
		}
		return jButton1;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BoxLayout(jPanel3,BoxLayout.Y_AXIS));
			jPanel3.setBorder(BorderFactory.createTitledBorder("Base options"));
			jPanel3.add(getJPanel8(), null);
			jPanel3.add(getJPanel9(), null);
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
			jPanel4.setLayout(new BoxLayout(jPanel4,BoxLayout.Y_AXIS));
			jPanel4.setBorder(BorderFactory.createTitledBorder("Updating the local/remote files"));
			jPanel4.add(getJPanel10(), null);
			jPanel4.add(getJPanel11(), null);
			jPanel4.add(getJPanel12(), null);
			jPanel4.add(getJPanel13(), null);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jPanel5	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(new BoxLayout(jPanel5,BoxLayout.Y_AXIS));
			jPanel5.setBorder(BorderFactory.createTitledBorder("Deleting parameters"));
			jPanel5.add(getJPanel14(), null);
			jPanel5.add(getJPanel15(), null);
			jPanel5.add(getJPanel16(), null);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jPanel6	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel();
			jPanel6.setLayout(new BoxLayout(jPanel6,BoxLayout.Y_AXIS));
			jPanel6.setBorder(BorderFactory.createTitledBorder("Ignoring some things ..."));
			jPanel6.add(getJPanel17(), null);
			jPanel6.add(getJPanel18(), null);
		}
		return jPanel6;
	}

	/**
	 * This method initializes jPanel7	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel7() {
		if (jPanel7 == null) {
			jPanel7 = new JPanel();
			jPanel7.setLayout(new BoxLayout(jPanel7,BoxLayout.Y_AXIS));
			jPanel7.setBorder(BorderFactory.createTitledBorder("Backup"));
			jPanel7.add(getJPanel20(), null);
			jPanel7.add(getJPanel19(), null);
			jPanel7.add(getJPanel21(), null);
		}
		return jPanel7;
	}

	/**
	 * This method initializes jPanel8	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel8() {
		if (jPanel8 == null) {
			jPanel8 = new JPanel();
			jPanel8.setLayout(new BoxLayout(jPanel8,BoxLayout.X_AXIS));
			checkboxA = new JCheckBox();
			JLabel labelA = new JLabel("Archive mode : all is conserved");
			jPanel8.add(checkboxA);
			jPanel8.add(Box.createHorizontalGlue());
			jPanel8.add(labelA);
			
		}
		return jPanel8;
	}

	/**
	 * This method initializes jPanel9	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel9() {
		if (jPanel9 == null) {
			jPanel9 = new JPanel();
			jPanel9.setLayout(new BoxLayout(jPanel9,BoxLayout.X_AXIS));
			checkboxZ = new JCheckBox();
			JLabel labelZ = new JLabel("Compression of the data to transfer");
			jPanel9.add(checkboxZ);
			jPanel9.add(Box.createHorizontalGlue());
			jPanel9.add(labelZ);
		}
		return jPanel9;
	}

	/**
	 * This method initializes jPanel10	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel10() {
		if (jPanel10 == null) {
			jLabel = new JLabel();
			jLabel.setText("Recursive work");
			jPanel10 = new JPanel();
			jPanel10.setLayout(new BoxLayout(jPanel10,BoxLayout.X_AXIS));
			jPanel10.add(getJCheckBox(), null);
			jPanel10.add(Box.createHorizontalGlue());
			jPanel10.add(jLabel, null);
		}
		return jPanel10;
	}

	/**
	 * This method initializes jPanel11	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Skip files that are newer on the receiver");
			jPanel11 = new JPanel();
			jPanel11.setLayout(new BoxLayout(jPanel11,BoxLayout.X_AXIS));
			jPanel11.add(getJCheckBox1(), null);
			jPanel11.add(Box.createHorizontalGlue());
			jPanel11.add(jLabel1, null);
		}
		return jPanel11;
	}

	/**
	 * This method initializes jPanel12	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel12() {
		if (jPanel12 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("Update destination files in-place");
			jPanel12 = new JPanel();
			jPanel12.setLayout(new BoxLayout(jPanel12,BoxLayout.X_AXIS));
			jPanel12.add(getJCheckBox2(), null);
			jPanel12.add(Box.createHorizontalGlue());
			jPanel12.add(jLabel2, null);
		}
		return jPanel12;
	}

	/**
	 * This method initializes jPanel13	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel13() {
		if (jPanel13 == null) {
			jLabel10 = new JLabel();
			jLabel10.setText("Append data onto shorter files");
			jPanel13 = new JPanel();
			jPanel13.setLayout(new BoxLayout(jPanel13,BoxLayout.X_AXIS));
			jPanel13.add(getJCheckBox10(), null);
			jPanel13.add(Box.createHorizontalGlue());
			jPanel13.add(jLabel10, null);
		}
		return jPanel13;
	}

	/**
	 * This method initializes jPanel14	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel14() {
		if (jPanel14 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("Deleting files before transfer");
			jPanel14 = new JPanel();
			jPanel14.setLayout(new BoxLayout(jPanel14,BoxLayout.X_AXIS));
			jPanel14.add(getJRadioButton(), null);
			jPanel14.add(Box.createHorizontalGlue());
			jPanel14.add(jLabel3, null);
			
		}
		return jPanel14;
	}

	/**
	 * This method initializes jPanel15	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel15() {
		if (jPanel15 == null) {
			jLabel4 = new JLabel();
			jLabel4.setText("Deleting files during transfer");
			jPanel15 = new JPanel();
			jPanel15.setLayout(new BoxLayout(jPanel15,BoxLayout.X_AXIS));
			jPanel15.add(getJRadioButton1(), null);
			jPanel15.add(Box.createHorizontalGlue());
			jPanel15.add(jLabel4, null);
			
		}
		return jPanel15;
	}

	/**
	 * This method initializes jPanel16	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel16() {
		if (jPanel16 == null) {
			jLabel5 = new JLabel();
			jLabel5.setText("Deleting files after transfer");
			jPanel16 = new JPanel();
			jPanel16.setLayout(new BoxLayout(jPanel16,BoxLayout.X_AXIS));
			jPanel16.add(getJRadioButton2(), null);
			jPanel16.add(Box.createHorizontalGlue());
			jPanel16.add(jLabel5, null);
			
		}
		return jPanel16;
	}

	/**
	 * This method initializes jPanel17	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel17() {
		if (jPanel17 == null) {
			jLabel6 = new JLabel();
			jLabel6.setText("Ignoring symlinks that point outside the directory");
			jPanel17 = new JPanel();
			jPanel17.setLayout(new BoxLayout(jPanel17,BoxLayout.X_AXIS));
			jPanel17.add(getJCheckBox6(), null);
			jPanel17.add(Box.createHorizontalGlue());
			jPanel17.add(jLabel6, null);
		}
		return jPanel17;
	}

	/**
	 * This method initializes jPanel18	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel18() {
		if (jPanel18 == null) {
			jLabel7 = new JLabel();
			jLabel7.setText("Skipping updating files that exist on receiver");
			jPanel18 = new JPanel();
			jPanel18.setLayout(new BoxLayout(jPanel18,BoxLayout.X_AXIS));
			jPanel18.add(getJCheckBox7(), null);
			jPanel18.add(Box.createHorizontalGlue());
			jPanel18.add(jLabel7, null);
		}
		return jPanel18;
	}

	/**
	 * This method initializes jPanel19	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel19() {
		if (jPanel19 == null) {
			jLabel9 = new JLabel();
			jLabel9.setText("Make backup into hierachies base directory DIR");
			jPanel19 = new JPanel();
			jPanel19.setLayout(new GridLayout(1,3));
			jPanel19.add(getJTextField1(), null);
			jPanel19.add(Box.createHorizontalGlue());
			jPanel19.add(jLabel9, null);
		}
		return jPanel19;
	}

	/**
	 * This method initializes jPanel20	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel20() {
		if (jPanel20 == null) {
			jLabel8 = new JLabel();
			jLabel8.setText("Make backup");
			jPanel20 = new JPanel();
			jPanel20.setLayout(new BoxLayout(jPanel20,BoxLayout.X_AXIS));
			jPanel20.add(getJCheckBox8(), null);
			jPanel20.add(Box.createHorizontalGlue());
			jPanel20.add(jLabel8, null);
		}
		return jPanel20;
	}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
		}
		return jCheckBox;
	}

	/**
	 * This method initializes jCheckBox1	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox1() {
		if (jCheckBox1 == null) {
			jCheckBox1 = new JCheckBox();
		}
		return jCheckBox1;
	}

	/**
	 * This method initializes jCheckBox2	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox2() {
		if (jCheckBox2 == null) {
			jCheckBox2 = new JCheckBox();
		}
		return jCheckBox2;
	}

	/**
	 * This method initializes jCheckBox6	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox6() {
		if (jCheckBox6 == null) {
			jCheckBox6 = new JCheckBox();
		}
		return jCheckBox6;
	}

	/**
	 * This method initializes jCheckBox7	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox7() {
		if (jCheckBox7 == null) {
			jCheckBox7 = new JCheckBox();
		}
		return jCheckBox7;
	}

	/**
	 * This method initializes jCheckBox8	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox8() {
		if (jCheckBox8 == null) {
			jCheckBox8 = new JCheckBox();
			jCheckBox8.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					boolean state = jCheckBox8.isSelected();
						jTextField.setEnabled(state);
						jTextField1.setEnabled(state);
				}
			});
		}
		return jCheckBox8;
	}

	/**
	 * This method initializes jCheckBox10	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBox10() {
		if (jCheckBox10 == null) {
			jCheckBox10 = new JCheckBox();
		}
		return jCheckBox10;
	}

	/**
	 * This method initializes jPanel21	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel21() {
		if (jPanel21 == null) {
			jLabel11 = new JLabel();
			jLabel11.setText("Suffix for the backup");
			jLabel11.setHorizontalAlignment(JLabel.RIGHT);
			jPanel21 = new JPanel();
			jPanel21.setLayout(new GridLayout(1,3));
			jPanel21.add(getJTextField(), null);
			jPanel21.add(Box.createHorizontalGlue());
			jPanel21.add(jLabel11, null);
		}
		return jPanel21;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
		}
		return jTextField;
	}

	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextField1() {
		if (jTextField1 == null) {
			jTextField1 = new JTextField();
		}
		return jTextField1;
	}	
	
	private String getChecked(JToggleButton checkbox){
		if(checkbox.isSelected()) return RsyncModel.CHECKED;
		else{
			return RsyncModel.UNCHECKED;
		}
	}
	
	public HashMap<String, String> getStateFromView(){
		HashMap<String, String> states = new HashMap<String, String>();
		states.put(RsyncModel.RECURSIVE_OPTION, getChecked(jCheckBox));
		states.put(RsyncModel.UPDATE_STRING, getChecked(jCheckBox1));
		states.put(RsyncModel.INPLACE_OPTION, getChecked(jCheckBox2));
		states.put(RsyncModel.APPEND_OPTION, getChecked(jCheckBox10));
		states.put(RsyncModel.SAFE_LINKS_OPTION, getChecked(jCheckBox6));
		states.put(RsyncModel.IGNORE_EXISTING_OPTION, getChecked(jCheckBox7));
		states.put(RsyncModel.DELETE_DURING_OPTION, getChecked(jRadioButton1));
		states.put(RsyncModel.DELETE_AFTER_OPTION, getChecked(jRadioButton2));
		states.put(RsyncModel.A_OPTION, getChecked(checkboxA));
		states.put(RsyncModel.Z_OPTION, getChecked(checkboxZ));
		states.put(RsyncModel.B_OPTION, getChecked(jCheckBox8));
		states.put(RsyncModel.DELETE_OPTION, getChecked(jRadioButton));
		states.put(RsyncModel.BACKUP_OPTION, jTextField1.getText());
		states.put(RsyncModel.SUFFIX_OPTION,jTextField.getText());
		return states;
	}
	
	private boolean isSelected(String state){
		if(state.equalsIgnoreCase(RsyncModel.CHECKED)) return true;
		else return false;
	}
	
	public void setStatesToView(HashMap<String, String> states){
		jCheckBox.setSelected(isSelected(states.get(RsyncModel.RECURSIVE_OPTION)));
		jCheckBox1.setSelected(isSelected(states.get(RsyncModel.UPDATE_STRING)));
		jCheckBox2.setSelected(isSelected(states.get(RsyncModel.INPLACE_OPTION)));
		jCheckBox10.setSelected(isSelected(states.get(RsyncModel.APPEND_OPTION)));
		jCheckBox6.setSelected(isSelected(states.get(RsyncModel.SAFE_LINKS_OPTION)));
		jCheckBox7.setSelected(isSelected(states.get(RsyncModel.IGNORE_EXISTING_OPTION)));
		jRadioButton1.setSelected(isSelected(states.get(RsyncModel.DELETE_DURING_OPTION)));
		jRadioButton2.setSelected(isSelected(states.get(RsyncModel.DELETE_AFTER_OPTION)));
		checkboxA.setSelected(isSelected(states.get(RsyncModel.A_OPTION)));
		checkboxZ.setSelected(isSelected(states.get(RsyncModel.Z_OPTION)));
		jCheckBox8.setSelected(isSelected(states.get(RsyncModel.B_OPTION)));
		jRadioButton.setSelected(isSelected(states.get(RsyncModel.DELETE_OPTION)));
		if(jCheckBox8.isSelected()){
			jTextField1.setText(states.get(RsyncModel.BACKUP_OPTION));
			jTextField.setText(states.get(RsyncModel.SUFFIX_OPTION));
		}else{
			jTextField.setEnabled(false);
			jTextField1.setEnabled(false);
		}
		
	}

	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JCheckBox getJRadioButton() {
		if (jRadioButton == null) {
			jRadioButton = new JCheckBox();
			jRadioButton.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == e.SELECTED){
						jRadioButton1.setSelected(false);
						jRadioButton2.setSelected(false);
					}
//					else{
//						jRadioButton1.setSelected(true);
//						jRadioButton2.setSelected(true);
//					}
				}
			});
		}
		return jRadioButton;
	}

	/**
	 * This method initializes jRadioButton1	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JCheckBox getJRadioButton1() {
		if (jRadioButton1 == null) {
			jRadioButton1 = new JCheckBox();
			jRadioButton1.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == e.SELECTED){
						jRadioButton.setSelected(false);
						jRadioButton2.setSelected(false);
					}
//					else{
//						jRadioButton.setSelected(true);
//						jRadioButton2.setSelected(true);
//					}
				}
			});
		}
		return jRadioButton1;
	}

	/**
	 * This method initializes jRadioButton2	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JCheckBox getJRadioButton2() {
		if (jRadioButton2 == null) {
			jRadioButton2 = new JCheckBox();
			jRadioButton2.addItemListener(new ItemListener(){

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == e.SELECTED){
						jRadioButton.setSelected(false);
						jRadioButton1.setSelected(false);
					}
//					else{
//						jRadioButton.setSelected(true);
//						jRadioButton1.setSelected(true);
//					}
				}
			});
		}
		return jRadioButton2;
	}
}  //  @jve:decl-index=0:visual-constraint="12,3"
