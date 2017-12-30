package se.lindquister.voicecontrol;

interface Command {
	void execute();

	String getMatch();
}
