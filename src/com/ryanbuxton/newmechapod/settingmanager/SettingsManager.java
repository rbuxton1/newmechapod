package com.ryanbuxton.newmechapod.settingmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class SettingsManager {
	private Yaml yaml;
	private Map<String, Object> settings;
	private String file;
	
	public SettingsManager(String file) {
		yaml = new Yaml();
		settings = new HashMap<String, Object>();
		this.file = file;
		
		try{
			new File(file).createNewFile();
			settings = yaml.load(new FileInputStream(file));
			if(settings == null) {
				settings = new HashMap<String, Object>();
				dumpSettings();
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void dumpSettings() {
		System.out.print("Dumping settings to '" + file + "' . . . ");
		try {
			yaml.dump(settings, new FileWriter(file));
			System.out.println("Done.");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public String getString(String key) {
		if (settings.containsKey(key)) return (String) settings.get(key);
		else return null;
	}
	public boolean getBoolean(String key) {
		return (boolean) settings.get(key);
	}

	public void set(String key, Object val) {
		System.out.println(settings == null);
		settings.put(key, val);
	}
	
	public void set(String key, Object val, boolean dump) {
		settings.put(key, val);
		if(dump) dumpSettings();
	}
}
