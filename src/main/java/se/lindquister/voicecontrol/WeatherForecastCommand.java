package se.lindquister.voicecontrol;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import se.lindquister.voicecontrol.weather.WeatherForecastResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class WeatherForecastCommand extends Command {

	private String longitude;
	private String latitude;
	private Voice voice;
	private RestTemplate restTemplate;

	public WeatherForecastCommand() {
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		VoiceManager vm = VoiceManager.getInstance();
		voice = vm.getVoice("kevin16");


		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
		converter.getObjectMapper().registerModule(new JavaTimeModule());
		messageConverters.add(converter);

		restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(messageConverters);
	}

	@Override
	public void execute() {
		ResponseEntity<WeatherForecastResponse> response = getForecast();
		if (response.getStatusCode() == HttpStatus.OK) {
			String forecast = createForecast(response.getBody());
			System.out.println(forecast);
			speak(forecast);
		} else {
			System.out.println(response.getStatusCode());
			speak("I failed to get a weather forecast");
		}
	}

	private ResponseEntity<WeatherForecastResponse> getForecast() {
		String url = String.format(
				"https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/%s/lat/%s/data.json",
				longitude, latitude);
		return restTemplate.getForEntity(url, WeatherForecastResponse.class);
	}

	private String createForecast(WeatherForecastResponse body) {
		String forecast = "Average temperature for today is %s degrees and tomorrow %s degrees";
		return String.format(forecast, Math.round(getAverageTempForDate(body, LocalDate.now())), Math.round(getAverageTempForDate(body, LocalDate.now().plusDays(1))));
	}

	private void speak(String forecast) {
		voice.allocate();
		voice.speak(forecast);
		voice.deallocate();
	}

	private double getAverageTempForDate(WeatherForecastResponse forecast, LocalDate localDate) {
		return forecast.getTimeSeries().stream()
				.filter(t -> t.getValidTime().toLocalDate().isEqual(localDate))
				.mapToDouble(t -> t.getParameters().stream().filter(p -> p.getName().equals("t")).findFirst().get().getValues().get(0))
				.average()
				.orElse(Double.NaN);
	}
}

