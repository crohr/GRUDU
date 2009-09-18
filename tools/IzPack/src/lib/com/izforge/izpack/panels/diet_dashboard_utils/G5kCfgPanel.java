/****************************************************************************/
/* This class corresponds to the a G5k configuration panel of a site        */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kCfgPanel.java,v 1.5 2007/11/05 17:57:35 dloureir Exp $
 * $Log: G5kCfgPanel.java,v $
 * Revision 1.5  2007/11/05 17:57:35  dloureir
 * Adding some bug correction (see DIETDashBOard CVS commits)
 *
 * Revision 1.16  2007/11/05 17:26:05  dloureir
 * Bug correction : when saving the variables and adding/removing/importing variables some variables their were some mistakes in the xml files and in the GUI.
 * Bug correction : the output of the mapping tool is now correct : good variables are generated and the output is well formatted
 *
 * Revision 1.15  2007/10/05 13:11:49  dloureir
 * Changing the call to the apporpriate method of G5KCluster (that will be soon deprecated) by a call to the G5KSite appropriate method
 *
 * Revision 1.14  2007/07/18 14:07:47  dloureir
 * The configuration of GRUDU has now only two buttons for the saving of the modified (or not) configuration and the correction of a little bug : the JList of theUserPanel is composed of external acces frontales whereas it was not the case before (it was site's names).
 *
 * Revision 1.13  2007/07/12 14:04:08  dloureir
 * Some clean up in the import and the commented code. There si also some javadoc for the new code elements.
 *
 * Revision 1.12  2007/06/26 15:26:34  dloureir
 * Changing the border fo the PropertyPanel
 *
 * Revision 1.11  2007/06/26 15:06:13  dloureir
 * The ways the environment variables are stored has been modified to be compliant with GoDIET (the last version). You can now supply all the variables you want for each sites.
 *
 * Revision 1.10  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package com.izforge.izpack.panels.diet_dashboard_utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.izforge.izpack.panels.g5k_utils.G5kCfg;
import com.izforge.izpack.panels.g5k_utils.G5kConstants;
import com.izforge.izpack.panels.g5k_utils.G5kSite;
import com.izforge.izpack.panels.g5k_utils.SiteCfg;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

/**
 * This class corresponds to the a G5k configuration panel of a site
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kCfgPanel extends JPanel implements ActionListener {
    /**
     * serialVersionUID defines the version for the serialisation of
     * this object
	*/
	private static final long serialVersionUID = -4744922316608519183L;
    /**
     * name of the site
     */
    private String myName;
    /**
     * Index of the panel
     */
    private int myIndex;
    /**
     * Site configuration associated
     */
    protected SiteCfg myCfg;
//    /**
//     * Button that should be pressed to apply the changes
//     */
//    private JButton    applyBtn;
    /**
     * ComboBox for the others grid5000 sites used to import values from
     */
    private JComboBox  otherSites;
    /**
     * Button that should be used to import values from an other site
     */
    private JButton    importBtn;
        /**
     * Button that should be used to save values for that site
     */
    private JButton    saveBtn; 
    /**
     * List of properties for that site
     */
    private ListOfPropertyJPanel listOfProperty = null;
    /**
     * HashMap of property name and PropertyJPanel 
     */
    private HashMap<String, PropertyJPanel> propertiesHashMap = new HashMap<String, PropertyJPanel>();
    
    /**
     * Default constructor for the G5kCfgPanel
     *
     * @param parent parent frame
     * @param site name of the site
     * @param index index of the site
     */
    public G5kCfgPanel(String site, int index) {
        this.myName = site;
        this.myIndex = index;
        this.myCfg = null;

        initComponents();
    }

    /**
     * Method initializing the components of this Panel
     *
     */
    protected void initComponents() {
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        
//        this.applyBtn = new JButton("Apply");
//        this.applyBtn.setToolTipText("Apply the changes realized in this panel");
//        this.applyBtn.addActionListener(this);
        this.otherSites = new JComboBox();
        this.importBtn = new JButton("import");
        this.importBtn.setToolTipText("Import the settings of an other site");
        this.saveBtn = new JButton("Save");
        this.saveBtn.setToolTipText("Validate these values");
        this.saveBtn.addActionListener(this);
        this.importBtn.addActionListener(this);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        listOfProperty = new ListOfPropertyJPanel();
        add(listOfProperty,c);
        c.gridwidth = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        add(this.otherSites, c);

        c.weightx = 0.25;
        c.gridx = 1;
        c.gridy = 1;
        add(this.importBtn, c);
        
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridx =0;
        c.gridy = 2;
        add(this.saveBtn, c);    

//        JPanel panel = new JPanel();
//        panel.setLayout(new FlowLayout());
//        panel.add(this.applyBtn);
//        c.gridx = 0;
//        c.gridy = 2;
//        c.gridwidth = 2;
//        add(panel, c);

        fillValues();
    }

    /**
     * Method filling the values of the configuration for this site
     *
     */
    protected void fillValues() {
        for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
            if (ix != this.myIndex) {
                this.otherSites.addItem(G5kConstants.sites.get(ix).getName());
            }
        }
    }

    /**
     * Method applying the changes operated in this panel
     *
     */
    public void apply() {
        this.myCfg = G5kCfg.getSite(this.myIndex);        
        Set<String> keySet = propertiesHashMap.keySet();
        Iterator<String> iter = keySet.iterator();
        while(iter.hasNext()){
        	String key = iter.next();
        	String value = (propertiesHashMap.get(key)).getPropertyValue();
        	this.myCfg.set(key, value);
        }
    }

    /**
     * Method used to manage the event for this panel
     *
     * @param event an event
     */
    public void actionPerformed(ActionEvent event) {
//        if (event.getSource() == this.applyBtn) {
//            apply();
//        }
        if (event.getSource() == this.importBtn) {
            String selectedSite = (String)this.otherSites.getSelectedItem();
            if (selectedSite == null) {
                JOptionPane.showMessageDialog(null,
                        "ERROR No site selected or the selected site was not found",
                        "ERROR", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int index = G5kSite.getIndexForSite(selectedSite);
            if (index == -1) {
                JOptionPane.showMessageDialog(null,
                        "ERROR No site selected or the selected site was not found",
                        "ERROR", JOptionPane.INFORMATION_MESSAGE);
            }
            SiteCfg cfg  = G5kCfg.getSite(index);
            if (cfg != null) {
            	listOfProperty.removeAllProperties();
            	Iterator<String> iter = cfg.getKeySet().iterator();
            	while(iter.hasNext()){
            		String key = iter.next();
            		String value = cfg.get(key);
            		listOfProperty.addProperty(key, value);
            		
            	}
            	listOfProperty.validate();
            }
        }
        if(event.getSource().equals(this.saveBtn)){
        	apply();
        }
    }

    /**
     * Method returning the name of the site
     *
     * @return the myName the name of the site
     */
    public String getMyName() {
        return myName;
    }

    /**
     * Method setting the name of the site
     *
     * @param myName the myName to set
     */
    public void setMyName(String myName) {
        this.myName = myName;
    }
    
    /**
     * This class corresponds property panel that corresponds to the
     * element representing the property and where you can define it
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    private class PropertyJPanel extends JPanel {

    	/**
		 * Serial version ID
		 */
		private static final long serialVersionUID = 543598524774770166L;
		/**
		 * JLabel containing the text "the property :"
		 */
		private JLabel jLabel = null;
		/**
		 * JTextArea containing the value of the property
		 */
    	private JTextArea jTextArea = null;
    	/**
    	 * JLabel containing the na me of the property
    	 */
    	private JLabel jLabel1 = null;
    	/**
    	 * JCheckBox that can be checked if you want to remove the 
    	 * corresponding property
    	 */
    	private JCheckBox checkBoxToRemove = null;
    	/**
    	 * Boolean value telling if it can be removed from the properties
    	 */
    	private boolean isCheckable = true;
    	/**
    	 * JScrollPane used for the JTextArea when their is a lot of thing is
    	 * the variable
    	 */
    	private JScrollPane propertyJScrollPane = null;
    	
    	/**
    	 * This is the default constructor
    	 */
    	public PropertyJPanel() {
    		super();
    		
    	}
    	
    	/**
    	 * This is the default constructor
    	 */
    	public PropertyJPanel(String propertyName, String propertyValue, boolean check) {
    		this();
    		this.isCheckable = check;
    		initialize();
    		jLabel1.setText(propertyName);
    		jTextArea.setText(propertyValue);
    	}

    	/**
    	 * This method initializes this
    	 * 
    	 */
    	private void initialize() {
    		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    		
    		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
    		gridBagConstraints4.gridx = 2;
    		gridBagConstraints4.gridy = 0;
    		gridBagConstraints4.anchor = GridBagConstraints.EAST;
    		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
    		gridBagConstraints3.gridx = 1;
    		gridBagConstraints3.gridy = 0;
    		gridBagConstraints3.anchor = GridBagConstraints.EAST;
    		jLabel1 = new JLabel();
    		jLabel1.setText("The_property");
    		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
    		gridBagConstraints1.gridy = 1;
    		gridBagConstraints1.weightx = 1.0;
    		gridBagConstraints1.weighty = 1.0;
    		gridBagConstraints1.gridwidth = 3;
    		gridBagConstraints1.gridx = 0;
    		GridBagConstraints gridBagConstraints = new GridBagConstraints();
    		gridBagConstraints.gridx = 0;
    		gridBagConstraints.gridy = 0;
    		jLabel = new JLabel();
    		jLabel.setText("Property : ");
    		checkBoxToRemove = new JCheckBox();
    		this.setLayout(new GridBagLayout());
    		this.add(jLabel, gridBagConstraints);
    		this.add(getJTextArea(), gridBagConstraints1);
    		this.add(jLabel1, gridBagConstraints3);
    		if(isCheckable){
    			this.add(checkBoxToRemove,gridBagConstraints4);
    		}
    	}

    	/**
    	 * This method initializes jTextArea	
    	 * 	
    	 * @return {@link javax.swing.JScrollPane}
    	 */
    	private JScrollPane getJTextArea() {
    		if (jTextArea == null) {
    			jTextArea = new JTextArea("",5,59);
        		jTextArea.setWrapStyleWord(true);
        		jTextArea.setLineWrap(true);
        		propertyJScrollPane = new JScrollPane(jTextArea);
        		propertyJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    		}
    		return propertyJScrollPane;
    	}
    	
    	/**
    	 * Method returning the value of the property
    	 * 
    	 * @return the value of the property
    	 */
    	public String getPropertyValue(){
    		return jTextArea.getText();
    	}
    	
    	/**
    	 * Method returning the remove state of the property
    	 * 
    	 * @return a boolean value telling if the property can be removed or not
    	 */
    	public boolean getRemoveState(){
    		if(isCheckable)return checkBoxToRemove.isSelected();
    		else return false;
    	}
    	
    	/**
    	 * Method returning the name of the property
    	 * 
    	 * @return the name of th property
    	 */
    	public String getPropertyName(){
    		return jLabel1.getText();
    	}
    }
    
    /**
     * This class corresponds to the JPanel containing the list of PropertyPanel
     *
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    private class ListOfPropertyJPanel extends JPanel{
        	
        /**
		 * Serial version ID
		 */
		private static final long serialVersionUID = -2225768365813438346L;
		/**
         * JButton used to add a property
         */
        private JButton addPropertyJButton = null;
        /**
         * JButton used to remove a property
         */
        private JButton removePropertyJButton = null;
        
    	/**
    	 * JPanel for the add property panel 
    	 */
        private JPanel addPropertyJPanel = null;
        
        /**
         * Jpanel for the buttons
         */
        private JPanel panelOfButtons = null;
        
        /**
         * JTextField that contains the name of the new property
         */
        private JTextField addPropertyJTextField = null;
        
        /**
         * scrollPane that will contain the different propertyJPanel
         */
        private JScrollPane scrollPane = null;
        
        /**
         * View to display in the JScrollPane
         *
         */
        private JPanel viewPanel = null;
        /**
         * Remove property panel
         */
        private JPanel removePropertyJPanel = null;
        /**
         * A glue
         */
        private Component glue = Box.createVerticalGlue();
        
        /**
         * Default constructor
         */
        public ListOfPropertyJPanel(){
        	super();
        	initialize();
        }
        
        /**
         * Method returning the panel of buttons for the addition and removing
         * of properties
         * 
         * @return a JPanel
         */
        private JPanel getPanelOfButtons(){
        	if(panelOfButtons == null){
        		panelOfButtons = new JPanel();
        		panelOfButtons.setLayout(new BoxLayout(panelOfButtons,BoxLayout.Y_AXIS));
        		panelOfButtons.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        		panelOfButtons.add(getAddPropertyJPanel());
        		panelOfButtons.add(getRemovePropertyJPanel());
        		panelOfButtons.setAlignmentY(JPanel.LEFT_ALIGNMENT);
        	}
        	return panelOfButtons;
        }
        
        /**
         * Method returning the RemoveProperty panel
         * 
         * @return a JPanel
         */
        private JPanel getRemovePropertyJPanel(){
        	if(removePropertyJPanel == null){
        		removePropertyJPanel = new JPanel();
        		removePropertyJPanel.setLayout(new BoxLayout(removePropertyJPanel,BoxLayout.X_AXIS));
        		removePropertyJPanel.add(getRemovePropertyJButton());
        		removePropertyJPanel.add(Box.createHorizontalStrut(10));
        		removePropertyJPanel.add(Box.createHorizontalGlue());
        	}
        	return removePropertyJPanel;
        }
        
        /**
         * Method returning the AddProperty Japenl
         * 
         * @return a JPanel
         */
        private JPanel getAddPropertyJPanel(){
        	if(addPropertyJPanel == null){
        		addPropertyJPanel = new JPanel();
        		addPropertyJPanel.setLayout(new BoxLayout(addPropertyJPanel,BoxLayout.X_AXIS));
        		addPropertyJPanel.add(getAddPropertyJButton());
        		addPropertyJPanel.add(Box.createHorizontalStrut(10));
        		addPropertyJTextField = new JTextField();
        		addPropertyJPanel.add(addPropertyJTextField);
        	}
        	return addPropertyJPanel;
        }
        
        /**
         * Method returning the AddProperty JButton
         * 
         * @return a JButton
         */
        private JButton getAddPropertyJButton(){
        	if(addPropertyJButton == null){
        		addPropertyJButton = new JButton("Add property");
        	}
        	return addPropertyJButton;
        }
        
        /**
         * Method returning the RemoveProperty JButton
         * 
         * @return a JButton
         */ 
        private JButton getRemovePropertyJButton(){
        	if(removePropertyJButton == null){
        		removePropertyJButton = new JButton("Remove property");
        	}
        	return removePropertyJButton;
        }
        
        /**
         * Method returning the View Panel
         * 
         * @return a JPanel
         */
        private JPanel getViewPanel(){
        	if(viewPanel == null){
        		viewPanel = new JPanel();
        		viewPanel.setLayout(new BoxLayout(viewPanel,BoxLayout.Y_AXIS));
        		myCfg = G5kCfg.getSite(myIndex);
                if(myCfg.get(SiteCfg.SCRATCH_RUNTIME) == null){
                	myCfg.set(SiteCfg.SCRATCH_RUNTIME, "");
                }
        		Iterator<String> iterator = myCfg.getKeySet().iterator();
                while(iterator.hasNext()){
                	String key = iterator.next();
                	String value = myCfg.get(key);
                	boolean isCheckable = true;
                	if(key.equalsIgnoreCase(SiteCfg.SCRATCH_RUNTIME)){
                		isCheckable = false;
                	}
                	PropertyJPanel panel = new PropertyJPanel(key,value,isCheckable);
                	viewPanel.add(panel);
                	propertiesHashMap.put(key,panel);
                }
                viewPanel.add(glue);
                
        	}
        	return viewPanel;
        }
        
        /**
         * Method initializing the ListOfPropertyPanel
         */
        private void initialize(){
        	setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        	add(getPanelOfButtons());
        	scrollPane = new JScrollPane(getViewPanel());
        	scrollPane.setBorder(LineBorder.createBlackLineBorder());
        	scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        	scrollPane.setMaximumSize(new Dimension(600,350));
        	scrollPane.setMinimumSize(new Dimension(600,350));
        	scrollPane.setPreferredSize(new Dimension(600,350));
        	add(Box.createVerticalStrut(10));
        	add(scrollPane);
        	add(Box.createVerticalStrut(10));
            addEventManagement();
        }
        
        /**
         * Method managing the events
         */
        private void addEventManagement(){
        	removePropertyJButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					Component[] components = getViewPanel().getComponents();
					for(int i = 0; i < components.length ; i++){
						if(components[i] instanceof PropertyJPanel){
							PropertyJPanel panel = (PropertyJPanel)components[i];
							if(panel.getRemoveState()){
								String key = panel.getPropertyName();
								removeProperty(key);
							}
						}
					}
					ListOfPropertyJPanel.this.getViewPanel().validate();
				}
        	});
        	
        	addPropertyJButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					String key = addPropertyJTextField.getText();
					String value = addPropertyJTextField.getText();
					addProperty(key, value);
					ListOfPropertyJPanel.this.getViewPanel().validate();
				}
        		
        	});
        }
        
        /**
         * Method adding a property with a name and a value
         * 
         * @param key the name of the property to add
         * @param value value of the property
         */
        public void addProperty(String key, String value){
        	boolean isCheckable = true;
        	if(key.equalsIgnoreCase(SiteCfg.SCRATCH_RUNTIME)){
        		isCheckable = false;
        	}
        	PropertyJPanel panel = new PropertyJPanel(key,value,isCheckable);
			//if(!propertiesHashMap.containsKey(key)){
				propertiesHashMap.put(key,panel);
				ListOfPropertyJPanel.this.getViewPanel().remove(glue);
				ListOfPropertyJPanel.this.getViewPanel().add(panel);
				ListOfPropertyJPanel.this.getViewPanel().add(glue);
			//}
        }
        
        /**
         * Method removing a property from its name
         * 
         * @param key the name of the property
         */
        public void removeProperty(String key){
        	if(propertiesHashMap.containsKey(key)){
        		//if(!key.equalsIgnoreCase(SiteCfg.SCRATCH_RUNTIME)){
        			PropertyJPanel panel = propertiesHashMap.get(key);
        			ListOfPropertyJPanel.this.getViewPanel().remove(panel);
        			propertiesHashMap.remove(key);
        		//}
        	}
        }
        
        /**
         * Method removing all the properties
         */
        public void removeAllProperties(){
        	Iterator<String> iter = propertiesHashMap.keySet().iterator();
        	while(iter.hasNext()){
        		removeProperty(iter.next());
        	}
        }
    }
}