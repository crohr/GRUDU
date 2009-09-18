/****************************************************************************/
/* OAR2 utility class                                                       */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: Oar2Scheduler.java,v 1.2 2007/11/19 15:12:25 dloureir Exp $
 * $Log: Oar2Scheduler.java,v $
 * Revision 1.2  2007/11/19 15:12:25  dloureir
 * Changing the version of the ssh library
 *
 * Revision 1.1  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.6  2007/10/09 13:41:23  dloureir
 * Moving the parsing of the oar history from the GanttChart class to the oar1 scheduler and modifications of the history command for all shceduler in the batchScheduler class
 *
 * Revision 1.5  2007/10/08 15:24:21  dloureir
 * Adding the name as a property of an OAR2 job
 *
 * Revision 1.4  2007/10/08 14:53:57  dloureir
 * Changing the structure of the grid job and allowing oar1 and oar2 jobs to have specific informations
 *
 * Revision 1.3  2007/10/05 13:55:55  aamar
 * Adding script parameter to OAR2 scheduler.
 *
 * Revision 1.2  2007/10/05 13:52:37  aamar
 * Adding script parameter to submission.
 *
 * Revision 1.1  2007/09/28 16:01:08  aamar
 * Initial revision
 *
 ****************************************************************************/

package com.izforge.izpack.panels.g5k_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;



/**
 * @author aamar
 *
 */
/**
 * @author aamar
 *
 */
public class Oar2Scheduler extends AbstractBatchScheduler {

    /* (non-Javadoc)
     * @see grid.api.BatchScheduler#delJob(com.trilead.ssh2.Connection, java.lang.String, java.lang.String)
     */
    public String[] delJob(Connection conn, String batch_host, String jobId, BatchSchedulerListener listener) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            Session sess = conn.openSession();

            sess.execCommand("ssh -o ConnectTimeout=5 " +
                    batch_host +
                    " \"oardel " +  jobId + "\"");

