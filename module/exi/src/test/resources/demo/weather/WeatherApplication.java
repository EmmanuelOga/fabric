package com.example.weather;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * The application's main class.
 */
public class WeatherApplication {

	/**
	 * Main function of the application.
	 */
	public static void main(String[] args) {
		// Instanziate application
		WeatherApplication application = new WeatherApplication();

		// Create instance of the Java bean class
		Weather weather = new Weather();

    /*****************************************************************
     * Setup thermometer object
     *****************************************************************/
    Weather.ThermometerType thermometer = new Weather.ThermometerType();

		// Unit
		thermometer.setUnit(Weather.ThermometerType.UnitEnum.Celsius);

		// Current temperature
		TemperatureType temperature = new TemperatureType();
		temperature.setValue(BigDecimal.valueOf(14.0));
		thermometer.setCurrentTemperature(temperature);

		// Latest temperatures
		ArrayList<BigDecimal> list = new ArrayList<BigDecimal>();
		list.add(BigDecimal.valueOf(14.5));
		list.add(BigDecimal.valueOf(14.3));
		list.add(BigDecimal.valueOf(14.0));

    TemperatureListType tempList = new TemperatureListType();
		tempList.setValues(list);
		thermometer.setLatestTemperatures(tempList);
		weather.setThermometer(thermometer);

    /*****************************************************************
     * Setup anemometer object
     *****************************************************************/
		Weather.AnemometerType anemometer = new Weather.AnemometerType();

		// Current wind speed
		WindSpeedType speed = new WindSpeedType();
		speed.setValue(BigDecimal.valueOf(100));
		anemometer.setCurrentWindSpeed(speed);

		// Wind speed range
		WindSpeedRangeType speedRange = new WindSpeedRangeType();

    WindSpeedType minSpeed = new WindSpeedType();
		minSpeed.setValue(BigDecimal.valueOf(56));
    speedRange.setMinimum(minSpeed);

		WindSpeedType maxSpeed = new WindSpeedType();
		maxSpeed.setValue(BigDecimal.valueOf(103));
		speedRange.setMaximum(maxSpeed);

		anemometer.setWindSpeedRange(speedRange);
		weather.setAnemometer(anemometer);

    /*****************************************************************
     * Setup barometer object
     *****************************************************************/
		Weather.BarometerType barometer = new Weather.BarometerType();

		AirPressureType pressure = new AirPressureType();
		pressure.setValue(BigDecimal.valueOf(1013.4));
		barometer.setCurrentAirPressure(pressure);
		weather.setBarometer(barometer);

    /*****************************************************************
     * Setup weather forecast object
     *****************************************************************/
		Weather.WeatherForecastType weatherForecast = new Weather.WeatherForecastType();
		
    // Forecast 1
    ForecastType forecast1 = new ForecastType();
		try {
      forecast1.setTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(
              new GregorianCalendar(2011, 10, 25, 13, 00)));
    }
    catch (DatatypeConfigurationException e) {
      e.printStackTrace();
    }
		forecast1.setWeather(ForecastType.WeatherEnum.Cloudy);

    // Forecast 2
		ForecastType forecast2 = new ForecastType();
		try {
      forecast2.setTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(
              new GregorianCalendar(2011, 10, 25, 14, 00)));
    }
    catch (DatatypeConfigurationException e) {
      e.printStackTrace();
		}
		forecast2.setWeather(ForecastType.WeatherEnum.Rainy);

		ArrayList<ForecastType> list2 = new ArrayList<ForecastType>();
		list2.add(forecast1);
		list2.add(forecast2);
		weatherForecast.setForecast(list2);
		weather.setWeatherForecast(weatherForecast);

		try {
			// Convert bean instance to XML document
			String xmlDocument = application.toXML(weather);

			// Print XML document for debug purposes
			System.out.println(xmlDocument);

			System.out.println(application.fromEXIStream(application.toEXIStream(xmlDocument)));
			Weather obj = application.toInstance(application.fromEXIStream(application.toEXIStream(xmlDocument)).replaceAll("MyStringContent", "MyAlteredContent"));
			//System.out.println(obj.getSimpleBuiltIn() + " " + obj.getSimpleLocal() + " " + obj.getSimpleCustom().getValue());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convert Java bean object to XML document.
	 */
	public String toXML(final Weather beanObject) throws Exception {
		return WeatherConverter.instanceToXML(beanObject);
	}

	/**
	 * Convert XML document to Java bean object.
	 */
	public Weather toInstance(final String xmlDocument) throws Exception {
		return WeatherConverter.xmlToInstance(xmlDocument);
	}

	/**
	 * Serialize XML document to EXI byte stream.
	 */
	public byte[] toEXIStream(final String xmlDocument) throws Exception {
		return EXIConverter.serialize(xmlDocument);
	}

	/**
	 * Deserialize EXI byte stream to XML document.
	 */
	public String fromEXIStream(final byte[] exiStream) throws Exception {
		return EXIConverter.deserialize(exiStream);
	}

}
