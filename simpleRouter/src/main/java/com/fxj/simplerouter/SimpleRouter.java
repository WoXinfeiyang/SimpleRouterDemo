package com.fxj.simplerouter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRouter {

    private static final String TAG=SimpleRouter.class.getSimpleName();

    private static volatile SimpleRouter sInstance;
    private Map<String,Class<? extends Activity>> activities;
    private Context mContext;

    private SimpleRouter(){
        activities=new ConcurrentHashMap<String,Class<? extends Activity>>();
    }

    public static SimpleRouter getInstance(){
        if(sInstance==null){
            synchronized (SimpleRouter.class){
                if(sInstance==null){
                    sInstance=new SimpleRouter();
                }
            }
        }
        return sInstance;
    }

    public void init(Application application){
        mContext = application;
    }

    public void putActivity(String path,Class<? extends Activity> clazz){
        if(!TextUtils.isEmpty(path)&&clazz!=null){
            activities.put(path,clazz);
        }
    }

    public void navigation(String path, Bundle bundle){
        navigation(null,path,bundle);
    }
    public void navigation(Context context,String path, Bundle bundle){
        if(!TextUtils.isEmpty(path)){
            Context currentContext=context!=null?context:mContext;
            Class<? extends Activity> activityClazz=activities.get(path);
            if(activityClazz==null){
                return;
            }

            String msg="currentContext="+currentContext+",path="+path+",bundle"+bundle+",activityClazz="+activityClazz!=null?activityClazz.getCanonicalName():null;
            Log.d(TAG,msg);

            Intent intent=new Intent(context,activityClazz);
            if(bundle!=null){
                intent.putExtra("bundle",bundle);
            }

            if(!(currentContext instanceof Activity)){/*如果Context不是Activity，则启动一个Activity需要给Intent添加Intent.FLAG_ACTIVITY_NEW_TASK*/
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            currentContext.startActivity(intent);
        }
    }

}
