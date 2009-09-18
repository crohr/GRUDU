/**
 * 
 */
package ganglia.views;

import ganglia.models.Host;
import ganglia.models.HostXML;
import ganglia.models.Metric;
import ganglia.views.charts.CPUChart;
import ganglia.views.charts.LoadChart;
import ganglia.views.charts.MeterChart;
import ganglia.views.charts.NetworkChart;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

/**
 * @author david
 *
 */
public class HostView  extends JTaskPane implements HostXML {

	private JSplitPane splitPane = null;
	private JPanel taskPanel = null;
	private JPanel presentationPanel = null;
	private JPanel groupTitlePanel = null;
	private JPanel groupElementTitlePanel = null;
	private JPanel cardPanel = null;
	private CardLayout carder = null;
	private JLabel groupJLabel = null;
	private JLabel groupElementJLabel = null;
	private Host myHost = null;
	
	private JPanel mainInformationJPanel = null;
	
	private JPanel networkJPanel = null;
	
	private JPanel cpuJPanel = null;
	
	private JPanel loadJPanel = null;
	
	private JPanel diskJPanel = null;
	
	private JPanel memJPanel = null;
	
	private JPanel swapJPanel = null;
	
	public HostView(Host aHost) {
		myHost = aHost;
		initialize();
	}

	private void initialize(){
		add(getJSplitPane());
	}
	
