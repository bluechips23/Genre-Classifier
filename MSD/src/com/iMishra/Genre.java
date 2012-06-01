package com.iMishra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
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

public class Genre {

	protected static String fileName = "/Users/Sudip/Documents/MSD/MillionSongSubset/data/GenreList.txt";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getGenreList();
	}
	
	public static void getGenreList() {
		String results = "";
		String line = null;
		try {
	          URL genre = new URL(
	          		"http://developer.echonest.com/api/v4/artist/list_terms?api_key=N6E4NIOVYMTHNDM8J&format=json&type=style");
	          URLConnection tc = genre.openConnection();
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

		JSONArray terms = jObject.getJSONObject("response").getJSONArray("terms");
		String genre = "";
		ArrayList<String> genreList = new ArrayList<String>();
		
		for(int i = 0; i < terms.size(); i++) {
			genre = terms.getJSONObject(i).getString("name");
			genreList.add(genre);
			System.out.println(genre);
		}
		
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
	 
		    for(int i = 0; i < genreList.size(); i++) {
		    	writer.append(genreList.get(i));
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
