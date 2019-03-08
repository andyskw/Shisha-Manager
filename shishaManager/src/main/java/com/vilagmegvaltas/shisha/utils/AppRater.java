package com.vilagmegvaltas.shisha.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.vilagmegvaltas.shisha.R;

public class AppRater {

    private final static String APP_PNAME = "com.vilagmegvaltas.shisha";
    

    private final static int LAUNCHES_UNTIL_PROMPT = 10;
    private static LayoutParams buttonLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }
        
        SharedPreferences.Editor editor = prefs.edit();
        
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
           
                showRateDialog(mContext, editor);

        }
        
        editor.commit();
    }   
    
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle(mContext.getString(R.string.rate_title));

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        
        TextView tv = new TextView(mContext);
        tv.setText(mContext.getString(R.string.rate_descr));
        tv.setWidth(300);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);
        buttonLayoutParams.setMargins(10, 5, 10, 5);
        
        
        Button btn_rate = new Button(mContext);
        btn_rate.setText(mContext.getString(R.string.rate_button_rate));
        setButtonLayout(btn_rate);
        btn_rate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	editor.putBoolean("dontshowagain", true);
            	editor.commit();
            	mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });        
        ll.addView(btn_rate);

        Button btn_remindLater = new Button(mContext);
        setButtonLayout(btn_remindLater);
        btn_remindLater.setText(mContext.getString(R.string.rate_button_remind_later));
        btn_remindLater.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	editor.putLong("launch_count", 0);
            	editor.commit();
                dialog.dismiss();
            }
        });
        ll.addView(btn_remindLater);

        Button btn_noThanks = new Button(mContext);
        setButtonLayout(btn_noThanks);
        btn_noThanks.setText(mContext.getString(R.string.rate_button_no_thanks));
        btn_noThanks.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(btn_noThanks);

        dialog.setContentView(ll);        
        dialog.show();        
    }
    
    private static void setButtonLayout(Button b) {
    	
    	b.setBackgroundResource(R.drawable.green_button);
    	b.setPadding(10, 20, 10, 20);
    	b.setTextColor(b.getContext().getResources().getColor(R.color.buttonTextColor));
    	
    	b.setTextAppearance(b.getContext(), android.R.style.TextAppearance);
    	b.setLayoutParams(buttonLayoutParams);
    }
}