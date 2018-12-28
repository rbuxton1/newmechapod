package com.ryanbuxton.newmechapod.commands;

import java.awt.Color;
import java.util.ArrayList;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

public class HelpCommand extends Command{
	private ArrayList<Command> cmds;
	
	public HelpCommand(String prefix, ArrayList<Command> cmds) {
		super(prefix, "help", "lists all the commands and their uses. Usage: '" + prefix + " help'");
		this.cmds = cmds;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent())) {
			EmbedBuilder emb = new EmbedBuilder().setTitle("Help");
			for(Command c: cmds) emb.addField(c.getCmd(), c.getAbout());
			emb.setColor(Color.CYAN);
			//emb.setFooter("For more info, see github.com/rbuxton1/newmechapod");
			e.getChannel().sendMessage(emb);
		}
	}
}
