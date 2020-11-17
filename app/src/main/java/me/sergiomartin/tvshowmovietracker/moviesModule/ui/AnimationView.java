package me.sergiomartin.tvshowmovietracker.moviesModule.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

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

    /*
     * Personalización de animación en el scrolling de las listas de películas
     * https://stackoverflow.com/a/36545709/1552146
     */
    private static int lastPosition = -1;

    public static void setScrollingAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

}
