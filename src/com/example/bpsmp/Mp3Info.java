
package com.example.bpsmp;

/*
 * mp3 entity
 */
public class Mp3Info {

    private int id;

    private String title;

    private String artist;

    private long duration;

    private int size;

    private String url;

    private int isMusic;

    public int isMusic() {
        return isMusic;
    }

    public void setMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
