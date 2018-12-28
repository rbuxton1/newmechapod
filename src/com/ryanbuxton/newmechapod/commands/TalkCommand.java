package com.ryanbuxton.newmechapod.commands;

import org.javacord.api.event.message.MessageCreateEvent;

import com.ryanbuxton.newmechapod.talker.Talker;

public class TalkCommand extends Command{
	private Talker talker;

	public TalkCommand(String prefix, Talker talker) {
		super(prefix, "shit", "makes mechapod do what mechapod is known for. Usage: '" + prefix + " shit'");
		this.talker = talker;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent())) {
			e.getChannel().sendMessage(talker.generate());
		}
	}
}
