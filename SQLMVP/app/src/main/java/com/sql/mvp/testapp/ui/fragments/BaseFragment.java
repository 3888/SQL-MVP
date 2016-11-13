package com.sql.mvp.testapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sql.mvp.testapp.R;
import com.sql.mvp.testapp.ui.views.ProgressHelper;

public abstract class BaseFragment extends Fragment {

    private ProgressHelper progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = new ProgressHelper(getContext());
    }

    public void showProgress(boolean show) {
        if (show) {
            progress.show(R.string.app_loading, true);
        } else {
            progress.hide();
        }
    }

    public interface BaseMvpView {
        void showProgress(boolean show);
    }
}