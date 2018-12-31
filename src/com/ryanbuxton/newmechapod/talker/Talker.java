package com.ryanbuxton.newmechapod.talker;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Talker {
	private String path;
	private File verbFile, adjFile, nounFile, advFile, templateFile;
	private ArrayList<String> verbs, adjs, nouns, advs, templates;
	
	public final static int VERB = 0;
	public final static int ADJ = 1;
	public final static int ADV = 2;
	public final static int NOUN = 4;
	public final static int TEMPLATE =5;
	
	public Talker(String path) throws IOException{
		verbs = new ArrayList<String>();
		adjs = new ArrayList<String>();
		nouns = new ArrayList<String>();
		advs = new ArrayList<String>();
		templates = new ArrayList<String>();
		
		verbFile = new File(path + File.separator + "verbs.txt");
		verbFile.createNewFile();
		Scanner in = new Scanner(verbFile);
		while(in.hasNextLine()){
			verbs.add(in.nextLine());
		}
		
		adjFile = new File(path + File.separator + "adjs.txt");
		adjFile.createNewFile();
		in = new Scanner(adjFile);
		while(in.hasNextLine()){
			adjs.add(in.nextLine());
		}
		
		nounFile = new File(path + File.separator + "nouns.txt");
		nounFile.createNewFile();
		in = new Scanner(nounFile);
		while(in.hasNextLine()){
			nouns.add(in.nextLine());
		}
		
		advFile = new File(path + File.separator + "advs.txt");
		advFile.createNewFile();
		in = new Scanner(advFile);
		while(in.hasNextLine()){
			advs.add(in.nextLine());
		}
		
		templateFile = new File(path + File.separator + "templates.txt");
		templateFile.createNewFile();
		in = new Scanner(templateFile);
		while(in.hasNextLine()){
			templates.add(in.nextLine());
		}
	}
	
	public String generate(){
		Random r = new Random();
		String gen = templates.get((int) (Math.random() * templates.size()));
		while(gen.contains("!v")){
			gen = gen.replaceFirst(Pattern.quote("!v"), verbs.get((int) (Math.random() * verbs.size())));
		}
		while(gen.contains("!adj")){
			gen = gen.replaceFirst(Pattern.quote("!adj"), adjs.get((int) (Math.random() * adjs.size())));
		}
		while(gen.contains("!n")){
			gen = gen.replaceFirst(Pattern.quote("!n"), nouns.get((int) (Math.random() * nouns.size())));
		}
		while(gen.contains("!adv")){
			gen = gen.replaceFirst(Pattern.quote("!adv"), advs.get((int) (Math.random() * advs.size())));
		}
		while(gen.contains("!#")) {
			gen = gen.replaceFirst("!#", "" + r.nextInt(Integer.MAX_VALUE));
		}
		while(gen.contains("!%")) {
			gen = gen.replaceFirst("!%", r.nextInt(100) + "%");
		}
		return gen;
	}
	
	public String randomGenerate() {
		String ret = "";
		Random gen = new Random();
		int chars = gen.nextInt(30) + 10;
		while(ret.length() < chars) {
			switch(gen.nextInt(5)) {
				case 0:
					ret += nouns.get(gen.nextInt(nouns.size()));
					break;
				case 1:
					ret += verbs.get(gen.nextInt(verbs.size()));
					break;
				case 2:
					ret += adjs.get(gen.nextInt(adjs.size()));
					break;
				case 3:
					ret += advs.get(gen.nextInt(advs.size()));
					break;
				case 4:
					switch(gen.nextInt(5)) {
						case 0:
							ret += ".";
							break;
						case 1:
							ret += ",";
							break;
						case 2:
							ret += "!";
							break;
						case 3:
							ret += "?";
							break;
						case 4: 
							ret += ";";
							break;
					}
					break;
			}
			ret += " ";
		}
		return ret;
	}
	
	public void add(String word, int type){
		FileWriter writer;
		try {
			switch(type){
				case VERB:
					verbs.add(word);
					writer = new FileWriter(verbFile, true);
					writer.append(word + "\n");
					writer.close();
					break;
				case ADJ:
					adjs.add(word);
					writer = new FileWriter(adjFile, true);
					writer.append(word + "\n");
					writer.close();
					break;
				case NOUN:
					nouns.add(word);
					writer = new FileWriter(nounFile, true);
					writer.append(word + "\n");
					writer.close();
					break;
				case ADV:
					advs.add(word);
					writer = new FileWriter(advFile, true);
					writer.append(word + "\n");
					writer.close();
					break;
				case TEMPLATE:
					templates.add(word);
					writer = new FileWriter(templateFile, true);
					writer.append(word + "\n");
					writer.close();
					break;
			}
		} catch (IOException e) {}
	}
	
	public void remove(String word, int type){
		FileWriter writer;
		try {
			switch(type){
			case VERB:
				verbs.remove(verbs.indexOf(word));
				writer = new FileWriter(verbFile, false);
				writer.write(getAll(VERB));
				writer.close();
				break;
			case ADJ:
				adjs.remove(adjs.indexOf(word));
				writer = new FileWriter(adjFile, false);
				writer.write(getAll(ADJ));
				writer.close();
				break;
			case NOUN:
				nouns.remove(nouns.indexOf(word));
				writer = new FileWriter(nounFile, false);
				writer.write(getAll(NOUN));
				writer.close();
				break;
			case ADV:
				advs.remove(advs.indexOf(word));
				writer = new FileWriter(advFile, false);
				writer.write(getAll(ADV));
				writer.close();
				break;
			case TEMPLATE:
				templates.remove(templates.indexOf(word));
				writer = new FileWriter(templateFile, false);
				writer.write(getAll(TEMPLATE));
				writer.close();
				break;
		}
		} catch (IOException e) {}
	}
	
	public String getAll(int type){
		String ret = "";
		switch(type){
			case VERB:
				for(String s : verbs) ret += s + "\n";
				break;
			case ADJ:
				for(String s : adjs) ret += s + "\n";
				break;
			case NOUN:
				for(String s : nouns) ret += s + "\n";
				break;
			case ADV:
				for(String s : advs) ret += s + "\n";
				break;
			case TEMPLATE:
				for(String s : templates) ret += s + "\n";
				break;
		}
		return ret;
	}
	
	public ArrayList<String> getList(int type){
		ArrayList<String> ret = null;
		switch(type){
			case VERB:
				ret = verbs;
				break;
			case ADJ:
				ret = adjs;
				break;
			case NOUN:
				ret = nouns;
				break;
			case ADV:
				ret = advs;
				break;
			case TEMPLATE:
				ret = templates;
				break;
		}
		return ret;
	}

}