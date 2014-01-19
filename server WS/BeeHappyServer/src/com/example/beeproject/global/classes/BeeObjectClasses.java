package com.example.beeproject.global.classes;

/**
 * Class that provide a list of all the classes that are being persisted to DB
 * by ORMLite. These classes must implement BeeObjectInterface
 * The list of classes is used for convenience, f.e. to loop through while creating the database.
 * @author rezolya
 *
 */
public class BeeObjectClasses {
	
	/**
	 * List of classes, that implement this interface
	 * Tables for storing objects of this classes will be created in the database when DBCreateServlet is run
	 */
	private static final Class[] list = { UserObject.class, YardObject.class, HiveObject.class, 
			CheckFormObject.class, DiseaseObject.class, OutbrakeObject.class, DiseaseNotesObject.class};
	public static Class[] getClassesList(){
		return list;
	}
}
