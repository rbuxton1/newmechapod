package com.ryanbuxton.newmechapod.commands;

import java.util.prefs.Preferences;

import org.javacord.api.event.message.MessageCreateEvent;

import com.ryanbuxton.newmechapod.settingmanager.Settings;

public class EchoCommand extends Command{
	private Settings settings;
	
	public EchoCommand(String prefix, Settings settings) {
		super(prefix, "echo", "Echos what you put in. Usage: '" + prefix + " echo [some text]'");
		this.settings = settings;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent()) && super.isManager(settings, e)) {
			String[] args = super.parseArgs(e.getMessageContent());
			String res = "";
			for(String s : args) res += s + " ";
			res = res.substring(0, res.length() -1);
			e.getChannel().sendMessage(res);
		}
	}
}
