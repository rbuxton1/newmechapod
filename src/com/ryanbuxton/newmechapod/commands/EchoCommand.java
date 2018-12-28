package com.ryanbuxton.newmechapod.commands;

import org.javacord.api.event.message.MessageCreateEvent;

public class EchoCommand extends Command{
	public EchoCommand(String prefix) {
		super(prefix, "echo", "Echos what you put in. Usage: '" + prefix + " echo [some text]'");
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent())) {
			String[] args = super.parseArgs(e.getMessageContent());
			String res = "";
			for(String s : args) res += s + " ";
			res = res.substring(0, res.length() -1);
			e.getChannel().sendMessage(res);
		}
	}
}
