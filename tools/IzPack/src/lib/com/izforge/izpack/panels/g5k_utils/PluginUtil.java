/****************************************************************************/
/* [Documentation Here!]                                                    */
/*                                                                          */
/* Author(s)                                                                */
/* - Abdelkader AMAR (Abdelkader.Amar@ens-lyon.fr)                          */
/*                                                                          */
/* $LICENSE$                                                                */
/****************************************************************************/
/* $Id: PluginUtil.java,v 1.1 2007/10/30 10:25:23 dloureir Exp $
 * $Log: PluginUtil.java,v $
 * Revision 1.1  2007/10/30 10:25:23  dloureir
 * Update to OAR2 version of the softwares
 *
 * Revision 1.2  2007/10/30 10:18:52  dloureir
 * The getClassesForPackage(String) method now works with jar files
 *
 * Revision 1.1  2007/09/28 16:04:36  aamar
 * Initial revision
 *
 ****************************************************************************/
package com.izforge.izpack.panels.g5k_utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
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
        // This will hold a list of directories matching the pckgname. There may be more than one if a package is split over multiple jars/paths
//        ArrayList<File> directories = new ArrayList<File>();
//        try {
//            ClassLoader cld = Thread.currentThread().getContextClassLoader();
//            if (cld == null) {
//                throw new ClassNotFoundException("Can't get class loader.");
//            }
//            String path = pckgname.replace('.', '/');
//            // Ask for all resources for the path
//            Enumeration<URL> resources = cld.getResources(path);
//            while (resources.hasMoreElements()) {
//                directories.add(new File(URLDecoder.decode(resources.nextElement().getPath(), "UTF-8")));
//            }
//        } catch (NullPointerException x) {
//            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)");
//        } catch (UnsupportedEncodingException encex) {
//            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Unsupported encoding)");
//        } catch (IOException ioex) {
//            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname);
//        }
//
//        ArrayList<Class> classes = new ArrayList<Class>();
//        // For every directory identified capture all the .class files
//        for (File directory : directories) {
//            if (directory.exists()) {
//                // Get the list of the files contained in the package
//                String[] files = directory.list();
//                for (String file : files) {
//                    // we are only interested in .class files
//                    if (file.endsWith(".class")) {
//                        // removes the .class extension
//                        classes.add(Class.forName(pckgname + '.' + file.substring(0, file.length() - 6)));
//                    }
//                    else {
//                    	File subF = new File(directory + "/" + file);
//                    	if (subF.exists() && subF.isDirectory()) {
//                    			ArrayList<Class> subClasses = getClassesForPackage(pckgname + "." + file);
//                    			System.out.println("  ++ " + subClasses.size() + " sub-classes found");
//                    			for (Class c : subClasses)
//                    				classes.add(c);
//                    	}
//                    }
//                }
//            } else {
//                throw new ClassNotFoundException(pckgname + " (" + directory.getPath() + ") does not appear to be a valid package");
//            }
//        }
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
				JarFile jarFile = new JarFile(jarFilePath);
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
				File experiencesPackageDirectory = new File(fileURL.getFile());
				// we are not in a jar file
				File[] experiencesPackageDirectoryFiles = experiencesPackageDirectory.listFiles();
				for(int i = 0 ; i < experiencesPackageDirectoryFiles.length ; i ++){
					String fileName = experiencesPackageDirectoryFiles[i].getName();
					if(fileName.endsWith(".class")){
						int index = fileName.indexOf(".");
						String subString = fileName.substring(0, index);
						Class aClass = Class.forName(pckgname+"."+subString);
						classes.add(aClass);
					}
					else if(experiencesPackageDirectoryFiles[i].isDirectory()){
						
						classes.addAll(getClassesForPackage(pckgname+"."+fileName));
					}
				}
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
