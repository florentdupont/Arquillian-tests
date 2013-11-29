package fr.lynchmaniac.test;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.lynchmaniac.arquilliantest.business.WeatherInfoService;
import fr.lynchmaniac.arquilliantest.service.WeatherWs;

@RunWith(Arquillian.class)
public class WeatherWsTest {

	// injection du webservice
	@EJB
	WeatherWs ws;
	
	@EJB 
	WeatherInfoService wis;

	
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
