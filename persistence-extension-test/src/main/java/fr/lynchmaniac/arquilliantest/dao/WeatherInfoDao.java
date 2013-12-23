package fr.lynchmaniac.arquilliantest.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.lynchmaniac.arquilliantest.model.WeatherInfo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class WeatherInfoDao {

	
	@PersistenceContext
	EntityManager em;
	
	public WeatherInfo getInfoFromTown(String townId) {
		
		WeatherInfo info = em.createQuery("select w from WeatherInfo w where w.townCode='" + townId + "'", WeatherInfo.class).getSingleResult();
	   
		return info;
	}
	
	
    public List<WeatherInfo> getAllInfos() {
		
		List<WeatherInfo> infos = em.createQuery("select w from WeatherInfo w", WeatherInfo.class).getResultList();
	   
		return infos;
	}
    
    public void saveInfo(WeatherInfo info) {
    	em.merge(info);
    }
    
    public void createNewInfo(WeatherInfo newInfo) {
    	
    	em.persist(newInfo);
    	
    	/*String queryStr = "insert into WeatherInfo (isRainingInAnHour, temperature, townCode, townName, id) values (?, ?, ?, ?, ?)";
    	 Query query = em.createNativeQuery(queryStr);
    	 query.setParameter(1, newInfo.isRainingInAnhour());
    	 query.setParameter(2, newInfo.getTemperature());
    	 query.setParameter(3, newInfo.getTownCode());
    	 query.setParameter(4, newInfo.getTownName());
    	 
    	 // nouvel id ici ! 
    	 query.setParameter(5, 3);
    	 
    	 query.executeUpdate();*/
    	
    /*
     * Ne fonctionne pas : il faut obligatoirement que l'id soit renseigné.
     * 
    	String queryStr = "insert into WeatherInfo (isRainingInAnHour, temperature, townCode, townName) values (?, ?, ?, ?)";
   	 Query query = em.createNativeQuery(queryStr);
   	 query.setParameter(1, newInfo.isRainingInAnhour());
   	 query.setParameter(2, newInfo.getTemperature());
   	 query.setParameter(3, newInfo.getTownCode());
   	 query.setParameter(4, newInfo.getTownName());
   	 
   	 query.executeUpdate();
   	 */
    	
    }
	
}
