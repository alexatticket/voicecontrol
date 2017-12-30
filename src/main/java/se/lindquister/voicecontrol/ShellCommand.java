package se.lindquister.voicecontrol;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@NoArgsConstructor
@Getter
@Setter
public class ShellCommand implements Command {
	private String match;
	private String shellCommand;

	@Override
	public String getMatch() {
		return match;
	}

	public void execute() {
		String[] command = {"/bin/bash", "-c", shellCommand};
		ProcessBuilder pb = new ProcessBuilder(command);
		try {
			Process p = pb.start();
			int exitCode = p.waitFor();
			System.out.println(exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
