/**
 * 
 */
package diet.application.totd;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.l2fprod.common.swing.tips.DefaultTip;
import com.l2fprod.common.swing.tips.DefaultTipModel;

import diet.application.ApplicationConfiguration;

/**
 * @author david
 *
 */
public class TipsLoader {

	public static DefaultTipModel getTipsModel(){
		DefaultTipModel tipsModel = new DefaultTipModel();
		String file = ApplicationConfiguration.getInstance().getProperty(ApplicationConfiguration.PROPERTY_TIPOFTHEDAY_FILEOFTIPS);
		File fileOfTips = new File(file);
		boolean fileExist = fileOfTips.exists();
		if(!fileExist) System.err.println("The file of the tips does not exist !!!");
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = null;
		Element rootElement = null;
		try{
			document = saxBuilder.build(fileOfTips);
			rootElement = document.getRootElement();
			List<Element> listOfTips = rootElement.getChildren("tip");
			Iterator<Element> iter = listOfTips.iterator();
			while(iter.hasNext()){
				Element tipElement = iter.next();
				String tipName = tipElement.getAttributeValue("name");
				String tipDescription = tipElement.getText();
				tipsModel.add(new DefaultTip(tipName,tipDescription));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return tipsModel;
	}
	
}
