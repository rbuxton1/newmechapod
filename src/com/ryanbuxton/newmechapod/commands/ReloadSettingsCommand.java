package com.ryanbuxton.newmechapod.commands;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import java.util.prefs.Preferences;

import org.javacord.api.event.message.MessageCreateEvent;
import org.yaml.snakeyaml.Yaml;

import com.ryanbuxton.newmechapod.settingmanager.Settings;

public class ReloadSettingsCommand extends Command{
	private Settings settings;
	
	public ReloadSettingsCommand(String prefix, Settings settings) {
		super(prefix, "reloadpref", "This forces a reload of the Preferences file. User has to be the bot owner, and some preferences (like prefix) will not be affected until a reboot happens. Usage: " + prefix + " reloadpref ");
		this.settings = settings;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent()) && e.getMessageAuthor().isBotOwner()) {
			try{
				new File("config.yml").createNewFile();
				Yaml yaml = new Yaml();
				settings = yaml.load(new FileInputStream("config.yml"));
				String titles[] = {"boss", "cheif", "b", "man", e.getMessageAuthor().getDisplayName()};
				Random r = new Random();
				e.getChannel().sendMessage("reloaded, " + titles[r.nextInt(titles.length)]);
			} catch (Exception ex) {
				e.getChannel().sendMessage("uh oh, no bueno. read this: ``" + ex.getLocalizedMessage() + "``");
			}
		}
	}
}