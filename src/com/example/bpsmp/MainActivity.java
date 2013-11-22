
package com.example.bpsmp;

import android.widget.RelativeLayout.LayoutParams;

import android.widget.RelativeLayout;

import android.widget.ImageView;

import android.hardware.SensorEvent;

import android.app.ActionBar;

import android.widget.Button;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements OnCompletionListener,
        SeekBar.OnSeekBarChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        ActionBar ab = getActionBar();
        ab.hide();
        
        init();

        songsList = songManager.getMp3Maplist(songManager.getMp3List(getApplicationContext()));
        mp.setOnCompletionListener(this);

        // 再生ボタンのイメージ初期化
        change_play_pause_icon();

        // 再生ボタンが押された
        ic_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();
                } else {
                    mp.start();
                }
                change_play_pause_icon();
            }
        });

        // nextボタンが押された
        // stopでonCompletionListenerにしだい
        ic_next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mp.stop();
                mp.start();
            }
        });

        // previousボタンが押された
        // repeatの場合クリックしても変わらない
        // randomの場合randomじゃなくて、前再生した曲に戻る(現在前の1曲しか覚えない)
        ic_previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isRepeat) {
                    currentIndex += 0;
                }

                if (isShuffle) {
                    currentIndex = previousIndex;
                }

                if (isNormal) {

                    if (currentIndex > 0) {
                        currentIndex -= 1;
                    } else {
                        currentIndex = songsList.size() - 1;
                    }
                }
                playByIndex(currentIndex);
                change_play_pause_icon();
            }
        });

        /* stopボタンが押された
        ic_stop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mp.stop();
                change_play_pause_icon();
            }
        });
        */

        // shuffleボタンが押された
        ic_repeat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isNormal) {
                    isNormal = false;
                    isRepeat = true;
                    isShuffle = false;
                    ic_repeat.setBackgroundResource(R.drawable.btn_shuffle);
                    Toast.makeText(getApplicationContext(), "Change mode to 1 曲　REPEAT ",
                            Toast.LENGTH_SHORT).show();
                } else if (isRepeat) {
                    isRepeat = false;
                    isNormal = false;
                    isShuffle = true;
                    ic_repeat.setBackgroundResource(R.drawable.btn_repeat);
                    Toast.makeText(getApplicationContext(), "Change mode to RANDOM",
                            Toast.LENGTH_SHORT).show();
                } else if (isShuffle) {
                    isShuffle = false;
                    isNormal = false;
                    isRepeat = true;
                    ic_repeat.setBackgroundResource(R.drawable.btn_shuffle);
                    Toast.makeText(getApplicationContext(), "Change mode to 1曲　REPEAT ",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // normalボタンが押された
        ic_normal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isRepeat || isShuffle) {
                    isRepeat = false;
                    isShuffle = false;
                    isNormal = true;
                    Toast.makeText(getApplicationContext(), "Change mode to 曲リスト順",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "it is now Normal mode, no need to change", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // control the music volumn
        ic_volumn_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                currentVolumn += 0.10f;
                mp.setVolume(currentVolumn, currentVolumn);
            }
        });

        ic_volumn_down.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                currentVolumn -= 0.10f;
                mp.setVolume(currentVolumn, currentVolumn);
            }
        });

    }

    // redirect to songlist
    public void test(View view) {
        Intent intent = new Intent(this, SongListActivity.class);
        startActivityForResult(intent, 2);
    }

    // back from songlist according to result code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            // test
            Toast.makeText(getApplicationContext(), "選択された曲名：" + data.getExtras().getString("str"),
                    Toast.LENGTH_SHORT).show();
            playByIndex(data.getExtras().getInt("songIndex"));
            change_play_pause_icon();
        }
    }

    // 1曲が終わった時点
    @Override
    public void onCompletion(MediaPlayer mp) {

        if (isNormal) {
            if (currentIndex < songsList.size() - 1) {
                currentIndex += 1;
            } else {
                currentIndex = 0;
            }
        } else if (isRepeat) {
            currentIndex += 0;
        } else if (isShuffle) {
            Random random = new Random();
            previousIndex = currentIndex;
            currentIndex = random.nextInt(songsList.size() - 1);
            // test
            Toast.makeText(getApplicationContext(), "random song #: " + currentIndex,
                    Toast.LENGTH_SHORT).show();
        }
        playByIndex(currentIndex);
        change_play_pause_icon();
    }

    // 音曲番号により再生する
    // 音曲のタイトルを再生する曲のnameに変更する
    public void playByIndex(int songIndex) {
        mp.reset();
        try {
            mp.setDataSource(songsList.get(songIndex).get("mp3_path"));
            mp.prepare();
            mp.start();
            songTitleLabel.setText(songsList.get(songIndex).get("mp3_title"));
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

    // 再生ボタンと停止ボタンの変更
    public void change_play_pause_icon() {
        if (mp.isPlaying()) {
            ic_play.setBackgroundResource(R.drawable.btn_pause);
        } else {
            ic_play.setBackgroundResource(R.drawable.btn_play);
        }
    }

    @Override
    public void onDestroy() {
            super.onDestroy();
            mp.release();        
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
       

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
       

    }

    /*
    public void onSensorChanged(SensorEvent event){
        ImageView album = (ImageView) findViewById(R.id.album_cover);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.artist);
        RelativeLayout.LayoutParams params = (LayoutParams)layout.getLayoutParams();
        params.width = 20;
        album.setLayoutParams(params);
    }
    */
    
    private void init() {

        // initialize buttons and textview variable
        ic_songList = (Button)findViewById(R.id.songList_icon);
        ic_play = (Button)findViewById(R.id.player_play_icon);
        ic_next = (Button)findViewById(R.id.player_next_icon);
        ic_previous = (Button)findViewById(R.id.player_previous_icon);
        //c_stop = (Button)findViewById(R.id.player_stop_icon);
        ic_repeat = (Button)findViewById(R.id.player_repeat_icon);
        ic_normal = (Button)findViewById(R.id.player_normal_icon);
        ic_volumn_up = (ImageButton)findViewById(R.id.volumn_up_icon);
        ic_volumn_down = (ImageButton)findViewById(R.id.volumn_down_icon);
        songTitleLabel = (TextView)findViewById(R.id.song_title);

        mp = new MediaPlayer();

        songManager = new SongManager();
        songsList = new ArrayList<HashMap<String, String>>();

        currentIndex = 3;
        currentVolumn = 0.40f;
        mp.setVolume(currentVolumn, currentVolumn);
        previousIndex = currentIndex;

        // play mode
        isNormal = true;
        isRepeat = false;
        isShuffle = false;
    }

    private Button ic_songList;

    private Button ic_play;

    private Button ic_next;

    private Button ic_previous;

    //private ImageButton ic_stop;

    private Button ic_repeat;

    private Button ic_normal;

    private ImageButton ic_volumn_up;

    private ImageButton ic_volumn_down;

    private TextView songTitleLabel;

    private MediaPlayer mp;

    private SongManager songManager;

    private List<HashMap<String, String>> songsList;

    private int currentIndex;

    private int previousIndex;

    private float currentVolumn;

    private boolean isNormal;

    private boolean isRepeat;

    private boolean isShuffle;

}
