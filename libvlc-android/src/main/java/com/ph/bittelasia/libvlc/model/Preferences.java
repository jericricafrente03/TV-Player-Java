package com.ph.bittelasia.libvlc.model;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Preferences {
    public final static String TAG = "VLC/Util/Preferences";

    public static float[] getFloatArray(SharedPreferences pref, String key) {
        float[] array = null;
        String s = pref.getString(key, null);
        if (s != null) {
            try {
                JSONArray json = new JSONArray(s);
                array = new float[json.length()];
                for (int i = 0; i < array.length; i++)
                    array[i] = (float) json.getDouble(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public static void putFloatArray(SharedPreferences.Editor editor, String key, float[] array) {
        try {
            JSONArray json = new JSONArray();
            for (float f : array)
                json.put(f);
            editor.putString(key, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void putVideoInfoArray(SharedPreferences.Editor editor, String key, ArrayList<VideoInfo> list)
    {
        try {
            JSONArray json = new JSONArray();
            for(VideoInfo videoInfo : list)
            {
                json.put(videoInfo);
            }
            editor.putString(key, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}