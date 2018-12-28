package com.ryanbuxton.newmechapod.commands;

import java.util.Random;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;

public class ShutdownCommand extends Command{
	private DiscordApi api;
	private String[] byes = { "damn alright, " , "oof, ", "aight i guess ill see myself out, ",
								"raise mouse, ", "rip me, "};
	
	public ShutdownCommand(String prefix, DiscordApi api) {
		super(prefix, "shutdown", "kills the lad. Usage: '" +prefix + " shutdown'");
		this.api = api;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent()) && e.getMessage().getAuthor().isBotOwner()) {
			Random gen = new Random();
			e.getChannel().sendMessage(byes[gen.nextInt(byes.length)] + "disengaging");
			try{ Thread.sleep(250); } catch (Exception ex) {}
			api.disconnect();
			System.exit(0);
		}
	}
}
