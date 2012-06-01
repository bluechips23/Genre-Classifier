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

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;

public class MSD {
	protected static String fileName = "/Users/Sudip/Documents/MSD/MillionSongSubset/data/fulloutput-may15-A.csv";
	protected static int count = 1;

	public static void getMSD(String songID, String genre) {
		  String results = "";
		  String line = null;
		  String apiCall = "http://developer.echonest.com/api/v4/song/profile?api_key=PKD9LLJPFILB9ZOX6&format=json&id=" + songID +
    		"&bucket=audio_summary";
		  
			try {
	          URL msd = new URL(apiCall);
	          //System.out.println(apiCall);
	          URLConnection tc = msd.openConnection();
	          BufferedReader in = new BufferedReader(new InputStreamReader(
	                  tc.getInputStream()));
	          
	          while ((line = in.readLine()) != null) {
	          	results = results + line;
	          	//System.out.println(line);
	          }
	  
	          in.close();
	      } catch (MalformedURLException e) {
	          e.printStackTrace();
	      } catch (IOException e) {
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
      
		
		JSONObject songs = jObject.getJSONObject("response").getJSONArray("songs").getJSONObject(0);
		JSONObject audioSummary = songs.getJSONObject("audio_summary");

		String key = audioSummary.getString("key");
		String mode = audioSummary.getString("mode");
		String duration = audioSummary.getString("duration");
		String loudness = audioSummary.getString("loudness");
		String danceability = audioSummary.getString("danceability");
		String energy = audioSummary.getString("energy");
		String tempo = audioSummary.getString("tempo");
		
		System.out.println(count++);
		
		ArrayList<String> exportData = new ArrayList<String>();
		exportData.add(songID);
		exportData.add(genre);
		exportData.add(key);
		exportData.add(mode);
		exportData.add(duration);
		exportData.add(danceability);
		exportData.add(energy);
		exportData.add(loudness);
		exportData.add(tempo);
		
		
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
	 
		    for(int i = 0; i < exportData.size(); i++) {
		    	writer.append(exportData.get(i));
		    	if(i != (exportData.size()-1)) {
		    		writer.append(",");
		    	}
		    }
		    writer.append('\n');
	 	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			  
			  FileInputStream fstream = new FileInputStream("/Users/Sudip/Documents/MSD/MillionSongSubset/data/SongIDListwGenre-May15-A.csv");
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  while ((strLine = br.readLine()) != null)   {
				  String[] songValues = strLine.split(",");
				  String songID = songValues[0];
				  String songGenre = songValues[1];

				  //if(count % 60 == 0) {
					  	//System.out.println("Sleeping... " + count + "\n");
				  		//Thread.sleep(600);
				  //}
			  	  getMSD(songID,songGenre);
			  	
			  }
			  in.close();
		}catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
		}
	}
		

}
