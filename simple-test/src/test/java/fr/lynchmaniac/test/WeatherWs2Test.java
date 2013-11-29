package fr.lynchmaniac.test;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.lynchmaniac.arquilliantest.service.WeatherWs;

/**
 * Ici, j'ai un deuxième cas de test pour tester une deuxième fonctionnalité.
 * 
 */
@RunWith(Arquillian.class)
public class WeatherWs2Test {

	// injection du webservice
	@EJB
	WeatherWs ws;

	@Deployment
	public static WebArchive createDeployment() {
		
		WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
				                          .addAsWebInfResource("META-INF/ejb-jar.xml")
		                                  .addAsWebInfResource("META-INF/beans.xml")
		                                  .addPackages(true, "fr.lynchmaniac.arquilliantest");
		                                 
		
		return webArchive;
	}

	@Test
	public void wsIsNotNull() {
		Assert.assertNotNull(ws);
	}
	
	@Test
	public void TemperatureTest() {
		String temperature = ws.getTemperatureOfTown("Nantes");
		Assert.assertEquals("32°C", temperature);
	}
}
