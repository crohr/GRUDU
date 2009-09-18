/**
 * 
 */
package diet.gridr.g5k.gui;

import ganglia.controller.GlobalDataController;
import ganglia.views.JobPanelWithGanglia;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.jdom.Element;

import com.l2fprod.common.swing.JButtonBar;

import com.trilead.ssh2.Connection;

import diet.grid.api.GridJob;
import diet.gridr.g5k.util.G5kCfg;
import diet.gridr.g5k.util.G5kSite;

/**
 * @author david
 *
 */
public class SiteInformationPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	/**
     * Data to display
     */
	private int[] data = null;
    /**
     * Name of the cluster
     */
	private String siteName = null;
    /**
     * HashMap containing all jobs
     */
	private Map<String, GridJob> jobsMap = null;
	
	private Connection connection = null;
	
	private ClusterInfoPanel clusterInfoPanel = null;
	
	private JButtonBar toolbar = null;
	
	private ButtonGroup group = null;
	
	private JPanel cardPanel = null;
	
	private CardLayout cardLayout = null;
	
	private JPanel gangliaInformationPanel = null;
	
	private GlobalDataController globaldataController = null;
	
	private HashMap<String, String> properties = null;
	
	private static String gangliaTitle = "<html><center>Ganglia<br> Info</center></html>";
	
	public SiteInformationPanel (int[] myData, Map<String, GridJob> myJobsMap, String mySiteName,Connection myConnection){
		data = myData;
		jobsMap = myJobsMap;
		siteName = mySiteName;
		connection = myConnection;
		properties = new HashMap<String, String>();
		properties.put("user",G5kCfg.get(G5kCfg.USERNAME));
		properties.put("keyFile", G5kCfg.get(G5kCfg.SSHKEYFILE));
		properties.put("passphrase", G5kCfg.getSSHKey());
		properties.put("accesFrontale",G5kCfg.get(G5kCfg.PREFERREDACCESPOINT));
		properties.put("destination", G5kSite.getInternalFrontalForSite(siteName));
		properties.put("connexionCommand", "ssh");
		initialize();
	}
	
	private void initialize(){
		this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		this.setLayout(new BorderLayout());
		this.add(getJButtomBar(),BorderLayout.EAST);
		this.add(getCardPanel(),BorderLayout.CENTER);
	}
	
	private JPanel getCardPanel(){
		if(cardPanel == null){
			cardPanel = new JPanel();
			cardPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			cardLayout = new CardLayout();
			cardPanel.setLayout(cardLayout);
			addButton("<html><center>Cluster<br> Info</center></html>", "/resources/gridr/g5k/jobIcon.png" ,
					makePanel("Job description",getClusterInfoPanel()), toolbar, group);
			boolean classLoadingSuccess = false;
			try{
				Class aClass = Class.forName("ganglia.controller.GangliaAnalyzer");
				classLoadingSuccess = true;
			}
			catch(Exception e){}
			if(classLoadingSuccess){
				addButton("<html><center>Ganglia<br> Info</center></html>", "/images/gangliaIcon.png" ,
						makePanel("Ganglia Information",getGangliaInformationPanel()), toolbar, group);
			}

		}
		return cardPanel;
	}
	
	private JPanel getGangliaInformationPanel(){
		if(gangliaInformationPanel == null){
			gangliaInformationPanel = new JPanel();
			globaldataController = new GlobalDataController(properties,gangliaInformationPanel);
		}
		return gangliaInformationPanel;
	}
	
	private JPanel getClusterInfoPanel(){
		if(clusterInfoPanel == null){
			clusterInfoPanel = new ClusterInfoPanel(data,jobsMap,siteName,connection);
		}
		return clusterInfoPanel;
	}
	
	private JPanel makePanel(String title,JPanel paneltoInsert){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2,2));
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		JLabel top = new JLabel(title);
		top.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		top.setFont(top.getFont().deriveFont(Font.BOLD).deriveFont(20.0f));
		top.setForeground(Color.blue);
		top.setOpaque(true);
//		top.setMinimumSize(new Dimension(675,40));
//		top.setPreferredSize(new Dimension(675,40));
		top.setBackground(Color.white);
		topPanel.add(top,BorderLayout.NORTH);
		
		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		topPanel.add(separator,BorderLayout.CENTER);	
		
		panel.add(topPanel,BorderLayout.NORTH);
		
		panel.add(paneltoInsert,BorderLayout.CENTER);
		
		return panel;
	}
	
	private void addButton(final String title, String iconUrl,final Component component, JButtonBar bar, ButtonGroup group) {
		Action action = new AbstractAction(title, new ImageIcon(getClass().getResource(iconUrl))) {
			public void actionPerformed(ActionEvent e) {
				if(title.equals(gangliaTitle)){
					globaldataController.start();
					cardPanel.remove(gangliaInformationPanel);
					gangliaInformationPanel = globaldataController.getDataPanel();
					cardPanel.add(title, makePanel("Ganglia Information",gangliaInformationPanel));
					cardPanel.revalidate();
					cardPanel.repaint();
					gangliaInformationPanel.invalidate();
					gangliaInformationPanel.revalidate();
					gangliaInformationPanel.repaint();
				}
				
				cardLayout.show(cardPanel,title);
			}
		};
		cardPanel.add(title, component);
		JToggleButton button = new JToggleButton(action);
		bar.add(button);

		group.add(button);

		if (group.getSelection() == null) {
			button.setSelected(true);
			cardLayout.show(cardPanel,title);
		}
	}
	
	private JButtonBar getJButtomBar(){
		if(toolbar == null){
			toolbar = new JButtonBar(JButtonBar.VERTICAL);
			group = new ButtonGroup();
		}
		return toolbar;
	}
	
}
