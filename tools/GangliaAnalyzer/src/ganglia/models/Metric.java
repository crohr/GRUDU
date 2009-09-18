/**
 * 
 */
package ganglia.models;

import org.jdom.Element;

/**
 * @author david
 *
 */
public class Metric implements MetricXML {

	private String name = null;
	private String value = null;
	private String units = null;
	
	public Metric(Element xmlElement){
		name  = xmlElement.getAttributeValue(METRIC_ATTRIBUTE_NAME);
		if(name == null) name= "";
		value = xmlElement.getAttributeValue(METRIC_ATTRIBUTE_VAL);
		if(value == null) value = "";
		units = xmlElement.getAttributeValue(METRIC_ATTRIBUTE_UNITS);
		if(units == null) units = "";
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
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
	
}
