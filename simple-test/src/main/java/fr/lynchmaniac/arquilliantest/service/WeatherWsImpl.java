package fr.lynchmaniac.arquilliantest.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

import fr.lynchmaniac.arquilliantest.business.WeatherInfoService;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@WebService(endpointInterface = "fr.lynchmaniac.arquilliantest.service.WeatherWs", portName = "WeatherWsPort", serviceName = "TheWeatherWs", targetNamespace = "http://www.lynchmaniac.fr/arquillian/weather/1.0")
public class WeatherWsImpl implements WeatherWs {

	@EJB
	WeatherInfoService service;
	
	@Override
	@WebMethod(operationName = "getTemperatureOfTown")
	@WebResult(name = "getTemperatureOfTownResponse")
	public String getTemperatureOfTown(String town) {
		return service.getInfoFromTown(town).getTemperature();
	}

	@Override
	@WebMethod(operationName = "isRainingInAnHour")
	@WebResult(name = "isRainingInAnHourResponse")
	public Boolean isRainingInAnHour(String town) {
		return service.getInfoFromTown(town).isRainingInAnhour();
	}


}
