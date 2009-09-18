/****************************************************************************/
/* Utility class (Not used anymore)                                         */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: OarJobDesc.java,v 1.3 2007/07/12 15:56:54 dloureir Exp $
 * $Log: OarJobDesc.java,v $
 * Revision 1.3  2007/07/12 15:56:54  dloureir
 * Some javadoc with the deprecated tag
 *
 * Revision 1.2  2007/05/22 07:52:39  aamar
 * Correct headers.
 *
 ****************************************************************************/
package diet.gridr.util;

/**
 * Describe class OarJobDesc here.
 *
 * @deprecated This class is no more used 
 *
 * Created: Wed Jun 14 16:15:49 2006
 *
 * @author <a href="mailto:hektor@localhost.localdomain">Abdelkader Amar</a>
 * @version 1.0
 */
public class OarJobDesc {
	public String jobId;
	public String jobName;
	public String jobOwner;
	public String jobState;
	public String jobComment;
	public String jobHosts;
	public String jobQueue;
	public String jobNbNodes;
	public String jobWeight;
	public String jobCommand;
	public String jobLaunchingDir;
	public String jobType;
	public String jobProperties;
	public String jobReservation;
	public String jobWallTime;
	public String jobSubmTime;
	public String jobStartTime;
	public String jobScheduleStart;

	/**
	 * Creates a new <code>OarJobDesc</code> instance.
	 * 
	 * @deprecated
	 *
	 */
	public OarJobDesc(String _jobName, String _jobOwner) {
		this.jobName = new String(_jobName);
		this.jobOwner = new String(_jobOwner);
	}

	/**
	 * @deprecated
	 * 
	 * @param lines
	 */
	public OarJobDesc(String[] lines) {
		int ix = 0;
		jobId = lines[ix++].substring((new String("Job Id : ")).length());
		jobName = lines[ix++].split(" = ", 3)[1];
		jobOwner = lines[ix++].split(" = ", 3)[1];
		jobState = lines[ix++].split(" = ", 3)[1];
		jobComment = lines[ix++].split(" = ", 3)[1];
		jobHosts = lines[ix++].split(" = ", 3)[1];
		jobQueue = lines[ix++].split(" = ", 3)[1];
		jobNbNodes = lines[ix++].split(" = ", 3)[1];
		jobWeight = lines[ix++].split(" = ", 3)[1];
		jobCommand = lines[ix++].split(" = ", 3)[1];
		jobLaunchingDir = lines[ix++].split(" = ", 3)[1];
		jobType = lines[ix++].split(" = ", 3)[1];
		jobProperties = lines[ix++].split(" = ", 3)[1]; // multiple ?
		jobReservation = lines[ix++].split(" = ", 3)[1];
		jobWallTime = lines[ix++].split(" = ", 3)[1];
		jobSubmTime = lines[ix++].split(" = ", 3)[1];
		jobStartTime = lines[ix++].split(" = ", 3)[1];
		jobScheduleStart= lines[ix].split(" = ", 3)[1];

	}

	/**
	 * @deprecated
	 * 
	 */
	public String toString() {
		String str = "---------------------------------------------------------";
		str += "----> Job Id           : |" + jobId + "|";
		str += "----> jobOwner         : |" + jobOwner + "|";
		str += "----> jobState         : |" + jobState + "|";
		str += "----> jobHosts         : |" + jobHosts + "|";
		str += "----> jobNbNodes       : |" + jobNbNodes + "|";
		str += "----> jobReservation   : |" + jobReservation + "|";
		str += "----> jobWallTime      : |" + jobWallTime + "|";
		str += "----> jobSubmTime      : |" + jobSubmTime + "|";
		str += "----> jobStartTime     : |" + jobStartTime +"|";
		str += "----> jobScheduleStart : | "+ jobScheduleStart + "|";

		return str;
	}
}
