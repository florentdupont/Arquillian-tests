package fr.lynchmaniac.arquilliantest.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import fr.lynchmaniac.arquilliantest.model.WeatherInfo;

@Stateless
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
    
    
    public void createNewInfo(WeatherInfo newInfo) {
    	
    	em.persist(newInfo);
    }
	
}
