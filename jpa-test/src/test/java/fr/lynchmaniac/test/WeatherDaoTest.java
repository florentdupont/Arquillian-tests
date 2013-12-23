package fr.lynchmaniac.test;

import java.io.File;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.lynchmaniac.arquilliantest.dao.WeatherInfoDao;
import fr.lynchmaniac.arquilliantest.model.WeatherInfo;

@RunWith(Arquillian.class)
public class WeatherDaoTest {

	
	@EJB
	WeatherInfoDao dao;
	
	@PersistenceContext
	EntityManager em;
	    
	/* Notice we have to explicitly enlist the EntityManager in the JTA transaction. 
	 * This step is necessary since we are using the two resources independently. 
	 * This may look abnormal if you’re used to using JPA from within an EJB, 
	 * where enlistment happens automatically.*/
	@Inject
	UserTransaction utx;
	
	
	/* Arquillian executes the @Before and @After methods inside the container,
       before and after each test method, respectively. 
       The @Before method gets invoked after the injections have occurred.
    */
	
	@Before
	public void preparePersistenceTest() throws Exception {
	    clearData();
	    insertData();
	    startTransaction();
	}
	
	private void clearData() throws Exception {
	    utx.begin();
	    em.joinTransaction();
	    System.out.println("Dumping old records...");
	    em.createQuery("delete from WeatherInfo").executeUpdate();
	    utx.commit();
	}

	private void insertData() throws Exception {
	    utx.begin();
	    em.joinTransaction();
	    System.out.println("Inserting records...");
	    
	    // on ajoute des objets
	    WeatherInfo info1 = new WeatherInfo("NTE", "Nantes", "32°C", false);
	    WeatherInfo info2 = new WeatherInfo("BRT", "Brest", "18°C", true);
	    
	    em.persist(info1);
	    em.persist(info2);
	    
	    utx.commit();
	    // clear the persistence context (first-level cache)
	    em.clear();
	}

	private void startTransaction() throws Exception {
	    utx.begin();
	    em.joinTransaction();
	}
	
	
	@After
	public void commitTransaction() throws Exception {
	    utx.commit();
	}

	@Deployment
	public static WebArchive createDeployment() {
		System.out.println("=== FLO createDeployment !");
		WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
             
                .addAsWebInfResource("META-INF/ejb-jar.xml")
                .addAsWebInfResource("META-INF/beans.xml")
                .addPackages(true, "fr.lynchmaniac.arquilliantest")
                
                // ajout du datasource
                .addAsWebInfResource("weather-ds.xml")
                
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
	public void accessTemperature() {
		
		WeatherInfo info = dao.getInfoFromTown("NTE");
		Assert.assertEquals("32°C", info.getTemperature());
		
	}
	
	@Test
	public void accessAllInfo() {
		
		Assert.assertEquals(2, dao.getAllInfos().size());
		
	}
	
	@Test
	public void addNewInfo() {
		
		  WeatherInfo info = new WeatherInfo("LMS", "Le Mans", "28°C", false);
		  
		  
		  // ici on est dans une transaction, donc c'est normal que ca fonctionne.
		  dao.createNewInfo(info);
		  
		  /* c'est une approche intéressante qui fonctionne, mais : 
		   - pas simple à mettre en place. beaucoup de code technique
		   - des méthodes de chargement de données qui sont communes à toutes les méthodes de tests. 
		   petu etre que dans certains tests, précis, on souhaite charger que certains lots de données spécifiques.
		   - nous n'implémentons pas le chargement des données, ni les tests. Comment assurer que les données de sorties 
		   sont bien celles que nous attendions pour notre cas de test précis ?
		   demande beaucoup de code technique pour être mis en place.
		  */
		  
		  Assert.assertEquals(3, dao.getAllInfos().size());
		  
	}
	
}
