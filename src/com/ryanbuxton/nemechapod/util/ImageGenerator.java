package com.ryanbuxton.nemechapod.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.ryanbuxton.newmechapod.talker.Talker;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.pagination.DefaultPaginator;

public class ImageGenerator {
	private RedditClient rc;
	private Talker talker;
	
	public ImageGenerator(RedditClient rc, Talker talker) {
		this.rc = rc;
		this.talker = talker;
	}

	public File generateImageFromSubreddit(String subreddit) throws IOException {
		DefaultPaginator<Submission> paginator = rc.subreddit(subreddit).posts()
				.limit(250) // 50 posts per page
			    .sorting(SubredditSort.HOT) // top posts
			    .build();
		Random r = new Random();
		
		Listing<Submission> posts = paginator.next();
		Submission s = posts.get(r.nextInt(posts.size()));
		File f = new File(s.getUrl());
		
		BufferedImage img = ImageIO.read(new URL(s.getUrl()));
		Graphics g = img.getGraphics();
		Font font = g.getFont().deriveFont(img.getHeight()/15f);
		g.setFont(font);
		
		Rectangle rect = new Rectangle(img.getWidth(), (img.getHeight()/6) * 3);
		rect.setLocation(0, r.nextInt((img.getHeight()/3) * 2));
		drawCenteredString(g, talker.generate(), rect, font);
		
		while(img.getWidth() > 1920 || img.getHeight() > 1080) {
			img = resize(img, img.getWidth() /2, img.getHeight() /2);
		}
		
		File out = new File("output.png");
		ImageIO.write(img, "png", out);
		return out;
	}
	
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    FontMetrics fm = g.getFontMetrics(font);
	    if(fm.stringWidth(text) > rect.getWidth()) {
	    	ArrayList<String> lines = new ArrayList<String>();
	    	lines.add(0, "");
	    	String[] raw = text.split(" ");
	    	int count = 0;
	    	for(String s : raw) {
	    		if(fm.stringWidth(lines.get(count) + " " + s) < rect.getWidth()) {
	    			lines.set(count, lines.get(count) + " " + s);
	    		} else {
	    			lines.add(s);
	    			count++;
	    		}
	    	}
	    	
	    	for(int i =0; i < lines.size(); i++) {
	    		g.drawString(lines.get(i), (int) rect.getX(), (int) rect.getY() + (i * fm.getHeight())); 
	    	}
	    } else {
	    	g.drawString(text, (int) rect.getX(), (int) rect.getY());
	    }
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;  
    } 
}
