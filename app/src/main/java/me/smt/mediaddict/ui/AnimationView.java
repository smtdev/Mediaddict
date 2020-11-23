package me.smt.mediaddict.ui;

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

/**
 * Clase que contiene utilidades para la aplicación en
 * cuanto a animación se refiere.
 * @author Sergio Martín Teruel
 * @version 1.0
 **/
public class AnimationView {

    /**
     * Método que rota la vista enviada por parámetro.
     * @param v vista.
     * @param rotate rotado true o false.
     * @return la rotación.
     */
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

    /**
     * Método que genera una animación de saqueo en la vista asociada.
     * Más info: https://stackoverflow.com/a/42055691/1552146
     * @param v vista.
     * @param durationMillis duración en milisegundos.
     */
    public static void shakeFab(final View v, long durationMillis) {
        Animation anim = AnimationUtils.loadAnimation(v.getContext(), R.anim.shake_animation);
        anim.setDuration(durationMillis);

        v.startAnimation(anim);
    }


    /**
     * Atributo utilizado en el método setScrollingAnimation
     * para almacenar la última posición del scroll.
     */
    private static int lastPosition = -1;

    /**
     * Método que se encarga de la personalización de animación en el
     * scrolling de las listas de películas.
     * Más info: https://stackoverflow.com/a/36545709/1552146
     */
    public static void setScrollingAnimation(View viewToAnimate, int position) {
        // Si la vista enlazada no se mostró anteriormente en la pantalla, está animada
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    /**
     * Método utilizado para dar curvatura en las esquinas a los ImageView.
     * Más info: https://stackoverflow.com/a/60132891/1552146
     * Más info: https://developer.android.com/training/material/shadows-clipping
     * @param img la imagen a aplicar la curvatura.
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
