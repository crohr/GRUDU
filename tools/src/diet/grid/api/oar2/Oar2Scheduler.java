/****************************************************************************/
/* OAR2 utility class                                                       */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: Oar2Scheduler.java,v 1.15 2007/12/07 09:44:44 dloureir Exp $
 * $Log: Oar2Scheduler.java,v $
 * Revision 1.15  2007/12/07 09:44:44  dloureir
 * When the reservation was done with the deploy or allow_classc_ssh type they were not well taken into account (types variable)
 *
 * Revision 1.14  2007/12/06 11:59:26  aamar
 * *** empty log message ***
 *
 * Revision 1.12  2007/11/23 14:43:50  dloureir
 * Removing the call to the Oar2StatClassicProperties.java because it was no more useful
 *
 * Revision 1.11  2007/11/23 14:40:28  dloureir
 *  Implementing the recuperation of the oar2 version and the recuperation of some information for the classic output.
 *
 * Revision 1.10  2007/11/19 15:15:45  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.9  2007/11/07 10:39:24  dloureir
 * Correcting the call to the addOutput method of the listener
 *
 * Revision 1.8  2007/11/06 14:58:37  dloureir
 * Bug correction concerning the OAR properties
 *
 * Revision 1.7  2007/11/06 11:21:25  aamar
 * Adding for oarstat operation in OAR2 to choose between XML or classic output (via .diet/oar2.properties)
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

package diet.grid.api.oar2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;
import diet.grid.api.AbstractBatchScheduler;
import diet.grid.api.GridJob;
import diet.grid.api.util.BatchSchedulerListener;

/**
 * @author aamar
 *
 */

public class Oar2Scheduler extends AbstractBatchScheduler {

    /**
     * OAR2 properties 
     */
    private Map<String, String> myProperties;

