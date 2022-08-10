package com.app.sunsafe.Retrofit.Helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.app.sunsafe.R;


public class ProcessDialog {
    private static Dialog progressDialog;

    public static void start(Context context) {
        if (!isShoving()) {
            if (!((Activity) context).isFinishing()) {
                progressDialog = new Dialog(context);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.view_progress_dialog);
                progressDialog.show();
            }
        }
    }

    public static void dismiss() {
        try {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            progressDialog = null;
        }
    }

    public static boolean isShoving() {
        if (progressDialog != null) {
            return progressDialog.isShowing();
        } else {
            return false;
        }
    }
}
