/**
 * 
 */
package ganglia.views;

import ganglia.controller.history.GangliaHistoryController;
import ganglia.models.history.HistoryConfigModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * @author david
 *
 */
public class GangliaHistoryConfigurationView extends JPanel{

	private JPanel optionsJPanel = null;
	private JPanel buttonJPanel = null;
	private JPanel refreshingPeriodJPanel = null;
	private JTextField refreshingPeriodTextField = null;
	private JTextField rangeTextField = null;
	private JPanel rangeJPanel = null;
	private JButton startButton = null;
	private GangliaHistoryView parentView = null;;
	private static final long serialVersionUID = 1L;
	private GangliaHistoryController controller = null;
	private JPanel onlineJPanel = null;
	private JPanel offlineJPanel = null;
	private JTabbedPane jTabbedPane = null;
	private JPanel offlineFileJPanel = null;
	private JPanel offlineGenerationJPanel = null;
	private JTextField offlineFileJTextField = null;
	private JTextField javaPathTextField = null;
	private JPanel javaPathJPanel = null;
	private boolean isAlreadyLaunched = false;
	
	/**
	 * This is the default constructor
	 */
	public GangliaHistoryConfigurationView(GangliaHistoryController myController, GangliaHistoryView myParentView) {
		super();
		isAlreadyLaunched = myController.isAlreadyLaunched();
		parentView = myParentView;
		this.controller = myController;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.add(getJTabbedPane());
	}
	
	private JPanel getOnlineJPanel(){
		if(onlineJPanel == null){
			onlineJPanel = new JPanel();
			onlineJPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			onlineJPanel.setLayout(new BorderLayout());
			onlineJPanel.add(getOptionsJPanel(),BorderLayout.NORTH);
			onlineJPanel.add(Box.createVerticalGlue(),BorderLayout.CENTER);
			onlineJPanel.add(getButtonJPanel(),BorderLayout.SOUTH);
		}
		return onlineJPanel;
	}
	
	private JPanel getOffLineJPanel(){
		if(offlineJPanel == null){
			offlineJPanel = new JPanel();
			offlineJPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			offlineJPanel.setLayout(new BorderLayout());
			offlineJPanel.add(getFileSelectionJPanel(),BorderLayout.NORTH);
			offlineJPanel.add(Box.createVerticalGlue(),BorderLayout.CENTER);
			offlineJPanel.add(getOfflineButtonJPanel(), BorderLayout.SOUTH);
		}
		return offlineJPanel;
	}
	
