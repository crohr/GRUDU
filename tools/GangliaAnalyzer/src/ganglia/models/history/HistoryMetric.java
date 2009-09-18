/**
 * 
 */
package ganglia.models.history;

import java.util.HashMap;

/**
 * @author david
 *
 */
public class HistoryMetric {
	
	private String name;
	
	private HashMap<Long, Double> values;
	
	private String units;
	
	public HistoryMetric(String name, String units){
		this.name = name;
		values = new HashMap<Long, Double>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the units
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * @param units the units to set
	 */
	public void setUnits(String units) {
		this.units = units;
	}
	
	public void putValue(long date, double value){
		values.put(date, value);
	}
	
	public void removeValue(long date){
		values.remove(date);
	}
	
	public void clearValues(){
		values.clear();
	}
	
	public double getValue(long date){
		return values.get(date);
	}

	/**
	 * @return the values
	 */
	public HashMap<Long, Double> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(HashMap<Long, Double> values) {
		this.values = values;
	}
	
}
