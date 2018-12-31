# newmechapod
### "a mechapod for the peoples."

You've heard of [ShitpostBot 5000](https://twitter.com/ShitpostBot5000), now get ready for the worse version! Mechapod aims to be a multipurpose shitpost and utility bot that can be easily configured to work on mulitple servers and support multiple use scenarios. Another important feature of Mechapod is that it has been designed such that no one Mechapod will be alike, owing to the fact that every robo-hand crafted joke comes from the users mind. It was originally created just for my personal server, but I thought as an exercise in documenting and making a project as configurable as this it would be good to make it available on GitHub for others. 

Mechapod is built using [Javacord](https://github.com/Javacord/Javacord) and as such does not have an audio interface yet. At the time of writing this is in the works for the next major version of Javacord. I may do some experimentations as I learn more about using GitHub and as the Javacord audio support becomes more of a reality.

This code is all a passion project of mine and is something that I plan on working on as I have time and as I see fit. Any command reccomendations and general improvements are greatly appreciated, along with spellchecking my horrible spelling. Thanks for looking!

# How to
To unleash an unholy fire storm of flaming hot mechanized shit posts, all you must do is download the latest release under the [release tab](https://github.com/rbuxton1/newmechapod/releases). 

Recommended reading is listed under the [wiki](https://github.com/rbuxton1/newmechapod/wiki/), and should be of great help understanding this mess of a project.

## IMPORTANT!
To get it to work as smoothly as possible you should create both a directory called ``talker`` and a file called ``pod.config``, and atleast for the first run you should run the executable with sudo (or elevated command prompt) status. This is because it will need to create some files in the ``talker`` directory and some systems have problems with that with regular user privileges.

## ALSO IMPORTANT!
This code does not have a GUI and should be ran through a terminal. I recommend buying a raspberry pi, ssh-ing into it, and running the Java command (``sudo java -jar mechapod-[version]``) in a terminal multiplexer like Screen. I do not know how to do this on Windows. If someone knows how please let me know!

Warning: I am not responsible for any damage, and I cannot garuntee that this code will not cause your computer to explode or that it will not become more funny than you. 
