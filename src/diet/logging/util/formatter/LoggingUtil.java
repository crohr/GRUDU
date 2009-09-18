/****************************************************************************/
/* This class corresponds to an utility class                               */
/*                                                                          */
/* Author(s)                                                                */
/* - David LOUREIRO (David.Loureiro@ens-lyon.fr)                            */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: LoggingUtil.java,v 1.3 2007/07/09 14:29:28 dloureir Exp $
 * $Log: LoggingUtil.java,v $
 * Revision 1.3  2007/07/09 14:29:28  dloureir
 * Correction of a javadoc tag
 *
 * Revision 1.2  2007/03/05 16:06:32  dloureir
 * Addind documentation to the Logging package
 *
 *
 ****************************************************************************/
package diet.logging.util.formatter;

import java.awt.Color;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;

/**
 * This class corresponds to an utility class
 *
 * @author David LOUREIRO (David.Loureiro@ens-lyon.fr)
 *
 */
public class LoggingUtil {

    /**
     * HashMap containing the color corresponding to the available Levels
     */
	public static final HashMap<Level, Color> colorLevels = initializeColorLevels();

    /**
     * Available levels for the logging of information
     */
	public static Vector<String> availableLevels = initializeAvailableLevels();

    /**
     * Method initializing the available levels Vector
     *
     * @return a vector of available level names
     */
	private static Vector<String> initializeAvailableLevels(){
		Vector<String> levels = new Vector<String>();
		levels.add(Level.SEVERE.getName());
		levels.add(Level.WARNING.getName());
		levels.add(Level.INFO.getName());
		levels.add(Level.CONFIG.getName());
		levels.add(Level.FINE.getName());
		levels.add(Level.FINER.getName());
		levels.add(Level.FINEST.getName());
		return levels;
	}

    /**
     * Method returning a HashMap containing a color for each available
     * label of logging
     *
     * @return a hashmap of (Level,Color)
     */
	private static HashMap<Level, Color> initializeColorLevels(){
		HashMap<Level, Color> newHashMap = new HashMap<Level, Color>();
		newHashMap.put(Level.SEVERE, Color.red);
		newHashMap.put(Level.WARNING, Color.orange);
		newHashMap.put(Level.INFO,Color.green);
		newHashMap.put(Level.CONFIG,Color.blue);
		newHashMap.put(Level.FINE,Color.black);
		newHashMap.put(Level.FINER, Color.gray);
		newHashMap.put(Level.FINEST,Color.lightGray);
		return newHashMap;
	}

    /**
     * Method returning the color corresponding to a level of log
     *
     * @param aLevel level of which we want to know the color
     *
     * @return the color corresponding to the level given in parameter
     */
	public static Color getColorFromLevel(Level aLevel){
		return colorLevels.get(aLevel);
	}

    /**
     * Method returning the HTMl code for a defined level of Log
     *
     * @param aLevel level of which we want to know the color
     * @return the HTML string corresponding to the color of the level
     */
	public static String getHTMLColorFromLevel(Level aLevel){
		String htmlColor = "#";
		Color aColor = colorLevels.get(aLevel);
		int aValue = aColor.getRed();
		String colorHex = Integer.toHexString(aValue);
		if(colorHex.equalsIgnoreCase("0")) colorHex = "00";
		htmlColor+=colorHex;
		aValue = aColor.getGreen();
		colorHex = Integer.toHexString(aValue);
		if(colorHex.equalsIgnoreCase("0")) colorHex = "00";
		htmlColor+=colorHex;
		aValue = aColor.getBlue();
		colorHex = Integer.toHexString(aValue);
		if(colorHex.equalsIgnoreCase("0")) colorHex = "00";
		htmlColor+=colorHex;
		return htmlColor;
	}
}