package com.ryanbuxton.newmechapod.commands;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;

import com.ryanbuxton.nemechapod.util.ImageGenerator;
import com.ryanbuxton.newmechapod.talker.Talker;

import net.dean.jraw.RedditClient;

public class TalkTextScheduleMinuteCommand extends Command {
	private Timer timer;
	private DiscordApi api;
	private Talker talker;
	
	public TalkTextScheduleMinuteCommand(String prefix, DiscordApi api, Talker talker) {
		super(prefix, "scheduleminute", "schedules regular dosings of mechapod brand shitposts, just like the doctor ordered. This version does NOT update the status of mechapod, and is ONLY text. Usage: '" + prefix+ " schedule [channel id] [delay (in minutes)]'");
		timer = new Timer();
		this.api = api;
		this.talker = talker;
	}

	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent())) {
			String[] args = super.parseArgs(e.getMessageContent());
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					api.getChannelById(args[0]).get().asServerTextChannel().get().sendMessage(talker.generate());
				}
			}, 1, TimeUnit.MINUTES.toMillis(Long.parseLong(args[1])));
			
			e.getChannel().sendMessage("scheduled to post in " + TimeUnit.MINUTES.toMillis(Long.parseLong(args[1])) + " milliseconds. be in channel id '" + args[0] + "' to see it first");
			//api.getChannelById(args[0]).get().asServerTextChannel().get().sendMessage("prepare for mechanical shitposts in " + args[1] + " hours. you have been warned.");
		}
	}
}
