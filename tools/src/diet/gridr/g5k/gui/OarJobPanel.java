/****************************************************************************/
/* This class corresponds to the panel that displays the informations about */
/* an OAR Job                                                               */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: OarJobPanel.java,v 1.11 2007/10/30 14:11:39 dloureir Exp $
 * $Log: OarJobPanel.java,v $
 * Revision 1.11  2007/10/30 14:11:39  dloureir
 * Integration of the Ganglia plugin
 *
 * Revision 1.10  2007/10/08 14:53:55  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.9  2007/10/05 16:12:55  dloureir
 * Changing the way hosts are displayed in the OarJobPanel
 *
 * Revision 1.8  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.7  2007/07/12 14:48:05  dloureir
 * Some typo correction
 *
 * Revision 1.6  2007/07/12 09:08:02  dloureir
 * The JTextField are no more editable.
 *
 * Revision 1.5  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.swing.*;

import java.awt.*;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

import diet.grid.api.GridJob;
import diet.grid.api.util.HistoryUtil;

/**
 * This class corresponds to the panel that displays the informations about
 * an OAR Job
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class OarJobPanel extends JPanel {
    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
	private static final long serialVersionUID = -6972087252488131802L;
    /**
     * array of JTextFields for the job properties
     */
	private JTextField [] jobsTextField;

	private GridJob job = null;

    /**
     * Default constructor
     *
     */
	public OarJobPanel(GridJob aJob){
		job = aJob;
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.jobsTextField = new JTextField[job.getParametersValuesCount()];
		Iterator<String> iterator = job.getKeys().iterator();
		int ix = 0;
		// dummy size for good size of fields and labels
		Dimension taille = new Dimension(Integer.MAX_VALUE,20);
		while(iterator.hasNext()){
			JPanel aPanel = new JPanel();
			aPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			aPanel.setLayout(new GridLayout(1,2));
			String key = iterator.next();
			JLabel hostLabel = new JLabel(job.getParameterDescription(key));
			hostLabel.setMaximumSize(taille);
			aPanel.add(hostLabel);
			this.jobsTextField[ix] = new JTextField(20);
			if(key.equalsIgnoreCase(GridJob.KEY_GRID_JOB_HOSTS)){
				jobsTextField[ix].setText(job.getHostsAsString(" + "));
			}
			if(key.equalsIgnoreCase(GridJob.KEY_GRID_JOB_SCHEDTIME) || 
					key.equalsIgnoreCase(GridJob.KEY_GRID_JOB_STARTTIME) ||
					key.equalsIgnoreCase(GridJob.KEY_GRID_JOB_SUBTIME)){
				try{
					long time = Long.parseLong(job.getParameterValue(key))*1000;
					Date date = new Date(time);
					jobsTextField[ix].setText(HistoryUtil.getOARDateFromDate(date));
				}
				catch(Exception e){
					jobsTextField[ix].setText(job.getParameterValue(key));
				}
			}
			else if(key.equalsIgnoreCase(GridJob.KEY_GRID_JOB_WALLTIME)){
				try{
					long time = Long.parseLong(job.getParameterValue(key))*1000;
					jobsTextField[ix].setText(HistoryUtil.getWallTimeFromDate(time));
				}
				catch(Exception e){
					jobsTextField[ix].setText(job.getParameterValue(key));
				}
			}
			else{
				jobsTextField[ix].setText(job.getParameterValue(key));
			}
			jobsTextField[ix].setEditable(false);
			jobsTextField[ix].setMaximumSize(taille);
			aPanel.add(this.jobsTextField[ix]);
			aPanel.setMaximumSize(taille);
			add(aPanel);
			ix++;
		}
		add(Box.createVerticalGlue());
		revalidate();
		LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "OarJobPanel", "OarJob panel initialized");
	}
}