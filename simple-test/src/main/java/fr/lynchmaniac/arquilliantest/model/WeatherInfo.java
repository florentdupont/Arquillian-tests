package fr.lynchmaniac.arquilliantest.model;

import java.io.Serializable;

public class WeatherInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String townName;
    
    private String townCode;
    
    private String temperature;
    
    private boolean isRainingInAnHour;
    
    
    public WeatherInfo() { }


	public String getTownName() {
		return townName;
	}


	public void setTownName(String townName) {
		this.townName = townName;
	}


	public String getTownCode() {
		return townCode;
	}


	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}


	public String getTemperature() {
		return temperature;
	}


	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}


	public boolean isRainingInAnhour() {
		return isRainingInAnHour;
	}


	public void setRainningInAnHour(boolean isRainingInAnHour) {
		this.isRainingInAnHour = isRainingInAnHour;
	}
}
