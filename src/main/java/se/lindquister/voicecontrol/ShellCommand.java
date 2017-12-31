package se.lindquister.voicecontrol;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@NoArgsConstructor
@Getter
@Setter
public class ShellCommand extends Command {

	private String shellCommand;

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
