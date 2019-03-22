package com.ryanbuxton.newmechapod.commands;

import java.util.prefs.Preferences;

import org.javacord.api.event.message.MessageCreateEvent;

import com.ryanbuxton.newmechapod.talker.Talker;

public class TalkAddCommand extends Command{
	private Talker talker;
	private Preferences prefs;
	
	public TalkAddCommand(String prefix, Talker talker, Preferences prefs) {
		super(prefix, "add", "adds to mechapods famous talk box. Usage: '" + prefix + " add [noun|verb|adv|adj|template] [item]'");
		this.talker = talker;
		this.prefs = prefs;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent()) /*&& super.isManager(settings, e)*/) {
			String[] args = super.parseArgs(e.getMessageContent());
			String type = args[0].toLowerCase();
			String data = "";
			for(int i = 1; i < args.length; i++) data += args[i] + " ";
			data = data.substring(0, data.length() -1);
			if(type.equals("noun")) {
				talker.add(data, Talker.NOUN);
				e.getChannel().sendMessage("added '"+ data + "' as type '"+ type + "'.");
			} else if(type.equals("verb")) {
				talker.add(data, Talker.VERB);
				e.getChannel().sendMessage("added '"+ data + "' as type '"+ type + "'.");
			} else if(type.equals("adv")) {
				talker.add(data, Talker.ADV);
				e.getChannel().sendMessage("added '"+ data + "' as type '"+ type + "'.");
			} else if(type.equals("adj")) {
				talker.add(data, Talker.ADJ);
				e.getChannel().sendMessage("added '"+ data + "' as type '"+ type + "'.");
			} else if(type.equals("template")) {
				talker.add(data, Talker.TEMPLATE);
				e.getChannel().sendMessage("added '"+ data + "' as type '"+ type + "'.");
			} else
				e.getChannel().sendMessage("something doesnt seem right with that. try again");
		}
	}
}