	private JSplitPane getJSplitPane(){
		if(splitPane == null){
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true);
			splitPane.setDividerLocation(0.3);
			splitPane.setRightComponent(getPresentationPanel());
			splitPane.setLeftComponent(getTaskPane());
		}
		return splitPane;
	}
	
	private JPanel getPresentationPanel(){
		if(presentationPanel == null){
			presentationPanel = new JPanel();
			presentationPanel.setLayout(new BoxLayout(presentationPanel,BoxLayout.Y_AXIS));

			presentationPanel.add(getGroupTitlePanel());
			presentationPanel.add(Box.createVerticalStrut(10));
			presentationPanel.add(getGroupElementTitlePanel());
			presentationPanel.add(Box.createVerticalStrut(10));
			presentationPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
			presentationPanel.add(getCardPanel());
		}
		return presentationPanel;
	}
	
	private JPanel getGroupTitlePanel(){
		if(groupTitlePanel == null){
			groupTitlePanel = new JPanel();
			groupTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			groupJLabel = new JLabel("Group");
			groupJLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			groupJLabel.setFont(new Font("Dialog", Font.BOLD, 20));
			groupTitlePanel.add(groupJLabel);
		}
		return groupTitlePanel;
	}
	
	private JPanel getGroupElementTitlePanel(){
		if(groupElementTitlePanel == null){
			groupElementTitlePanel = new JPanel();
			groupElementTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			groupElementJLabel = new JLabel("<html><u>Group Element</u></html>");
			groupElementJLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			groupElementJLabel.setHorizontalAlignment(JLabel.LEFT);
			groupElementJLabel.setHorizontalTextPosition(JLabel.LEFT);
			groupElementJLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
			groupElementTitlePanel.add(Box.createHorizontalStrut(20));
			groupElementTitlePanel.add(groupElementJLabel);
		}
		return groupElementTitlePanel;
	}
	
	private void setGroupTitle(String title){
		groupJLabel.setText(title);
	}
	
	private void setGroupElementTitle(String title){
		groupElementJLabel.setText("<html><u>\t\t " + title + "</u></html>");
	}
	
	private JPanel getCardPanel(){
		if(cardPanel == null){
			cardPanel = new JPanel();
			carder = new CardLayout();
			cardPanel.setLayout(carder);
			cardPanel.add("Main information",getMainInformationJPanel());
			cardPanel.add("Network", getNetworkJPanel());
			cardPanel.add("CPU", getCPUJPanel());
			cardPanel.add("Load",getLoadPanel());
			cardPanel.add("Disk",getDiskPanel());
			cardPanel.add("Memory",getMemPanel());
			cardPanel.add("Swap",getSwapPanel());
			
			setGroupTitle("Main");
			setGroupElementTitle("Main information");
			
			carder.show(cardPanel, "Main information");
		}
		return cardPanel;
	}

	private JPanel getTaskPane(){

		if(taskPanel == null){
			taskPanel = new JPanel();
			JTaskPane taskPane = new JTaskPane();

			// "Main" GROUP
			JTaskPaneGroup systemGroup = new JTaskPaneGroup();
			systemGroup.setTitle("Main");
			systemGroup.setToolTipText("Main information from Ganglia for that host");
			systemGroup.setSpecial(true);
			systemGroup.setCollapsable(false);

			systemGroup.add(makeAction("Main information", "Main information from Ganglia for that host","Main"));

			taskPane.add(systemGroup);

			// "NCL" GROUP
			JTaskPaneGroup networkGroup = new JTaskPaneGroup();
			networkGroup.setTitle("Network");
			networkGroup.setToolTipText("Information about the networking");
			networkGroup.setExpanded(false);
			networkGroup.setScrollOnExpand(true);
			networkGroup.add(makeAction("Network", "Networking information","Network"));
			
			taskPane.add(networkGroup);
			
			JTaskPaneGroup cpuAndLoadGroup = new JTaskPaneGroup();
			cpuAndLoadGroup.setTitle("CPU & Load");
			cpuAndLoadGroup.setToolTipText("Information about the CPu and the Load");
			
			cpuAndLoadGroup.add(makeAction("CPU", "CPU(s) information","CPU & Load"));
			cpuAndLoadGroup.add(makeAction("Load", "Load information","CPU & Load"));
			cpuAndLoadGroup.setExpanded(false);
			cpuAndLoadGroup.setScrollOnExpand(true);

			taskPane.add(cpuAndLoadGroup);

			// "DMS" GROUP
			JTaskPaneGroup diskGroup = new JTaskPaneGroup();
			diskGroup.setTitle("Disk");
			diskGroup.setToolTipText("Information about the disk");
			diskGroup.add(makeAction("Disk", "Disk information","Disk"));
			diskGroup.setExpanded(false);
			diskGroup.setScrollOnExpand(true);			
			taskPane.add(diskGroup);
			
			JTaskPaneGroup memAndSwapGroup = new JTaskPaneGroup();
			memAndSwapGroup.setTitle("Mem & Swap");
			memAndSwapGroup.setToolTipText("Information about the memory and the swap");
			memAndSwapGroup.add(makeAction("Memory", "Memory information","Mem & Swap"));
			memAndSwapGroup.add(makeAction("Swap", "Swap information","Mem & Swap"));
			memAndSwapGroup.setExpanded(false);
			memAndSwapGroup.setScrollOnExpand(true);

			taskPane.add(memAndSwapGroup);
			
			JScrollPane scroll = new JScrollPane(taskPane);
			scroll.setBorder(null);

			taskPanel.setLayout(new BorderLayout());
			taskPanel.add("Center", scroll);

			setBorder(null);
		}
		return taskPanel;
	}

	Action makeAction(String title, String tooltiptext, String group) {
		final String aTitle = title;
		final String aGroup  = group;
		Action action = new AbstractAction(title) {
			public void actionPerformed(ActionEvent e) {
				setGroupTitle(aGroup);
				setGroupElementTitle(aTitle);
				
				carder.show(cardPanel, aTitle);
			}
		};
		action.putValue(Action.SHORT_DESCRIPTION, tooltiptext);
		return action;
	}
	
	private JPanel getMainInformationJPanel(){
		if(mainInformationJPanel == null){
			mainInformationJPanel = new JPanel();
			mainInformationJPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			mainInformationJPanel.setLayout(new BoxLayout(mainInformationJPanel,BoxLayout.Y_AXIS));
			mainInformationJPanel.add(Box.createVerticalStrut(20));
			//System.out.println(myHost);
			HashMap<String, Metric> metrics = myHost.getMetrics();
			String osName = metrics.get(HOST_OS_NAME).getValue();
			String osRelease = metrics.get(HOST_OS_RELEASE).getValue();
			JLabel osLabel = new JLabel("OS Name : " + osName + " " + osRelease);
			mainInformationJPanel.add(osLabel);
			String machineType = metrics.get(HOST_MACHINE_TYPE).getValue();
			JLabel machineLabel = new JLabel("Machine type : " + machineType);
			mainInformationJPanel.add(machineLabel);
			mainInformationJPanel.add(Box.createVerticalStrut(20));
			String cpuNumber = metrics.get(HOST_CPU_NUM).getValue();
			String cpuSpeed = metrics.get(HOST_CPU_SPEED).getValue();
			String cpuUnits = metrics.get(HOST_CPU_SPEED).getUnits();
			JLabel cpuLabel = new JLabel("There is : " + cpuNumber + " CPU(s) @ " + cpuSpeed + " " + cpuUnits);
			mainInformationJPanel.add(cpuLabel);
			mainInformationJPanel.add(Box.createVerticalStrut(20));
			String procTotal = metrics.get(HOST_PROC_TOTAL).getValue();
			String procRunning = metrics.get(HOST_PROC_RUN).getValue();
			JLabel procLabel = new JLabel("There is : " + procTotal + " processes with "  + procRunning + " running.");
			mainInformationJPanel.add(procLabel);
			String boottime = metrics.get(HOST_BOOTTIME).getValue();
			JLabel boottimeJLabel = new JLabel("The host has booted " + (new Date(Long.parseLong(boottime)*1000)).toString() + " for the last time.");
			mainInformationJPanel.add(Box.createVerticalStrut(20));
			mainInformationJPanel.add(boottimeJLabel);
		}
		return mainInformationJPanel;
	}
	
	private JPanel getNetworkJPanel(){
		if(networkJPanel == null){
			HashMap<String, Metric> metrics = myHost.getMetrics();
			networkJPanel = new JPanel();
			networkJPanel.setLayout(new BoxLayout(networkJPanel,BoxLayout.Y_AXIS));
			double[] values = new double[4];
			values[0] = Double.parseDouble(metrics.get(HOST_BYTES_IN).getValue());
			values[1] = Double.parseDouble(metrics.get(HOST_BYTES_OUT).getValue());
			values[2] = Double.parseDouble(metrics.get(HOST_PKTS_IN).getValue());
			values[3] = Double.parseDouble(metrics.get(HOST_PKTS_OUT).getValue());
			networkJPanel.add(new NetworkChart(values));
		}
		return networkJPanel;
	}
	
	private JPanel getCPUJPanel(){
		if(cpuJPanel == null){
			HashMap<String, Metric> metrics = myHost.getMetrics();
			cpuJPanel = new JPanel();
			cpuJPanel.setLayout(new BoxLayout(cpuJPanel,BoxLayout.Y_AXIS));
			double[] values = new double[3];
			values[0] = Double.parseDouble(metrics.get(HOST_CPU_IDLE).getValue());
			values[1] = Double.parseDouble(metrics.get(HOST_CPU_SYSTEM).getValue());
			values[2] = Double.parseDouble(metrics.get(HOST_CPU_USER).getValue());
			cpuJPanel.add(new CPUChart(values));
		}
		return cpuJPanel;
	}
	
	private JPanel getLoadPanel(){
		if(loadJPanel == null){
			HashMap<String, Metric> metrics = myHost.getMetrics();
			loadJPanel = new JPanel();
			loadJPanel.setLayout(new BoxLayout(loadJPanel,BoxLayout.Y_AXIS));
			double[] values = new double[3];
			values[0] = Double.parseDouble(metrics.get(HOST_LOAD_ONE).getValue());
			values[1] = Double.parseDouble(metrics.get(HOST_LOAD_FIVE).getValue());
			values[2] = Double.parseDouble(metrics.get(HOST_LOAD_FIFTEEN).getValue());
			loadJPanel.add(new LoadChart(values));
		}
		return loadJPanel;
	}
	
	private JPanel getDiskPanel(){
		if(diskJPanel == null){
			HashMap<String, Metric> metrics = myHost.getMetrics();
			diskJPanel = new JPanel();
			diskJPanel.setLayout(new BoxLayout(diskJPanel,BoxLayout.Y_AXIS));
			double[] values = new double[3];
			values[0] = Double.parseDouble(metrics.get(HOST_DISK_TOTAL).getValue());
			values[1] = Double.parseDouble(metrics.get(HOST_DISK_FREE).getValue());
			String units = metrics.get(HOST_DISK_FREE).getUnits();
			if(values[0] != 0.0){
				diskJPanel.add(new MeterChart(values[0],values[1],units,"Disk Usage"));
			}
			else{
				System.err.println("Disk space problem on " + myHost.getName()+ "\n" + "Unable to display data");
				diskJPanel.add(new JLabel("No data to display !! \n There is a problem with the disk space on that node."));
			}
		}
		return diskJPanel;
	}
	
	private JPanel getMemPanel(){
		if(memJPanel == null){
			HashMap<String, Metric> metrics = myHost.getMetrics();
			memJPanel = new JPanel();
			memJPanel.setLayout(new BoxLayout(memJPanel,BoxLayout.Y_AXIS));
			double[] values = new double[2];
			values[0] = Double.parseDouble(metrics.get(HOST_MEM_TOTAL).getValue());
			values[1] = Double.parseDouble(metrics.get(HOST_MEM_FREE).getValue());
			String units = metrics.get(HOST_MEM_FREE).getUnits();
			if(values[0] != 0.0){
				memJPanel.add(new MeterChart(values[0],values[1],units,"Memory Usage"));
			}
			else{
				System.err.println("Memory space problem on " + myHost.getName()+ "\n" + "Unable to display data");
				memJPanel.add(new JLabel("No data to display !! \n There is a problem with the memory space on that node."));
			}
		}
		return memJPanel;
	}
	
	private JPanel getSwapPanel(){
		if(swapJPanel == null){
			//System.out.println(myHost.getName());
			HashMap<String, Metric> metrics = myHost.getMetrics();
			swapJPanel = new JPanel();
			swapJPanel.setLayout(new BoxLayout(swapJPanel,BoxLayout.Y_AXIS));
			double[] values = new double[2];
			values[0] = Double.parseDouble(metrics.get(HOST_SWAP_TOTAL).getValue());
			values[1] = Double.parseDouble(metrics.get(HOST_SWAP_FREE).getValue());
			String units = metrics.get(HOST_SWAP_FREE).getUnits();
			if(values[0] != 0.0){
				swapJPanel.add(new MeterChart(values[0],values[1],units,"Swap Usage"));
			}
			else{
				System.err.println("Swap space problem on " + myHost.getName()+ "\n" + "Unable to display data");
				swapJPanel.add(new JLabel("No data to display !! \n There is a problem with the swap space on that node."));
			}
			
		}
		return swapJPanel;
	}

}