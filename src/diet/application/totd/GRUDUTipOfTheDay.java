/**
 * 
 */
package diet.application.totd;

import java.util.Random;

import javax.swing.JFrame;

import com.l2fprod.common.swing.JTipOfTheDay;
import com.l2fprod.common.swing.tips.DefaultTipModel;

import diet.application.ApplicationConfiguration;

/**
 * @author david
 *
 */
public class GRUDUTipOfTheDay {
	
	final JTipOfTheDay.ShowOnStartupChoice fake = new JTipOfTheDay.ShowOnStartupChoice() {

		public boolean isShowingOnStartup() {
			boolean response = true;;
			ApplicationConfiguration config = ApplicationConfiguration.getInstance();
			if(config.getProperty(ApplicationConfiguration.PROPERTY_TIPOFTHEDAY_SHOWONSTARTUP) == null){
				config.setProperty(ApplicationConfiguration.PROPERTY_TIPOFTHEDAY_SHOWONSTARTUP,"true");
				config.saveConfiguration();
			}
			response = Boolean.parseBoolean(config.getProperty(ApplicationConfiguration.PROPERTY_TIPOFTHEDAY_SHOWONSTARTUP));
			return response;
		}
		public void setShowingOnStartup(boolean showOnStartup) {
			ApplicationConfiguration config = ApplicationConfiguration.getInstance();
			config.setProperty(ApplicationConfiguration.PROPERTY_TIPOFTHEDAY_SHOWONSTARTUP,Boolean.toString(showOnStartup));
			config.saveConfiguration();
		}
	};
	
	DefaultTipModel tips = null;
	JTipOfTheDay totd = null;
	
	public GRUDUTipOfTheDay(){
		initializeTips();
		totd = new JTipOfTheDay(tips);	
		Random randomgenerator = new Random(System.currentTimeMillis());
		totd.setCurrentTip(randomgenerator.nextInt(tips.getTipCount()));
		totd.showDialog(new JFrame("Tip of the day for GRUDU"), fake);
		totd.setVisible(true);
	}
	
	private void initializeTips(){
		tips = TipsLoader.getTipsModel();	
	}
	
}
