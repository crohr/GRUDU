/****************************************************************************/
/* G5K reservation window                                                   */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: ResaDlg.java,v 1.28 2007/12/07 08:03:14 dloureir Exp $
 * $Log: ResaDlg.java,v $
 * Revision 1.28  2007/12/07 08:03:14  dloureir
 * Adding the connection used to get information from Grid'5000 in order to get the time on the grid in the TimePanel
 *
 * (part of the resolution of the bug #47)
 *
 * Revision 1.27  2007/12/06 14:36:01  dloureir
 * Correction concerning the problem appearing if the preferred access point is not used to log into grid'5000 because it is not reachable.
 *
 * The calendar that was returning the date used to set the starting time as if the user was in grid'5000 is not constructed if there is a problem when connecting to the preferred access frontend, so an exception is raised when the calendar is used.
 * In this case (in the catch block) a calendar is constructed and manually setted to the grid'5000 time using the difference between the local time, the GMT time and the grid'5000 time.
 *
 * This solution should be a emergency solution because the grid'5000 time is always better for the reservation that the one obtained by this method.
 *
 * Revision 1.26  2007/11/20 17:19:56  dloureir
 * Correction of two bugs :
 * - The variables are well retrieved when doing an oar nodes command for the whole grid
 * - The correction of the timezone in the reservation dialog frame led to a bug when reserving some nodes (there were no data anymore but integer), and it is fixed
 *
 * Revision 1.25  2007/11/19 17:32:13  dloureir
 * The reservation start time is now based on the time on the preferred access frontend
 *
 * Revision 1.24  2007/11/19 15:16:19  dloureir
 * - Changing the version of the ssh library
 * - Correction of the bug concerning the hour of the reservation
 * - Correction of the Bug concerning the remote copy of the WfLogSrv jar file
 * - Parallelization of some OAR commands
 *
 * Revision 1.23  2007/10/24 12:51:05  dloureir
 * Removing some calls to the G5KCluster class by the corresponding calls to the G5KSite class
 *
 * Revision 1.22  2007/10/17 15:51:44  dloureir
 * Adding a little "hack" for the support of the classic ssh usage with OAR2
 *
 * Revision 1.21  2007/09/28 16:02:44  aamar
 * Updating GRUDU with OAR2 & new Grid5000 architecture
 *
 * Revision 1.20  2007/07/12 14:55:30  dloureir
 * Some javadoc
 *
 * Revision 1.19  2007/06/25 09:52:45  dloureir
 * Adding a feature : the user can now provide a scriptto launch when the reservation comes up (the path should be absolute)
 *
 * Revision 1.18  2007/06/22 11:53:50  dloureir
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
 * Revision 1.17  2007/03/08 12:14:35  dloureir
 * Package file for the javadoc and deletion of the printStackTrace for a new log function
 *
 * Revision 1.16  2007/03/07 14:13:16  dloureir
 * Adding javadoc to GRUDU
 *
 * Revision 1.15  2007/02/28 12:40:07  dloureir
 * Correcting some bugs concerning logging
 *
 * Revision 1.14  2007/02/27 14:10:06  dloureir
 * Adding sol cluster of sophia and removing some print outs
 *
 * Revision 1.13  2007/02/22 15:05:26  aamar
 * Adding queues for reservation. This version is a recovery from .# emacs
 * files. The original was deleted by mistake due to conflicts.
 *
 ****************************************************************************/
package diet.gridr.g5k.gui;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import diet.gridr.g5k.util.*;
import diet.logging.LoggingManager;

/**
 * G5K reservation window
 *
 * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class ResaDlg extends JDialog implements ActionListener {
    /**
     * serialVersionUID defines the version for the serialization of
     * this object
     */
    private static final long serialVersionUID = -7579090401471184760L;
    /**
     * ok button
     */
    private JButton ok_btn;
    /**
     * cancel button
     */
    private JButton cancel_btn;
    /**
     * spinner for the hour
     */
    private JSpinner startHourSpinner;
    /**
     * spinner for the minute
     */
    private JSpinner startMinuteSpinner;
    /**
     * spinner for the second
     */
    private JSpinner startSecondSpinner;
    /**
     * spinner for the hour
     */
    private JSpinner hourSpinner;
    /**
     * Spinner for the minute
     */
    private JSpinner minuteSpinner;

    /**
     * OAR queue JComboBox
     */
    private JComboBox queueCombo;
    /**
     * reservation
     */
    private G5kReservation myResa;  //  @jve:decl-index=0:
    /**
     * Array of reservation views
     */
    private ResaView[] sitesResaViews;
    /**
     * array for the nodes reserved for all the clusters
     */
    private int[] reservedNodes = null;
    /**
     * status?
     */
    private int status;
    /**
     * Date chooser for the start date of the reservation
     */
    JDateChooser myDateChooser = null;
    
    /**
     * OARGridSub behaviour JCheckBox
     */
    private JCheckBox oargridsubCheckbox = null;
    
    /**
     * properties Panel
     */
    private PropertiesPanel propertiesPanel = null;
    
    /**
     * JCheckBox for the execution of a script/executable for the reservation
     */
    private JCheckBox scriptCheckBox = null;
    
    /**
     * JLabel for the execution of a script/executable for the reservation
     */
    private JLabel scriptLabel = null;
    
    /**
     * TextField for the absolute path to the script/executable to launch
     */
    private JTextField scriptField = null;
    /**
     * Connection used to get the time on Grid'5000
     */
    private Connection myConnection = null;
    

    /**
     * Class that represents the panel of buttons
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class BtnPanel extends JPanel {
        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
        private static final long serialVersionUID = 2610887206613268990L;
        /**
         * Default constructor
         *
         */
        public BtnPanel() {
            super();
            setLayout(new FlowLayout());
            ok_btn = new JButton("OK");
            cancel_btn = new JButton("Cancel");
            add(ok_btn);
            add(cancel_btn);
            LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "BtnPanel", "Button Panel initialized");
        }
    }
    /**
     * Class representing the panel for the time parameter
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class TimeParamPanel extends JPanel {
        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
        private static final long serialVersionUID = -7127020513296198050L;

        /**
         * Default constructor
         *
         */
        public TimeParamPanel() {
            super();
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;

            c.weightx = 0.25;
            c.insets = new Insets(5, 5, 5, 5);
            Calendar cal = null;
            Date localDate = null;
            // on cherche a récupérer le temps de grid'5000 avec la frontale d'acces utilisée
            try {
                /* Create a session */

                Session sess = myConnection.openSession();
                sess.execCommand("date +\"%s\"");

                InputStream stdout = new StreamGobbler(sess.getStdout());

                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                while (true) {
                    String line = br.readLine();
                    System.out.println(line);
                    cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
//                    localDate = new Date(Long.parseLong(line)*1000);
                    cal.setTimeInMillis(Long.parseLong(line)*1000);
                    break;
                }
                sess.close();
            }
            catch(Exception e){
            	// apparemment il y a eu un problème de connexion ou autre donc au lieu de se fixer
            	// par rapport au temps de grid'5000 on se fixe par rapport au temps local 
            	//e.printStackTrace();
            	// calcul du temps actuel dans notre timezone local
            	cal = GregorianCalendar.getInstance();
            	// un petit peu de log
            	System.out.println(cal.getTime() +" in the following TimeZone " + cal.getTimeZone().getDisplayName());
            	// timezone locale
            	TimeZone tzLocal = cal.getTimeZone();
            	// timezone de grid'5000
            	TimeZone tzG5K = TimeZone.getTimeZone("Europe/Paris");
            	// nombre de millisecondes de différence du temps local avec GMT 
            	int offsetLocal = tzLocal.getOffset((new Date()).getTime());
            	// nombre de millisecondes de différence du temps de grid'5000 avec GMT
            	int offsetG5k = tzG5K.getOffset((new Date()).getTime());
            	// un peu de log ...
            	System.out.println("jet lag with the GMT TimeZone of the local time : " + offsetLocal/3600000);
            	System.out.println("jet lag with the GMT TimeZone of the G5K time: " + offsetG5k/3600000);
            	// on supprime la différence du temps local par rapport à GMT et on ajoute la différence de temps entre GMT et grid'5000 
            	cal.add(Calendar.MILLISECOND, -offsetLocal + offsetG5k);
            	// encore un peu de log ...
            	System.out.println(cal.getTime() +" in the G5K TimeZone");
            }
            
//            cal = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
//            System.out.println(cal.getTimeZone().getDisplayName());
            localDate = new Date(cal.getTimeInMillis());
            System.out.println(localDate);
            myDateChooser = new JDateChooser(localDate);
            myDateChooser.setDateFormatString("dd/MM/yyyy");
            Dimension dim = new Dimension(120, 20);
            myDateChooser.setSize(dim);
            myDateChooser.setPreferredSize(dim);
            
//            SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss z");
//            
//            time_format.setTimeZone(cal.getTimeZone());
//            System.out.println(time_format.format(localDate));
//            try{
//            	localDate = time_format.parse(time_format.format(localDate));
//            	System.out.println(localDate);
//            }
//            catch(Exception e){
//            	e.printStackTrace();
//            }
            SpinnerNumberModel hourModel = new SpinnerNumberModel(1,0,23,1);
            startHourSpinner   = new JSpinner(hourModel);

            startHourSpinner.setValue(cal.get(Calendar.HOUR_OF_DAY));
            
//            startHourSpinner.setEditor(new JSpinner.DateEditor(startHourSpinner, "HH"));
//            startHourSpinner.setValue(localDate);
            SpinnerNumberModel minutesModel = new SpinnerNumberModel(1,0,60,1);
            startMinuteSpinner = new JSpinner(minutesModel);
            startMinuteSpinner.setValue(cal.get(Calendar.MINUTE));
//            startMinuteSpinner.setEditor(new JSpinner.DateEditor(startMinuteSpinner, "mm"));
//            startMinuteSpinner.setValue(myDateChooser.getDate());
            SpinnerNumberModel secondsModel = new SpinnerNumberModel(1,0,60,1);
            
            startSecondSpinner = new JSpinner(secondsModel);
            startSecondSpinner.setValue(cal.get(Calendar.SECOND));
//            startSecondSpinner.setEditor(new JSpinner.DateEditor(startSecondSpinner, "ss"));
//            startSecondSpinner.setValue(myDateChooser.getDate());

            SpinnerNumberModel hModel = new SpinnerNumberModel(1, 0, 96, 1);
            hourSpinner = new JSpinner(hModel);

            SpinnerNumberModel mModel = new SpinnerNumberModel(0, 0, 60, 1);
            minuteSpinner = new JSpinner(mModel);

            queueCombo = new JComboBox(KadeployUtil.queues);
            
            oargridsubCheckbox = new JCheckBox();
            
            scriptCheckBox = new JCheckBox();
            scriptCheckBox.setSelected(false);
            scriptCheckBox.addItemListener(new ItemListener(){

				/* (non-Javadoc)
				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
				 */
				public void itemStateChanged(ItemEvent e) {
					// TODO Auto-generated method stub
					scriptField.setEditable(scriptCheckBox.isSelected());
				}
            	
            });
            scriptLabel = new JLabel("Script/Executable to launch");
            
            scriptField = new JTextField();
            scriptField.setEditable(scriptCheckBox.isSelected());

            // add the spinners to the panel
            c.gridx = 0;
            c.gridy = 1;
            add(new JLabel("Starting date"), c);
            c.gridx = 1;
            add(myDateChooser, c);

            c.gridx = 0;
            c.gridy = 2;
            add(new JLabel("Starting hour"), c);
            c.gridx = 1;
            add(startHourSpinner, c);

            c.gridx = 0;
            c.gridy = 3;
            add(new JLabel("Starting minute"), c);
            c.gridx = 1;
            add(startMinuteSpinner, c);

            c.gridx = 0;
            c.gridy = 4;
            add(new JLabel("Starting second"), c);
            c.gridx = 1;
            add(startSecondSpinner, c);

            c.gridx = 0;
            c.gridy = 5;
            add(new JLabel("Hour"), c);
            c.gridx = 1;
            add(hourSpinner, c);

            c.gridx = 0;
            c.gridy = 6;
            add(new JLabel("Minutes"), c);
            c.gridx = 1;
            add(minuteSpinner, c);

            c.gridx = 0;
            c.gridy = 7;
            add(new JLabel("Queue"), c);
            c.gridx = 1;
            add(queueCombo, c);

            c.gridx = 0;
            c.gridy = 8;
            add(new JLabel("OARGridSub behaviour"), c);
            c.gridx = 1;
            add(oargridsubCheckbox, c);
            
            c.gridx = 1;
            c.gridy = 9;
            add(scriptCheckBox,c);
            c.gridx = 0;
            add(scriptLabel,c);
            
            c.gridx = 0;
            c.gridy = 10;
            c.gridwidth = 2;
            add(scriptField,c);
            
            setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "TimeParamPanel", "TimeParamPanel initialized");
        }
    }

    /**
     * Class representing the map with the different reservation views
     *
     * @author Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    public class ResaStep01Panel extends JPanel {
        /**
         * serialVersionUID defines the version for the serialization of
         * this object
         */
        private static final long serialVersionUID = 4225076189709099103L;
        /**
         * Background image
         */
        private Image background;
        /**
         * Coordinates of the different resa views
         */
        private Point [] coords =  {
            new Point(330, 250), // lyon
            new Point(410, 350), // nice
            new Point(90, 130), // rennes
            new Point(220, 110), // orsay
            new Point(120, 290), // bordeaux
            new Point(180, 390), // toulouse
            new Point(240, 20), // lille
            new Point(390, 130), // nancy
            new Point(370, 290) // grenoble
        };

        /**
         * Default constructor
         *
         */
        public ResaStep01Panel() {
            this.background = null;
            java.net.URL url = getClass().getResource("/resources/gridr/g5k/france.png");
            try {
                this.background =
                    javax.imageio.ImageIO.read(url);
            }
            catch(Exception e){/*handled in paintComponent()*/
                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ResaStep01Panel", "Image loading failed : " + "/resources/gridr/g5k/france.png");
                LoggingManager.log(Level.WARNING, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ResaStep01Panel",e);
            }
            setSize(491, 453);
            setLayout(null);
            sitesResaViews = new ResaView[G5kSite.getSitesNumber()];
            reservedNodes = new int[G5kSite.getSitesNumber()];
            for(int i = 0 ; i < reservedNodes.length; i++){
                reservedNodes[i] = 0;
            }
            Dimension dim = new Dimension(80,30);

            for(int i = 0 ; i < sitesResaViews.length ; i ++){
                //int[] clustersIndexes = G5kSite.getClustersIndexesForIndex(i);
                boolean siteEnabled = Config.isSiteEnable(i);
                if(siteEnabled){
                    sitesResaViews[i] = new ResaView(i, reservedNodes, this);
                    sitesResaViews[i].setSize(dim);
                    sitesResaViews[i].setPreferredSize(dim);
                    sitesResaViews[i].setMinimumSize(dim);
                    sitesResaViews[i].setVisible(true);
                    sitesResaViews[i] .setLocation((int)coords[i].getX(), (int)coords[i].getY());
                    sitesResaViews[i].setEnabled(true);
                    add(sitesResaViews[i]);
                }
            }
            LoggingManager.log(Level.FINER, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ResaStep01Panel", "ResaStep01Panel initialized");
        }

        /**
         * Method that paints the components of the panel (e.g. when updating, etc ...)
         *
         * @param g graphics where the panel will be rendered
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(this.background != null)
                g.drawImage(this.background,
                            0, 0,
                            491, 453,
                            this);
            Dimension dim = new Dimension(80,30);
            for(int i = 0 ; i < sitesResaViews.length ; i ++){
                int[] clustersIndexes = G5kSite.getClustersIndexesForIndex(i);
                boolean siteEnabled = Config.isSiteEnable(i);
                if(siteEnabled){
                    sitesResaViews[i].setSize(dim);
                    sitesResaViews[i].setPreferredSize(dim);
                    sitesResaViews[i].setMinimumSize(dim);
                    sitesResaViews[i].setVisible(true);
                    sitesResaViews[i].setLocation((int)coords[i].getX(), (int)coords[i].getY());
                    sitesResaViews[i].setFocusable(true);
                }
            }
        }
    }

    /**
     * Method used to manage the events for this panel
     *
     * @param event an event
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.ok_btn) {
            this.myResa = new G5kReservation();
            //
            LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Realized reservation :");
            for (int ix=0; ix<G5kSite.getSitesNumber(); ix++) {
                if (Config.isSiteEnable(ix)) {
                    LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", G5kConstants.clusters.get(ix).getName() +" : "+ reservedNodes[ix]);
                    this.myResa.setRequestedNodeCount(ix, reservedNodes[ix]);
                    this.myResa.setChosenCluster(ix, this.sitesResaViews[ix].getChosenCluster());
                }
            }

//            if(	(this.startHourSpinner.getValue() instanceof Date) &&
//                (this.startMinuteSpinner.getValue() instanceof Date) &&
//                (this.startSecondSpinner.getValue() instanceof Date)) {
                // Get the time
                Date d = myDateChooser.getDate();
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
                // get the date day, month, year
                cal.setTime(d);
                this.myResa.setYear(cal.get(Calendar.YEAR));
                this.myResa.setMonth(cal.get(Calendar.MONTH)+1);
                this.myResa.setDay(cal.get(Calendar.DAY_OF_MONTH));
                // get the hour
//                d = (Date)this.startHourSpinner.getValue();
//                cal.setTime(d);
//                this.myResa.setHour(cal.get(Calendar.HOUR_OF_DAY));
                this.myResa.setHour((Integer)this.startHourSpinner.getValue());
                // get the minute
//                d = (Date)this.startMinuteSpinner.getValue();
//                cal.setTime(d);
//                this.myResa.setMinute(cal.get(Calendar.MINUTE));
                this.myResa.setMinute((Integer)startMinuteSpinner.getValue());
                // get the second
//                d = (Date)this.startSecondSpinner.getValue();
//                cal.setTime(d);
//                this.myResa.setSecond(cal.get(Calendar.SECOND));
                this.myResa.setSecond((Integer)startSecondSpinner.getValue());
                LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed","Reservation start time : " +
                		cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) +
                		" :: " + (Integer)this.startHourSpinner.getValue() + ":" + (Integer)startMinuteSpinner.getValue() + ":" + (Integer)startSecondSpinner.getValue()
                		);
                if ((hourSpinner.getValue() instanceof Integer) &&
                    (minuteSpinner.getValue() instanceof Integer)) {
                    LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "actionPerformed", "Reservation time : " + ((Integer)hourSpinner.getValue()).intValue() + "h" + ((Integer)minuteSpinner.getValue()).intValue());

                    myResa.setWallTime( ((Integer)hourSpinner.getValue()).intValue(),
                                        ((Integer)minuteSpinner.getValue()).intValue());
                }
//            }
            this.myResa.setSelectedQueue(this.queueCombo.getSelectedIndex());
            
            
            this.myResa.setOargridsubBehaviour(oargridsubCheckbox.isSelected());
            this.myResa.setPropertiesHashMap(propertiesPanel.getProperties());
            
            if (scriptCheckBox.isSelected()) 
                this.myResa.setScriptToLaunch(scriptField.getText());
            
            LoggingManager.log(Level.CONFIG, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ResaDlg", this.myResa.toString());
            this.status = JOptionPane.OK_OPTION;
            dispose();
        }
        if (event.getSource() == this.cancel_btn) {
            this.status = JOptionPane.CANCEL_OPTION;
            dispose();
        }
    }

    /**
     * Method launching the panel
     *
     * @return the status of the reservation
     */
    public int exec() {
        setVisible(true);
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "exec", "Reservation Dialog Frame loaded");
        return this.status;
    }

    /**
     * Method returning the reservation made
     *
     * @return reservation
     */
    public G5kReservation getResa() {
        return this.myResa;
    }

    /**
     * Default constructor
     *
     * @param frame parent frame
     */
    public ResaDlg(JFrame frame,Connection aConnection) {
        super(frame,"Resources allocation - first step",true);
        myConnection = aConnection;
        JTabbedPane tabbedPanel = new JTabbedPane(JTabbedPane.TOP);
        JPanel reservationPanel = new JPanel();
        reservationPanel.setLayout(new BorderLayout());
        add(tabbedPanel,BorderLayout.CENTER);
        reservationPanel.add(new ResaStep01Panel(), BorderLayout.CENTER);
        reservationPanel.add(new BtnPanel(), BorderLayout.SOUTH);
        ok_btn.addActionListener(this);
        cancel_btn.addActionListener(this);
        reservationPanel.add(new TimeParamPanel(), BorderLayout.EAST);
        tabbedPanel.add(reservationPanel, 0);
        tabbedPanel.setTitleAt(0, "Main information");
        
        propertiesPanel = new PropertiesPanel();
        tabbedPanel.add(propertiesPanel,1);
        tabbedPanel.setTitleAt(1, "Advanced -- oar properties");
        
        tabbedPanel.setSelectedIndex(0);
        
        setResizable(false);
        setSize(820, 550);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){

                /* (non-Javadoc)
                 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
                 */
                @Override
                    public void windowClosing(WindowEvent e) {
                    LoggingManager.log(Level.INFO, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "windowClosing", "Reservation Dialog Frame is closing");
                }
            });
        LoggingManager.log(Level.FINE, LoggingManager.RESOURCESTOOL, this.getClass().getName(), "ResaDlg", "Reservation Dialog Frame initialized");
    }
    
    /**
     * Class representing the OAR properties for each clusters
     * 
     * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
     *
     */
    private class PropertiesPanel extends JPanel{
    	
    	/**
		 * Default serial version ID
		 */
		private static final long serialVersionUID = -2863283062038180417L;
		/**
		 * Array of property panels
		 */
		JPanel[] propertyPanels = null;
		/**
		 * Array of JLabels for the name of the clusters
		 */
    	JLabel[] propertyLabels = null;
    	/**
    	 * Array of JTextfields for the edition of the properties
    	 */
    	JTextField[] propertyTextFields = null;
    	
    	/**
    	 * Default constructor
    	 */
    	public PropertiesPanel(){
    		initialize();
    		setVisible(true);
    		
    	}
    	
    	/**
    	 * Main initialization method of the PropertiesPanel class
    	 */
    	
    	private void initialize(){
    		ArrayList<Integer> siteIndexes = new ArrayList<Integer>();
    		
    		for(int i = 0 ; i < G5kSite.getSitesNumber(); i++){
    			if(Config.isSiteEnable(i)){
    				siteIndexes.add(i);
    			}
    		}
    		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    		propertyPanels = new JPanel[siteIndexes.size()];
    		propertyTextFields = new JTextField[siteIndexes.size()];
    		propertyLabels = new JLabel[siteIndexes.size()];
    		GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;

            c.weightx = 0.5;
            c.insets = new Insets(10, 10, 10, 10);
    		for(int i = 0 ; i < propertyPanels.length ; i++){
    			propertyPanels[i] = new JPanel();
    			propertyPanels[i].setLayout(new GridBagLayout());
                c.gridx = 0;
                c.gridy = i;
                propertyLabels[i] = new JLabel(G5kSite.getSiteForIndex(siteIndexes.get(i)));
                propertyPanels[i].add(propertyLabels[i],c);
                propertyTextFields[i] = new JTextField();
                c.gridx = 1;
                propertyPanels[i].add(propertyTextFields[i],c);
                add(propertyPanels[i]);
    		}
    	}
    	
    	/**
    	 * Method returning the properties for each clusters (whenever the user 
    	 * entered something ...)
    	 * 
    	 * @return an HashMap of String, String with the cluster as a property and the
    	 * property itself for the value 
    	 */
    	public HashMap<Integer, String> getProperties(){
    		HashMap<Integer, String> propertiesHashMap = new HashMap<Integer, String>();
    		
    		for(int i = 0 ; i < propertyLabels.length ; i++){
    			int oarIndex = G5kSite.getIndexForSite(propertyLabels[i].getText());
    				//G5kCluster.getClusterByOar(propertyLabels[i].getText());
    			propertiesHashMap.put(oarIndex, propertyTextFields[i].getText());
    		}
    		return propertiesHashMap;
    	}
    }
}