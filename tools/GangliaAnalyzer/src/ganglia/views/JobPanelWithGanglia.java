/**
 * 
 */
package ganglia.views;

import ganglia.controller.DataRetriever;
import ganglia.controller.GlobalDataController;
import ganglia.controller.history.GangliaHistoryController;
import ganglia.models.Cluster;
import ganglia.models.ClusterXML;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.l2fprod.common.swing.JButtonBar;

/**
 * @author david
 *
 */
public class JobPanelWithGanglia extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButtonBar toolbar = null;
	private ButtonGroup group = null;
	private JPanel cardPanel = null;
	private CardLayout cardLayout = null;
	private JPanel gangliaInformationPanel = null;
	private boolean gangliaInfoGenerated = false;
	private HashMap<String, String> properties = null;
	private ArrayList<String> hostsToUse = null;
	private JPanel gangliaHistoryPanel = null;
	private static String jobTitle = "<html><center>Job <br> Info </center></html>";
	private static String gangliaTitle = "<html><center>Ganglia <br> Info </center></html>";
	private static String gangliaHistoryTitle = "<html><center>Ganglia <br> History </center></html>";
	private static String jobIcon = "/images/jobIcon.png";
	private static String gangliaIcon = "/images/gangliaIcon.png";
	private static String gangliaHistoryIcon = "/images/gangliaHistory.png";
	private int jobId;
	private long endOfTheReservation;
	private GlobalDataController globaldataController = null;
	/**
	 * This is the default constructor
	 */
	public JobPanelWithGanglia(HashMap<String, String> properties,ArrayList<String> hostsList,int jobId, long endOfTheReservation ) {
		super();
		hostsToUse = hostsList;
		this.properties = properties;
		this.jobId = jobId;
		this.endOfTheReservation = endOfTheReservation;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.add(getJContentPane());
		
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
			jContentPane.add(getJButtomBar(),BorderLayout.EAST);
			jContentPane.add(getCardPanel(),BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private JButtonBar getJButtomBar(){
		if(toolbar == null){
			toolbar = new JButtonBar(JButtonBar.VERTICAL);
			group = new ButtonGroup();
		}
		return toolbar;
	}

	private JPanel getCardPanel(){
		if(cardPanel == null){
			cardPanel = new JPanel();
			cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			cardLayout = new CardLayout();
			cardPanel.setLayout(cardLayout);
			addButton(jobTitle, jobIcon,
					makePanel("Job description",getJobInformationPanel()), toolbar, group);

			addButton(gangliaTitle, gangliaIcon,
					makePanel("Ganglia information",getGangliaInformationPanel()), toolbar, group);
			addButton(gangliaHistoryTitle, gangliaHistoryIcon,
					makePanel("Ganglia history",getGangliaHistoryPanel()), toolbar, group);

		}
		return cardPanel;
	}
	
	private JPanel getGangliaHistoryPanel(){
		if(gangliaHistoryPanel == null){
			GangliaHistoryController controller = new GangliaHistoryController(jobId,hostsToUse,endOfTheReservation,properties);
			gangliaHistoryPanel = new GangliaHistoryView(controller);
		}
		return gangliaHistoryPanel;
	}

	private JPanel getGangliaInformationPanel(){
		if(gangliaInformationPanel == null){
			gangliaInformationPanel = new JPanel();
			globaldataController = new GlobalDataController(properties,gangliaInformationPanel);
		}
		return gangliaInformationPanel;
	}
	
	private JPanel getJobInformationPanel(){
		return new JPanel();
	}

	private JPanel makePanel(String title,JPanel paneltoInsert){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		JLabel top = new JLabel(title);
		top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		top.setFont(top.getFont().deriveFont(Font.BOLD).deriveFont(20.0f));
		top.setForeground(Color.blue);
		top.setOpaque(true);
		top.setMinimumSize(new Dimension(675,40));
		top.setPreferredSize(new Dimension(675,40));
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
					cardLayout.show(cardPanel,title);
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

}
