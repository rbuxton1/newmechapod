package com.ryanbuxton.newmechapod.commands;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;

import com.ryanbuxton.nemechapod.util.ImageGenerator;
import com.ryanbuxton.newmechapod.settingmanager.Settings;
import com.ryanbuxton.newmechapod.talker.Talker;

import net.dean.jraw.RedditClient;

public class TalkScheduleCommand extends Command {
	private Timer timer;
	private DiscordApi api;
	private Talker talker;
	private double timeR = -1;
	private ImageGenerator igen;
	private boolean reddit = false;
	private Settings settings;
	
	public TalkScheduleCommand(String prefix, DiscordApi api, Talker talker, RedditClient rc, boolean reddit, Settings settings) {
		super(prefix, "schedule", "schedules regular dosings of mechapod brand shitposts, just like the doctor ordered. Usage: '" + prefix+ " schedule [channel id] [delay (in hours)]'");
		timer = new Timer();
		this.api = api;
		this.talker = talker;
		igen = new ImageGenerator(rc, talker);
		this.reddit = reddit;
		this.settings = settings;
	}

	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent()) && super.isManager(settings, e)) {
			String[] args = super.parseArgs(e.getMessageContent());
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					//TODO: Allow for multiple kinds of posts, IE reddit etc
					Random gen = new Random();
					
					String[] subreddits = {
							"osha",
							"hmm",
							"hmmm",
							"cursedimgaes",
							"mountains",
							"flowers"};
					
					switch(gen.nextInt(2)) {
						case 0:
							api.getChannelById(args[0]).get().asServerTextChannel().get().sendMessage(talker.generate());
							timeR = Double.parseDouble(args[1]);
							break;
						case 1:
							try{
								if(reddit)api.getChannelById(args[0]).get().asServerTextChannel().get().sendMessage(igen.generateImageFromSubreddit(subreddits[gen.nextInt(subreddits.length)]));
								else api.getChannelById(args[0]).get().asServerTextChannel().get().sendMessage(talker.generate());
							} catch (Exception ex){
								api.getChannelById(args[0]).get().asServerTextChannel().get().sendMessage(talker.generate());
							}
							break;
						default:
							api.getChannelById(args[0]).get().asServerTextChannel().get().sendMessage(talker.generate());
							break;
					}
					timeR = Double.parseDouble(args[1]);
				}
			}, 1, TimeUnit.HOURS.toMillis(Long.parseLong(args[1])));
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					timeR = timeR - .0833;
					Random gen = new Random();
					String[] acts = {"bringing home the bacon in ",
									 "holding down e in ",
									 "rushing b in ",
									 "screaming in ",
									 "breaking down in ",
									 "connecting in ",
									 "booting up in ",
									 "apeing out in "};
					String time = timeR + "";
					time = time.substring(0, 4);
					api.updateActivity(acts[gen.nextInt(acts.length)] + time + " hours");
				}
			}, 1, TimeUnit.HOURS.toMillis(1) / 12);
			e.getChannel().sendMessage("scheduled to post in " + TimeUnit.HOURS.toMillis(Long.parseLong(args[1])) + " milliseconds. be in channel id '" + args[0] + "' to see it first");
			//api.getChannelById(args[0]).get().asServerTextChannel().get().sendMessage("prepare for mechanical shitposts in " + args[1] + " hours. you have been warned.");
		}
	}
}
