/**
 * 
 */
package net.sf.jftp.net.rsync;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import net.sf.jftp.config.Settings;

/**
 * @author david
 *
 */
public class RsyncController {

	private RsyncModel model = null;
	private RsyncView view = null;
	
	public RsyncController(){
		
		model = new RsyncModel();
		view = new RsyncView(this);
		loadModelFromFile();
		view.setStatesToView(getStateFromModel());
	}
	
	private void loadModelFromFile(){
		File rsyncoptionsFile = new File(Settings.rsyncOptions);
		if(rsyncoptionsFile.exists()){
			SAXBuilder builder = new SAXBuilder();
			try{
				Document document = builder.build(rsyncoptionsFile);
				Element rootNode = document.getRootElement();
				List listOfElement = rootNode.getChildren("option");
				Iterator iter = listOfElement.iterator();
				HashMap<String, String> states = new HashMap<String, String>();
				while(iter.hasNext()){
					Element element = (Element)iter.next();
					states.put(element.getAttributeValue("name"), element.getText());
				}
				setStatesFromView(states);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void saveModelToFile(){
		Element rsyncOption = new Element("rsync");
		Document document = new Document(rsyncOption);
		HashMap<String, String> states = getStateFromModel();
		Set aSet = states.keySet();
		Iterator iter = aSet.iterator();
		while(iter.hasNext()){
			String key = (String)iter.next();
			String value = states.get(key);
			Element anElement = new Element("option");
			anElement.setAttribute("name", key);
			anElement.setText(value);
			rsyncOption.addContent(anElement);
		}
		try{
			XMLOutputter sortie =new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, new FileOutputStream(Settings.rsyncOptions));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void show(){
		view.initialize();
		view.setStatesToView(getStateFromModel());
		view.showView();
	}
	
	public void setStatesFromView(HashMap<String, String> states){
		model.setRecursive(isChecked(states.get(RsyncModel.RECURSIVE_OPTION)));
		model.setUpdate(isChecked(states.get(RsyncModel.UPDATE_STRING)));
		model.setInplace(isChecked(states.get(RsyncModel.INPLACE_OPTION)));
		model.setAppend(isChecked(states.get(RsyncModel.APPEND_OPTION)));
		model.setSafeLinks(isChecked(states.get(RsyncModel.SAFE_LINKS_OPTION)));
		model.setIgnoreExisting(isChecked(states.get(RsyncModel.IGNORE_EXISTING_OPTION)));
		model.setDeleteDuring(isChecked(states.get(RsyncModel.DELETE_DURING_OPTION)));
		model.setDeleteAfter(isChecked(states.get(RsyncModel.DELETE_AFTER_OPTION)));
		model.setA(isChecked(states.get(RsyncModel.A_OPTION)));
		model.setZ(isChecked(states.get(RsyncModel.Z_OPTION)));
		model.setB(isChecked(states.get(RsyncModel.B_OPTION)));
		if(isChecked(states.get(RsyncModel.B_OPTION))){
			model.setBackupDir(states.get(RsyncModel.BACKUP_OPTION));
			model.setSuffix(states.get(RsyncModel.SUFFIX_OPTION));
		}
		saveModelToFile();
	}
	
	public HashMap<String, String> getStateFromModel(){
		HashMap<String, String> states = new HashMap<String, String>();
		states.put(RsyncModel.RECURSIVE_OPTION, getChecked(model.isRecursive()));
		states.put(RsyncModel.UPDATE_STRING, getChecked(model.isUpdate()));
		states.put(RsyncModel.INPLACE_OPTION, getChecked(model.isInplace()));
		states.put(RsyncModel.APPEND_OPTION, getChecked(model.isAppend()));
		states.put(RsyncModel.SAFE_LINKS_OPTION, getChecked(model.isSafeLinks()));
		states.put(RsyncModel.IGNORE_EXISTING_OPTION, getChecked(model.isIgnoreExisting()));
		states.put(RsyncModel.DELETE_DURING_OPTION, getChecked(model.isDeleteDuring()));
		states.put(RsyncModel.DELETE_AFTER_OPTION, getChecked(model.isDeleteAfter()));
		states.put(RsyncModel.A_OPTION, getChecked(model.isA()));
		states.put(RsyncModel.Z_OPTION, getChecked(model.isZ()));
		states.put(RsyncModel.B_OPTION, getChecked(model.isB()));
		states.put(RsyncModel.DELETE_OPTION, getChecked(model.isDelete()));
		states.put(RsyncModel.BACKUP_OPTION, model.getBackupDir());
		states.put(RsyncModel.SUFFIX_OPTION, model.getSuffix());
		return states;
	}
	
	private String getChecked(boolean check){
		if(check) return RsyncModel.CHECKED;
		else{
			return RsyncModel.UNCHECKED;
		}
	}
	
	private boolean isChecked(String state){
		if(state.equalsIgnoreCase(RsyncModel.CHECKED)) return true;
		else return false;
	}
	
	public String getRsyncOptions(){
		return model.getRsyncOptions();
	}
	
}
