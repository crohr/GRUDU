/**
 * 
 */
package ganglia.views;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ganglia.controller.history.GangliaHistoryController;

/**
 * @author david
 *
 */
public class GangliaHistoryView extends JPanel {

	private GangliaHistoryController controller = null;
	
	private JPanel viewJPanel = null;
	
	private JPanel hostSelectionJPanel = null;
	
	private JPanel clusterInformationPanel = null;
	
	private CardLayout cardLayout = null;
	
	private JPanel rightPanel = null;
	
	private JComboBox jComboBoxOfHosts = null;
	
	private JButton refreshJButton = null;
	
	public GangliaHistoryView(GangliaHistoryController myController){
		controller = myController;
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		add(getRightPanel());
	}
	
	private JPanel getHostSelectionPanel(){
		if(hostSelectionJPanel == null){
			hostSelectionJPanel = new JPanel();
			hostSelectionJPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			hostSelectionJPanel.setLayout(new BoxLayout(hostSelectionJPanel,BoxLayout.X_AXIS));
			refreshJButton = new JButton("Get fresh data");
			refreshJButton.setVisible(false);
			hostSelectionJPanel.add(refreshJButton);
			hostSelectionJPanel.add(Box.createHorizontalStrut(10));
			hostSelectionJPanel.add(new JLabel("Host : "));
			hostSelectionJPanel.add(Box.createHorizontalGlue());
			hostSelectionJPanel.add(getJListOfHosts());
		}
		return hostSelectionJPanel;
	}
	
	private JComboBox getJListOfHosts(){
		if(jComboBoxOfHosts == null){
			Vector<String> vector = new Vector<String>();
			vector.add("Configuration");
			jComboBoxOfHosts = new JComboBox(vector);
		}
		return jComboBoxOfHosts;
	}
	
	private JPanel getViewJPanel(){
		if(viewJPanel == null){
			viewJPanel = new JPanel();
			cardLayout = new CardLayout();
			viewJPanel.setLayout(cardLayout);
			viewJPanel.add("Configuration",getClusterInformationPanel() );
			cardLayout.show(viewJPanel, "Configuration");
		}
		return viewJPanel;
	}
	
	private JPanel getClusterInformationPanel(){
		if(clusterInformationPanel == null){
			clusterInformationPanel = new GangliaHistoryConfigurationView(controller,this);
		}
		return clusterInformationPanel;
	}
	
	private JPanel getRightPanel(){
		if(rightPanel == null){
			rightPanel = new JPanel();
			rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
			rightPanel.add(getHostSelectionPanel());
			rightPanel.add(getViewJPanel());
			rightPanel.add(Box.createVerticalGlue());
		}
		return rightPanel;
	}
	
	public void updateTheView(){
		refreshJButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.updateObserver();
			}
		});
		refreshJButton.setVisible(true);
		controller.startHistoryGenerator();
		ArrayList<String> keyList = new ArrayList<String>(controller.getModel().getListOfHosts());
		Collections.sort(keyList);
		Iterator<String> iter = keyList.iterator();
		Vector<String> vector = new Vector<String>();
		vector.add("Configuration");
		
		while(iter.hasNext()){
			String hostName = iter.next();
			vector.add(hostName);
		}
		hostSelectionJPanel.remove(jComboBoxOfHosts);
		jComboBoxOfHosts = new JComboBox(vector);
		jComboBoxOfHosts.validate();
		jComboBoxOfHosts.addItemListener(new ItemListener(){

			/* (non-Javadoc)
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				cardLayout.show(viewJPanel, jComboBoxOfHosts.getSelectedItem().toString());
				validate();
			}
		});
		
		iter = controller.getModel().getListOfHosts().iterator();
		hostSelectionJPanel.add(jComboBoxOfHosts);

		while(iter.hasNext()){
			String hostName = iter.next();
			HostHistoryView view = new HostHistoryView(hostName,controller);
			view.validate();
			viewJPanel.add(hostName,view);
			viewJPanel.validate();
		}
		validate();
	}
}
