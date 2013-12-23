package fr.lynchmaniac.arquilliantest.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WeatherInfo implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    @Id @GeneratedValue
    private long id;
    
    private String townName;
    
    private String townCode;
    
    private String temperature;
    
    private boolean isRainingInAnHour;
    
    
    /**
     * Default Constructor
     */
    public WeatherInfo() {
        
    }
    
    public WeatherInfo(String townCode, String townName, String temperature, boolean rainingSoon) {
    	this.townCode = townCode;
    	this.townName = townName;
    	this.temperature = temperature;
    	this.isRainingInAnHour = rainingSoon;
    }


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
