/****************************************************************************/
/* This class represents the view of the reservations on a cluster          */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ResaClusterView.java,v 1.5 2007/07/12 14:50:21 dloureir Exp $
 * $Log: ResaClusterView.java,v $
 * Revision 1.5  2007/07/12 14:50:21  dloureir
 * Some typo corrections.
 *
 * Revision 1.4  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import java.util.logging.Level;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import diet.gridr.g5k.util.G5kConstants;
import diet.logging.LoggingManager;

/**
 * This class represents the view of the reservations on a cluster
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class ResaClusterView extends JPanel{
    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
    private static final long serialVersionUID = 2451767961105252963L;
    /**
     * label for the name of the cluster
     */
    private JLabel clusterNameLabel = null;
    /**
     * spinner for the selected nodes of the cluster
     */
	private JSpinner selectedValueSpinner = null;
    /**
     * index of the cluster on which we are reserving some nodes
     */
	private int clusterIndex = 0;
    /**
     * array containing the number of reserved nodes for all clusters of grid5000
     */
	private int[] reservedNodes = null;

    /**
     * Default constructor
     *
     * @param clusterIndex index of the cluster
     * @param reservedNodes array containing the number of
     * reserved nodes for all clusters of grid5000
     */
	public ResaClusterView(int clusterIndex,int[] reservedNodes){
		super();
		this.reservedNodes = reservedNodes;
		this.clusterIndex = clusterIndex;
		initialize();
		LoggingManager.log(Level.FINE,LoggingManager.RESOURCESTOOL,this.getClass().getName(),"ResaClusterView","ResaClusterview initialized");
	}

    /**
     * Method that initialized the object view
     *
     */
	private void initialize(){
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		this.add(getClusterNameLabel(),null);
		this.add(Box.createHorizontalGlue(),null);
		this.add(getSelectedValueSpinner(),null);
	}

	/**
     * Method that returns the label for the cluster name
     *
	 * @return the clusterNameLabel
	 */
	public JLabel getClusterNameLabel() {
		if(clusterNameLabel == null){
			clusterNameLabel = new JLabel();
			String theName = G5kConstants.clusters.get(clusterIndex).getName();
			int beginIndex = theName.indexOf("--");
			if(beginIndex != -1) theName = theName.substring(beginIndex+2);
			clusterNameLabel.setText(theName);
		}
		return clusterNameLabel;
	}

	/**
     * Method that returns the spinner
     *
	 * @return the selectedValueSpinner
	 */
	public JSpinner getSelectedValueSpinner() {
		if(selectedValueSpinner == null){
			SpinnerNumberModel model = new SpinnerNumberModel(reservedNodes[clusterIndex],0,G5kConstants.clusters.get(clusterIndex).getCapacity(),1);
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