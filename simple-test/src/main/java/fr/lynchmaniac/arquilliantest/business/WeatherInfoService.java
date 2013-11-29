package fr.lynchmaniac.arquilliantest.business;

import javax.ejb.Stateless;

import fr.lynchmaniac.arquilliantest.model.WeatherInfo;

@Stateless
public class WeatherInfoService {

	public WeatherInfo getInfoFromTown(String townId) {
		
		// retourne un WeatherInfo bidon
		WeatherInfo info = new WeatherInfo();
		info.setTownCode("FR");
		info.setTemperature("32°C");
		info.setRainningInAnHour(false);
		info.setTownCode("NTES");
		info.setTownName("Nantes");
		
		return info;
	}
}
