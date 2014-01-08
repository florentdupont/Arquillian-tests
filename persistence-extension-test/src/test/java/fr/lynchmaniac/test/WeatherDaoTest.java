package fr.lynchmaniac.test;

import java.io.File;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.lynchmaniac.arquilliantest.dao.WeatherInfoDao;
import fr.lynchmaniac.arquilliantest.model.WeatherInfo;

@RunWith(Arquillian.class)
public class WeatherDaoTest {

	/** 
	 * Pas de EntityManager a prendre en compte.
	 * La prise en compte de transaction est automatique, dès lors que la méthode est annotée avec @UsingDataSet ou @ShouldMatchDataSet
	 * 
	 * Pour le développeur, pas besoin de créer ses transactions manuellement.
	 * 
	 * 
	 * */
	
	//@PersistenceContext
	//EntityManager em;
	
	//@Resource
	//UserTransaction utx;
	
	@EJB
	WeatherInfoDao dao;
	
	
	@Deployment
	public static WebArchive createDeployment() {
		System.out.println("=== FLO createDeployment !");
		WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
             
                .addAsWebInfResource("META-INF/ejb-jar.xml")
                .addAsWebInfResource("META-INF/beans.xml")
                .addPackages(true, "fr.lynchmaniac.arquilliantest")
                
                // ajout du datasource
                .addAsWebInfResource("weatherh2-ds.xml", "weather-ds.xml")
                
                /* We define the Persistence Unit in a test-persistence.xml file that’s corresponding 
                 * to the target container. ShrinkWrap takes this file from the classpath and puts it
                 * into its standard location within the archive.*/
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml");
                
              webArchive.as(ZipExporter.class).exportTo(new File("target", "JPAtest.war"), true);

              System.out.println(webArchive.toString(true));
            
              
            return webArchive;
	}

	@Test
	public void wsIsNotNull() {
		Assert.assertNotNull(dao);
	}
	
	@Test
	@UsingDataSet("datasets/weather.json")
	public void accessTemperature() {
		
		WeatherInfo info = dao.getInfoFromTown("NTE");
		Assert.assertEquals("32°C", info.getTemperature());
		
	}
	
