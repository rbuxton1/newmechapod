package com.ryanbuxton.newmechapod.commands;

import java.util.Random;

import org.javacord.api.event.message.MessageCreateEvent;

import com.ryanbuxton.newmechapod.talker.Talker;

public class PickCommand extends Command{
	private Talker talker;
	public PickCommand(String prefix, Talker talker) {
		super(prefix, "pick", "Picks from a comment seperated list. Usage: '" + prefix + " pick [option 1], [option 2], ... , [option N]'");
		this.talker = talker;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent())) {
			if(e.getMessageContent().contains(", ")) {
				String[] options = super.parseArgsAsCommaList(e.getMessageContent());
				Random r  = new Random();
				e.getChannel().sendMessage("i asked " + talker.getList(Talker.NOUN).get(r.nextInt(talker.getList(Talker.NOUN).size())) + ", they said ``" + options[r.nextInt(options.length)] + "``");
			} else {
				e.getChannel().sendMessage("I cant pick from one item thats not fair");
			}
		}
	}
}