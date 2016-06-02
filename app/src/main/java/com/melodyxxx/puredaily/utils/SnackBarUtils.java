package com.melodyxxx.puredaily.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackBarUtils {

    private static final int SNACK_BAR_BACKGROUND_COLOR = 0xffe91e63;

    private Snackbar mSnackBar;

    private SnackBarUtils(Snackbar snackbar) {
        mSnackBar = snackbar;
    }

    public static SnackBarUtils makeShort(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        return new SnackBarUtils(snackbar);
    }

    public static SnackBarUtils makeLong(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        return new SnackBarUtils(snackbar);
    }

    public void show() {
        setSnackBarBackgroundColor(SNACK_BAR_BACKGROUND_COLOR);
        mSnackBar.show();
    }

    private void setSnackBarBackgroundColor(int color) {
        View view = mSnackBar.getView();
        view.setBackgroundColor(color);
    }


//    public static void customSnackBar(Snackbar snackbar, int backgroundColor, int textColor, int actionTextColor) {
//        View view = snackbar.getView();
//        view.setBackgroundColor(backgroundColor);
//        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(textColor);
//        if (actionTextColor != 0) {
//            ((TextView) view.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(actionTextColor);
//        }
//    }

}
