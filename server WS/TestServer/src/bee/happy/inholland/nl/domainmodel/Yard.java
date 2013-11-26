package bee.happy.inholland.nl.domainmodel;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Yard Model Object
 * <p>Attribute of yard and related behaviour
 * <p>Objects of this class can be persisted to a database using ORMLite
 * @author rezolya
 * @version 1.0
 */
@DatabaseTable(tableName = "yards")
public class Yard {
	@DatabaseField(generatedId = true)
    private int id;
	
	@DatabaseField(canBeNull = false, width = 30)
    private String name;

	@DatabaseField(canBeNull = true)
	private double latitude;

	@DatabaseField(canBeNull = true)
	private double longitude;
	
	/**
	 * Default no-arg constructor
	 * required by ORMLite
	 */
	public Yard() {
    }
	

	/**
	 * Constructor
	 * @param name
	 * @param latitude
	 * @param longitude
	 */
	public Yard(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	public String toString(){
		return "Yard:{id: " + getId() + ", name: "+getName() + 
				", latitude: "+getLatitude() + ", longitude: " + getLongitude() + "}";
		
	}
	
}
