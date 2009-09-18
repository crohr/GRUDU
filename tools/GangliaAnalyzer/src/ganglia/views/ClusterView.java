/**
 * 
 */
package ganglia.views;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import ganglia.models.Cluster;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * @author david
 *
 */
public class ClusterView extends JPanel {

	private static final long serialVersionUID = 1L;

	private Cluster myCluster = null;
	
	private JPanel viewJPanel = null;
	
	private JPanel hostSelectionJPanel = null;
	
	private JPanel clusterInformationPanel = null;
	
	private CardLayout cardLayout = null;
	
	private JPanel rightPanel = null;
	
	private HostView[] hostViews = null;
	
	private JComboBox jComboBoxOfHosts = null;
	
	private ArrayList<String> hostsToDisplay = null;
	
	/**
	 * This is the default constructor
	 */
	public ClusterView(Cluster aCluster) {
		super();
		myCluster = aCluster;
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
			hostSelectionJPanel.add(new JLabel("Host : "));
			hostSelectionJPanel.add(Box.createHorizontalGlue());
			hostSelectionJPanel.add(getJListOfHosts());
		}
		return hostSelectionJPanel;
	}
	
	private JComboBox getJListOfHosts(){
		if(jComboBoxOfHosts == null){
			
			hostsToDisplay = new ArrayList<String>(myCluster.getHosts().keySet());
			Collections.sort(hostsToDisplay);
			Iterator<String> iter = hostsToDisplay.iterator();
			Vector<String> vector = new Vector<String>();
			vector.add(myCluster.getName());
			while(iter.hasNext()){
				String hostName = iter.next();
				vector.add(hostName);
			}
			jComboBoxOfHosts = new JComboBox(vector);
			//jListsOfHosts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		}
		return jComboBoxOfHosts;
	}

//				/* (non-Javadoc)
//				 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
//				 */
//				public void valueChanged(TreeSelectionEvent e) {
//					
//					cardLayout.show(viewJPanel, currentNode.toString());
//                    validate();
//				}
	
	private JPanel getViewJPanel(){
		if(viewJPanel == null){
			viewJPanel = new JPanel();
			cardLayout = new CardLayout();
			viewJPanel.setLayout(cardLayout);
			viewJPanel.add(myCluster.getName(),getClusterInformationPanel() );
			Iterator<String> iter = hostsToDisplay.iterator();
			hostViews = new HostView[hostsToDisplay.size()];
			int i = 0;
			while(iter.hasNext()){
				String hostName = iter.next();
				//System.out.println(hostName);
				HostView hostView = new HostView(myCluster.getHosts().get(hostName));
				hostViews[i] = hostView;
				viewJPanel.add(hostName,hostView);
			}
			cardLayout.show(viewJPanel, myCluster.getName());
		}
		return viewJPanel;
	}
	
	private JPanel getClusterInformationPanel(){
		if(clusterInformationPanel == null){
			clusterInformationPanel = new JPanel();
			clusterInformationPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			clusterInformationPanel.setLayout(new BoxLayout(clusterInformationPanel,BoxLayout.Y_AXIS));
			JPanel tempJPanel = new JPanel();
			tempJPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			clusterInformationPanel.add(tempJPanel);
			JLabel tempJLabel = new JLabel("Page of information taken from Ganglia");
			tempJLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
			tempJPanel.add(tempJLabel);
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
	
}
