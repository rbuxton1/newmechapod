package com.ryanbuxton.newmechapod.commands;

import java.util.prefs.Preferences;

import org.javacord.api.event.message.MessageCreateEvent;

public class ShowPrefCommand extends Command{
	private Preferences prefs;
	
	public ShowPrefCommand(String prefix, Preferences prefs) {
		super(prefix, "showpref", "shows a stored preference. Note: this command can only be ran by the bot owner. Usage: '" + prefix + " showpref [preference name]'");
		this.prefs = prefs;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent()) && e.getMessage().getAuthor().isBotOwner()) {
			String[] args = super.parseArgs(e.getMessageContent());
			System.out.println(args.length);
			e.getChannel().sendMessage("the preference '" + args[0] + "' is set to '" + prefs.get(args[0], "NOT SET!") + "'");
		}
	}
}
