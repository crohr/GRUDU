/**
 * 
 */
package ganglia.views;

import ganglia.controller.history.GangliaHistoryController;
import ganglia.misc.Observer;
import ganglia.views.charts.history.BytesHistoryChart;
import ganglia.views.charts.history.CPUHistoryChart;
import ganglia.views.charts.history.DiskHistoryChart;
import ganglia.views.charts.history.LoadHistoryChart;
import ganglia.views.charts.history.MemoryHistoryChart;
import ganglia.views.charts.history.PktsHistoryChart;
import ganglia.views.charts.history.ProcessesHistoryChart;
import ganglia.views.charts.history.SwapHistoryChart;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

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
public class HostHistoryView extends JPanel{
	private JSplitPane splitPane = null;
	private JPanel taskPanel = null;
	private JPanel presentationPanel = null;
	private JPanel groupTitlePanel = null;
	private JPanel groupElementTitlePanel = null;
	private JPanel cardPanel = null;
	private CardLayout carder = null;
	private JLabel groupJLabel = null;
	private JLabel groupElementJLabel = null;
	private JPanel mainInformationJPanel = null;
	private GangliaHistoryController controller = null;
	private JPanel bytesJPanel = null;
	private JPanel pktsJPanel = null;
	private JPanel cpuJPanel = null;
	private JPanel diskJPanel = null;
	private JPanel swapJPanel = null;
	private JPanel memJPanel = null;
	private JPanel procJPanel = null;
	private JPanel loadJPanel = null;
	private String hostName = null;
	
	public HostHistoryView(String hostName,GangliaHistoryController aController) {
		controller = aController;
		this.hostName =hostName; 
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
			cardPanel.add("Bytes/sec",getBytesJPanel());
			cardPanel.add("Packets/sec",getPktsJPanel());
			cardPanel.add("CPU Info",getCpuJPanel());
			cardPanel.add("Proc Info",getProcJPanel());
			cardPanel.add("Load Info",getLoadJPanel());
			cardPanel.add("Disk Info",getDiskJPanel());
			cardPanel.add("Memory Info",getMemJPanel());
			cardPanel.add("Swap Info",getSwapJPanel());
			setGroupTitle("Main");
			setGroupElementTitle("Main information");
			carder.show(cardPanel, "Main information");
		}
		return cardPanel;
	}
	
	private JPanel getMainInformationJPanel(){
		if(mainInformationJPanel == null){
			mainInformationJPanel = new JPanel();
			mainInformationJPanel.setLayout(new BorderLayout(10,10));
			mainInformationJPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			mainInformationJPanel.add(new JLabel("Main page of the Ganglia History"),BorderLayout.CENTER);
		}
		return mainInformationJPanel;
	}
	
	private JPanel getBytesJPanel(){
		if(bytesJPanel == null){
			bytesJPanel = new BytesHistoryChart(hostName,controller.getModel().getRangeForTheGraph());
			controller.addObserver((Observer)bytesJPanel);
		}
		return bytesJPanel;
	}
	
	private JPanel getPktsJPanel(){
		if(pktsJPanel == null){
			pktsJPanel = new PktsHistoryChart(hostName,controller.getModel().getRangeForTheGraph());
			controller.addObserver((Observer)pktsJPanel);
		}
		return pktsJPanel;
	}
	
	private JPanel getCpuJPanel(){
		if(cpuJPanel == null){
			cpuJPanel = new CPUHistoryChart(hostName,controller.getModel().getRangeForTheGraph());
			controller.addObserver((Observer)cpuJPanel);
		}
		return cpuJPanel;
	}
	
	private JPanel getDiskJPanel(){
		if(diskJPanel == null){
			diskJPanel = new DiskHistoryChart(hostName,controller.getModel().getRangeForTheGraph());
			controller.addObserver((Observer)diskJPanel);
		}
		return diskJPanel;
	}
	
	private JPanel getSwapJPanel(){
		if(swapJPanel == null){
			swapJPanel = new SwapHistoryChart(hostName,controller.getModel().getRangeForTheGraph());
			controller.addObserver((Observer)swapJPanel);
		}
		return swapJPanel;
	}
	
	private JPanel getMemJPanel(){
		if(memJPanel == null){
			memJPanel = new MemoryHistoryChart(hostName,controller.getModel().getRangeForTheGraph());
			controller.addObserver((Observer)memJPanel);
		}
		return memJPanel;
	}
	
	private JPanel getProcJPanel(){
		if(procJPanel == null){
			procJPanel = new ProcessesHistoryChart(hostName,controller.getModel().getRangeForTheGraph());
			controller.addObserver((Observer)procJPanel);
		}
		return procJPanel;
	}
	
	private JPanel getLoadJPanel(){
		if(loadJPanel == null){
			loadJPanel = new LoadHistoryChart(hostName,controller.getModel().getRangeForTheGraph());
			controller.addObserver((Observer)loadJPanel);
		}
		return loadJPanel;
	}

	private JPanel getTaskPane(){

		if(taskPanel == null){
			taskPanel = new JPanel();
			JTaskPane taskPane = new JTaskPane();

			// main Group
			JTaskPaneGroup mainGroup = new JTaskPaneGroup();
			mainGroup.setTitle("Main");
			mainGroup.setToolTipText("Main information");
			mainGroup.setExpanded(false);
			mainGroup.setSpecial(true);
			mainGroup.add(makeAction("Main information", "Main page","Main"));

			taskPane.add(mainGroup);
			
			// network Group
			JTaskPaneGroup metricsGroup = new JTaskPaneGroup();
			metricsGroup.setTitle("Metrics");
			metricsGroup.setToolTipText("Metrics information");
			metricsGroup.setExpanded(false);

			metricsGroup.add(makeAction("Bytes/sec", "Information about bytes in/out","Metrics"));
			metricsGroup.add(makeAction("Packets/sec", "Information about packets in/out","Metrics"));
			metricsGroup.add(makeAction("CPU Info", "Information about CPU idle, system and user","Metrics"));
			metricsGroup.add(makeAction("Proc Info", "Information about processes","Metrics"));
			metricsGroup.add(makeAction("Load Info", "Information about the Load (one, five & fifteen)","Metrics"));
			metricsGroup.add(makeAction("Disk Info", "Information about the disk","Metrics"));
			metricsGroup.add(makeAction("Memory Info", "Information about the memory","Metrics"));
			metricsGroup.add(makeAction("Swap Info", "Information about the swap","Metrics"));
			taskPane.add(metricsGroup);			
			JScrollPane scroll = new JScrollPane(taskPane);
			scroll.setBorder(null);

			taskPanel.setLayout(new BorderLayout());
			taskPanel.add("Center", scroll);

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
}
