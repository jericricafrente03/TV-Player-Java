package com.ph.bittelasia.libvlc.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ph.bittelasia.libvlc.model.Channels;
import com.ph.bittelasia.libvlc.views.activity.AbstractPlayerActivity;

import java.io.File;

public final class ActivityControl {

    @SuppressWarnings("rawtypes")
    public static void launchPlayer(Channels channels, AppCompatActivity activity, Class c, int REQUEST_CODE){
        Bundle bundle = new Bundle();
        bundle.putParcelable(AbstractPlayerActivity.KEY,channels);
        Intent intent =new Intent(activity, c);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent , REQUEST_CODE);
    }

    @SuppressWarnings("rawtypes")
    public static void refreshPlayer(Channels channels, AppCompatActivity activity, Class c){
        Bundle bundle = new Bundle();
        bundle.putParcelable(AbstractPlayerActivity.KEY,channels);
        Intent intent =new Intent(activity, c);
        intent.putExtras(bundle);
        activity.finish();
        activity.startActivity(intent);
    }

    //--------------------------Function to check and request permission----------------------------
    public static void checkPermission(Context context,String permission, int requestCode)
    {
        try {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
            } else {
                Log.i(context.toString(), "checkPermission: " + "Permission already granted");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static String getInput(KeyEvent event)
    {
        String channel="";
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_NUMPAD_0:
                channel = channel + 0;
                break;
            case  KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_NUMPAD_1:
                channel = channel + 1;
                break;
            case  KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_NUMPAD_2:
                channel = channel + 2;
                break;
            case  KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_NUMPAD_3:
                channel = channel + 3;
                break;
            case  KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_NUMPAD_4:
                channel = channel + 4;
                break;
            case  KeyEvent.KEYCODE_5:
            case KeyEvent.KEYCODE_NUMPAD_5:
                channel = channel + 5;
                break;
            case  KeyEvent.KEYCODE_6:
            case KeyEvent.KEYCODE_NUMPAD_6:
                channel = channel + 6;
                break;
            case  KeyEvent.KEYCODE_7:
            case KeyEvent.KEYCODE_NUMPAD_7:
                channel = channel + 7;
                break;
            case  KeyEvent.KEYCODE_8:
            case KeyEvent.KEYCODE_NUMPAD_8:
                channel = channel + 8;
                break;
            case  KeyEvent.KEYCODE_9:
            case KeyEvent.KEYCODE_NUMPAD_9:
                channel = channel + 9;
                break;
        }
        return channel;
    }


}
