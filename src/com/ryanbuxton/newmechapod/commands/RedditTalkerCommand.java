package com.ryanbuxton.newmechapod.commands;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.javacord.api.event.message.MessageCreateEvent;

import com.ryanbuxton.nemechapod.util.ImageGenerator;
import com.ryanbuxton.newmechapod.talker.Talker;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.pagination.DefaultPaginator;

public class RedditTalkerCommand extends Command{
	private RedditClient rc;
	private Talker talker;
	public RedditTalkerCommand(String prefix, RedditClient rc, Talker talker) {
		super(prefix, "rshit", "pulls an image from a requested subreddit and adds mechapod flavorings. Usage: '" + prefix + " rshit [subreddit name]'");
		this.rc = rc;
		this.talker = talker;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent e) {
		if(super.isThisCommand(e.getMessageContent())) {
			String[] args = super.parseArgs(e.getMessageContent());
			ImageGenerator igen = new ImageGenerator(rc, talker);
			new Thread(){
		        public void run(){
		        	try {
		        		System.out.print("baking . . .");
		        		e.getChannel().sendMessage(igen.generateImageFromSubreddit(args[0]));
		        		System.out.println(" done.");
		        	} catch(Exception ex) {  
						e.getChannel().sendMessage("that one was a little too much, try again. heres the error for some light reading later: ```" + ex.getLocalizedMessage() + "```");
					}
		        }
		      }.start();
		}
	}
}
