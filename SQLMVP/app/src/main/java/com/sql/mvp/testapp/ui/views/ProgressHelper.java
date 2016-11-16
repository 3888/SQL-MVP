package com.sql.mvp.testapp.ui.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;

public class ProgressHelper {

    private Context context;
    private ProgressDialog progress;

    public ProgressHelper(Context context) {
        this.context = context;
        progress = null;
    }

    public void show(String message, boolean cancelable) {
        if (progress == null) {
            if (cancelable) {
                progress = ProgressDialog.show(context, "", message, true, true, dialog -> progress = null);
            } else {
                progress = ProgressDialog.show(context, "", message, true, false);
            }
        }
    }

    public void show(@StringRes int message, boolean cancelable) {
        show(context.getString(message), cancelable);
    }

    public synchronized void hide() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    public boolean isShowing() {
        return progress != null && progress.isShowing();
    }
}