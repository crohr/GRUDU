/****************************************************************************/
/* This class corresponds to the Grid5000 reservation                       */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: G5kReservation.java,v 1.12 2007/09/28 16:03:19 aamar Exp $
 * $Log: G5kReservation.java,v $
 * Revision 1.12  2007/09/28 16:03:19  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.11  2007/07/12 15:35:06  dloureir
 * Some typo corrections.
 *
 * Revision 1.10  2007/06/25 09:52:45  dloureir
 * Adding a feature : the user can now provide a scriptto launch when the reservation comes up (the path should be absolute)
 *
 * Revision 1.9  2007/06/22 11:53:50  dloureir
 * Corrections for bugs :
 *
 *  - bug 37 : It was about the wrong number of nodes on Nancy. In fact, it was a problem of properties when reserving some nodes. Now you have a extra panel in the reservation frame that allows you to specify some properties for each clusters.
 *    * G5KRes : Adding  the propertiesHashMap in the constructor call of the OAROpRunnable
 *    * OAROpRunnable : Adding an attribute (properties), and changing the constructor and the call to the oarsub method of the OARUtil class.
 *    * ResaDlg : Adding a propertiesPanel as an internal class. There is now two tabs (one for the basic and one for the properties of the reservation)
 *    * G5KReservation : Adding a propertiesHashMap and the associated getter/setter
 *    * OARUtil : Adding the properties when call the oarsub
 *
 *  - bug 39 : Allows the OAR grid sub behaviour when reserving (It means that when you want to reserve on the grid, if one of the reservation fails, all the already realized jobs are deleted). The choice is realized at the reservation time be checking/uncheking a checkbox in the reservation panel.
 *    * G5KRes : Adding a hashmap for the conservation of the ids of jobs submitted with success in order to delete them if their is one job not successfully submitted in the same "reservation" when the oargridsub behaviour is activated
 *    * OAROPRunnable : conservation of the id job, and getter/setter for that idjob
 *    * ResaDlg : Adding a checkbox for the selection of the oargridsub behaviour, and adding this behaviour to the reservation
 *    * G5KReservation : Boolean for the oargridsub behaviour with the associated getter and setter
 *    * OARUtil :  oarsub now returns the job id (when it is submitted successfully or not). When the job is not submitted successfully, the job id is lower than zero.
 *
 *  - bug 40 : To solve the lack of reservation status after the submission, a reservation status frame has been added. It displays the statuses of the different submissions realized by this reservation.
 *    * G5KRes : Adding the call to the ReservationStatusFrame constructor that displays the status frame
 *    * ReservationStatusFrame : frame for the status of the reservation that displays the information about the different submissions.
 *
 * Revision 1.8  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 *
 ****************************************************************************/
package diet.gridr.g5k.util;

import java.util.HashMap;
import java.util.logging.Level;

import diet.logging.LoggingManager;