	private JPanel getFileSelectionJPanel(){
		if(offlineFileJPanel == null){
			offlineFileJPanel = new JPanel();
			offlineFileJPanel.setLayout(new BoxLayout(offlineFileJPanel,BoxLayout.X_AXIS));
			offlineFileJPanel.add(new JLabel("File of Ganglia History"));
			offlineFileJPanel.add(Box.createHorizontalStrut(10));
			offlineFileJTextField = new JTextField(5);
			offlineFileJTextField.setEditable(false);
			offlineFileJPanel.add(offlineFileJTextField);
			JButton offLineFileSelectionJButton = new JButton("...");
			offLineFileSelectionJButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileHidingEnabled(false);
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int retour = fileChooser.showOpenDialog(null);

					if(retour == JFileChooser.APPROVE_OPTION) {
						String fichier = fileChooser.getSelectedFile().getAbsolutePath();
						offlineFileJTextField.setText(fichier);
						controller.getModel().setGangliaHistoryFile(fichier);
					}
				}
			});
			offlineFileJPanel.add(offLineFileSelectionJButton);
		}
		return offlineFileJPanel;
	}
	
	private JPanel getOfflineButtonJPanel(){
		if(offlineGenerationJPanel == null){
			offlineGenerationJPanel = new JPanel();
			offlineGenerationJPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
			JButton generationButton = new JButton("Start");
			generationButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					controller.getModel().setConnexionMode(HistoryConfigModel.OFFLINE);
					parentView.updateTheView();
				}
			});
			offlineGenerationJPanel.add(generationButton);
		}
		return offlineGenerationJPanel;
	}
	
	private JTabbedPane getJTabbedPane(){
		if(jTabbedPane == null){
			jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
			jTabbedPane.addTab("Online", getOnlineJPanel());
			jTabbedPane.addTab("Offline", getOffLineJPanel());
		}
		return jTabbedPane;
	}
	
	private JPanel getOptionsJPanel(){
		if(optionsJPanel == null){
			optionsJPanel = new JPanel();
			optionsJPanel.setBorder(BorderFactory.createEmptyBorder(10,10, 10, 10));
			optionsJPanel.setLayout(new BoxLayout(optionsJPanel,BoxLayout.PAGE_AXIS));
			if(!isAlreadyLaunched) optionsJPanel.add(getRefreshingPeriodJPanel());
			else getRefreshingPeriodJPanel();
			optionsJPanel.add(getRangeJPanel());			
			if(!isAlreadyLaunched) optionsJPanel.add(getJavaPathJPanel());
			else getJavaPathJPanel();
			optionsJPanel.add(Box.createVerticalStrut(300));
			optionsJPanel.add(new JPanel());
		}
		return optionsJPanel;
	}
	
	private JPanel getButtonJPanel(){
		if(buttonJPanel == null){
			buttonJPanel = new JPanel();
			buttonJPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
			startButton = new JButton("Start");
			startButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					controller.validateConfiguration(refreshingPeriodTextField.getText(), rangeTextField.getText());
					controller.getModel().setConnexionMode(HistoryConfigModel.ONLINE);
					controller.getModel().setJavaPath(javaPathTextField.getText());
					parentView.updateTheView();
				}
			});
			buttonJPanel.add(startButton);
		}
		return buttonJPanel;
	}
	
	private JPanel getRefreshingPeriodJPanel(){
		if(refreshingPeriodJPanel == null){
			refreshingPeriodJPanel = new JPanel();
			refreshingPeriodJPanel.setLayout(new GridLayout(1,2,2,2));
			JLabel refreshingPeriodJLabel = new JLabel("Period of data refeshing : ");
			refreshingPeriodJPanel.add(refreshingPeriodJLabel);
			refreshingPeriodTextField = new JTextField(10);
			refreshingPeriodTextField.setText("hh:mm:ss");
			refreshingPeriodTextField.setToolTipText("<html>This period corresponds to the amount of time between two ganglia information requests.<br>" +
					"Must by a period of time expressed as follows:<br>" +
					"<b>hh:mm:ss</b><html>");
			refreshingPeriodJPanel.add(refreshingPeriodTextField);
		}
		return refreshingPeriodJPanel;
	}
	
	private JPanel getRangeJPanel(){
		if(rangeJPanel == null){
			rangeJPanel = new JPanel();
			rangeJPanel.setLayout(new GridLayout(1,2,2,2));
			JLabel rangeJLabel = new JLabel("Range of the Chart : ");
			rangeJPanel.add(rangeJLabel);
			rangeTextField = new JTextField(10);
			rangeTextField.setText("hh:mm:ss");
			rangeTextField.setToolTipText("<html>This value corresponds to the maximum period displayed by the history chart.<br>" +
					"Must by a period of time expressed as follows:<br>" +
					"<b>hh:mm:ss</b><html>");
			rangeJPanel.add(rangeTextField);
		}
		return rangeJPanel;
	}
	
	private JPanel getJavaPathJPanel(){
		if(javaPathJPanel == null){
			javaPathJPanel = new JPanel();
			javaPathJPanel.setLayout(new GridLayout(1,2,2,2));
			JLabel javaPathJLabel = new JLabel("Java home (main node of the reservation) : ");
			javaPathJPanel.add(javaPathJLabel);
			javaPathTextField = new JTextField(10);
			javaPathTextField.setToolTipText("Path to the java home on the nodes reserved");
			javaPathJPanel.add(javaPathTextField);
		}
		return javaPathJPanel;
	}

}
