package se.lindquister.voicecontrol;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class SpeechInputHandler {

	private static final String ACOUSTIC_MODEL = "resource:/edu/cmu/sphinx/models/en-us/en-us";
	@Value("${dictionary.location}")
	private String DICTIONARY_PATH;
	@Value("${languagemodel.location}")
	private String LANGUAGE_MODEL;
	@Autowired
	private CommandConfig commandConfig;


	@PostConstruct
	public void init() throws Exception {
		startRecognizer();
	}

	private void startRecognizer() throws IOException {
		LiveSpeechRecognizer recognizer = createRecognizer();
		recognizer.startRecognition(true);

		try {
			while (true) {
				SpeechResult result = recognizer.getResult();
				String utterance = result.getHypothesis();
				System.out.println(utterance);

				commandConfig.getCommands().stream()
						.filter(c -> StringUtils.equalsAnyIgnoreCase(utterance, c.getMatch()))
						.findFirst()
						.ifPresent(Command::execute);
				if (StringUtils.startsWithIgnoreCase(utterance, "EXIT VOICECONTROL")) {
					break;
				}

			}
		} finally {
			recognizer.stopRecognition();
		}
	}

	private LiveSpeechRecognizer createRecognizer() throws IOException {
		Configuration configuration = new Configuration();
		configuration.setAcousticModelPath(ACOUSTIC_MODEL);
		configuration.setDictionaryPath(DICTIONARY_PATH);
		configuration.setLanguageModelPath(LANGUAGE_MODEL);
		configuration.setUseGrammar(false);
		return new LiveSpeechRecognizer(configuration);
	}
}