/**
 * This class corresponds to the Grid5000 reservation
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class G5kReservation {
    /**
     * User of the reservation
     */
    private String myUser;
    /**
     * Array containing for each site the number of reserved nodes
     */
    private int [] nodesCount;
    /**
     * Array containing the chosen cluster for each site (equal to -1 if no cluster selected)
     */
    private int [] chosenClusters;
    /**
     * Integer corresponding to the date of the reservation
     */
    private int year, month, day, hour, minute, second;
    /**
     * Integer corresponding to the walltime hour
     */
    private int wallTime_hour;
    /**
     * Integer corresponding to the walltime minutes
     */
    private int wallTime_min;
    /**
     * Integer corresponding to the queue of the reservation
     */
    private int selectedQueue;
    /**
     * OAR Grid Sub behaviour
     */
    private boolean oargridsubBehaviour = false;
    
    /**
     * properties for the reservations
     */
    private HashMap<Integer, String> propertiesHashMap = null;
    
    /**
     * script/executable to launch when the reservation comes up
     */
    private String scriptToLaunch = null;
    
    /**
     * Default constructor of a Reservation on G5k
     *
     */
    public G5kReservation() {
        this.nodesCount = new int[G5kSite.getSitesNumber()];
        this.chosenClusters = new int[G5kSite.getSitesNumber()];
        this.wallTime_hour = 0;
        this.wallTime_min = 0;
        this.myUser = Config.getUser();
        this.selectedQueue = 0;
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "G5kReservation", "G5kReservation created");
    }

    /**
     * Method returning the user of the reservation
     *
     * @return user of the reservation
     */
    public String getUser() {
        return this.myUser;
    }

    /**
     * Method setting the use of the reservation
     *
     * @param user user of the reservation
     */
    public void setUser(String user) {
        this.myUser = user;
    }

    /**
     * Method returning the number of nodes requested for the
     * index corresponding to a cluster
     *
     * @param index index of a cluster
     * @return number of requested nodes for that cluster
     */
    public int getRequestedNodeCount(int index) {
        return this.nodesCount[index];
    }

    /**
     * Method setting the number of requested nodes for
     * that cluster
     *
     * @param index index of a cluster
     * @param count number of requested nodes for that cluster
     */
    public void setRequestedNodeCount(int index, int count) {
        this.nodesCount[index] = count;
    }

    /**
     * Method returning the year of the reservation
     *
     * @return the year of the reservation
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Method returning the month of the reservation
     *
     * @return the month of the reservation
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Method returning the day of the reservation
     *
     * @return the day of the reservation
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Method returning the hour of the reservation
     *
     * @return the hour of the reservation
     */
    public int getHour() {
        return this.hour;
    }

    /**
     * Method returning the minute of the reservation
     *
     * @return the minute of the reservation
     */
    public int getMin() {
        return this.minute;
    }

    /**
     * Method returning the second of the reservation
     *
     * @return the second of the reservation
     */
    public int getSec() {
        return this.second;
    }

    /**
     * Method setting the year of the reservation
     *
     * @param y the year of the reservation
     */
    public void setYear(int y) {
        this.year = y;
    }

    /**
     * Method setting the month of the reservation
     *
     * @param m the month of the reservation
     */
    public void setMonth(int m) {
        this.month = m;
    }

    /**
     * Method setting the day of the reservation
     *
     * @param d the day of the reservation
     */
    public void setDay(int d) {
        this.day = d;
    }

    /**
     * Method setting the hour of the reservation
     *
     * @param h the hour of the reservation
     */
    public void setHour(int h) {
        this.hour = h;
    }

    /**
     * Method setting the minute of the reservation
     *
     * @param m the minute of the reservation
     */
    public void setMinute(int m) {
        this.minute = m;
    }

    /**
     * Method setting the second of the reservation
     *
     * @param s the second of the reservation
     */
    public void setSecond(int s) {
        this.second = s;
    }

    /**
     * Method returning the number of requested nodes of a cluster
     *
     * @param cluster_ind index of a cluster of which we want to know
     * the number of requested nodes
     * @return number of nodes requested for the given cluster
     */
    public int requestedNodeCount(int cluster_ind) {
        if ((cluster_ind >= 0) && (cluster_ind < nodesCount.length))
            return this.nodesCount[cluster_ind];
        else
            return -1;
    }

    /**
     * Method setting the walltime
     *
     * @param h hour of the walltime
     * @param m minute of the walltime
     */
    public void setWallTime(int h, int m) {
        this.wallTime_hour = h;
        this.wallTime_min  = m;
    }

    /**
     * Method returning a string corresponding to the description
     * of this reservation
     *
     * @return a String describing the reservation
     *
     */
    public String toString() {
        String str = "THE RESERVATION STATUS :\n";
        for (int ix=0; ix<this.nodesCount.length; ix++) {
            str += G5kConstants.clusters.get(ix).getName() + " : requested " + this.nodesCount[ix] + " node";
            if (this.nodesCount[ix]>1)
                str += "s";
            str+= "\n";
        }
        str += "Reservation Time : \n";
        str += this.year + "/" + this.month + "/" + this.day + " " + this.hour + ":" + this.minute + ":" + this.second + "\n";
        str += "Walltime : \n";
        str += this.wallTime_hour + ":" + this.wallTime_min + "\n";
        LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "toString", str);
        return str;
    }

    /**
     * Method returning the number of clusters used
     *
     * @return number of clusters used
     */
    public int usedClustersCount() {
        int used = 0;
        for (int ix=0; ix < nodesCount.length; ix++) {
            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "usedClustersCount" , "#" + this.nodesCount[ix]);
        }
        for (int ix=0; ix < nodesCount.length; ix++) {
            if ( this.nodesCount[ix] != 0 )
                used++;
        }
        return used;
    }

    /**
     * Method returning the number of requested nodes for a certain cluster
     *
     * @param i index of a cluster
     * @return number of nodes requested for that cluster
     */
    public int usedByInd(int i) {
        int used  = 0;
        for (int ix=0; ix< nodesCount.length; ix++) {
            if ( this.nodesCount[ix] != 0 ) {
                if (used == i)
                    return ix;
                used++;
            }
        }
        return -1;
    }

    /**
     * Method returning a string corresponding to the walltime
     *
     * @return a string corresponding to the walltime
     */
    public String getWallTime() {
        return this.wallTime_hour + ":" + this.wallTime_min;
    }

    /**
     * Method returning a string corresponding to start time
     *
     * @return a string corresponding to the start time
     */
    public String getStartTime() {
        return 	this.year + "-" + this.month + "-" + this.day + " " + this.hour + ":" + this.minute + ":" + this.second;
    }

    /**
     * Method setting the selected queue
     *
     * @param queue the queue to set
     */
    public void setSelectedQueue(int queue) {
        this.selectedQueue = queue;
    }

    /**
     * Method returning the selected queue
     *
     * @return the selected queue
     */
    public String getSelectedQueue() {
        return KadeployUtil.getQueue(this.selectedQueue);
    }

	/**
	 * @return the oargridsubBehaviour
	 */
	public boolean isOargridsubBehaviour() {
		return oargridsubBehaviour;
	}

	/**
	 * @param oargridsubBehaviour the oargridsubBehaviour to set
	 */
	public void setOargridsubBehaviour(boolean oargridsubBehaviour) {
		this.oargridsubBehaviour = oargridsubBehaviour;
	}

	/**
	 * @return the propertiesHashMap
	 */
	public HashMap<Integer, String> getPropertiesHashMap() {
		return propertiesHashMap;
	}

	/**
	 * @param propertiesHashMap the propertiesHashMap to set
	 */
	public void setPropertiesHashMap(HashMap<Integer, String> propertiesHashMap) {
		this.propertiesHashMap = propertiesHashMap;
	}

	/**
	 * @return the scriptToLaunch
	 */
	public String getScriptToLaunch() {
		return scriptToLaunch;
	}

	/**
	 * @param scriptToLaunch the scriptToLaunch to set
	 */
	public void setScriptToLaunch(String scriptToLaunch) {
		this.scriptToLaunch = scriptToLaunch;
	}
	
    /**
     * Method returning the chosen cluster for a site
     * index corresponding to a site index
     *
     * @param index index of a site
     * @return chosen cluster or -1 if no one chosen
     */
    public int getChosenCluster(int index) {
        return this.chosenClusters[index];
    }

    /**
     * Method setting the chosen cluster for a site
     *
     * @param index index of a site
     * @param cluster chosen cluster index
     */
    public void setChosenCluster(int index, int cluster) {
        this.chosenClusters[index] = cluster;
    }
	
}
