package com.silead.fp;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DataUtil{
    private SharedPreferences mLockAppPreferences;
    private SharedPreferences.Editor editor;
    private static final String FP_SWITCH_KEY = "fp_switch_key";

    public DataUtil(Context context) {
        mLockAppPreferences = context.getSharedPreferences(
                "LockAppPreferences", Context.MODE_PRIVATE);
        editor = mLockAppPreferences.edit();
    }

    public boolean getAppLockState(ComponentName componentName) {

        String packagename = mLockAppPreferences.getString(
                componentName.getPackageName(), null);
        String classname = mLockAppPreferences.getString(
                componentName.getClassName(), null);
        if (componentName.getPackageName().equals(packagename)
                && componentName.getClassName().equals(classname)) {
            return true;
        }
        return false;

    }

    public void storeLockApp(ComponentName componentName) {
        editor.putString(componentName.getPackageName(),
                componentName.getPackageName());
        editor.putString(componentName.getClassName(),
                componentName.getClassName());
        editor.commit();
    }

    public void removeLockApp(ComponentName componentName) {
        editor.putString(componentName.getPackageName(), null);
        editor.putString(componentName.getClassName(), null);
        editor.commit();
    }
    
    public void storeFPSwitch(boolean fpswitch){
         editor.putBoolean(FP_SWITCH_KEY, fpswitch);
         editor.commit();
    }
    public boolean getFPSwitch(){
        return mLockAppPreferences.getBoolean(FP_SWITCH_KEY, false);
    }

}
