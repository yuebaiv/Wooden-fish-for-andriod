package com.wk.muyu;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wk.muyu.listener.MeritNumListener;
import com.wk.muyu.sound.SoundAsset;
import com.wk.muyu.view.MuYuView;

public class MainActivity extends AppCompatActivity {
    private MuYuView view;
    private boolean isLayout;

    private int meritNum;
    private boolean isAuto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SoundAsset asset = new SoundAsset(this);
        asset.loadingSound();
        view = findViewById(R.id.muyuview);
        view.setSound(asset);
        TextView meritNumText = findViewById(R.id.meritText);

        view.setMeritNumListener(new MeritNumListener() {
            @Override
            public void meritNum(int num) {
                meritNum += num;
                meritNumText.setText("Merit: "+meritNum);
            }
        });

        Button auto = findViewById(R.id.auto);
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAuto = !isAuto;
                view.setAuto(isAuto);
                if (isAuto) {
                    auto.setText("自动");
                }else {
                    auto.setText("手动");
                }
            }
        });

        findViewById(R.id.qingkong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meritNum = 0;
                meritNumText.setText("Merit: "+meritNum);
            }
        });

        findViewById(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.userClick();
            }
        });

//        findViewById(R.id.targetnum);
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.resume();
    }
}