package com.ryanbuxton.newmechapod.commands;

import java.util.ArrayList;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class Command implements MessageCreateListener{
	private String prefix, cmd, about;
	
	public Command(String prefix, String cmd, String about) {
		this.prefix = prefix.replace(" ", "");
		this.cmd = cmd;
		this.about = about;
	}
	
	public boolean isThisCommand(String s) {
		String[] vals = s.split(" ");
		for(int i = 0; i < vals.length; i++) {
			vals[i] = vals[i].replaceAll(" ", "");
		}
		if(prefix.length() > 1) {
			if(vals[0].equals(prefix) && vals[1].equals(cmd))
				return true;
			else 
				return false;
		} else {
			if(vals[0].equals(prefix + cmd))
				return true;
			else 
				return false;
		}
	}
	public String[] parseArgs(String s) {
		String[] raw = s.split(" ");
		String[] args;
		int startPos = 0;
		if(prefix.length() > 1) startPos = 2; 
		else startPos = 1;
		args = new String[raw.length - startPos];
		int c = 0;
		for(int i = startPos; i < raw.length; i++) {
			args[i - startPos] = raw[i];
		}
		return args;
	}

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		
	}

	public String getCmd() { return cmd; }
	public String getAbout() { return about; }
}
