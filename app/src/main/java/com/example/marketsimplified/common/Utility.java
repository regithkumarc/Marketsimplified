package com.example.marketsimplified.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.marketsimplified.R;
import com.example.marketsimplified.model.Details;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<Details> listDetail = new ArrayList<>();
    public static List<Details> listDetails = new ArrayList<>();
    public static boolean flag = true;
    public static int previousPageCount = 1;

    private static Dialog dialog;
    private static TextView popup_close;
    private static TextView name,privateOrPublic,repoUrl;
    private static ImageView imageView;

    public static void showDetails(final Context activity, Details details) {

        try {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.alertborder);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            dialog.getWindow().setContentView(R.layout.alertborder);
            dialog.setCancelable(true);

            int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.70);
            int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.35);

            imageView = dialog.findViewById(R.id.avatarUrl);
            name = dialog.findViewById(R.id.name);
            privateOrPublic = dialog.findViewById(R.id.privateOrPublic);
            repoUrl = dialog.findViewById(R.id.repo_url);

            Glide.with(activity).load(details.getOwner().getAvatarUrl()).into(imageView);
            name.setText(details.getName());

            if(details.getPriv().equals(true)){
                privateOrPublic.setText("private");
            }else{
                privateOrPublic.setText("public");
            }

            repoUrl.setText(details.getOwner().getReposUrl());

            dialog.getWindow().setLayout(width, height);
            dialog.show();

            popup_close = (TextView) dialog.findViewById(R.id.popup_close);
            popup_close.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
