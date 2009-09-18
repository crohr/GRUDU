/**
 * 
 */
package ganglia.misc;

/**
 * @author david
 *
 */
public interface Observable {

	public void addObserver(Observer anObserver);
	
	public void removeObserver(Observer anObserver); 
	
	public void updateObserver();
	
}
