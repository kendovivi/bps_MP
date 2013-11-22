
package com.example.bpsmp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongListActivity extends ListActivity {

    public List<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlist);
        List<Mp3Info> mp3list = new ArrayList<Mp3Info>();

        SongManager plm = new SongManager();
        mp3list = plm.getMp3List(getApplicationContext());
        songsListData = plm.getMp3Maplist(mp3list);

        //fill mp3 data into listview adapter
        ListAdapter adapter = new SimpleAdapter(this, songsListData, R.layout.songlist_item,
                new String[] {
                    "mp3_title"
                }, new int[] {
                    R.id.song_title
                });

        setListAdapter(adapter);

        
        //listview item click case
        ListView listview = getListView();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting listitem index
                int songIndex = position;
                String str = songsListData.get(songIndex).get("mp3_title");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
