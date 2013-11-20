package com.example.bpsmp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		//AudioManager audiomgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		//currentVolumn = audiomgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		init();
		
		songsList = songManager.getPlayList();
		mp.setOnCompletionListener(this);
		
		//再生ボタンのイメージ初期化
		change_play_pause_icon();
		
		
		
		
		//再生ボタン事件
		ic_play.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (mp.isPlaying()) {
					mp.pause();			
				}
				else {
					mp.start();	
				}
				change_play_pause_icon();
			}
		});
			
		//nextボタン事件
		//stopでonCompletionListenerにしだい
		ic_next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mp.stop();
				mp.start();			
			}			
		});
		
		//previousボタン事件
		//repeatの場合クリックしても変わらない
		//randomの場合randomじゃなくて、前再生した曲に戻る(現在前の1曲しか覚えない)
		ic_previous.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				if ( isRepeat ) {
					currentIndex += 0;
				}
				
				if ( isShuffle ) {
					currentIndex = previousIndex;
				}
				
				if ( isNormal ) {
				
					if ( currentIndex > 0 ){
						currentIndex -= 1; 
					}
					else {
						currentIndex = songsList.size() - 1;
					}
				}				
				playByIndex(currentIndex);
				change_play_pause_icon();
			}
		});
		
		//stopボタン事件
		ic_stop.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View v){
				mp.stop();
				change_play_pause_icon();
			}
		});
        
		//shuffleボタン事件
	    ic_repeat.setOnClickListener(new OnClickListener(){
	    	
	    	@Override
	    	public void onClick(View v){
	    		if ( isNormal ){
	    			isNormal = false;
	    			isRepeat = true;
	    			isShuffle = false;
	    			ic_repeat.setImageResource(R.drawable.ic_shuffle);
	    			Toast.makeText(getApplicationContext(), "Change mode to 1 曲　REPEAT ", Toast.LENGTH_SHORT).show();
	    		}
	    		else if ( isRepeat ) {
	    			isRepeat = false;
	    			isNormal = false;
	    			isShuffle = true;
	    			ic_repeat.setImageResource(R.drawable.ic_repeat);
	    			Toast.makeText(getApplicationContext(), "Change mode to RANDOM", Toast.LENGTH_SHORT).show();
	    		}
	    		else if ( isShuffle ){		
	    			isShuffle = false;
	    			isNormal = false;
	    			isRepeat = true;
	    			ic_repeat.setImageResource(R.drawable.ic_shuffle);
	    			Toast.makeText(getApplicationContext(), "Change mode to 1曲　REPEAT ", Toast.LENGTH_SHORT).show();
	    		}
	    	}
	    });
	    
	    //normalボタン事件
	    ic_normal.setOnClickListener(new OnClickListener(){
	    	
	    	@Override
	    	public void onClick(View v){
	    		if ( isRepeat || isShuffle ){
	    			isRepeat = false;
	    			isShuffle = false;
	    			isNormal = true;
	    			Toast.makeText(getApplicationContext(), "Change mode to 曲リスト順", Toast.LENGTH_SHORT).show();
	    		} else {
	    			Toast.makeText(getApplicationContext(), "it is now Normal mode, no need to change", Toast.LENGTH_SHORT).show();
	    		}
	    	}
	    });
	    
	    
	    //control the music volumn
	    ic_volumn_up.setOnClickListener(new OnClickListener(){
	    	
	    	@Override
	    	public void onClick(View v){    		
	    		currentVolumn += 0.10f;	    		
	    		mp.setVolume(currentVolumn, currentVolumn);
	    	}
	    });
	    
	    
	    ic_volumn_down.setOnClickListener(new OnClickListener(){
	    	
	    	@Override
	    	public void onClick(View v){
	    		currentVolumn -= 0.10f;
	    		mp.setVolume(currentVolumn, currentVolumn);
	    		}
	    });
	    
	}
	
	//redirect to songlist
	public void test(View view){
		Intent intent = new Intent(this, SongListActivity.class);
		startActivityForResult(intent, 2);
	}
	
	//back from songlist according to result code
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == 1) {
			//test
			Toast.makeText(getApplicationContext(), "選択された曲名：" + data.getExtras().getString("str"), Toast.LENGTH_SHORT).show();
			playByIndex(data.getExtras().getInt("songIndex"));
			change_play_pause_icon();
		}
	}
	
	//1曲が終わった事件
	@Override
	public void onCompletion(MediaPlayer mp) {
		
		if ( isNormal ){
			if (currentIndex < songsList.size() - 1){
				currentIndex += 1;
			} 
			else {
				currentIndex = 0;
			}
		}
		else if ( isRepeat ) {
			currentIndex += 0;
		}
		else if ( isShuffle ){
			Random random = new Random();
			previousIndex = currentIndex;
			currentIndex = random.nextInt(songsList.size() - 1);
			//test
			Toast.makeText(getApplicationContext(), "random song #: " + currentIndex, Toast.LENGTH_SHORT).show();
		}
		playByIndex(currentIndex);
		change_play_pause_icon();
	}
		
	//音曲番号により再生する
	//音曲のタイトルを再生する曲のnameに変更する
	public void playByIndex(int songIndex){
		mp.reset();
		try {
			mp.setDataSource(songsList.get(songIndex).get("songPath"));
			mp.prepare();
			mp.start();
			songTitleLabel.setText(songsList.get(songIndex).get("songTitle"));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//再生ボタンと停止ボタンの変更
	public void change_play_pause_icon(){
		if (mp.isPlaying()){
			ic_play.setImageResource(R.drawable.ic_pause); 
		}
		else {
			ic_play.setImageResource(R.drawable.ic_play);
		}			
	}
	
	public void onDestroy(){
		super.onDestroy();
		mp.release();
	}
	
	private OnClickListener  OnClickListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	

	private void init(){
		
		//initialize buttons and textview variable 
		ic_songList = (ImageButton) findViewById(R.id.songList_icon);
		ic_play = (ImageButton) findViewById(R.id.player_play_icon);
		ic_next = (ImageButton) findViewById(R.id.player_next_icon);
		ic_previous = (ImageButton) findViewById(R.id.player_previous_icon);
		ic_stop = (ImageButton) findViewById(R.id.player_stop_icon);
		ic_repeat = (ImageButton) findViewById(R.id.player_repeat_icon);
		ic_normal = (ImageButton) findViewById(R.id.player_normal_icon);
		ic_volumn_up = (ImageButton) findViewById(R.id.volumn_up_icon);
		ic_volumn_down = (ImageButton) findViewById(R.id.volumn_down_icon);
		songTitleLabel = (TextView) findViewById(R.id.song_title);
		
		mp = new MediaPlayer();
		
		songManager = new SongsManager();
		songsList = new ArrayList<HashMap<String, String>>();
		
		
		currentIndex = 3;
		currentVolumn = 0.40f;
		mp.setVolume(currentIndex, currentVolumn);
		previousIndex = currentIndex;
		
		//play mode
		isNormal = true;
		isRepeat = false;
		isShuffle = false;
	}

    private ImageButton ic_songList;
	private ImageButton ic_play;
    private ImageButton ic_next;
    private ImageButton ic_previous;
    private ImageButton ic_stop;
    private ImageButton ic_repeat;
    private ImageButton ic_normal;
    private ImageButton ic_volumn_up;
    private ImageButton ic_volumn_down;
    
    private TextView songTitleLabel;
    
    private MediaPlayer mp;
    private SongsManager songManager;
    private ArrayList<HashMap<String, String>> songsList;
    
    private int currentIndex;
    private int previousIndex;
    private float currentVolumn;
    private boolean isNormal;
    private boolean isRepeat;
    private boolean isShuffle;

}