    /**
     * Default constructor. Create an OAR2 scheduler and initialize its properties if the configuration
     * file is found
     */
    public Oar2Scheduler() {
        this.myProperties = new HashMap<String, String>();
        initProperties();
    }

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
                //listener.addOutput(line);
            } // end while
            while ( true ) {
                String line = brerr.readLine();
                if (line == null)
                    break;
                lines.add("** "+ line);
                listener.addError("delJob " + batch_host + " ... " + line);
            } // end while
            brerr.close();
            br.close();
            sess.close();
        }
        catch (Exception e) {
        	listener.addError("delJob " + batch_host + " ... " + e.toString());
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
        String [] stat_content = this.stat(conn, batch_host,listener);
        String stat_method = getOAR2StatMethod(conn, batch_host, listener);
//        if (this.myProperties.get(Oar2Properties.stat_method) != null && 
//        		this.myProperties.get(Oar2Properties.stat_method).equals(Oar2Properties.Cst.classic_stat)) {
//            return this.getJobsClassic(stat_content, listener);
//        }
//        else {
//            return this.getJobsXML(stat_content, listener);
//        }
        if(stat_method.equalsIgnoreCase(Oar2Properties.Cst.classic_stat))return this.getJobsClassic(stat_content, listener);
        else return this.getJobsXML(stat_content, listener);
    }
    
    private String getOAR2StatMethod(Connection conn, String batch_host, BatchSchedulerListener listener){
    	String method = Oar2Properties.Cst.xml_stat;
    	try{
    		Session sess = conn.openSession();
    		sess.execCommand("ssh -ttto ConnectTimeout=5 " + batch_host + " \" oarsub -V\"");

    		InputStream stdout = new StreamGobbler(sess.getStdout());
    		InputStream stderr = new StreamGobbler(sess.getStderr());

    		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
    		BufferedReader brerr = new BufferedReader(new InputStreamReader(stderr));
    		String line;
    		String oar_line = "";
    		while ( true ) {
    			line = br.readLine();
    			
    			if (line == null)
    				break;
    			else{
    				if(line.toLowerCase().startsWith("oar")){
    					oar_line = line;
    					break;
    				}
    			}
    			//listener.addOutput(line);
    		} // end while
    		while ( true ) {
    			line = brerr.readLine();
    			
    			if (line == null )
    				break;
    			//listener.addError(line);
    		} // end while
    		brerr.close();
    		br.close();
    		sess.close();
    		if(oar_line.contains("2.2.7"))  method = Oar2Properties.Cst.xml_stat;
    		else method = Oar2Properties.Cst.classic_stat;
    	}
    	catch(Exception e){
    		listener.addError("getOAR2StatMethod " + batch_host + " ... " + e.getLocalizedMessage());
    	}
    	return method;
    }

    private GridJob[] getJobsXML(String [] stat_content, BatchSchedulerListener listener) { 
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
        	listener.addError("getJobsXML "+e.toString());
            return null;
        }
        catch (SAXException e) {
        	listener.addError("getJobsXML "+e.toString());
            return null;
        }
        catch (IOException e) {
        	listener.addError("getJobsXML "+e.toString());
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
            listener.addError("getJobsXML "+e.getMessage());
        }

        return allJobs;
    }

    private GridJob[] getJobsClassic(String [] stat_content, BatchSchedulerListener listener) {
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for (int ix=0; ix<stat_content.length; ix++) {
            String line = stat_content[ix];
            if (line.matches("^Job_Id:.+")) {
                //listener.addOutput(line);
                indexes.add(new Integer(ix));
            }
        } // end for
        indexes.add(new Integer(stat_content.length));
        GridJob  [] allJobs = new Oar2Job[indexes.size()-1];
        for (int ix=0; ix<(indexes.size()-1); ix++) {
            ArrayList<String> list = new ArrayList<String>();
            for (int jx = indexes.get(ix); jx < indexes.get(ix+1); jx++)
                list.add(stat_content[jx]);
            // stat_content.subList(indexes.get(ix), indexes.get(ix+1));
            GridJob job = new Oar2Job();
            boolean queueIsSetWithTheType = false;
            for (String line : list) {
            	
                line = line.trim();
                if (line.startsWith("Job_Id")) {
                    String jobId = line.substring(line.indexOf(':')+1).trim();
                    job.setParameterValue(GridJob.KEY_GRID_JOB_ID, jobId);
                    
                    job.setId(jobId);
                }
                if(line.indexOf('=') != -1 ){
                	String param = line.substring(0, line.indexOf('=')).trim();
                	String value = line.substring(line.indexOf('=')+1).trim();
                	if(param.equalsIgnoreCase("owner")) job.setParameterValue(GridJob.KEY_GRID_JOB_OWNER, value);
                	if(param.equalsIgnoreCase("state")) job.setParameterValue(GridJob.KEY_GRID_JOB_STATE, value);
                	if(param.equalsIgnoreCase("command")) job.setParameterValue(GridJob.KEY_GRID_JOB_COMMAND, value);
                	if(param.equalsIgnoreCase("cpuset_name")) job.setParameterValue(Oar2Job.KEY_OAR2_JOB_CPUSET_NAME, value);
                	if(param.equalsIgnoreCase("exit_code"))job.setParameterValue(Oar2Job.KEY_OAR2_JOB_EXIT_CODE,value);
                	if(param.equalsIgnoreCase("launchingDirectory")) job.setParameterValue(GridJob.KEY_GRID_JOB_LAUNCHING_DIRECTORY,value);
                	if(param.equalsIgnoreCase("message")) job.setParameterValue(Oar2Job.KEY_OAR2_JOB_MESSAGE,value);
                	if(param.equalsIgnoreCase("project")) job.setParameterValue(Oar2Job.KEY_OAR2_JOB_PROJECT, value);
                	if(param.equalsIgnoreCase("name")) job.setParameterValue(Oar2Job.KEY_OAR2_JOB_NAME,value);
                	if(param.equalsIgnoreCase("properties")) job.setParameterValue(GridJob.KEY_GRID_JOB_PROPERTIES,value);
                	if(param.equalsIgnoreCase("reservation")) job.setParameterValue(GridJob.KEY_GRID_JOB_RESERVATION,value);
                	if(param.equalsIgnoreCase("resubmit_job_id")) job.setParameterValue(Oar2Job.KEY_OAR2_JOB_RESUBMIT_JOB_ID,value);
                	if(param.equalsIgnoreCase("types")){
                		job.setParameterValue(GridJob.KEY_GRID_JOB_QUEUE, value);
                		queueIsSetWithTheType = true;
                	}
                	if(param.equalsIgnoreCase("wanted_resources")) job.setParameterValue(Oar2Job.KEY_OAR2_JOB_WANTED_RESOURCES,value);
                	if(param.equalsIgnoreCase("queue")){
                		if(!queueIsSetWithTheType)job.setParameterValue(GridJob.KEY_GRID_JOB_QUEUE, value);
                	}
                	if(param.equalsIgnoreCase("jobType")) job.setParameterValue(GridJob.KEY_GRID_JOB_TYPE, value);
                	if(param.equalsIgnoreCase("walltime")) job.setParameterValue(GridJob.KEY_GRID_JOB_WALLTIME, value);
                	if(param.equalsIgnoreCase("submissionTime")) job.setParameterValue(GridJob.KEY_GRID_JOB_SUBTIME, value);
                	if(param.equalsIgnoreCase("startTime")) job.setParameterValue(GridJob.KEY_GRID_JOB_STARTTIME, value);
                	if(param.equalsIgnoreCase("scheduledStart")) job.setParameterValue(GridJob.KEY_GRID_JOB_SCHEDTIME, value);
                	if(param.equalsIgnoreCase("assigned_hostnames")){
                		StringTokenizer tokenizer = new StringTokenizer(value,"+");
                		ArrayList<String> hosts = new ArrayList<String>();
                		while(tokenizer.hasMoreTokens()){
                			hosts.add(tokenizer.nextToken());
                		}
                		String[] myHosts = hosts.toArray(new String[hosts.size()]);
                		job.setHostsFromArray(myHosts);
                	}
                }
//                if (line.indexOf('=') != -1) {
//                        String param = line.substring(0, line.indexOf('=')).trim();
//                        String value = line.substring(line.indexOf('=')+1).trim();
//                        //if (properties.contains(param)) {
//                        //    String key = Oar2StatClassicProperties.getProperty(param);
//                            job.setParameterValue(key, value);
//                        //}
//                }
                if (line.startsWith("wanted_resources") &&
                        line.matches(".*network_address=(\\d+).*")) {
                    String resCount = 
                        line.replaceFirst(".*network_address=(\\d+).*",  "$1");
                    job.setParameterValue(GridJob.KEY_GRID_JOB_RESOURCES_COUNT, 
                            resCount);
                }
                
            }
            allJobs[ix] = job;
        } // end for
        return allJobs;
    }
    /* (non-Javadoc)
     * @see grid.api.BatchScheduler#getNodesStatus(com.trilead.ssh2.Connection, java.lang.String)
     */
    public int[] getNodesStatus(Connection conn, String batch_host, BatchSchedulerListener listener) {
    	String stat_method = getOAR2StatMethod(conn, batch_host, listener);
    	if(stat_method.equalsIgnoreCase(Oar2Properties.Cst.xml_stat)) return getNodesStatusXML(conn, batch_host, listener);
    	else{
    		return getNodesStatusClassic(conn, batch_host, listener);
    	}
    }
    
    public int[] getNodesStatusClassic(Connection conn, String batch_host, BatchSchedulerListener listener){
    	int[] result = new int[NODE_STATUS_COUNT + 1];
    	for (int ix=0; ix<(NODE_STATUS_COUNT +1); ix++)
            //result[ix] = -1;
            // TODO change this if it does not work well
            result[ix] = 0;
        String cmd = "ssh -o ConnectTimeout=5 " + batch_host + 
        " oarnodes";
        ArrayList<String> lines = new ArrayList<String>(); 
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
                lines.add(line);
                //listener.addOutput(line);
            } // end while
            while ( true ) {
                line = brerr.readLine();
                if (line == null)
                    break;
                listener.addError("getNodesStatusClassic " + batch_host + " ... " +line);
            } // end while
            brerr.close();
            br.close();
            sess.close();

        }
        catch (Exception e) {
            listener.addError("getNodesStatusClassic " + batch_host + " ... " +e.getMessage());
        }
        Iterator<String> iter = lines.iterator();
        boolean nodeFind = false;
        while(iter.hasNext()){
        	String line = iter.next();
        	if(line.matches("^[a-z]+\\-[0-9]+\\.[a-zA-Z]+\\.grid5000\\.fr$")){
        		nodeFind = true;
        	}
        	if(nodeFind){
        		if (line.toLowerCase().trim().contains("jobs : ")) {
        			result[JOB_NODE]++;
        			nodeFind = false;
        		}
        		if(line.toLowerCase().trim().contains("state : ")){
        			for (int jx=0; jx<availableStates.length; jx++){
        				if (line.contains(availableStates[jx])){
        					result[jx]++;
        					nodeFind = false;
        					break;
        				}
        			} 
        		}
        	}
        }
        for (int ix=0; ix<NODE_STATUS_COUNT; ix++) result[NODE_STATUS_COUNT] += result[ix];
        return result;
    }
    
    public int[] getNodesStatusXML(Connection conn, String batch_host, BatchSchedulerListener listener){
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
                //listener.addOutput(line);
            } // end while
            while ( true ) {
                line = brerr.readLine();
                if (line == null)
                    break;
                listener.addError("getNodesStatusXML " + batch_host +  " ... " + line);
            } // end while
            brerr.close();
            br.close();
            sess.close();

        }
        catch (Exception e) {
            listener.addError("getNodesStatusXML " + batch_host +  " ... " + e.getMessage());
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
        	listener.addError("getNodesStatusXML " + batch_host +  " ... " + e.toString());
            return null;
        }
        catch (SAXException e) {
            listener.addError("getNodesStatusXML " + batch_host +  " ... " + e.toString());
            return null;
        }
        catch (IOException e) {
            listener.addError("getNodesStatusXML " + batch_host +  " ... " + e.toString());
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
            listener.addError("getNodesStatusXML " + batch_host +  " ... " + e.toString());
        }

        for (int ix=0; ix<NODE_STATUS_COUNT; ix++) result[NODE_STATUS_COUNT] += result[ix];
        return result;
    }

    /* (non-Javadoc)
     * @see grid.api.BatchScheduler#stat(com.trilead.ssh2.Connection, java.lang.String)
     */
    public String[] stat(Connection conn, String batch_host, BatchSchedulerListener listener) {
    	String stat_method = getOAR2StatMethod(conn, batch_host, listener);
//        if (this.myProperties.get(Oar2Properties.stat_method) != null && 
//        		this.myProperties.get(Oar2Properties.stat_method).equals(Oar2Properties.Cst.classic_stat)) {
//            listener.addOutput("Classic STAT");
//            return this.statClassic(conn, batch_host, listener);
//        }
//        else {
//            return this.statXML(conn, batch_host, listener);
//        }
    	if(stat_method.equalsIgnoreCase(Oar2Properties.Cst.classic_stat)) {
    		listener.addOutput("Classic STAT");
    		return this.statClassic(conn, batch_host, listener);
    	}
    	else{
    		listener.addOutput("XML STAT");
    		return this.statXML(conn, batch_host, listener);
    	}
    }

    private String[] statXML(Connection conn, String batch_host, BatchSchedulerListener listener) {
        String cmd = "ssh -o ConnectTimeout=5 " + batch_host + " \"oarstat -Xf\"";
        return this.statCommon(conn, cmd, listener);
    }

    private String[] statClassic(Connection conn, String batch_host, 
            BatchSchedulerListener listener) {
        String cmd = "ssh -o ConnectTimeout=5 " + batch_host + " \"oarstat -f\"";
        return statCommon(conn, cmd, listener);
    }

    private String[] statCommon(Connection conn, String cmd, BatchSchedulerListener listener) {
        ArrayList<String> lines = new ArrayList<String>();
        try {
            Session sess = conn.openSession();
            sess.execCommand(cmd);

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
                //listener.addOutput(line);
            } // end while
            while ( true ) {
                line = brerr.readLine();
                if (line == null)
                    break;
                listener.addError("stat_common " + cmd +  " ... " + line);
            } // end while
            brerr.close();
            br.close();
            sess.close();

        }
        catch (Exception e) {
            listener.addError("stat_common " + cmd +  " ... " + e.toString());
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
                listener.addError("sub " + batch_host +  " ... " + line);
            } // end while
            brerr.close();
            br.close();
            sess.close();
        }
        catch (IOException e) {
            listener.addError("sub " + batch_host +  " ... " + e.toString());
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

    /**
     * Initialize OAR2 properties
     */
    private void initProperties() {
        File cfgFile = new File(System.getProperty("user.home") + "/.diet/" + "oar2.properties");
        if (cfgFile.exists()) {
            Document doc = getDoc(cfgFile);
            if (doc != null) {
                try {
                    XPathFactory xpfactory = XPathFactory.newInstance();
                    XPath xpath = xpfactory.newXPath();
                    
                    int count = ((Number) xpath.evaluate("count(/oar2/property)",
                            doc, XPathConstants.NUMBER)).intValue();
                    for (int ix=0; ix < count; ix++) {
                        Node node = (Node) xpath.evaluate("/oar2/property[" + (ix+1) +"]", doc, 
                                XPathConstants.NODE);
                        String propertyName   = xpath.evaluate("@name", node);
                        String propertyValue  = xpath.evaluate("@value", node);
                        this.myProperties.put(propertyName, propertyValue);

                    }
                }
                catch (Exception e) {
                	e.printStackTrace();
                }
            }
        }
    }
    /**
     * Get the XML document of a file
     * @param file XML file
     * @return the XML document or null
     */
    private static Document getDoc(File file) {
        try {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource in =
                new InputSource(new FileInputStream(file));
            Document doc = builder.parse(in);
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    } // end getDoc

}
