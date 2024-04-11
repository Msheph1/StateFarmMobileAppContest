package com.msheph1.foodfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingPopup {

    private final Activity activity;
    private AlertDialog dialog;

    LoadingPopup(Activity myActivity)
    {
        this.activity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        if(dialog != null &&  dialog.isShowing())
            dialog.dismiss();
    }
}