            InputStream stdout = new StreamGobbler(sess.getStdout());
            InputStream stderr = new StreamGobbler(sess.getStderr());

            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));

            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                lines.add("-- " + line);
                listener.addOutput(line);
            } // end while
            while ( true ) {
                String line = brerr.readLine();
                if (line == null)
                    break;
                lines.add("** "+ line);
                listener.addError(line);
            } // end while
            brerr.close();
            br.close();
            sess.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return lines.toArray(new String[lines.size()]);        
    }

    /* (non-Javadoc)
     * @see grid.api.BatchScheduler#getJobs(com.trilead.ssh2.Connection)
     */
    public GridJob[] getJobs(Connection conn, BatchSchedulerListener listener) {
        listener.addError("METHOD NOT IMPLEMENTED YET");
        return null;
    }

    /* (non-Javadoc)
     * @see grid.api.BatchScheduler#getJobs(com.trilead.ssh2.Connection, java.lang.String)
     */
    public GridJob[] getJobs(Connection conn, String batch_host, BatchSchedulerListener listener) {
        String [] stat_content = this.stat(conn, batch_host); 
        String content = "<oar2>";
        for (int ix=0; ix<stat_content.length; ix++) {
            content += stat_content[ix];
        } // end for
        content += "</oar2>";
        Document doc = null;
        GridJob [] allJobs  = null;
        try {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource in =
                new InputSource(new StringReader(content));
            doc = builder.parse(in);
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        catch (SAXException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            XPathFactory xpfactory = XPathFactory.newInstance();
            XPath xpath = xpfactory.newXPath();

            // get g5k clusters
            int count = ((Number) xpath.evaluate("count(/oar2/opt)",
                    doc, XPathConstants.NUMBER)).intValue();
            allJobs = new GridJob[count];
            for (int ix=0; ix < count; ix++) {
                Node node = (Node) xpath.evaluate("/oar2/opt[" + (ix+1) +"]", 
                        doc, XPathConstants.NODE);
                allJobs[ix] = new Oar2Job();
                String jobId   = xpath.evaluate("@Job_Id", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_ID, jobId);
                String owner   = xpath.evaluate("@owner", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_OWNER, owner);
                String state   = xpath.evaluate("@state", node);
                if (state != null && state.length()> 0)
                    state = state.substring(0, 1);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_STATE, state);
                String command = xpath.evaluate("@command", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_COMMAND, command);
                String cpuset_name = xpath.evaluate("@cpuset_name", node);
                allJobs[ix].setParameterValue(Oar2Job.KEY_OAR2_JOB_CPUSET_NAME,cpuset_name);
                
                String exit_code = xpath.evaluate("@exit_code", node);
                allJobs[ix].setParameterValue(Oar2Job.KEY_OAR2_JOB_EXIT_CODE,exit_code);
                
                String launchingDirectory = xpath.evaluate("@launchingDirectory", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_LAUNCHING_DIRECTORY,launchingDirectory);
                
                String message = xpath.evaluate("@message", node);
                allJobs[ix].setParameterValue(Oar2Job.KEY_OAR2_JOB_MESSAGE,message);
                
                String project = xpath.evaluate("@project", node);
                allJobs[ix].setParameterValue(Oar2Job.KEY_OAR2_JOB_PROJECT,project);
                
                String name = xpath.evaluate("@name", node);
                allJobs[ix].setParameterValue(Oar2Job.KEY_OAR2_JOB_NAME,name);
                
                String properties = xpath.evaluate("@properties", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_PROPERTIES,properties);
                
                String reservation = xpath.evaluate("@reservation", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_RESERVATION,reservation);
                
                String resubmit_job_id = xpath.evaluate("@resubmit_job_id", node);
                allJobs[ix].setParameterValue(Oar2Job.KEY_OAR2_JOB_RESUBMIT_JOB_ID,resubmit_job_id);
                
                String wantedResources = xpath.evaluate("@wanted_resources", node);
                allJobs[ix].setParameterValue(Oar2Job.KEY_OAR2_JOB_WANTED_RESOURCES,wantedResources);
                
                String queue = xpath.evaluate("@queue", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_QUEUE, queue);
                String jobType = xpath.evaluate("@jobType", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_TYPE, jobType);
                String wallTime = xpath.evaluate("@walltime", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_WALLTIME, wallTime);
                String subTime = xpath.evaluate("@submissionTime", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_SUBTIME, subTime);
                String startTime = xpath.evaluate("@startTime", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_STARTTIME, startTime);
                String schedTime = xpath.evaluate("@scheduledStart", node);
                allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME, schedTime);
                Object obj = xpath.evaluate("count(assigned_network_address)", node, XPathConstants.NUMBER);
                if (obj instanceof Number) {
                    int hostCount = ((Number)obj).intValue();
                    allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT, ((Number)obj).intValue() + "");
                    String [] hosts = new String[hostCount];
                    for (int jx=0; jx<hostCount; jx++) {
                        Node child = (Node) xpath.evaluate("assigned_network_address[" + (jx+1) +"]", 
                                node, XPathConstants.NODE);
                        hosts[jx] = child.getTextContent();
                    } // end for jx
                    allJobs[ix].setHostsFromArray(hosts);                    
                }
                obj = xpath.evaluate("count(types)", node, XPathConstants.NUMBER);
                if (obj instanceof Number) {
                    int n = ((Number)obj).intValue();
                    if (n == 1) {
                        Node type_child = (Node) xpath.evaluate("types[1]", 
                                node, XPathConstants.NODE);
                        allJobs[ix].setParameterValue(GridJob.KEY_GRID_JOB_QUEUE, type_child.getTextContent());
                    }
                }
            }
        }
        catch (Exception e) {
            listener.addError(e.getMessage());
        }

        return allJobs;
    }

    /* (non-Javadoc)
     * @see grid.api.BatchScheduler#getNodesStatus(com.trilead.ssh2.Connection, java.lang.String)
     */
    public int[] getNodesStatus(Connection conn, String batch_host, BatchSchedulerListener listener) {
        int[] result = new int[NODE_STATUS_COUNT + 1];
        for (int ix=0; ix<(NODE_STATUS_COUNT +1); ix++)
            //result[ix] = -1;
        	// TODO change this if it does not work well
        	result[ix] = 0;
        String cmd = "ssh -o ConnectTimeout=5 " + batch_host + 
        " oarnodes -X";
        String content = new String();
        try {
            Session sess = conn.openSession();
            sess.execCommand(cmd);

            InputStream stdout = new StreamGobbler(sess.getStdout());
            InputStream stderr = new StreamGobbler(sess.getStderr());

            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));
            String line = "";
            while ( true ) {
                line = br.readLine();
                if (line == null)
                    break;
                content += line;
                listener.addOutput(line);
            } // end while
            while ( true ) {
                line = brerr.readLine();
                if (line == null)
                    break;
                listener.addError(line);
            } // end while
            brerr.close();
            br.close();
            sess.close();

        }
        catch (Exception e) {
            listener.addError(e.getMessage());
        }

        Document doc = null;
        try {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource in =
                new InputSource(new StringReader(content));
            doc = builder.parse(in);
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        catch (SAXException e) {
            listener.addError(e.getMessage());
            return null;
        }
        catch (IOException e) {
            listener.addError(e.getMessage());
            return null;
        }

        HashMap<String, Node> hostsMap = new HashMap<String, Node>(); 
        try {
            XPathFactory xpfactory = XPathFactory.newInstance();
            XPath xpath = xpfactory.newXPath();

            Node rootNode = (Node) xpath.evaluate("/opt", 
                    doc, XPathConstants.NODE);
            NodeList children = rootNode.getChildNodes();
            for (int ix=0; ix<children.getLength(); ix++) {
                Node node = children.item(ix);
                hostsMap.put(node.getNodeName(), node);
            } // end for ix
            Iterator<Node> p = hostsMap.values().iterator();
            while (p.hasNext()) {
                Node hNode = p.next();
                children = hNode.getChildNodes();
                for (int ix=0; ix<children.getLength(); ix++) {
                    Node node = children.item(ix);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element elt = (Element)node;
                        if (elt.getNodeName().equals("jobs")) {
                            result[JOB_NODE]++;
                            break;
                        }
                        if (elt.getNodeName().equals("properties")) {
                            String state = elt.getAttribute("state");
                            for (int jx=0; jx<availableStates.length; jx++)
                                if (state.equals(availableStates[jx]))
                                    result[jx]++;
                        } 
                    } // end if ELEMENT_NODE
                }
            }
        }
        catch (Exception e) {
            listener.addError(e.getMessage());
        }

        for (int ix=0; ix<NODE_STATUS_COUNT; ix++)
            result[NODE_STATUS_COUNT] += result[ix];
        return result;
    }

    /* (non-Javadoc)
     * @see grid.api.BatchScheduler#stat(com.trilead.ssh2.Connection, java.lang.String)
     */
    public String[] stat(Connection conn, String batch_host, BatchSchedulerListener listener) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            Session sess = conn.openSession();
            sess.execCommand("ssh -o ConnectTimeout=5 " +
                    batch_host +
            " \"oarstat -Xf\"");

            InputStream stdout = new StreamGobbler(sess.getStdout());
            InputStream stderr = new StreamGobbler(sess.getStderr());

            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));
            String line;
            while ( true ) {
                line = br.readLine();
                if (line == null)
                    break;
                lines.add(line);
                listener.addOutput(line);
            } // end while
            while ( true ) {
                line = brerr.readLine();
                if (line == null)
                    break;
                listener.addError(line);
            } // end while
            brerr.close();
            br.close();
            sess.close();

        }
        catch (Exception e) {
            listener.addError(e.getMessage());
        }

        return lines.toArray(new String[lines.size()]);
    }

    /* (non-Javadoc)
     * @see diet.grid.api.BatchScheduler#sub(com.trilead.ssh2.Connection, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, diet.grid.api.util.BatchSchedulerListener)
     */
    public int sub(Connection conn, String user, 
            int nodeCount, String startTime, String wallTime, String queue,
            String script,
            BatchSchedulerListener listener) {
        listener.addError("METHOD NOT IMPLEMENTED YET");
        return -1;
    }

    /* (non-Javadoc)
     * @see diet.grid.api.BatchScheduler#sub(com.trilead.ssh2.Connection, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, diet.grid.api.util.BatchSchedulerListener)
     */
    public int sub(Connection conn, String user, 
            String batch_host,
            int nodeCount, String startTime, String wallTime, String queue, 
            String[] params, 
            String script, 
            BatchSchedulerListener listener) {
        String subCmd =                                  // Reservation cmd
            "ssh -o ConnectTimeout=10 " + batch_host + 
            " \"export PATH=/usr/local/bin:$PATH; oarsub -r " +
            "\\\""+ startTime +  "\\\"";
        if (queue != "default")
            subCmd += " -t " + queue;
        subCmd += " -l " +
            "/nodes="+nodeCount + "," +
            "walltime="+wallTime;
        if (params != null) {
            for (int ix=0; ix<params.length; ix++) {
                subCmd += " " + params[ix];
            }
        }
        if ((script != null) && (script.length() != 0)) 
                subCmd += " " + script;
        subCmd += "\"";

        int idJob = -1;
        String lines = new String();
        boolean substate = false;
        try {
            Session sess = conn.openSession();
            // Exec the submission command
            sess.execCommand(subCmd);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            InputStream stderr = new StreamGobbler(sess.getStderr());

            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));

            while (true)
            {
                String line = br.readLine();
                if (line == null)
                    break;
                if (line.contains("OK") &&
                        line.contains("Reservation valid")) substate = true;
                if (line.contains("OAR_JOB_ID=")){
                    int equalIndex = line.indexOf("=");
                    idJob = Integer.parseInt(line.substring(equalIndex+1, line.length()).trim());
                }

                lines += line;
                listener.addOutput(line);
            }

            while ( true ) {
                String line = brerr.readLine();
                if (line == null)
                    break;
                listener.addError(line);
            } // end while
            brerr.close();
            br.close();
            sess.close();
        }
        catch (IOException e) {
            listener.addError(e.getMessage());
        }

        if (substate)
            return idJob;
        else
            return -1;
    }

    /* (non-Javadoc)
     * @see diet.grid.api.BatchScheduler#sub(com.trilead.ssh2.Connection, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, diet.grid.api.util.BatchSchedulerListener)
     */
    public int sub(Connection conn, 
            String user, String batch_host, int nodeCount, 
            String startTime, String wallTime, String queue,
            String script,
            BatchSchedulerListener listener) {
        return sub(conn, user, batch_host, nodeCount, startTime, wallTime, queue, null, script, listener);
    }

    /* (non-Javadoc)
     * @see diet.grid.api.BatchScheduler#getID()
     */
    public String getID() {
        return "OAR2";
    }

    /**
     * Node states description as OAR2 display them
     */
    public final static String [] availableStates = {
        "Alive",
        "Jobs",
        "Suspected",
        "Dead",
        "Absent"
    };

    public Class getJobType() {
        return Oar2Job.class;
    }

    public ArrayList<GridJob> history(Connection connection, 
    		ArrayList<GridJob> aJobList,
    		String batch_host,
            Date startDate, Date endDate,
            BatchSchedulerListener listener) {
    	ArrayList<GridJob> jobsList = new ArrayList<GridJob>();
        try {
            Session sess = connection.openSession();
            sess.execCommand("ssh " +
                    batch_host +
                    " oarstat -h \\\"" + 
                    startDate + "," + 
                    endDate +" \\\" \"");
        }
        catch (Exception e) {
            listener.addError("oarstat " + e);
        }
        return jobsList;
    }

}
