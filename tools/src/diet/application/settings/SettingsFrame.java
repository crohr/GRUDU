/**
 *
 */
package diet.application.settings;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import diet.application.ApplicationConfiguration;
import diet.util.gui.DietGuiTool;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * @author david
 *
 */
public class SettingsFrame extends JFrame implements DietGuiTool{
	//implements LocaleListener {

	// -----------------------------------------------

//	public static final String FRAMETITLE="frameTitle";
//	public static final String FRAMEPRESENTATIONTEXT = "framePresentationText";
//	public static final String SETTINGSFRAMEPRESENTATIONJPANELNODETEXT = "SettingsFramePresentationJPanelNodeText";
//	public static final String SETTINGSFRAMEPRESENTATIONJPANELTITLE = "SettingsFramePresentationJPanelTitle";
//	public static final String SETTINGSFRAMEPRESENTATIONJPANELTEXT = "SettingsFramePresentationJPanelText";

	/* (non-Javadoc)
	 * @see diet.i18n.LocaleListener#initializeI18nHashMap()
	 */
//	public void initializeI18nHashMap() {
//		i18nHashMap.put(FRAMETITLE, "DIET_DashBoard Settings");
//		i18nHashMap.put(FRAMEPRESENTATIONTEXT, "Here are the settings of the DIET_DashBoard : ");
//		i18nHashMap.put(SETTINGSFRAMEPRESENTATIONJPANELNODETEXT, "DIET_DashBoard");
//		i18nHashMap.put(SETTINGSFRAMEPRESENTATIONJPANELTITLE, "DIET_DashBoard Presentation");
//		i18nHashMap.put(SETTINGSFRAMEPRESENTATIONJPANELTEXT, "This frame will present the settings of the DIET_DashBoard that you can modify");
//	}

//	/* (non-Javadoc)
//	 * @see diet.i18n.LocaleListener#refreshView()
//	 */
//	public void refreshView() {
//		refreshI18nHashMap();
//		refreshComponents();
//	}

//	private void refreshI18nHashMap(){
//		Iterator<String> iter = i18nHashMap.keySet().iterator();
//		while(iter.hasNext()){
//			String key = iter.next();
//			String value = Localizer.getTranslationFor(this.getClass(), key);
//			if(value != null)	i18nHashMap.put(key, value);
//		}
//	}

//	private void refreshComponents(){
//		this.setTitle(i18nHashMap.get(FRAMETITLE));
//		jLabel.setText(i18nHashMap.get(FRAMEPRESENTATIONTEXT));
//		presentationJPanel.myPresentationJLabel.setText("<html>" +i18nHashMap.get(SETTINGSFRAMEPRESENTATIONJPANELTEXT)+"</html>");
//	}

	//	 -----------------------------------------------

	/* (non-Javadoc)
	 * @see diet.util.gui.DietGuiTool#go()
	 */
	public void go() {
		this.setVisible(true);
		this.pack();
		
	}

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JButton jButton = null;

	private JButton jButton1 = null;

	private JPanel jPanel5 = null;

	private JLabel jLabel = null;

	private CardLayout carder = null;

	private JSplitPane jSplitPane = null;

	private JTree jTree = null;

	private JPanel jPanel2 = null;

	private DefaultMutableTreeNode rootNode = null;

	private HashMap<String, DefaultMutableTreeNode> nodesOfTheJTree = null;

	private HashMap<String, SettingsPanelInterface> jPanelOfTheCarder = null;  //  @jve:decl-index=0:

	private SettingsFramePresentationJPanel presentationJPanel = null;

	private JPanel jPanel3 = null;

	private JPanel jPanel4 = null;

	private JPanel jPanel6 = null;

	private JLabel jLabel1 = null;

