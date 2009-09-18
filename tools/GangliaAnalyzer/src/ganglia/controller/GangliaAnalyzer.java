package ganglia.controller;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import ganglia.views.JobPanelWithGanglia;

import com.birosoft.liquid.LiquidLookAndFeel;

public class GangliaAnalyzer {

	public static void main(String[] args){
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("user","");
		properties.put("keyFile", "");
		properties.put("passphrase", "");
		properties.put("accesFrontale","");
		properties.put("destination", "");
		properties.put("connexionCommand", "");
    	try{UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");}catch(Exception e){e.printStackTrace();}
        LiquidLookAndFeel.setToolbarFlattedButtons(true);
        LiquidLookAndFeel.setToolbarButtonsFocusable(false);
		ArrayList<String> hostsList = new ArrayList<String>();
		hostsList.add("capricorne-25.lyon.grid5000.fr");
		hostsList.add("capricorne-26.lyon.grid5000.fr");
		hostsList.add("capricorne-27.lyon.grid5000.fr");
		hostsList.add("capricorne-56.lyon.grid5000.fr");
		hostsList.add("capricorne-9.lyon.grid5000.fr");
		JFrame frame = new JFrame();
		frame.setTitle("Reservation Frame");
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(950,580);
		frame.setVisible(true);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(GregorianCalendar.HOUR, 1);
		Long endOfTheReservation = calendar.getTimeInMillis();
		JobPanelWithGanglia panel = new JobPanelWithGanglia(properties,hostsList,97349,endOfTheReservation);
		frame.setContentPane(panel);
	}
}