/****************************************************************************/
/* This class represents the popup panel for a reservation                  */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ResaPopupPanel.java,v 1.7 2007/09/28 16:02:44 aamar Exp $
 * $Log: ResaPopupPanel.java,v $
 * Revision 1.7  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.6  2007/07/12 14:56:25  dloureir
 * Some typo correction
 *
 * Revision 1.5  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import diet.gridr.g5k.util.G5kCluster;
import diet.gridr.g5k.util.G5kConstants;
import diet.gridr.g5k.util.G5kSite;
import diet.logging.LoggingManager;

/**
 * This class represents the popup panel for a reservation
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class ResaPopupPanel extends JPanel{

    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
	private static final long serialVersionUID = 1L;
    /**
     * popup for the reservation
     */
	private JPopupMenu popup = null;
    /**
     * ok button
     */
	private JButton okButton = null;
    /**
     * cancel button
     */
	private JButton cancelButton = null;
    /**
     * Array of radio buttons to choose cluster
     */
	private JRadioButton[] clusters;
	
	/**
	 * Radio Button if the user doesn't choose a cluster
	 */
	private JRadioButton common;

     /**
     * boolean value defining if the popup has been already initialized
     */
	private boolean isInitialized = false;
    /**
     * ResaView corresponding to this popup
     */
	private ResaView myView;
    /**
     * panel for the buttons
     */
	private JPanel buttonsPanel = null;
    /**
     * array of the reserved nodes
     */
	private int[] reservedNodes = null;

	/**
     * Requested resources count spinner
     */
    private SpinnerPanel resourceSpinner;

    /**
     * Default constructor
     *
     * @param aView the reservation view associated
     * @param popup popup menu associated
     */
	public ResaPopupPanel(ResaView aView, JPopupMenu popup){
		super();
		reservedNodes = aView.getReservedNodes();
		this.popup = popup;
		myView = aView;
		initialize();
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ResaPopupPanel", "resaPopupPanel initialized");
	}

    /**
     * method used to initialize the popup
     *
     */
	private void initialize(){
		if(!isInitialized){
			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			int[] clustersIndexes = G5kSite.getClustersIndexesForIndex(myView.getSiteIndex());
			this.clusters = new JRadioButton[clustersIndexes.length];

            ButtonGroup group = new ButtonGroup();
            this.resourceSpinner = new SpinnerPanel(this.reservedNodes[myView.getSiteIndex()]);
            this.add(this.resourceSpinner);
            this.common = new JRadioButton(G5kSite.getSiteForIndex(myView.getSiteIndex()));
            this.add(this.common);
            group.add(this.common);
            for(int i =0 ; i < clustersIndexes.length ; i++){
			    this.clusters[i] = new JRadioButton(G5kCluster.getNameForIndex(clustersIndexes[i]));
			    add(this.clusters[i]);
			    group.add(this.clusters[i]);
			    // add(resaClusterViews[i]);
			}
            int chosenCluster = this.myView.getChosenCluster();
            if (chosenCluster == -1) {
                this.common.setSelected(true);
            }
            else {
                for(int ix = 0 ; ix < clustersIndexes.length ; ix++) {
                    if (clustersIndexes[ix] == chosenCluster)
                        this.clusters[ix].setSelected(true);
                }
            }
			add(getButtonsPanel(),null);
			isInitialized = !isInitialized;
			LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "initialize", "ResaPopupPanel initialized");
		}
	}

    /**
     * Method returning the panel of the buttons
     *
     * @return buttons panel
     */
	public JPanel getButtonsPanel(){
		if(buttonsPanel == null){
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new BoxLayout(buttonsPanel,BoxLayout.X_AXIS));
			buttonsPanel.add(getOkButton(),null);
			buttonsPanel.add(Box.createHorizontalGlue(),null);
			buttonsPanel.add(getCancelButton(),null);
			LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "getButtonsPanel", "Buttons Panel initialized");
		}
		return buttonsPanel;
	}
	/**
     * Method returning the cancel button
     *
	 * @return the cancelButton
	 */
	public JButton getCancelButton() {
		if(cancelButton == null){
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					setVisible(false);
					popup.setVisible(false);
					popup = null;
				}
			});
		}
		return cancelButton;
	}
	/**
     * Method returning the ok button
     *
	 * @return the okButton
	 */
	public JButton getOkButton() {
		if(okButton==null){
			okButton = new JButton("Validate");
			okButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int siteIndex = myView.getSiteIndex();
					reservedNodes[siteIndex] = resourceSpinner.getSpinnerValue(); 
					if (common.isSelected()) { 
					    myView.setChosenCluster(-1);
					}
					else {
					    int[] clustersIndexes = G5kSite.getClustersIndexesForIndex(myView.getSiteIndex());
					    int ix = 0;
					    for (int idx : clustersIndexes) {
					        if (clusters[ix++].isSelected()) {
					            myView.setChosenCluster(idx);
					            break;
					        }
					    }
					}
					myView.updateView();
					setVisible(false);
					popup.setVisible(false);
					popup = null;
				}
			});
		}
		return okButton;
	}
	
	class SpinnerPanel extends JPanel {

	    /**
	     * Number of reserved nodes for the site
	     */
	    private int reservedNodes;

	    /**
	     * spinner for the selected nodes of the cluster
	     */
	    private JSpinner selectedValueSpinner = null;

	    SpinnerPanel(int reservedNodes) {
	        this.reservedNodes = reservedNodes;
	        this.initialize();
	    }
	    /**
	     * Method that initialized the object view
	     *
	     */
	    private void initialize(){
	        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
	        this.add(new JLabel("Resources"));
	        this.add(Box.createHorizontalGlue(),null);
	        this.add(getSelectedValueSpinner(),null);
	    }

	    /**
	     * Method that returns the spinner
	     *
	     * @return the selectedValueSpinner
	     */
	    public JSpinner getSelectedValueSpinner() {
	        if(selectedValueSpinner == null){
	            int max = G5kSite.getResCount(myView.getSiteIndex());

	            SpinnerNumberModel model = new SpinnerNumberModel(
	                    reservedNodes,
	                    0,
	                    max,
	                    1);
	            selectedValueSpinner = new JSpinner(model);
	        }
	        return selectedValueSpinner;
	    }

	    /**
	     * Method returning the spinner value
	     *
	     * @return the spinner value
	     */
	    public int getSpinnerValue(){
	        return (Integer)selectedValueSpinner.getValue();
	    }
	}
}