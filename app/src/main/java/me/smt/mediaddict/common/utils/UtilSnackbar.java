package me.smt.mediaddict.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

/*
 * Based on https://gist.github.com/devrath/26a182559a643fc5085b
 */
public final class UtilSnackbar {

    /*
     *  ShowSnackbar with:
     *      - Message
     *      - AnchorView
     *      - Keep it displayed short
     *      - Receive resource as message
     */
    public static void showSnackbarShort(View rootView, int mMessage, View anchorView) {
        Snackbar.make(rootView, mMessage, Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .setAnchorView(anchorView)
                .show();
    }

    /*
     *  ShowSnackbar with:
     *      - Message
     *      - AnchorView
     *      - Keep it displayed short
     *      - Receive input String as message
     */
    public static void showSnackbarShort(View rootView, String mMessage, View anchorView) {
        Snackbar.make(rootView, mMessage, Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .setAnchorView(anchorView)
                .show();
    }

    /*
     *  ShowSnackbar with:
     *      - Message
     *      - AnchorView
     *      - Keep it displayed long
     *      - Receive resource as message
     */
    public static void showSnackbarLong(View rootView, int mMessage, View anchorView) {
        Snackbar.make(rootView, mMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(anchorView)
                .show();
    }

    /*
     *  ShowSnackbar with:
     *      - Message
     *      - AnchorView
     *      - Keep it displayed long
     *      - Receive input String as message
     */
    public static void showSnackbarLong(View rootView, String mMessage, View anchorView) {
        Snackbar.make(rootView, mMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(anchorView)
                .show();
    }

    /*
     * ShowSnackbar with:
     *      - Message
     *      - Keep it displayed indefinite
     */
    public static void showSnackbarInd(View rootView, String mMessage) {

         Snackbar.make(rootView, mMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null)
                .show();

    }

    /*
     * Snackbar with:
     *      - Message
     *      - Keep it displayed indefinite
     *      - OnClick for restart the activity
     */
    public static void showSnackbarWithRestart(View rootView, final Activity activity, String mMessage, String action) {

        Snackbar
                .make(rootView, mMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = activity.getIntent();
                        activity.finish();
                        activity.startActivity(intent);
                    }
                })
                .setActionTextColor(Color.CYAN)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                    }
                })
                .show();
    }
}