	@Test
	@UsingDataSet("datasets/weather.yml")
	public void accessAllInfo() {
		Assert.assertEquals(2, dao.getAllInfos().size());
	}
	
	
	/* cas des modifications : OK, tout semble correctement marcher. */
	@Test
	@UsingDataSet("datasets/weather.yml")
	@ShouldMatchDataSet("datasets/expected-weather.yml")
	public void modifyExistingInfo() {
		
		  WeatherInfo info = dao.getInfoFromTown("NTE");
		  //WeatherInfo info = new WeatherInfo("LMS", "Le Mans", "28°C", false);
		  
		  info.setTemperature("33°C");
		  // ici on est dans une transaction, donc c'est normal que ca fonctionne.
		  dao.saveInfo(info);
		  
	}
	
	
	/* cas des modifications : OK, tout semble correctement marcher. */
	@Test
	@UsingDataSet("datasets/weather2.yml")
	@ShouldMatchDataSet(value="datasets/expected-weather2.yml", excludeColumns="id")
	public void createNewInfo() throws Exception {
		
		   Assert.assertEquals(2, dao.getAllInfos().size());
		
		  // WeatherInfo info = dao.getInfoFromTown("NTE");
		  WeatherInfo info = new WeatherInfo("LMS", "Le Mans", "28°C", false);
		  
		 /* attention, a savoir : 
		  -> les dataset sont insérés dans incrémentés les séquences . Donc tous les id autogénérés pour les entités , seront bien positionnés
		   à partir de 0, indépendamment des id ajoutés "manuellement" dans les dataset.
		   
		     // il est donc préférable d'ajouter des id de dataset à des valeurs très hautes pour être sur de ne pas avoir de chevauchement.
		      
		    pour l'ajout l'ID est obligatoire.
		    Par contre, pour les dataset de matching, il est préférable d'exclure la colonne des id.
		     
		  */
		  
		  dao.saveInfo(info);
	}
	
	
	/* cas des modifications : OK, tout semble correctement marcher. */
	@Test
	@ShouldMatchDataSet(value="datasets/expected-weather2.yml", excludeColumns="id")
	public void createNewInfoFromScratch() throws Exception {
		
		   
		  // WeatherInfo info = dao.getInfoFromTown("NTE");
		  WeatherInfo info1 = new WeatherInfo("NTE", "Nantes", "32°C", false);
		  WeatherInfo info2 = new WeatherInfo("BRT", "Brest", "18°C", true);
		  WeatherInfo info3 = new WeatherInfo("LMS", "Le Mans", "28°C", false);
		  
		 /* attention, a savoir : 
		  -> les dataset sont insérés dans incrémentés les séquences . Donc tous les id autogénérés pour les entités , seront bien positionnés
		   à partir de 0, indépendamment des id ajoutés "manuellement" dans les dataset.
		   
		     // il est donc préférable d'ajouter des id de dataset à des valeurs très hautes pour être sur de ne pas avoir de chevauchement.
		      
		    pour l'ajout l'ID est obligatoire.
		    Par contre, pour les dataset de matching, il est préférable d'exclure la colonne des id.
		     
		  */
		  
		  dao.saveInfo(info1);
		  dao.saveInfo(info2);
		  dao.saveInfo(info3);
	}
	
	
	/* ici, ca pete normallement car hors transaction : J'ai forcé le DAO a être marqué pour s'exécuter au sein d'une transaction.
	 * => Il est en Transactionmanagement = MANDATORY.
	 * Ce flag nous permet de nous assurer qu'il est bien appelé depuis un contexte dans lequel une transaction est déjà ouverte.
	 * C'est le cas s'il est appelé depuis un service.
	 * Le positionnement du TransactionManagement nous assure aussi que le DAO ne sera pas appelé depuis autre part qu'un service métier (IHM par exemple).
	 * 
	 *  Dans ce cas, le TU doit donc obligatoirement être transactionnel.
	 *  C'est le cas avec @Dataset, mais avec un @Test classique, ce n'est pas le cas.
	 *  Pour résoudre cette problématique, on ajoute le @Transactional issu de l'extension Arquillian Transaction extension.
	 *  Cette annotation permet de prendre en compte la transaction sur les tests très facilement !
	 *  */
	@Test
	@Transactional
	public void createNewInfoFromScratchWithTransaction() throws Exception {
		
		   
		  // WeatherInfo info = dao.getInfoFromTown("NTE");
		  WeatherInfo info1 = new WeatherInfo("NTE", "Nantes", "32°C", false);
		  WeatherInfo info2 = new WeatherInfo("BRT", "Brest", "18°C", true);
		  WeatherInfo info3 = new WeatherInfo("LMS", "Le Mans", "28°C", false);
		  
		 /* attention, a savoir : 
		  -> les dataset sont insérés dans incrémentés les séquences . Donc tous les id autogénérés pour les entités , seront bien positionnés
		   à partir de 0, indépendamment des id ajoutés "manuellement" dans les dataset.
		   
		     // il est donc préférable d'ajouter des id de dataset à des valeurs très hautes pour être sur de ne pas avoir de chevauchement.
		      
		    pour l'ajout l'ID est obligatoire.
		    Par contre, pour les dataset de matching, il est préférable d'exclure la colonne des id.
		  */
		  
		 // System.out.println("STATUT DE LA TRANSACTION : " + printStatus(utx.getStatus()));
		  
		  dao.saveInfo(info1);
		  dao.saveInfo(info2);
		  dao.saveInfo(info3);
		  
		  Assert.assertEquals(3,  dao.getAllInfos().size());
	}
	
	
	public static String printStatus(int status) {
		switch(status) {
		 	case Status.STATUS_ACTIVE  :return "ACTIVE";
		 	case Status.STATUS_COMMITTED  :return "COMMITED";
		 	case Status.STATUS_COMMITTING  :return "COMMITING";
		 	case Status.STATUS_MARKED_ROLLBACK  :return "MARKED ROLLBACK";
		 	case Status.STATUS_NO_TRANSACTION  :return "NO TRANSACTION";
		 	case Status.STATUS_PREPARED  :return "PREPARED";
		 	case Status.STATUS_PREPARING  :return "PREPARING";
		 	case Status.STATUS_ROLLEDBACK  :return "ROLLED BACK";
		 	case Status.STATUS_ROLLING_BACK  :return "ROLLING BACK";
		 	case Status.STATUS_UNKNOWN  :return "UNKNOWN";
		 	default : return "UNKNOWN";
		}
	}
	
	
	
	
}
