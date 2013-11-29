package fr.lynchmaniac.arquilliantest.service;

import javax.ejb.Local;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Use;

@Local
@WebService(targetNamespace = "http://www.lynchmaniac.fr/arquillian/weather/1.0")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = Use.LITERAL)
public interface WeatherWs {

	
    @WebMethod(operationName = "getTemperatureOfTown")
    @WebResult(name = "getTemperatureOfTownResponse")
    String getTemperatureOfTown(String town);
    
    @WebMethod(operationName = "isRainingInAnHour")
    @WebResult(name = "isRainingInAnHourResponse")
    Boolean isRainingInAnHour(String town);
}
