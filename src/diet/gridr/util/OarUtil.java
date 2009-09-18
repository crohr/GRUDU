/****************************************************************************/
/* Utility class.                                                          */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: OarUtil.java,v 1.3 2007/07/12 15:56:54 dloureir Exp $
 * $Log: OarUtil.java,v $
 * Revision 1.3  2007/07/12 15:56:54  dloureir
 * Some javadoc with the deprecated tag
 *
 * Revision 1.2  2007/05/22 07:52:39  aamar
 * Correct headers.
 *
 ****************************************************************************/
package diet.gridr.util;

import java.util.*;
import java.io.*;

/**
 * Utility class. 
 * 
 * @deprecated
 * 
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 *
 */
public class OarUtil {

	/**
	 * @deprecated
	 * 
	 * @param user
	 * @param frontal
	 * @param oar_host
	 * @return
	 */
	public static Vector<OarJobDesc> getResaOf(String user, String frontal, String oar_host) {
		Map<String, OarJobDesc> jobs = oarStat(user, frontal, oar_host);
		Vector<OarJobDesc> v = new Vector<OarJobDesc>();
		Iterator<OarJobDesc> p = jobs.values().iterator();
		while (p.hasNext()) {
			OarJobDesc j = (OarJobDesc)p.next();
			if (j.jobOwner.equals(user)) {
				v.add(j);
			}
		}
		return v;
	}

	/**
	 * @deprecated
	 * 
	 * @param id
	 * @param user
	 * @param frontal
	 * @param oar_host
	 * @return
	 */
	public static OarJobDesc getResaById(String id, String user, String frontal, String oar_host) {
		Map<String, OarJobDesc> map = oarStat(user, frontal, oar_host);
		Iterator<OarJobDesc> p = map.values().iterator();
		while (p.hasNext()) {
			OarJobDesc j = (OarJobDesc)p.next();
			if (j.jobId.equals(id)) {
				return j;
			}
		}
		return null;
	}

	/**
	 * @deprecated
	 * 
	 * @param user
	 * @param frontal
	 * @param oar_host
	 * @return
	 */
	public static Map<String, OarJobDesc> oarStat(String user, String frontal, String oar_host) {
		Map<String, OarJobDesc> jobs = new HashMap<String, OarJobDesc>();
		List<String> lines = new ArrayList<String>();

		try {         // start up the command 

			String[] cmdArray = { "ssh",
					user+"@"+ frontal,
					"ssh "+oar_host +" \"oarstat -fa\""
			};

			// String cmdArray [] = {"ls"};
			Process child = Runtime.getRuntime().exec(cmdArray);

			InputStream cmdOut = child.getInputStream();
			InputStreamReader r = new InputStreamReader(cmdOut);
			BufferedReader in = new BufferedReader(r);

			InputStream cmdErrOut = child.getErrorStream();
			InputStreamReader rr = new InputStreamReader(cmdErrOut);
			BufferedReader inErr = new BufferedReader(rr);


			String line;
			// read the command error
			System.out.println("#ERROR#");
			while ((line = inErr.readLine()) != null)
				System.out.println(line);
			System.out.println("#");

			// read the command's output 

			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
			if (child.waitFor() != 0) {   // if the ssh failed 
				System.out.println("the ssh failed");
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		String [] result = lines.toArray(new String[lines.size()]);
		for (int ix=0; ix<result.length; ix++) {
			if (result[ix].startsWith("Job Id: ")) {
				String [] strs = new String[18];
				for (int jx=0; jx<18; jx++) {
					strs[jx] = result[jx+ix];
				}
				String jobId = result[ix++].substring((new String("Job Id : ")).length());
				OarJobDesc jobD = new OarJobDesc(strs);
				jobs.put(jobId, jobD);
			}
		}
		return jobs;
	}

	/**
	 * @deprecated
	 * 
	 * @param user
	 * @param frontal
	 * @param oar_host
	 * @param startTime
	 * @param wallTime
	 * @param nodeCount
	 * @return
	 */
	public static OarJobDesc sub(String user, String frontal, String oar_host,
			String startTime, String wallTime, int nodeCount) {
		// get the previous reservations
		Vector<OarJobDesc> previous = getResaOf(user, frontal, oar_host);
		// execute the oarsub
		try {
			String[] cmdArray = { "ssh", 
					user + "@" + frontal,
					// Reservation cmd
					"ssh " + oar_host +" \"oarsub -r " +
					"\\\""+ startTime +  "\\\"" +
					" -l " +
					"walltime="+wallTime+  
					",nodes="+nodeCount+"\""
			};

			System.out.println(cmdArray[0] + " " + cmdArray[1] + " " + cmdArray[2]);

			Process child = Runtime.getRuntime().exec(cmdArray);

			InputStream cmdOut = child.getInputStream();
			InputStreamReader r = new InputStreamReader(cmdOut);
			BufferedReader in = new BufferedReader(r);

			InputStream cmdErr = child.getErrorStream();
			InputStreamReader rr = new InputStreamReader(cmdErr);
			BufferedReader inErr = new BufferedReader(rr);


			String line;
			// read the command error
			System.out.println("#ERROR#");
			while ((line = inErr.readLine()) != null)
				System.out.println(line);
			System.out.println("#");

			List<String> lines = new ArrayList<String>();

			while ((line = in.readLine()) != null)
				lines.add(line);
			if (child.waitFor() != 0)   // if the ssh failed 
				System.out.println("the ssh failed");
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}


		Vector<OarJobDesc> actual = getResaOf(user, frontal, oar_host);

		OarJobDesc myResa = null;

		// get my new reservation
		for (int ix=0; ix<actual.size(); ix++) {
			OarJobDesc j = (OarJobDesc)actual.get(ix);
			if (j.jobOwner.equals(user) && 
					!inMyPrevious(previous, j)) {
				myResa = j;
			}
		}

		return myResa;
	}

	/**
	 * @deprecated
	 * 
	 * @param v
	 * @param j
	 * @return
	 */
	public static boolean inMyPrevious(Vector<OarJobDesc> v,  OarJobDesc j) {
		for (int ix=0; ix<v.size(); ix++) {
			if (v.get(ix).jobId.equals(j.jobId))
				return true;
		}
		return false;
	}   

}
