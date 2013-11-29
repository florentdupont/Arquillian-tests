package fr.lynchmaniac.test;

import java.io.File;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.lynchmaniac.arquilliantest.business.WeatherInfoService;
import fr.lynchmaniac.arquilliantest.service.WeatherWs;


/* The @RunWith annotation tells JUnit to use Arquillian as the test controller. 
 * Arquillian then looks for a public static method annotated with the @Deployment 
 * annotation to retrieve the test archive (i.e., micro-deployment). 
 * Then some magic happens and each @Test method is run inside the container environment.*/
@RunWith(Arquillian.class)
public class WeatherWsTest {

	// injection du webservice
	@EJB
	WeatherWs ws;
	
	/* Je peux tester le service Web ou le service EJB de la même manière : en l'injectant avec @EJB */
	@EJB
	WeatherInfoService wis;

	/* A public static method annotated with @Deployment that returns a ShrinkWrap archive
	 * 
	 * The @Deployment method is only mandatory for tests that run inside the container 
	 * and no extension is loaded that otherwise generates the test archive. 
	 * Client-side tests do not require a test archive, and therefore, 
	 * do not require a @Deployment method.
	 */
	@Deployment
	public static WebArchive createDeployment() {
		
		/* The purpose of the test archive is to isolate the classes and 
		 * resources which are needed by the test from the remainder of the classpath. 
		 * Unlike a normal unit test, Arquillian does not simply tap the entire classpath. 
		 * Instead, you include only what the test needs 
		 * (which may be the entire classpath, if that’s what you decide). 
		 * The archive is defined using ShrinkWrap, which is a Java API for 
		 * creating archives (e.g., jar, war, ear) in Java. 
		 * The micro-deployment strategy lets you focus on precisely the classes you want to test. As a result, the test remains very lean and manageable.*/
		
		WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
             
                .addAsWebInfResource("META-INF/ejb-jar.xml")
                .addAsWebInfResource("META-INF/beans.xml")
                .addPackages(true, "fr.lynchmaniac.arquilliantest");
        /* on remarque ici que les classes de tests de sont pas intégrées à l'archive.
         L'archive ne contient que les classe à tester : pas les classes de tests.
         les classes de tests.
        */    
        
		/*facilité pour exporter le contenu de l'archive : utile pour le débuggage*/
		webArchive.as(ZipExporter.class).exportTo(new File("target", "WeatherWs.war"), true);

		/* Une première approche peut être de juste afficher le contenu de l'archive */
		System.out.println(webArchive.toString(true));
              
              // question : comment Arquillian s'y retrouve pour "executer les tests" ?
              
            return webArchive;
	}

	@Test
	public void wsIsNotNull() {
		Assert.assertNotNull(ws);
	}
	
	@Test
	public void wisIsNotNull() {
		Assert.assertNotNull(wis);
	}
	
	@Test
	public void rainingTest() {
		Assert.assertFalse(ws.isRainingInAnHour("Nantes"));
	}
}
