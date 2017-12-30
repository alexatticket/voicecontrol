# voicecontrol
Spring boot app that uses sphinx4 (https://github.com/cmusphinx/sphinx4/) to match spoken words to execute commands.

Currently supports controlling spotify on a mac and opening bolibompa website.

Configure new commands by adding entries in application.yaml.
Like this:

	match: "DECREASE VOLUME"
    shellCommand: "/usr/bin/osascript -e 'set volume output volume (output volume of (get volume settings) -10)'"
    
Then add the new command, e.g. 'DECREASE VOLUME' to sentences.txt and create  a new dictionary here:
http://www.speech.cs.cmu.edu/tools/lmtool-new.html

Download the files and change the dictionary and language model configuration locations.
E.g. dictionary.location=resource:/7641/7641.dic