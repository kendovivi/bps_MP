
package com.example.bpsmp;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongManager {

    /*
     * get mp3 infomation from storage
     */
    public List<Mp3Info> getMp3List(Context context) {

        // use contentProvider to get mp3 infomation from mobile storage
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        List<Mp3Info> mp3List = new ArrayList<Mp3Info>();

        // if next mp3 file exists, loop
        while (cursor.moveToNext()) {

            Mp3Info mp3 = new Mp3Info();
            mp3.setMusic(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)));
            if (mp3.isMusic() != 0) {
                mp3.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                mp3.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                mp3.setDuration(cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DURATION)));
                mp3.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                mp3.setSize(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                mp3.setUrl(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                mp3List.add(mp3);
            }

        }
        cursor.close();
        return mp3List;
    }

    /*
     * put mp3 information into map, so it can be displayed in listview return
     * list<Hash>
     */
    public List<HashMap<String, String>> getMp3Maplist(List<Mp3Info> mp3List) {
        List<HashMap<String, String>> mp3MapList = new ArrayList<HashMap<String, String>>();
        for (Mp3Info mp3 : mp3List) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("mp3_title", mp3.getTitle());
            map.put("mp3_artist", mp3.getArtist());
            map.put("mp3_path", mp3.getUrl());
            mp3MapList.add(map);
        }

        return mp3MapList;
    }

}
