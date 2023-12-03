package com.wk.muyu.sound;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.wk.muyu.R;

import java.util.HashMap;

public class SoundAsset {
    public HashMap<String, SoundPool> stringSoundPoolHashMap;
    private SoundPool soundPool;
    private int soundId;
    private Context context;
    public SoundAsset(Context context){
        stringSoundPoolHashMap = new HashMap<>();
        this.context = context;
    }

    public void loadingSound(){
        // 初始化 SoundPool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }

        // 加载音频文件
        soundId = soundPool.load(context, R.raw.test, 1);
    }


    public void play(){
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    private void createOldSoundPool() {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

}