	/**
	 * This is the default constructor
	 */
	public SettingsFrame() {
		super();
//		Localizer.addListener(this);
		initialize();
//		initializeI18nHashMap();
	}
	
	

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 600);
		this.setPreferredSize(new Dimension(800,600));
		this.setMinimumSize(new Dimension(800,600));
		this.setContentPane(getJContentPane());
		//this.setTitle(i18nHashMap.get(FRAMETITLE));
		this.initTreeView();
		this.setTitle(ApplicationConfiguration.getApplicationContext() + " Settings");
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
			jContentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			jContentPane.add(getJPanel(), BorderLayout.SOUTH);
			jContentPane.add(getJPanel1(), BorderLayout.NORTH);
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);  // Generated
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.X_AXIS));
			jPanel.add(Box.createHorizontalGlue());
			jPanel.add(getJButton(), null);  // Generated
			jPanel.add(Box.createHorizontalStrut(10));
			jPanel.add(getJButton1(), null);  // Generated
			jPanel.add(Box.createHorizontalGlue());
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
			jPanel1.add(getJPanel5());  // Generated
		}
		return jPanel1;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			try {
				jButton = new JButton("Validate");
				jButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ex){
						Iterator<String> iter = jPanelOfTheCarder.keySet().iterator();
						while(iter.hasNext()){
							jPanelOfTheCarder.get(iter.next()).saveSettings();
						}
						ApplicationConfiguration.getInstance().saveConfiguration();
					}
				});
				dispose();
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
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
			try {
				jButton1 = new JButton("Close");
				jButton1.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						dispose();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jButton1;
	}

	/**
	 * This method initializes jPanel5
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			try {
				jLabel = new JLabel();
				//jLabel.setText(i18nHashMap.get(FRAMEPRESENTATIONTEXT));  // Generated
				jLabel.setText("Here are the settings of the "+ApplicationConfiguration.getApplicationContext()+" : ");
				jPanel5 = new JPanel();
				jPanel5.setLayout(new BoxLayout(jPanel5,BoxLayout.X_AXIS));  // Generated
				jPanel5.add(jLabel, null);  // Generated
				jPanel5.add(Box.createHorizontalGlue());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jPanel5;
	}

	/**
	 * This method initializes jSplitPane
	 *
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			try {
				jSplitPane = new JSplitPane();
				jSplitPane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
				jSplitPane.setLeftComponent(new JScrollPane(getJTree()));  // Generated
				jSplitPane.setRightComponent(getJPanel3());  // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jTree
	 *
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			try {
				presentationJPanel = new SettingsFramePresentationJPanel();
				rootNode = new DefaultMutableTreeNode(presentationJPanel.getSettingsPanelNodeText(),true);
				jTree = new JTree(rootNode);
				jTree.addTreeSelectionListener(new SettingsTreeSelectionListener());
				jTree.setMinimumSize(new Dimension(200,600));

			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jTree;
	}

	/**
	 * This method initializes jPanel2
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			try {
				jPanel2 = new JPanel();
				carder = new CardLayout();
				jPanel2.setLayout(carder);  // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jPanel2;
	}

	private void initTreeView(){
		nodesOfTheJTree = new HashMap<String, DefaultMutableTreeNode>();
		jPanelOfTheCarder = new HashMap<String, SettingsPanelInterface>();

		/* Language Selector */
		//LanguageSelector languageSelectorJPanel = new LanguageSelector();
		//DefaultMutableTreeNode  languageSelectorNode = new DefaultMutableTreeNode(languageSelectorJPanel.getSettingsPanelNodeText());
		//nodesOfTheJTree.put(languageSelectorNode.toString(), languageSelectorNode);
		//rootNode.add(languageSelectorNode);
		//jPanel2.add(languageSelectorNode.toString(),languageSelectorJPanel);
		//jPanelOfTheCarder.put(languageSelectorNode.toString(), languageSelectorJPanel);


		jPanel2.add(rootNode.toString(), presentationJPanel);
		carder.show(jPanel2,rootNode.toString());
		jLabel1.setText(presentationJPanel.getSettingsPanelTitle());
		nodesOfTheJTree.put(rootNode.toString(), rootNode);
		jPanelOfTheCarder.put(rootNode.toString(), presentationJPanel);
		
		GeneralSettingsPanel generalSettingsPanel = new GeneralSettingsPanel();
		DefaultMutableTreeNode generalSettingsPanelNode = new DefaultMutableTreeNode(generalSettingsPanel.getSettingsPanelNodeText());
		nodesOfTheJTree.put(generalSettingsPanelNode.toString(), generalSettingsPanelNode);
		rootNode.add(generalSettingsPanelNode);
		jPanel2.add(generalSettingsPanelNode.toString(),generalSettingsPanel);
		jPanelOfTheCarder.put(generalSettingsPanelNode.toString(), generalSettingsPanel);
		
		ArrayList<SettingsPanelImplementation> specificSettingsPanel = ApplicationConfiguration.getInstance().getSpecificSettingsPanelList();
		for(int i = 0 ; i < specificSettingsPanel.size() ; i ++){
			SettingsPanelImplementation aPanel = specificSettingsPanel.get(i);
			DefaultMutableTreeNode specificSettingsPanelNode = new DefaultMutableTreeNode(aPanel.getSettingsPanelNodeText());
			nodesOfTheJTree.put(specificSettingsPanelNode.toString(), specificSettingsPanelNode);
			rootNode.add(specificSettingsPanelNode);
			jPanel2.add(specificSettingsPanelNode.toString(),aPanel);
			jPanelOfTheCarder.put(specificSettingsPanelNode.toString(),aPanel);
			
		}
		
		jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		TreePath path = new TreePath(((DefaultTreeModel)jTree.getModel()).getPathToRoot(generalSettingsPanelNode));
		jTree.makeVisible(path);
	}

	public class SettingsTreeSelectionListener implements TreeSelectionListener{

		/* (non-Javadoc)
		 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
		 */
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode aNode = (DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();
			String value = aNode.toString();
			carder.show(jPanel2, value);
			SettingsPanelInterface tempPanel = jPanelOfTheCarder.get(value);
			String tempPanelString = tempPanel.getSettingsPanelTitle();
			jLabel1.setText(tempPanelString);
		}

	}

	public class SettingsFramePresentationJPanel extends JPanel implements SettingsPanelInterface{

		/* (non-Javadoc)
		 * @see diet.util.gui.SettingsPanelInterface#saveSettings()
		 */
		public void saveSettings() {
			// TODO Auto-generated method stub
			//there is nothing to do here! Of course ! 
		}

		/**
		 * Generated Serial version ID
		 * 
		 * TODO should be regenerated when the class changes
		 */
		private static final long serialVersionUID = -6086617324361077531L;

		/* (non-Javadoc)
		 * @see diet.SettingsPanelInterface#getSettingsPanelNodeText()
		 */
		public String getSettingsPanelNodeText() {
			// TODO Auto-generated method stub
			//return i18nHashMap.get(SETTINGSFRAMEPRESENTATIONJPANELNODETEXT);
			return ApplicationConfiguration.getApplicationContext();
			}

		/* (non-Javadoc)
		 * @see diet.SettingsPanelInterface#getSettingsPanelTitle()
		 */
		public String getSettingsPanelTitle() {
			// TODO Auto-generated method stub
			//return i18nHashMap.get(SETTINGSFRAMEPRESENTATIONJPANELTITLE);
			return ApplicationConfiguration.getApplicationContext()+" Presentation";
		}

		private JLabel myPresentationJLabel = null;

		public SettingsFramePresentationJPanel(){
			super();
			initialize();
		}

		private JLabel getMyPresentationJLabel(){
			if(myPresentationJLabel == null){
				myPresentationJLabel = new JLabel();
				//myPresentationJLabel.setText("<html>" +i18nHashMap.get(SETTINGSFRAMEPRESENTATIONJPANELTEXT)+"</html>");
				myPresentationJLabel.setText("<html>" +"This frame will present the settings of the "+ApplicationConfiguration.getApplicationContext()+"</html>");

			}
			return myPresentationJLabel;
		}

		private void initialize(){
			this.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
			this.add(getMyPresentationJLabel());
		}



	}

	/**
	 * This method initializes jPanel3
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			try {
				jPanel3 = new JPanel();
				jPanel3.setLayout(new BorderLayout());  // Generated

				jPanel3.add(getJPanel2(),BorderLayout.CENTER);
				jPanel3.add(getJPanel4(), BorderLayout.NORTH);  // Generated
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
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
			try {
				jPanel4 = new JPanel();
				jPanel4.setLayout(new BoxLayout(jPanel4,BoxLayout.Y_AXIS));  // Generated
				jPanel4.add(getJPanel6(), null);  // Generated
				jPanel4.add(new JSeparator(JSeparator.HORIZONTAL));
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jPanel4;
	}

	/**
	 * This method initializes jPanel6
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			try {
				jLabel1 = new JLabel();
				jLabel1.setText("");  // Generated
				jLabel1.setFont(new Font("Dialog",Font.ITALIC,14));
				jPanel6 = new JPanel();
				jPanel6.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
				jPanel6.setLayout(new BoxLayout(jPanel6,BoxLayout.X_AXIS));  // Generated
				jPanel6.add(Box.createHorizontalStrut(10));
				jPanel6.add(jLabel1, null);  // Generated
				jPanel6.add(Box.createHorizontalGlue());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jPanel6;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
