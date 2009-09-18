/****************************************************************************/
/* [Documentation Here!]                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: PluginUtil.java,v 1.5 2007/12/02 11:06:24 aamar Exp $
 * $Log: PluginUtil.java,v $
 * Revision 1.5  2007/12/02 11:06:24  aamar
 * Correcting a bug o jar file name decoding.
 *
 * Revision 1.4  2007/11/06 09:59:29  aamar
 * Replacing UTF-8 by used charset parameter in URL decoding
 *
 * Revision 1.3  2007/11/05 15:54:36  dloureir
 * Adding URL decoding for the recuperation of well-formatted urls
 *
 * Revision 1.2  2007/10/30 10:18:52  dloureir
 * The getClassesForPackage(String) method now works with jar files
 *
 * Revision 1.1  2007/09/28 16:04:36  aamar
 * Initial revision
 *
 ****************************************************************************/
package diet.plugins;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginUtil {


	/**
     * Attempts to list all the classes in the specified package as determined
     * by the context class loader
     * 
     * @param pckgname
     *            the package name to search
     * @return a list of classes that exist within that package
     * @throws ClassNotFoundException
     *             if something went wrong
     */
    public static ArrayList<Class> getClassesForPackage(String pckgname) throws ClassNotFoundException {
    	ArrayList<Class> classes = new ArrayList<Class>();
        try{
        	String path = pckgname.replace('.', '/');
			URL fileURL = PluginUtil.class.getResource("/"+path+"/");
			/*
			 * there is two possibilities:
			 * - we are in a jar file : to get the files list we have to get the entries
			 * of the jar file corresponding to what we are searching
			 * - we are not in a jar file : to get the files list we just have to
			 * use the listFiles() method
			 */
			
			if(fileURL.getFile().contains("!")){
				// we are in a jar file
				StringTokenizer splitter = new StringTokenizer(fileURL.getFile(),"!");
				String  jarFilePath = splitter.nextToken().substring(5);
                                JarFile jarFile = new JarFile(URLDecoder.decode(jarFilePath, 
                                                                                Charset.defaultCharset().displayName()));
				Enumeration theEnumeration = jarFile.entries();
				//System.out.println("-------------------");
				while(theEnumeration.hasMoreElements()){
					JarEntry jarEntry = (JarEntry)theEnumeration.nextElement();
					String jarEntryName = jarEntry.getName();
					
					if(jarEntryName.endsWith(".class") && jarEntryName.startsWith(path)){	
						
						int index = jarEntryName.indexOf(".");
						jarEntryName = jarEntryName.substring(0, index);
						jarEntryName = jarEntryName.replace('/', '.');
						Class aClass = Class.forName(jarEntryName);
						//System.out.println(jarEntryName);
						classes.add(aClass);
					}
					//System.out.println(jarEntry.getName());
				}
				//System.out.println("-------------------");
			}
			else{

				File experiencesPackageDirectory = new File(URLDecoder.decode(fileURL.getFile(), 
                        Charset.defaultCharset().displayName()));
				// we are not in a jar file
				File[] experiencesPackageDirectoryFiles = experiencesPackageDirectory.listFiles();
				for(int i = 0 ; i < experiencesPackageDirectoryFiles.length ; i ++){
					String fileName = experiencesPackageDirectoryFiles[i].getName();
					System.out.println("## " + fileName);
					if(fileName.endsWith(".class")){
						int index = fileName.indexOf(".");
						String subString = fileName.substring(0, index);
						Class aClass = Class.forName(pckgname+"."+subString);
						classes.add(aClass);
					}
					else if(experiencesPackageDirectoryFiles[i].isDirectory()){
						
						classes.addAll(getClassesForPackage(pckgname+"."+fileName));
					}
				} // end for
			}
        }   
		catch(Exception e){
			e.printStackTrace();
		}
        return classes;
    }

	/**
     * Attempts to list all the classes in the specified package as determined
     * by the context class loader
     * 
     * @param pckgname
     *            the package name to search
     * @return a list of classes that exist within that package
     * @throws ClassNotFoundException
     *             if something went wrong
     */
    public static ArrayList<Class> getClassesForPackage(String pckgname, Class baseClass) throws ClassNotFoundException {
    	ArrayList<Class> classes = getClassesForPackage(pckgname);
    	ArrayList<Class> requestedClasses = new ArrayList<Class>();
    	System.out.println(classes.size() + " class found");
    	for (Class aClass : classes) {
    		System.out.println("Processing " + aClass.getName());
    		boolean keep = false;
    		Class [] interfaces = aClass.getInterfaces();
            for (int ix=0; ix<interfaces.length; ix++) {
            	System.out.println("   - interface " + interfaces[ix].getName() + " ? " +
            			baseClass.getName());
            	if (interfaces[ix].getName().equals(baseClass.getName())) {
            		keep = true;
            		break;
            	}
            }
            if (keep)
            	requestedClasses.add(aClass);
            else {
                if (isSubClass(aClass, baseClass))
                    requestedClasses.add(aClass);
            }
    	}
    	return requestedClasses;
    }
    
    public static boolean isSubClass(Class aClass, Class baseClass) {
        if (aClass.getSuperclass() == null)
            return false;
        if (aClass.getSuperclass().getName().equals(baseClass.getName()))
            return true;
        Class [] interfaces = aClass.getInterfaces();
        for (int ix=0; ix<interfaces.length; ix++) {
            if (interfaces[ix].getName().equals(baseClass.getName())) {
                return true;
            }
        }
        return isSubClass(aClass.getSuperclass(), baseClass);
    }
}
