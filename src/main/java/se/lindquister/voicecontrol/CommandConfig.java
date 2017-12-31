package se.lindquister.voicecontrol;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "commandConfig")
@Getter
@Setter
public class CommandConfig {

	private List<ShellCommand> shellCommands;
	private List<WeatherForecastCommand> weatherForecastCommands;

	public Set<Command> getCommands() {
		Set<Command> commands = new HashSet<>();
		commands.addAll(weatherForecastCommands);
		commands.addAll(shellCommands);
		return commands;
	}
}
