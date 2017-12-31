package se.lindquister.voicecontrol;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class Command {

	private String match;

	abstract void execute();


}
