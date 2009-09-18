/**
 * 
 */
package diet.gridr.g5k.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;

import com.l2fprod.common.swing.JButtonBar;
import com.trilead.ssh2.Connection;

import diet.grid.api.GridJob;
import diet.grid.api.util.HistoryUtil;
import diet.gridr.g5k.util.Config;
import diet.gridr.g5k.util.G5kCfg;
import diet.gridr.g5k.util.G5kSite;

/**
 * @author david
 *
 */
public class JobInformationPanel extends JPanel {
	
	private GridJob job = null;

	private Connection connection = null;
	
	private JButtonBar toolbar = null;
	
	private ButtonGroup group = null;
	
	private JPanel cardPanel = null;
	
	private CardLayout cardLayout = null;

	private JPanel oarJobPanel = null;
	
	private HashMap<String, String> properties = null;
	
	public JobInformationPanel(GridJob aJob, Connection aConnection, String siteName){
		job = aJob;
		connection = aConnection;
		properties = new HashMap<String, String>();
		properties.put("user",G5kCfg.get(G5kCfg.USERNAME));
		properties.put("keyFile", G5kCfg.get(G5kCfg.SSHKEYFILE));
		properties.put("passphrase", G5kCfg.getSSHKey());
		properties.put("accesFrontale",G5kCfg.get(G5kCfg.PREFERREDACCESPOINT));
		properties.put("destination", G5kSite.getInternalFrontalForSite(siteName));
		properties.put("connexionCommand", aJob.getJobRSHConnectionCommand());
		properties.put("copyCommand", aJob.getJobRCPCommand());
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
			addButton("<html><center>Job<br> Info</center></html>", "/resources/gridr/g5k/jobIcon.png" ,
					makePanel("Job description",getOARInfoPanel()), toolbar, group);
			//boolean classLoadingSuccess = false;
//			try{
//				Class aClass = Class.forName("ganglia.controller.GangliaAnalyzer");
//				//classLoadingSuccess = true;
//				if(job.getParameterValue(job.KEY_GRID_JOB_STATE).toLowerCase().startsWith("r")
//						&& 
//						job.getParameterValue(job.KEY_GRID_JOB_OWNER).equalsIgnoreCase(Config.getUser())){
//					addButton(gangliaHistoryTitle, "/images/gangliaHistory.png" ,
//							makePanel("Ganglia History",getGangliaHistoryPanel()), toolbar, group);
//				}
//			}
//			catch(Exception e){}
//			if(classLoadingSuccess){
//			}
		}
		return cardPanel;
	}
	
//	private JPanel getGangliaHistoryPanel(){
//		if(gangliaHistoryPanel == null){
//			ArrayList<String> hostsToUse = new ArrayList<String>();
//			String[] hostsOfTheJob = job.getHostsAsArray();
//			for(int i = 0 ; i < hostsOfTheJob.length ; i++){
//				hostsToUse.add(hostsOfTheJob[i]);
//			}
//			long endReservationTime;
//			if(job.getGridJobId().contains("1")){
//				Date startDate = getDateFromOARDate(job.getParameterValue(job.KEY_GRID_JOB_STARTTIME));
//				Long walltime = getDateFromWallTime(job.getParameterValue(job.KEY_GRID_JOB_WALLTIME));
//				endReservationTime = startDate.getTime() + walltime;  
//			}else endReservationTime = Long.parseLong(job.getParameterValue(job.KEY_GRID_JOB_STARTTIME)) + Long.parseLong(job.getParameterValue(job.KEY_GRID_JOB_WALLTIME));
//			controller = new ganglia.controller.history.GangliaHistoryController(Integer.parseInt(job.getParameterValue(job.KEY_GRID_JOB_ID)),hostsToUse,endReservationTime*1000,properties);
//			gangliaHistoryPanel = new ganglia.views.GangliaHistoryView((ganglia.controller.history.GangliaHistoryController)controller);
//		}
//		return gangliaHistoryPanel;
//	}
	
	private JPanel getOARInfoPanel(){
		if(oarJobPanel == null){
			oarJobPanel = new OarJobPanel(job);
		}
		return oarJobPanel;
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
	
	// Should be removed (It is just for supercomputing !!!!! )
	
	/**
     * Method returning the date in the OAR format given in argument in the classical java format
     *
     * @param aFormattedDate an OAR Formatted date
     * @return a Date in the classical java format
     */
    public static Date getDateFromOARDate(String aFormattedDate){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(aFormattedDate);
        }catch(Exception e){
            return new Date();
        }
    }
    
    /**
     * Method returning a duration from the walltime
     *
     * @param aFormattedWallTime a walltime
     * @return a duration corresponding to the walltime
     */
    public static long getDateFromWallTime(String aFormattedWallTime){
        StringTokenizer tokenizer = new StringTokenizer(aFormattedWallTime,":");
        int hours = Integer.parseInt(tokenizer.nextToken());
        int minutes = Integer.parseInt(tokenizer.nextToken());
        int seconds = Integer.parseInt(tokenizer.nextToken());
        return (hours*HistoryUtil.HOUR + minutes*HistoryUtil.MINUTE + seconds*HistoryUtil.SECOND);
    }
	
}
