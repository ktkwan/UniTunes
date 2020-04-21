package edu.brown.cs.student.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/*class used to parse the file in order to build the tree*/
public class FileParsing {
	
	
	public FileParsing() {
		
	}
	
	public static List<String[]> parse_csv(String file){
		
//		System.out.println(file);
		List<String> lines = new ArrayList<String>();
    	List<String[]> song_info = new ArrayList<String[]>();
			BufferedReader csvReader = null;
			try {
				csvReader = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
			}
			String row;
			
			try {
				csvReader.readLine();
				while ((row = csvReader.readLine()) != null) {
					String[] arguments = row.split(",");
					song_info.add(arguments);
					
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				csvReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return song_info;
		
	}
}