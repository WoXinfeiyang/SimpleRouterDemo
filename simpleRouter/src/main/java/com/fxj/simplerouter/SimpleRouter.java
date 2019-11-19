package com.fxj.simplerouter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import dalvik.system.DexFile;
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
        List<String> classNames= getClassName("com.fxj.irouterimpl");
        Log.d(TAG,"**init**classNames.size()="+classNames.size());
        if(classNames!=null&&classNames.size()>0){
            for(String className:classNames){
                try {
                   Class<?> aClass =Class.forName(className);
                   if(IRouter.class.isAssignableFrom(aClass)){/*如果IRouter是aClass的父接口*/
                       IRouter iRouter= (IRouter) aClass.newInstance();
                       iRouter.loadInto();
                   }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<String> getClassName(String packageName){
        List<String> classNameList=new ArrayList<String>();
        String path=null;
        try {
            /*通过PackageManager获取到ApplicationInfo,然后获取到apk的完整路径*/
            path= mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), 0).sourceDir;
            Log.d(TAG,"##getClassName##path="+path);
            DexFile df = new DexFile(path);//根据apk完整路径获取编译后的dex文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {//遍历

                String className = (String) enumeration.nextElement();/*获取类名*/

                if (className.contains(packageName)) {//在当前所有可执行的类里面查找包含有该包名的所有类
                    classNameList.add(className);
                }
            }
        }  catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNameList;
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
                Log.e(TAG,"**navigation**对应Activity没有注册到SimpleRouter!");
                return;
            }

            String msg="**navigation**currentContext="+currentContext+",path="+path+",bundle="+bundle+",activityClazz="+(activityClazz!=null?activityClazz.getCanonicalName():null);
            Log.d(TAG,msg);

            Intent intent=new Intent().setClass(currentContext,activityClazz);
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
