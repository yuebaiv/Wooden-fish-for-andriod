package com.wk.muyu.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wk.muyu.R;
import com.wk.muyu.listener.MeritNumListener;
import com.wk.muyu.sound.SoundAsset;

import java.util.ArrayList;

public class MuYuView extends ViewGroup {
    private ImageView muYuG;
    private ImageView muyu;

    private SoundAsset asset;
    private boolean pause = false;

    private ArrayList<Animator> animators;
    public MuYuView(Context context) {
        this(context,null);
    }

    public MuYuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MuYuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        muYuG = new ImageView(getContext());
        muYuG.setImageResource(R.drawable.muyugun);
        addView(muYuG);
        muyu = new ImageView(getContext());
        muyu.setImageResource(R.drawable.muyu);
        addView(muyu);
        animators = new ArrayList<>();
    }

    public void userClick(){
        if (isAuto)return;
        shoudongAnimtion();

    }

    public ImageView getMuYuG() {
        return muYuG;
    }

    private boolean playing = false;
    public void startAnimation() {
        if (!isAuto)return;
        shoudongAnimtion();
    }

    public void shoudongAnimtion(){
        if (playing)return;
        playing = true;
        // 创建旋转动画
        muYuG.setPivotX(0);
        muYuG.setPivotY(muYuG.getHeight());
        ObjectAnimator muYuGRotateAnimator = ObjectAnimator.ofFloat(muYuG, "rotation", 0f,12f);
        muYuGRotateAnimator.setDuration(300);
        muYuGRotateAnimator.start();
        animators.add(muYuGRotateAnimator);
        muYuGRotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animators.remove(animation);
            }
        });


        ObjectAnimator muyuAnimatorX = ObjectAnimator.ofFloat(muyu,"scaleX",1.0f,0.9f);
        ObjectAnimator muyuAnimatorY = ObjectAnimator.ofFloat(muyu,"scaleY",1.0f,0.9f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(muyuAnimatorX,muyuAnimatorY);
        animatorSet.setDuration(200);
        animatorSet.start();
        animatorSet.setStartDelay(250);
        animators.add(animatorSet);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animators.remove(animation);
                asset.play();
                listener.meritNum(1);
                ObjectAnimator muyuAnimatorX = ObjectAnimator.ofFloat(muyu,"scaleX",0.9f,1.0f);
                ObjectAnimator muyuAnimatorY = ObjectAnimator.ofFloat(muyu,"scaleY",0.9f,1.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(muyuAnimatorX,muyuAnimatorY);
                animatorSet.setDuration(100);
                animatorSet.start();
            }
        });

        muYuGRotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(@NonNull Animator animation, boolean isReverse) {
                super.onAnimationEnd(animation, isReverse);
                muyu.setPivotX(0);
                muyu.setPivotY(muyu.getHeight());
                ObjectAnimator muYuGRotateAnimator = ObjectAnimator.ofFloat(muYuG, "rotation", 12f,0f);
                muYuGRotateAnimator.setDuration(200);
                muYuGRotateAnimator.start();
                muYuGRotateAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        playing = false;
                        if (!pause) {
                            startAnimation();
                        }else {
                            if (isAuto) {
                                startAnimation();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        {
            int childWidth = muYuG.getMeasuredWidth();
            int childHeight = muYuG.getMeasuredHeight();
            int childLeft = (int) (getWidth() / 2.0) - childWidth / 2 - 120;
            int childTop = (getHeight() - childHeight) / 2 - 60; // 居中垂直方向
            muYuG.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
        }
        {
            int childWidth = muyu.getMeasuredWidth();
            int childHeight = muyu.getMeasuredHeight();
            int childLeft = (int) (getWidth() / 2.0) - childWidth / 2 + 120;
            int childTop = (getHeight() - childHeight) / 2; // 居中垂直方向
            muyu.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        //measureChildren内部调用measureChild，这里我们就可以指定宽高
        measureChildren(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        setMeasuredDimension(widthSize,widthSize);
    }

    public void setSound(SoundAsset asset) {
        this.asset = asset;
    }

    public void pause(){
        pause = true;
        pauseAllAnimations(this);
    }

    public void resume(){
        if (pause) {
            startAnimation();
        }
        pause = false;
    }
    private void pauseAllAnimations(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                // 递归调用，以处理可能存在的嵌套 ViewGroup
                pauseAllAnimations(child);
            }
        }

        Animation animation = view.getAnimation();
        if (animation != null) {
            animation.cancel();
        }
        ViewPropertyAnimator animate = view.animate();
        if (animate!=null) {
            animate.cancel();
        }
    }

    private MeritNumListener listener;
    public void setMeritNumListener(MeritNumListener meritNumListener) {
        this.listener = meritNumListener;
    }

    private boolean isAuto;
    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
        if (isAuto) {
            startAnimation();
        }
    }
}
