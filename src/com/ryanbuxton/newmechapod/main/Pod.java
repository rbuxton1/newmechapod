package com.ryanbuxton.newmechapod.main;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.ryanbuxton.newmechapod.commands.*;
import com.ryanbuxton.newmechapod.settingmanager.Settings;
import com.ryanbuxton.newmechapod.settingmanager.SettingsManager;
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
	private String ver = "4.2.0";
	private String patchNotes = "Now has a minute schedule command. This is nice for rapid fire shitposts. ";
	private Settings settings;
	
	public Pod() {
		/*SettingsManager sm = new SettingsManager("pod.yml");
		sm.set("token", "StringTest");
		sm.set("booleanTest", true);
		sm.set("intTest", 12345);
		sm.dumpSettings();*/
		
		DumperOptions options = new DumperOptions();
	    //options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
		options.setWidth(5);
		//options.setPrettyFlow(true);
		Yaml yaml = new Yaml();
		try {
			new File("config.yml").createNewFile();
			settings = yaml.load(new FileInputStream("config.yml"));
			if(settings == null) {
				settings = new Settings();
				Scanner in = new Scanner(System.in);
				if(settings.discordToken == null) {
					System.out.println("Please enter a Discord Client Token!");
					settings.discordToken = in.nextLine();
				}
				if(settings.manageRole == null) {
					System.out.println("Please enter the ID for the role who can run management commands on mechapod! This step is very important!");
					settings.manageRole = in.nextLine();
				}
				if(settings.talkerDir == null) {
					System.out.println("Enter a directory for talker: ");
					settings.talkerDir = in.nextLine();
				}
				if(settings.prefix == null) {
					System.out.println("Enter a prefix for the instance: ");
					settings.prefix = in.nextLine();
				}
				
				yaml.dump(settings, new FileWriter("config.yml"));
			}
			
			System.out.println("SETTINGS: " + settings.allowReddit);
		} catch (Exception e) { settings = new Settings(); }
		
		System.out.println("Connecting with " + settings.discordToken);
		
		try{
			DiscordApi api = new DiscordApiBuilder().setToken(settings.discordToken).login().get();
			System.out.println(api.createBotInvite(new PermissionsBuilder().setAllAllowed().build()));
			Talker talker = new Talker(settings.talkerDir);
			
			ArrayList<Command> cmds = new ArrayList<Command>();
			cmds.add(new EchoCommand(settings.prefix, settings));
			cmds.add(new ShutdownCommand(settings.prefix, api));
			//cmds.add(new EditPrefCommand(settings.prefix, prefs));
			//cmds.add(new ShowPrefCommand(settings.prefix, prefs));
			cmds.add(new TalkCommand(settings.prefix, talker));
			//cmds.add(new TalkAddCommand(settings.prefix, talker, prefs));
			if(settings.allowReddit) {
				try {
					UserAgent userAgent = new UserAgent("bot", "com.ryanbuxton.mechapod", ver, "mechapod");
					Credentials cred = Credentials.script(settings.redditUsername, settings.redditPassword, settings.redditClientKey, settings.redditClientSecret);
					NetworkAdapter adapt = new OkHttpNetworkAdapter(userAgent);
					RedditClient reddit = OAuthHelper.automatic(adapt, cred);
					cmds.add(new RedditTalkerCommand(settings.prefix, reddit, talker));
					cmds.add(new TalkScheduleCommand(settings.prefix, api, talker, reddit, true, settings));
				} catch (Exception ex) {
					System.out.println("Error with loading reddit. Due to this the command will not be added. Remember that preference values 'redditUsername', 'redditPassword', 'redditClientKey', and 'redditClientSecret' all need to be set for this command to work."
							+ " Since this involves passwords and such, I reccomend doing it in a direct message with this bot.");
				}
			} else {
				System.out.println("'allowReddit' is not enabled, because of this you wont be able to use rshit to get images from reddit.");
			}
			if(settings.allowTwitter && settings.twitterEmote != null) {
				try {
					ConfigurationBuilder cb = new ConfigurationBuilder();
					cb.setDebugEnabled(true)
						.setOAuthConsumerKey(settings.twitterConsumerKey)
						.setOAuthConsumerSecret(settings.twitterConsumerSecret)
						.setOAuthAccessToken(settings.twitterAccessToken)
						.setOAuthAccessTokenSecret(settings.twitterAccessSecret);
					TwitterFactory tf = new TwitterFactory(cb.build());
					Twitter twitter = tf.getInstance();
					
					api.addReactionAddListener(new ReactionAddListener() {
						@Override
						public void onReactionAdd(ReactionAddEvent event) {
							//emote = settings.twitterEmote;
							boolean r = event.getReaction().get().getEmoji().asUnicodeEmoji().get().equals(EmojiParser.parseToUnicode(settings.twitterEmote));
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
			if(!settings.allowReddit)cmds.add(new TalkScheduleCommand(settings.prefix, api, talker, null, false, settings));
			cmds.add(new ReloadSettingsCommand(settings.prefix, settings));
			cmds.add(new RemindCommand(settings.prefix));
			cmds.add(new PickCommand(settings.prefix, talker));
			cmds.add(new TalkTextScheduleMinuteCommand(settings.prefix, api, talker, settings));
			
			//last
			cmds.add(new HelpCommand(settings.prefix, cmds));
			for(Command c : cmds) api.addMessageCreateListener(c);
			
			if(settings.announceChannel != null) {
				EmbedBuilder emb = new EmbedBuilder().setTitle("mechapod engaging version " + ver);
				emb.setDescription(patchNotes);
				emb.addField("Info", "this instance of mechapod has " + cmds.size() + " commands registered.");
				if(settings.allowTwitter) emb.addField("Twitter", "yeah thats right, this pod has a twitter. ask bot owner for the @");
				emb.addField("GitHub", "https://github.com/rbuxton1/newmechapod");
				emb.setFooter("to disable this message, set 'announceChannel' to 'NONE'");
				emb.setColor(Color.green);
				api.getChannelById(settings.announceChannel).get().asServerTextChannel().get().sendMessage(emb);
			} else System.out.println("Set an announcement channel if youd like announcements on startup!");
			
			System.out.println("Loaded " + api.getMessageCreateListeners().size() + " commands (MessageCreateListener)");
			System.out.println("\nLoaded preferences are stored in pod.config for quick reference.");
			System.out.println("If you do not want to send API keys over Discord messages (you shouldnt), you can edit this file and on the next reboot it will be loaded.");
			
			System.out.println("\nTo interact iwht mechapod, use " + settings.prefix + " in the server he has joined.");
			
			yaml.dump(settings, new FileWriter("config.yml"));
		} catch (Exception e) { e.printStackTrace(); }
	}
}
