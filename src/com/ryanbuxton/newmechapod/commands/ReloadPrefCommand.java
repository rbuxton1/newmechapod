package com.ryanbuxton.newmechapod.commands;

import java.io.FileInputStream;
import java.util.Random;
import java.util.prefs.Preferences;

import org.javacord.api.event.message.MessageCreateEvent;

public class ReloadPrefCommand extends Command{
	private Preferences pref;
	
	public ReloadPrefCommand(String prefix, Preferences pref) {
		super(prefix, "reloadpref", "This forces a reload of the Preferences file. User has to be the bot owner, and some preferences (like prefix) will not be affected until a reboot happens. Usage: " + prefix + " reloadpref ");
		this.pref = pref;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent()) && e.getMessageAuthor().isBotOwner()) {
			try{
				pref.importPreferences(new FileInputStream("pod.config"));
				String titles[] = {"boss", "cheif", "b", "man", e.getMessageAuthor().getDisplayName()};
				Random r = new Random();
				e.getChannel().sendMessage("reloaded, " + titles[r.nextInt(titles.length)]);
			} catch (Exception ex) {
				e.getChannel().sendMessage("uh oh, no bueno. read this: ``" + ex.getLocalizedMessage() + "``");
			}
		}
	}
}