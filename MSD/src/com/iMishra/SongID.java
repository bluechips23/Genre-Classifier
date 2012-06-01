package com.iMishra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;

public class SongID {
	
	protected static String fileName = "/Users/Sudip/Documents/MSD/MillionSongSubset/data/SongIDListwGenre-May15-A.csv";
	
	protected static int totalAPICount = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			  
			  FileInputStream fstream = new FileInputStream("/Users/Sudip/Documents/MSD/MillionSongSubset/data/GenreList5.txt");
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  int count = 1;
			  while ((strLine = br.readLine()) != null)   {
				System.out.println("Genre: " + count++ + "\n");
			  	getSongID(strLine);
			  }
			  in.close();
		}catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
		}
		
	}
	
	public static void getSongID(String value) {
		String results = "";
		String line = null;
		int songCount = 1;
		int start;
		for(start = 0; start<=9; start++) {
			
		try {
			int startValue = start*100;
			
			String apiCall = "http://developer.echonest.com/api/v4/song/search?api_key=N6E4NIOVYMTHNDM8J&format=json&style=" +
      			value +"&results=100&start="+startValue;
			System.out.println(apiCall);
			
	        URL songs = new URL(apiCall);
	        
	        if(songCount % 60 == 0) {
	        
	        	Thread.sleep(600);
	        
	        }
	        
	        URLConnection tc = songs.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                  tc.getInputStream()));
	          
	          while ((line = in.readLine()) != null) {
	          	results = line;
	          	//System.out.println(line);
	          }
	  
	          in.close();
	      } catch (MalformedURLException e) {
	          e.printStackTrace();
	      } catch (IOException e) {
	          e.printStackTrace();
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
		
	      InputStream input = null;
	      
	      try {
	    	  input = new ByteArrayInputStream(results.getBytes("UTF-8"));
	      } catch (UnsupportedEncodingException e) {
	    	  e.printStackTrace();
	      }
	      String jsonTxt = null;
			
	      try {
				jsonTxt = IOUtils.toString(input);
				//System.out.println(jsonTxt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
		JSONObject jObject = null;
		try {
			jObject = (JSONObject)JSONSerializer.toJSON(jsonTxt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONArray songs = jObject.getJSONObject("response").getJSONArray("songs");
		ArrayList<String> songIDList = new ArrayList<String>();
		
		for(int i = 0; i < songs.size(); i++){
			String songID = songs.getJSONObject(i).getString("id");
			//System.out.println(songID);
			//System.out.println("\tSong Count: " + songCount++ + " Song ID: "+ songID + "\n");
			songIDList.add(songID);
		}
		
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
	 
		    for(int i = 0; i < songIDList.size(); i++) {
		    	writer.append(songIDList.get(i));
		    	writer.append(",");
		    	writer.append(value);
		    	writer.append('\n');
		    }
	 	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
		
		}
		
	}

}
