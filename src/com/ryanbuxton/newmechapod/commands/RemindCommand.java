package com.ryanbuxton.newmechapod.commands;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.javacord.api.event.message.MessageCreateEvent;

import com.vdurmont.emoji.EmojiParser;

public class RemindCommand extends Command{
	private Timer timer;
	
	public RemindCommand(String prefix) {
		super(prefix, "remind", "Reminds the person whp runs the command after the spefied time. Usage !" + prefix + " remind [time (1h = 1 hour, 1m = 1 minute, 1d = 1 day) NOTE: only accepts one measurement!] [text (optional)]");
		timer = new Timer();
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent())) {
			String[] args = super.parseArgs(e.getMessageContent());
			if(args[0].toLowerCase().contains("d")) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						String res = "Automated mechapod reminder from " + args[0] + "ago";
						if(args.length > 1) res = res + ": ``" + args[1] + "``";
						e.getMessageAuthor().asUser().get().sendMessage(res);
						e.getMessage().addReaction(EmojiParser.parseToUnicode(":envelope:"));
					}}, TimeUnit.DAYS.toMillis(Long.parseLong(args[0].substring(0, args[0].length()-1))));
			}
			if(args[0].toLowerCase().contains("h")) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						String res = "Automated mechapod reminder from " + args[0];
						if(args.length > 1) res = res + ": ``" + args[1] + "``";
						e.getMessageAuthor().asUser().get().sendMessage(res);
						e.getMessage().addReaction(EmojiParser.parseToUnicode(":envelope:"));
					}}, TimeUnit.HOURS.toMillis(Long.parseLong(args[0].substring(0, args[0].length()-1))));
			}
			if(args[0].toLowerCase().contains("m")) {
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						String res = "Automated mechapod reminder from " + args[0];
						if(args.length > 1) res = res + ": ``" + args[1] + "``";
						e.getMessageAuthor().asUser().get().sendMessage(res);
						e.getMessage().addReaction(EmojiParser.parseToUnicode(":envelope:"));
					}}, TimeUnit.MINUTES.toMillis(Long.parseLong(args[0].substring(0, args[0].length()-1))));
				
			}
			e.getMessage().addReaction(EmojiParser.parseToUnicode(":ok_hand:"));
		}
	}
}
