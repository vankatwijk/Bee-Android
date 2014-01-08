package com.example.beeproject.global.classes;

/**
 * Class that provide a list of all the classes that are being persisted to DB
 * by ORMLite. These classes must implement BeeObjectInterface
 * The list of classes is used for convenience, f.e. to loop through while creating the database.
 * @author rezolya
 *
 */
public class BeeObjectClasses {
	private static final Class[] list = {YardObject.class, HiveObject.class, UserObject.class, CheckFormObject.class, DiseaseObject.class};

	private static final Class[] classesToSync = {YardObject.class, HiveObject.class, CheckFormObject.class};
	
	/**
	 * @return list of all classes that are being persisted to DB 
	 */
	public static Class[] getClassesList(){
		return list;
	}
	

	public static Class[] getClassesToSyncList(){
		return classesToSync;
	}
}
