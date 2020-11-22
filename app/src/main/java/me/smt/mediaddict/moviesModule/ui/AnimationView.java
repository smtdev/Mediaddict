package me.smt.mediaddict.moviesModule.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import java.util.Random;

import me.smt.mediaddict.R;

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
        // Si la vista enlazada no se mostró anteriormente en la pantalla, está animada
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    /*
     * Dar curvatura en las esquinas a los ImageView
     * https://stackoverflow.com/a/60132891/1552146
     * https://developer.android.com/training/material/shadows-clipping
     */
    public static void outlineImageview(ImageView img) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            ViewOutlineProvider provider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int curveRadius = 12;
                    // view.getHeight()+curveRadius deja curvatura solo en la parte superior
                    //outline.setRoundRect(0, 0, view.getWidth(), (view.getHeight()+curveRadius), curveRadius);
                    outline.setRoundRect(0, 0, view.getWidth(), (view.getHeight()), curveRadius);
                }
            };
            img.setOutlineProvider(provider);
            img.setClipToOutline(true);
        }
    }

}
