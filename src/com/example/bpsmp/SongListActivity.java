package com.example.bpsmp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.view.View; 
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SongListActivity extends ListActivity {
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.songlist);
    	ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();
    	
    	SongsManager plm = new SongsManager();
    	this.songsList = plm.getPlayList();
    	
    	for(int i=0; i < songsList.size(); i++ ){
    		HashMap<String, String> song = songsList.get(i);
    		songsListData.add(song);
    		
    	}
    	
    	//put the songslist into listview
    	ListAdapter adapter = new SimpleAdapter(this, songsListData,
    			R.layout.songlist_item, new String[] { "songTitle" }, new int[] {
    			R.id.song_title });
    	
    	setListAdapter(adapter);
    	
    	ListView listview = getListView();
    	listview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
		
    		
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view,
    				int position, long id) {
		    			// getting listitem index
		    			int songIndex = position;
		    			String str = songsList.get(songIndex).get("songTitle");
		    			
		    			Intent intent = new Intent(getApplicationContext(),
		    					MainActivity.class);
		    			// Sending songIndex to PlayerActivity
		    			intent.putExtra("songIndex", songIndex);
		    			intent.putExtra("str", str);
		    			setResult(1, intent);
		    			// Closing PlayListView
		    			finish();
    				} 
    		
    		});
    	}


}

