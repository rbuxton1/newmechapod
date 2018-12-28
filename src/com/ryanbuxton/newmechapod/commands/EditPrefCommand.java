package com.ryanbuxton.newmechapod.commands;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.javacord.api.event.message.MessageCreateEvent;

public class EditPrefCommand extends Command{
	private Preferences prefs;
	
	public EditPrefCommand(String prefix, Preferences prefs) {
		super(prefix, "editpref", "Edits a preference. Note: this command can only be ran by the bot owner. Usage: '" + prefix + " editprefs [preference name] [new value]'");
		this.prefs = prefs;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent()) && e.getMessage().getAuthor().isBotOwner()) {
			String[] args = super.parseArgs(e.getMessageContent());
			if(!args[0].equals("CLEAR_ALL")){
				prefs.put(args[0], args[1]);
			
				e.getChannel().sendMessage("the preference '" + args[0] + "' is now set to '" + prefs.get(args[0], "NOT SET!") + "'. remeber that some of these preferences will not change until a reboot.");
		
			} else {
				try {
					prefs.clear();
				} catch (BackingStoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.getChannel().sendMessage("cleared.");
			}
		}
	}
}
