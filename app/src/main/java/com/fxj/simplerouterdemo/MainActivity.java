package com.fxj.simplerouterdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvVersionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvVersionInfo = findViewById(R.id.tv_version_info);
        String versionName=getVersionName(this);
        if(!TextUtils.isEmpty(versionName)){
            tvVersionInfo.setVisibility(View.VISIBLE);
            tvVersionInfo.setText("v"+versionName);
        }else{
            tvVersionInfo.setVisibility(View.GONE);
        }
    }

    String getVersionName(Context context){
        String versionName=null;

        Context applicationContext= context.getApplicationContext();
        try {
            versionName=applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    };
}
