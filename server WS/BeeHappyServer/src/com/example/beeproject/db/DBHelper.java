package com.example.beeproject.db;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import com.example.beeproject.global.classes.BeeObjectClasses;
import com.example.beeproject.global.classes.BeeObjectInterface;
import com.example.beeproject.global.classes.DiseaseObject;
import com.example.beeproject.weather.classes.WeatherInfoObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper {
	public static void createDB(PrintWriter responseWriter){
		JdbcPooledConnectionSource connectionSource = ConnectionProvider.getConnectionSource();
		Class[] classList = BeeObjectClasses.getClassesList();
		try{
			for (Class objectClass : classList){
				createTableForClass(responseWriter, connectionSource, objectClass);
			}	
			checkAndFillDiseases(connectionSource);
			
			createTableForClass(responseWriter, connectionSource, WeatherInfoObject.class);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private static void createTableForClass(PrintWriter responseWriter,
			JdbcPooledConnectionSource connectionSource, Class objectClass)
			throws SQLException {
		// instantiate the dao
		Dao<BeeObjectInterface, Integer> classDao = DaoManager.createDao(connectionSource, objectClass);
		// if you need to create the 'accounts' table make this call
		responseWriter.println("Checking Table for class: " + objectClass);
		if(!classDao.isTableExists()){
			TableUtils.createTable(connectionSource, objectClass);
			responseWriter.println("Created Table for class: " + objectClass);
		}
	}
	
	/**
	 * Helper method to check if the diseases table is filled and fill it if its not
	 * @return 
	 * @throws SQLException 
	 */
	public static void checkAndFillDiseases(JdbcPooledConnectionSource connectionSource) throws SQLException{
		Dao<DiseaseObject, Integer> diseaseDao = DaoManager.createDao(connectionSource, DiseaseObject.class);
		List<DiseaseObject> diseases = diseaseDao.queryForAll();
		if(diseases.size() == 0){
			String disease1 = "Varroa mites";
			String disease2 = "Nosema";
			String disease3 = "American foulbrood (AFB)";
			String disease4 = "European foulbrood (EFB)";
			String disease5 = "Chalkbrood disease";
			String disease6 = "Stonebrood disease";
			String disease7 = "Sacbrood virus";
			String disease8 = "Virus Bee Paralysis";
			
			//table is empty, fill it
			String description1 = "Varroa destructor and Varroa jacobsoni are parasitic mites that feed on the bodily fluids of adult, pupal and larval bees. Varroa mites can be seen with the naked eye as a small red or brown spot on the bee's thorax. Varroa mites are carriers for a virus that is particularly damaging to the bees. Bees infected with this virus during their development will often have visibly deformed wings.";
			String treatment1 = "Common chemical controls include 'hard' chemicals such as fluvalinate (marketed as Apistan) and coumaphos (marketed as CheckMite) and 'soft' chemicals such as thymol (marketed as ApiLife-VAR and Apiguard), sucrose octanoate esters (marketed as Sucrocide), oxalic acid and formic acid (sold in gel packs as Mite-Away, but also used in other formulations).";
			DiseaseObject varroa = new DiseaseObject(disease1, description1, treatment1, false);
			diseaseDao.create(varroa);
			
			
			String description2 = "Nosema apis is a microsporidian that invades the intestinal tracts of adult bees and causes nosema disease, also known as nosemosis. It is a common protozoan disease that affects the intestinal tracks of adult bees, is kind of like dysentery in humans.";
			String treatment2 = "Nosema is treated by increasing the ventilation through the hive. Some beekeepers will treat a hive with antibiotics such as fumagillan. Nosema can also be prevented or minimized by removing much of the honey from the beehive then feeding the bees on sugar water in the late fall. Sugar water made from refined sugar has lower ash content than flower nectar, reducing the risk of dysentery.";
			DiseaseObject nosema = new DiseaseObject(disease2, description2, treatment2, true);
			diseaseDao.create(nosema);
			
			
			String description3 = "American foulbrood (AFB) is a highly contagious bacterial disease that attacks larvae and pupae. Some symptoms are: Infected larvae change color from a healthy pearly white to tan or dark brown and die after they’re capped. Cappings of dead brood sink inward (becoming concave) and often appear perforated with tiny holes. The capped brood pattern no longer is compact, but becomes spotty and random. This is sometimes referred to as a “shotgun” pattern. The surface of the cappings may appear wet or greasy.";
			String treatment3 = "The disease attacks only the very young larvae. Larvae older than 48 hours are not susceptible. Adult bees are not affected by the disease.  The 'Shook Swarm' technique of bee husbandry can also be used to effectively control the disease, the advantage being that chemicals are not used.";
			DiseaseObject afb = new DiseaseObject(disease3, description3, treatment3, true);
			diseaseDao.create(afb);
			
			String description4 = "European foulbrood (EFB) is a bacterial disease of larvae.Symptoms of EFB include the following: Very spotty brood pattern (many empty cells scattered among the capped brood). Infected larvae are twisted in the bottoms of their cells like an inverted corkscrew. The larvae are either a light tan or brown color, and have a smooth 'melted' appearance. With EFB, nearly all of the larvae die in their cells before they are capped. A sour odor may be present.";
			String treatment4 = "An otherwise healthy colony can usually survive European foulbrood. An outbreak of the disease may be controlled chemically with oxytetracycline hydrochloride, but honey from treated colonies could have chemical residues from the treatment. The 'Shook Swarm' technique of bee husbandry can also be used to effectively control the disease, the advantage being that chemicals are not used. Prophylactic treatments are not recommended as they lead to resistant bacteria.";
			DiseaseObject efb = new DiseaseObject(disease4, description4, treatment4, true);
			diseaseDao.create(efb);
			
			String description5 = "Chalkbrood is a common fungal disease that affects bee larvae. Chalkbrood pops up most frequently during damp conditions in early spring. It is rather common and usually not that serious. Infected larvae turn a chalky white color, become hard, and may occasionally turn black.";
			String treatment5 = "No medical treatment is necessary for chalkbrood. You can help the colony out by removing mummified carcasses from the hive’s entrance and from the ground around the hive. Hives with Chalkbrood can generally be recovered by increasing the ventilation through the hive.";
			DiseaseObject chalkbrood = new DiseaseObject(disease5, description5, treatment5, false);
			diseaseDao.create(chalkbrood);
			
			String description6 = "Stonebrood is a fungal disease that affects larvae and pupae. It is rare and doesn’t often show up. Stonebrood causes the mummification of brood. Mummies are hard and solid (not sponge-like and chalky as with chalkbrood). Some brood may become covered with a powdery green fungus.";
			String treatment6 = "No medical treatment is recommended for stonebrood. In most instances worker bees remove dead brood, and the colony recovers on its own. You can help things along by cleaning up mummies at the entrance and around the hive, and removing heavily infested frames.";
			DiseaseObject stonebrood = new DiseaseObject(disease6, description6, treatment6, false);
			diseaseDao.create(stonebrood);
			
			String description7 = "Death of a colony by sacbrood is rare. Because of the similarity to other diseases, however, the beekeeper should learn to distinguish sacbrood from the more serious diseases. The etiologic agent in sacbrood is a virus. Larvae die of sacbrood in capped cells in the elongated position. As the disease progresses, the larval skin forms a sac, which separates from the prepupal skin. Between these two layers of skin is an accumulation of fluid. The outer skin toughens and, as a result, the larva can be picked up in its entirety without the release of the fluid.";
			String treatment7 = "No recommended medical treatment exists for sacbrood. You can shorten the duration of this condition by removing the sacs with a pair of tweezers. Other than that intervention, let the bees fight it out for themselves.";
			DiseaseObject sacbrood = new DiseaseObject(disease7, description7, treatment7, true);
			diseaseDao.create(sacbrood);
			
			String description8 = "Bee paralysis is caused by several different viruses, but some nectars and pollens also may induce similar symptoms. Chronic bee paralysis and hairless black syndrome are caused by the same virus. Acute bee paralysis, caused by another virus, kills bees more quickly than the chronic virus. Affected bees quiver and cannot fly. Frequently, they appear greasy and shiny with no hair on their thorax. The disease is transmitted to healthy bees when they attack diseased bees or when food is exchanged between healthy and diseased bees.";
			String treatment8 = "Workers, drones, and queens are susceptible to chronic bee paralysis. It appears that susceptibility to the disease is inherited from the queen. Consequently, requeening of colonies with the disease may rid the colony of all symptoms.";
			DiseaseObject paralysis = new DiseaseObject(disease8, description8, treatment8, true);
			diseaseDao.create(paralysis);
		}
		
	}
}
