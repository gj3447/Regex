package com.regex.main;
import java.lang.String;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Func {
	public static String file_input(String file_name)
	{
		String folderpath = "C:/CD/JAVA/Regex/data/";
		String filepath = folderpath+file_name;
		try {
			FileInputStream fileStream = null;
			
			fileStream = new FileInputStream(filepath);
			byte[] buffer = new byte[fileStream.available()];
			while(fileStream.read(buffer)!=-1){}
			return new String(buffer);
		} catch(Exception e) {
			e.getStackTrace();
			return null;
		}
	}
}
