package com.ph.bittelasia.libvlc.views.activity;



import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ph.bittelasia.libvlc.control.ActivityControl;
import com.ph.bittelasia.libvlc.control.annotation.AttachToPlayer;
import com.ph.bittelasia.libvlc.control.exception.PlayerException;
import com.ph.bittelasia.libvlc.control.listener.OnChangeListener;
import com.ph.bittelasia.libvlc.control.listener.OnFragmentListener;
import com.ph.bittelasia.libvlc.control.listener.OnPermissionListener;
import com.ph.bittelasia.libvlc.model.VideoInfo;
import com.ph.bittelasia.libvlc.R;
import com.ph.bittelasia.libvlc.model.Channels;
import com.ph.bittelasia.libvlc.views.fragment.PlayerFragment;
import com.ph.bittelasia.libvlc.views.fragment.PlayerLoaderFragment;
import com.ph.bittelasia.libvlc.views.fragment.PlayerVLCFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractPlayerActivity extends AppCompatActivity implements OnChangeListener, OnFragmentListener,  OnPermissionListener {

    //=================================Variable=====================================================
    //---------------------------------Constant-----------------------------------------------------
    public static final String TAG = "VLC/TVActivity";
    public  static final String KEY = "serializable";
    public  static final String CHANNEL_NO = "channel_no";
    public static final int LAYOUT = R.layout.player_activity_layout;
    private static final int STORAGE_PERMISSION_CODE = 101;
    //---------------------------------Instance-----------------------------------------------------
    private PlayerFragment playerFragment;
    private ArrayList<VideoInfo> videoList;
    private  Bundle bundle;
    private boolean hasControl;
    private int index,monitor;
    private Timer timerMonitor, timerChannel;
    private TimerTask timerTaskMonitor, timerTaskChannel;
    private long counter,seconds,stats;
    private String channelNo="";
    private boolean isRestarted;
    //==============================================================================================


    @Override
    protected void onStart() {
        super.onStart();
        ActivityControl.checkPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRestarted = true;
        Log.i(TAG, "onRestart: ");
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        try {
            setContentView(getLayout());
            bundle = getIntent().getExtras();
            if(bundle!=null)
            {
                try {
                    if(Objects.requireNonNull(bundle.getParcelable(KEY)).getClass() == Channels.class) {
                        Channels channels = bundle.getParcelable(KEY);
                        assert channels != null;
                        if (channels.getList() != null) {
                            videoList = channels.sort().getList();
                            loadFragments(channels);
                        }
                        Log.i(TAG, "onCreate: has extras");
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.i(TAG, "onCreate: ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timerTaskMonitor !=null && timerMonitor !=null)
        {
            timerTaskMonitor.cancel();
            timerMonitor =null;
        }
        if(timerTaskChannel != null && timerChannel !=null)
        {
            timerTaskChannel.cancel();
            timerChannel = null;
        }
        Log.i(TAG, "onPause: ");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted();
            }
            else {
                onPermissionDenied();
            }
        }
    }
    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof PlayerFragment) {
            playerFragment = (PlayerFragment) fragment;
            if(playerFragment instanceof PlayerVLCFragment){
                if(bundle!=null) {
                    if(bundle.getString("uri")!=null) {
                        String uri = bundle.getString("uri");
                        assert uri != null;
                        ((PlayerVLCFragment) playerFragment).play(uri, false);
                    }
                }else {
                    ((PlayerVLCFragment) playerFragment).play(getVideoList().get(0), false);
                }
            }
        }
        Log.i("steward", "onAttachFragment: "+playerFragment.getTag());
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================


    //=================================OnFragmentListener===========================================
    //----------------------------------------------------------------------------------------------
    @Override
    public void onFragmentDetached(Fragment fragment) {
        if(fragment instanceof PlayerFragment)
            playerFragment = (PlayerFragment)fragment;
        Log.i(TAG, "onFragmentDetached: "+playerFragment.getTag());
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================

    //==============================OnPermissionListener============================================
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPermissionGranted() {
        Log.i(TAG, "onRequestPermissionsResult: "+"Storage Permission Granted");
    }

    @Override
    public void onPermissionDenied() {
        Log.e(TAG, "onRequestPermissionsResult: "+"Storage Permission Denied");
        ActivityControl.checkPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onPermissionAlreadyGranted() {

    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================


    //=====================================Method===================================================
    //----------------------------------------------------------------------------------------------
    public final void loadFragments(Channels channels){
        try {
            if (channels.getList().size() > 0) {
                setVideoList(channels.sort().getList());
                setChannelIndex(0);
                onChannelIndex(0);
                initializeObject(this);
            }
           playerMonitor();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public <T extends PlayerFragment>void attachFragment(@NonNull T player) {
        if(player.containerID()==0)
            throw new PlayerException(player.getClass().getSimpleName() + " -> Must use non-zero containerViewId");
        getSupportFragmentManager().beginTransaction().replace(
                player.containerID(),
                player,
                player.getClass().getSimpleName()).
                commitAllowingStateLoss();
    }

    public final <T extends PlayerVLCFragment>void removePlayer(@NonNull T player){
        try {
            getSupportFragmentManager().beginTransaction().remove(player).commitAllowingStateLoss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeObject(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AttachToPlayer.class)) {
                method.setAccessible(true);
                attachFragment((PlayerFragment) method.invoke(object));
            }
        }
    }

    /**
     * This method is used to change channel using up and down keypad
     * @param pageUp is Boolean, to identify whether the key is up or not
     * @return VideoInfo
     */
    public VideoInfo channelTune(boolean pageUp)
    {
        VideoInfo video=null;
        try {
            if (pageUp) {
                index++;
                // 1 < 5
                if (index < videoList.size()) {
                    video = videoList.get(index);
                } else {
                    video = videoList.get(0);
                    index = 0;
                }
            } else {
                index--;
                // -1 > 0
                if (index >= 0) {
                    video = videoList.get(index);
                } else {
                    video = videoList.get(videoList.size() - 1);
                    index = videoList.size();
                }
            }
            onChannelIndex(index);
            Log.d(TAG, "channelTune: source = " + video.getPath() + ";  no = " + video.getChannelNo());
        }catch (Exception e){
            e.printStackTrace();
        }
        return video;
    }

    public String channelNumber(KeyEvent event,@NonNull final PlayerVLCFragment player)
    {
        channelNo += ActivityControl.getInput(event);
        if(timerChannel !=null)
        {
            timerChannel.purge();
            timerChannel.cancel();
            timerChannel=null;
        }
        timerChannel = new Timer();
        timerChannel.schedule(timerTaskChannel =new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        VideoInfo info = null;
                        for(int x=0;x <videoList.size();x++)
                        {
                            String no = videoList.get(x).getChannelNo()+"";
                            info =  videoList.get(x);
                            if(no.equals(channelNo))
                            {
                                index=x;
                                timerChannel.purge();
                                timerChannel.cancel();
                                onChannelChanged(info);
                                break;
                            }
                        }
                        channelNo="";
                    }
                });
            }
        },3000);
        return channelNo;
    }

    /**
     * This method is used to monitor the status of the source
     */
    public void playerMonitor()
    {
        int count = 0;
        if(timerMonitor ==null)
            timerMonitor = new Timer();
        timerMonitor.scheduleAtFixedRate(timerTaskMonitor =new TimerTask() {
            @Override
            public void run() {
                try {
                    if (playerFragment != null) {
                        if (seconds < (counter - 2)) {
                            if (playerFragment instanceof PlayerVLCFragment)
                                if (!((PlayerVLCFragment) playerFragment).getmMediaPlayer().isPlaying()) {
                                    monitor++;
                                    onStatus(Calendar.getInstance().getTime() + " => " + ((PlayerVLCFragment) playerFragment).path + "", false);
                                } else {
                                    monitor = 0;
                                }

                        } else {
                            monitor = 0;
                        }
                    }
                    counter++;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },0, 1000);

    }
    public void setTVPreference(VideoInfo info){
        try {
            SharedPreferences.Editor editor = getSharedPreferences("TVPLAYERPreference", MODE_PRIVATE).edit();
            editor.putInt("channelNo", info.getChannelNo());
            editor.putString("path", info.getPath()+"");
            editor.putString("name", info.getName()+"");
            editor.putString("description", info.getDescription()+"");
            editor.putString("icon", info.getIcon()+"");
            editor.apply();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //--------------------------------Setter--------------------------------------------------------
    public void setVideoList(ArrayList<VideoInfo> videoList) {
        this.videoList = videoList;
    }

    public void setChannelIndex(int index) {
        this.index = index;
    }
    public void setCounter(long counter) {
        this.counter = counter;
    }
    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }
    public void setHasControl(boolean hasControl) {
        this.hasControl = hasControl;
    }

    public void setRestarted(boolean restarted) {
        isRestarted = restarted;
    }

    //--------------------------------Getter--------------------------------------------------------
    public int getChannelIndex() {
        return index;
    }
    public boolean isHasControl() {
        return hasControl;
    }
    public boolean isRestarted() {
        return isRestarted;
    }

    public ArrayList<VideoInfo> getVideoList() {
        return videoList;
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    public abstract int getLayout();
}
