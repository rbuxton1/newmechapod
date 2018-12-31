package com.ryanbuxton.newmechapod.main;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

import com.ryanbuxton.newmechapod.commands.*;
import com.ryanbuxton.newmechapod.talker.Talker;
import com.vdurmont.emoji.EmojiParser;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class Pod {
	private String token ="";
	private String ver = "4.0.2";
	private String patchNotes = "Official public release of mechapod! This version fixes some erros in the previous, namely the fact that"
			+" the countdown function in the 'schedule' command would go negative. Other than that this version implements s system by which"
			+ " we can load the preferences from a file (pod.config), allowing a user to set them through their text editing program of choice.";
	
	public Pod() {
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		try {
			prefs.importPreferences(new FileInputStream("pod.config"));
		} catch (Exception e) { e.printStackTrace(); }
		
		if(prefs.get("discordToken", "NONE").equals("NONE")) {
			Scanner in = new Scanner(System.in);
			System.out.println("Please enter a Discord Client Token!");
			token = in.nextLine();
			prefs.put("discordToken", token);
		}else {
			token = prefs.get("discordToken", "uhoh");
		}
		System.out.println("Connecting with " + token);
		
		try{
			DiscordApi api = new DiscordApiBuilder().setToken(token).login().get();
			System.out.println(api.createBotInvite(new PermissionsBuilder().setAllAllowed().build()));
			Talker talker = new Talker(prefs.get("talkerDir", "talker"));
			String announcementChannel = prefs.get("announceChannel", "NONE");
			
			
			String prefix = prefs.get("prefix", "!pod2");
			ArrayList<Command> cmds = new ArrayList<Command>();
			cmds.add(new EchoCommand(prefix));
			cmds.add(new ShutdownCommand(prefix, api));
			cmds.add(new EditPrefCommand(prefix, prefs));
			cmds.add(new ShowPrefCommand(prefix, prefs));
			cmds.add(new TalkCommand(prefix, talker));
			cmds.add(new TalkAddCommand(prefix, talker));
			if(prefs.getBoolean("allowReddit", false)) {
				try {
					UserAgent userAgent = new UserAgent("bot", "com.ryanbuxton.mechapod", ver, "mechapod");
					Credentials cred = Credentials.script(prefs.get("redditUsername", ""), prefs.get("redditPassword", ""), prefs.get("redditClientKey", ""), prefs.get("redditClientSecret", ""));
					NetworkAdapter adapt = new OkHttpNetworkAdapter(userAgent);
					RedditClient reddit = OAuthHelper.automatic(adapt, cred);
					cmds.add(new RedditTalkerCommand(prefix, reddit, talker));
					cmds.add(new TalkScheduleCommand(prefix, api, talker, reddit, true));
				} catch (Exception ex) {
					System.out.println("Error with loading reddit. Due to this the command will not be added. Remember that preference values 'redditUsername', 'redditPassword', 'redditClientKey', and 'redditClientSecret' all need to be set for this command to work."
							+ " Since this involves passwords and such, I reccomend doing it in a direct message with this bot.");
				}
			} else {
				System.out.println("'allowReddit' is not enabled, because of this you wont be able to use rshit to get images from reddit.");
			}
			if(prefs.getBoolean("allowTwitter", false) && !prefs.get("twitterEmote", "").equals("")) {
				try {
					ConfigurationBuilder cb = new ConfigurationBuilder();
					cb.setDebugEnabled(true)
						.setOAuthConsumerKey(prefs.get("twitterConsumerKey", ""))
						.setOAuthConsumerSecret(prefs.get("twitterConsumerSecret", ""))
						.setOAuthAccessToken(prefs.get("twitterAccessToken", ""))
						.setOAuthAccessTokenSecret(prefs.get("twitterAccessSecret", ""));
					TwitterFactory tf = new TwitterFactory(cb.build());
					Twitter twitter = tf.getInstance();
					
					api.addReactionAddListener(new ReactionAddListener() {
						@Override
						public void onReactionAdd(ReactionAddEvent event) {
							boolean r = event.getReaction().get().getEmoji().asUnicodeEmoji().get().equals(EmojiParser.parseToUnicode(prefs.get("twitterEmote", "NotAnEmoji")));
							//System.out.println(r+ " == " + EmojiParser.parseToUnicode(prefs.get("twitterEmote", "NotAnEmoji")));
							if(r && event.getMessage().get().getAuthor().asUser().get().isYourself()) {
								
								if(!event.getMessage().get().getContent().equals("")) {
									try {
										twitter.updateStatus(event.getMessage().get().getContent());
										event.getMessage().get().addReaction(EmojiParser.parseToUnicode(":ok_hand:"));
									} catch (Exception e) {
										event.getMessage().get().getChannel().sendMessage("whoops twitter didnt like that, heres what they say: ```" + e.getMessage() + "```");
									}
								} else if (!event.getMessage().get().getAttachments().isEmpty()) {
									try {
										StatusUpdate su = new StatusUpdate("");
										File f = new File("tweet.png");
										ImageIO.write(event.getMessage().get().getAttachments().get(0).downloadAsImage().get(), "png", f);
										su.setMedia(f);
										twitter.updateStatus(su);
										event.getMessage().get().addReaction(EmojiParser.parseToUnicode(":ok_hand:"));
									} catch (Exception e) {
										event.getMessage().get().getChannel().sendMessage("whoops twitter didnt like that, heres what they say: ```" + e.getMessage() + "```");
									}
								}
							}
						}
					});
				} catch (Exception ex) { ex.printStackTrace(); }
			} else {
				System.out.print("This version does not have a twitter output system. Probably for the better. To change this "
						+ "add 'twitterConsumerKey', 'twitterConsumerSecret', 'twitterAccessToken', and 'twitterAccessSecret'. Do this in a PM please. "
						+ "Also consider if you really need this.");
				System.out.println("You will also need to enable 'allowTwitter', and set a 'tweetEmote'. 'tweetEmote' would be just the string for the emote you want, ex 'bird'.");
			}
			if(!prefs.getBoolean("allowReddit", false))cmds.add(new TalkScheduleCommand(prefix, api, talker, null, false));
			cmds.add(new ReloadPrefCommand(prefix, prefs));
			cmds.add(new RemindCommand(prefix));
			cmds.add(new PickCommand(prefix, talker));
			
			//last
			cmds.add(new HelpCommand(prefix, cmds));
			for(Command c : cmds) api.addMessageCreateListener(c);
			
			if(!announcementChannel.equals("NONE")) {
				EmbedBuilder emb = new EmbedBuilder().setTitle("mechapod engaging version " + ver);
				emb.setDescription(patchNotes);
				emb.addField("Info", "this instance of mechapod has " + cmds.size() + " commands registered.");
				if(prefs.getBoolean("allowTwitter", false)) emb.addField("Twitter", "yeah thats right, this pod has a twitter. ask bot owner for the @");
				emb.setFooter("to disable this message, set 'announceChannel' to 'NONE'");
				emb.setColor(Color.green);
				api.getChannelById(announcementChannel).get().asServerTextChannel().get().sendMessage(emb);
			} else System.out.println("Set an announcement channel if youd like announcements on startup!");
			
			System.out.println("Loaded " + api.getMessageCreateListeners().size() + " commands (MessageCreateListener)");
			System.out.println("\nLoaded preferences are stored in pod.config for quick reference.");
			System.out.println("If you do not want to send API keys over Discord messages (you shouldnt), you can edit this file and on the next reboot it will be loaded.");
			prefs.exportSubtree(new FileOutputStream("pod.config"));
		} catch (Exception e) { e.printStackTrace(); }
	}
}
