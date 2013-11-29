package fr.lynchmaniac.test;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.lynchmaniac.arquilliantest.service.WeatherWs;

@RunWith(Arquillian.class)
public class WeatherWs2Test {

	// injection du webservice
	@EJB
	WeatherWs ws;

	
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
