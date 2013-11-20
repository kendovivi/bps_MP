package com.example.bpsmp;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {
	
	//path to songs, need to change, will change to read all file in the directory 
	final String MEDIA_PATH = new String("/storage/sdcard0/DUOMI/down/"); 
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    
    // Constructor      
    public SongsManager(){
    	
    }
    
    
    public ArrayList<HashMap<String, String>> getPlayList(){
    	
    	File home = new File(MEDIA_PATH);
    	
    	if (home.listFiles(new FileExtensionFilter()).length > 0) {
    		for (File file : home.listFiles(new FileExtensionFilter())) {
    			HashMap<String, String> song = new HashMap<String, String>();
    			song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
    			song.put("songPath", file.getPath());
    			// Adding each song to SongList
    			songsList.add(song);
    		}
    	}          // return songs list array
    		    	
    	return 	songsList;
    }
    
    //return only files with '.mp3' in the end of its name
    class FileExtensionFilter implements FilenameFilter{
    	public boolean accept(File dir, String name) {
    		return (name.endsWith(".mp3") || name.endsWith(".MP3"));
    	}
    }
}
