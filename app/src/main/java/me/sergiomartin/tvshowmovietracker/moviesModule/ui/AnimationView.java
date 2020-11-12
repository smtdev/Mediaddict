package me.sergiomartin.tvshowmovietracker.moviesModule.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import me.sergiomartin.tvshowmovietracker.R;

public class AnimationView {

    public static boolean rotateFab(final View v, boolean rotate) {
        v.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 360f : 0f);
        return rotate;
    }

    /*
     * https://stackoverflow.com/a/42055691/1552146
     */
    public static void shakeFab(final View v, long durationMillis) {
        Animation anim = AnimationUtils.loadAnimation(v.getContext(), R.anim.shake_animation);
        anim.setDuration(durationMillis);

        v.startAnimation(anim);
    }

}
