package com.ryanbuxton.newmechapod.settingmanager;

public class Settings {
	//General purpose settings
	public String discordToken;
	public String talkerDir;
	public String announceChannel;
	public String prefix;
	public String manageRole;
	
	//Reddit specific settings
	public boolean allowReddit;
	public String redditUsername;
	public String redditPassword;
	public String redditClientKey;
	public String redditClientSecret;
	
	//Twitter specific settings
	public boolean allowTwitter;
	public String twitterEmote;
	public String twitterConsumerKey;
	public String twitterConsumerSecret;
	public String twitterAccessToken;
	public String twitterAccessSecret;
}
