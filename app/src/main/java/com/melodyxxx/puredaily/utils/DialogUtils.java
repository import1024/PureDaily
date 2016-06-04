package com.melodyxxx.puredaily.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;

public class DialogUtils {

    public interface DialogCallBack {
        void onPositiveButton(DialogInterface dialog, int which);

        void onNegativeButton(DialogInterface dialog, int which);

        void onNeutralButton(DialogInterface dialog, int which);
    }

    public static void showAlertDialog(Context context, String title, String desc, String positiveButtonText, String negativeButtonText, String neutralButtonText, boolean cancelable, final DialogCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setCancelable(cancelable);
        if (callBack != null) {
            if (!TextUtils.isEmpty(positiveButtonText)) {
                builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.onPositiveButton(dialog, which);
                    }
                });
            }
            if (!TextUtils.isEmpty(negativeButtonText)) {
                builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.onNegativeButton(dialog, which);
                    }
                });
            }
            if (!TextUtils.isEmpty(neutralButtonText)) {
                builder.setNeutralButton(neutralButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.onNeutralButton(dialog, which);
                    }
                });
            }
        }
        AlertDialog dialog = builder.create();
        dialog.show();
        int themeColor = CommonUtils.getThemePrimaryColor(context);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(themeColor);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(themeColor);
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(themeColor);
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(16);
        textView.setTextColor(context.getResources().getColor(R.color.dialog_text_color));
    }
}
