package net.upd4ting.networklib.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public final class FichierLog {
	private static final FichierLog instance = new FichierLog("latest.log");
	
	private String fileName;
	
	private FichierLog(String fileName) {
		this.fileName = fileName;
	}
	
	public void writeLog(Level level, String log) {
		try {
			Date date = new Date();
			FileWriter fw = new FileWriter(fileName, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("[" + date + "] " + level + " : " + log);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public static FichierLog getInstance() {
		return instance;
	}
}
