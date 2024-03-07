package com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.RequiresApi;

import com.ph.bittelasia.libvlc.control.annotation.AttachToPlayer;
import com.ph.bittelasia.libvlc.model.Channels;
import com.ph.bittelasia.libvlc.model.VideoInfo;
import com.ph.bittelasia.libvlc.views.activity.AbstractPlayerActivity;
import com.ph.bittelasia.libvlc.views.fragment.PlayerChannelsFragment;
import com.ph.bittelasia.libvlc.views.fragment.PlayerLoaderFragment;
import com.ph.bittelasia.libvlc.views.fragment.PlayerStatusFragment;
import com.ph.bittelasia.libvlc.views.fragment.PlayerVLCFragment;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.R;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.fragment.ChannelsFragment;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.fragment.LoaderFragment;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.fragment.StatusFragment;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.fragment.TVPlayerFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class PlayerActivity extends AbstractPlayerActivity {

    private ArrayList<VideoInfo> channels = null;
    private ArrayList<VideoInfo> hdMap = null;
    private TVPlayerFragment tVPlayerFragment = null;
    private StatusFragment statusFragment = null;
    private LoaderFragment loaderFragment = null;
    private ChannelsFragment channelsFragment = null;
    private int position = 0;


    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        try {
            Log.i(TAG, "dispatchKeyEvent: "+ event.getKeyCode());
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_NUMPAD_0:
                        case KeyEvent.KEYCODE_NUMPAD_1:
                        case KeyEvent.KEYCODE_NUMPAD_2:
                        case KeyEvent.KEYCODE_NUMPAD_3:
                        case KeyEvent.KEYCODE_NUMPAD_4:
                        case KeyEvent.KEYCODE_NUMPAD_5:
                        case KeyEvent.KEYCODE_NUMPAD_6:
                        case KeyEvent.KEYCODE_NUMPAD_7:
                        case KeyEvent.KEYCODE_NUMPAD_8:
                        case KeyEvent.KEYCODE_NUMPAD_9:
                        case KeyEvent.KEYCODE_BACK:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_NUMPAD_ENTER:
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                        case KeyEvent.KEYCODE_MENU:
                        case KeyEvent.KEYCODE_POWER:
                        case KeyEvent.KEYCODE_SETTINGS:
                        case KeyEvent.META_SHIFT_LEFT_ON:
                            return true;
                        case KeyEvent.KEYCODE_PAGE_UP:
                        case KeyEvent.KEYCODE_CHANNEL_UP:
                        case KeyEvent.KEYCODE_DPAD_UP:
                            if(channelsFragment!=null)
                                if(channelsFragment.isDrawerOpen())
                                    channelsFragment.getClickedVideoItem(true);
                            return true;
                        case KeyEvent.KEYCODE_PAGE_DOWN:
                        case KeyEvent.KEYCODE_CHANNEL_DOWN:
                        case KeyEvent.KEYCODE_DPAD_DOWN://Channel down
                            if(channelsFragment!=null)
                                if(channelsFragment.isDrawerOpen())
                                    channelsFragment.getClickedVideoItem(false);
                            return true;
                        default:
                            onChannelChanged(channelNumber(event, tVPlayerFragment));
                    }
                    break;
                case KeyEvent.ACTION_UP:
                    switch (event.getKeyCode()) {
                        case KeyEvent.KEYCODE_SETTINGS:
                            Log.i(TAG, "dispatchKeyEvent: Settings");
                            try {
                                Intent i = new Intent();
                                i.setAction("android.intent.action.LAUNCH.SETTING");
                                sendBroadcast(i);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return true;
                        case KeyEvent.KEYCODE_MENU:
                            if(channelsFragment!=null){
                                if(!channelsFragment.isDrawerOpen())
                                    channelsFragment.openDrawer(getChannelIndex());
                                else
                                    channelsFragment.closeDrawer();
                            }
                            return true;
                        case KeyEvent.KEYCODE_BACK:
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.KILL");
                            intent.putExtra("package",getPackageName());
                            sendBroadcast(intent);

                            super.onBackPressed();
                            return true;
                        case KeyEvent.KEYCODE_PAGE_UP:
                        case KeyEvent.KEYCODE_CHANNEL_UP:
                        case KeyEvent.KEYCODE_DPAD_UP:
                            if(channelsFragment!=null)
                                if(!channelsFragment.isDrawerOpen())
                                    onChannelChanged(channelTune(true));
                            return true;
                        case KeyEvent.KEYCODE_PAGE_DOWN:
                        case KeyEvent.KEYCODE_CHANNEL_DOWN:
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if(channelsFragment!=null)
                                if(!channelsFragment.isDrawerOpen())
                                  onChannelChanged(channelTune(false));
                            return true;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_NUMPAD_ENTER:
                            try {
                                setChannelIndex(position);
                                playerVLCFragment().play(getVideoList().get(position), false);
                                playerStatusFragment().updateStatus(this, getVideoList().get(position));
                                channelsFragment.closeDrawer();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return true;
                        case KeyEvent.META_SHIFT_LEFT_ON:
                            try {
                                Intent i = new Intent();
                                i.setAction("android.intent.action.LAUNCH.SETTING");
                                sendBroadcast(i);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return true;
                    }
                    break;
                default:
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        channels = new ArrayList<>();

        //SD
        channels.add(new VideoInfo.Builder().no(75).name("CNN Philippines").path("file:///storage/emulated/0/Android/cnn-ph.mp4").desc("4.148").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.cnn_ph).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(52).name("CARTOON NETWORK").path("file:///storage/emulated/0/Android/cartoon-network.mp4").desc("4.154").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.cartoon_network).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(20).name("DISNEY CHANNEL").path("file:///storage/emulated/0/Android/disney.mp4").desc("4.148").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.disney_channel).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(58).name("ANIMAX").path("file:///storage/emulated/0/Android/animax.mp4").desc("4.151").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.animax).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(24).name("AXN").path("file:///storage/emulated/0/Android/axn.mp4").desc("3.634").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.axn).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(1).name("SOLAR SPORTS").path("file:///storage/emulated/0/Android/solar-sports.mp4").desc("3.625").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.solar_sports).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(57).name("FRANCE 24").path("file:///storage/emulated/0/Android/france-24.mp4").desc("4.151").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.france_24).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(37).name("FOX SPORTS").path("file:///storage/emulated/0/Android/fox-sports.mp4").desc("4.446").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.fox_sports).buildVideoInfo());
        //channels.add(new VideoInfo.Builder().no(84).name("RED BY HBO HD").path("file:///storage/emulated/0/Android/red-by-hbo.mp4").desc("10.041").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.red_by_hbo).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(63).name("CINEMAX HD").path("file:///storage/emulated/0/Android/cinemax-hd.mp4").desc("6.473").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.cinemax_hd).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(128).name("FOX MOVIES HD").path("file:///storage/emulated/0/Android/fox-movies-hd.mp4").desc("9.608").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.fox_movies).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(111).name("WARNER TV HD").path("file:///storage/emulated/0/Android/warner-tv-hd.mp4").desc("4.467").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.warner_tv).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(90).name("LIFETIME HD").path("file:///storage/emulated/0/Android/lifetime.mp4").desc("4.437").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.lifetime).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(126).name("FASHIONTV HD").path("file:///storage/emulated/0/Android/fashion.mp4").desc("5.135").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.fashion_tv).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(79).name("DISCOVERY ASIA HD").path("file:///storage/emulated/0/Android/discovery.mp4").desc("3.634").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.discovery_asia).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(14).name("NAT GEO WILD HD").path("file:///storage/emulated/0/Android/natgeo-wild.mp4").desc("4.163").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.nat_geo_wild_hd).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(96).name("NATIONAL GEOGRAPHIC CHANNEL").path("file:///storage/emulated/0/Android/ngc-hd.mp4").desc("4.422").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.national_geographic_channel_hd).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(121).name("HISTORY HD").path("file:///storage/emulated/0/Android/history-hd.mp4").desc("6.985").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.history_hd).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(23).name("NICKELODEON HD").path("file:///storage/emulated/0/Android/nickelodeon.mp4").desc("4.157").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.nick_hd).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(114).name("BLOOMBERG HD").path("file:///storage/emulated/0/Android/bloomberg.mp4").desc("5.454").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.bloomberg).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(87).name("FOX NEWS HD").path("file:///storage/emulated/0/Android/fox-news.mp4").desc("4.416").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.fox_news_channel).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(41).name("CNN HD").path("file:///storage/emulated/0/Android/cnn-hd.mp4").desc("7.965").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.cnn).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(94).name("NHK WORLD HD").path("file:///storage/emulated/0/Android/nhk-world.mp4").desc("4.443").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.nhk_world).buildVideoInfo());
        channels.add(new VideoInfo.Builder().no(122).name("TVN HD").path("file:///storage/emulated/0/Android/tvn-hd.mp4").desc("5.174").icon("android.resource://"+getPackageName()+"/drawable/"+R.drawable.tvn).buildVideoInfo());
        getData(channels);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(playerStatusFragment()!=null)
        {
            if(getVideoList().size()>0)
               playerStatusFragment().updateStatus(this,getVideoList().get(position));
        }
    }

    @AttachToPlayer
    public PlayerVLCFragment playerVLCFragment() {
        if(tVPlayerFragment ==null) {
            tVPlayerFragment = new TVPlayerFragment();
        }
        return tVPlayerFragment;
    }

    @AttachToPlayer
    public PlayerLoaderFragment playerLoaderFragment(){
        if(loaderFragment==null) {
            loaderFragment = new LoaderFragment();
        }
        return loaderFragment;
    }

    @AttachToPlayer
    public PlayerStatusFragment playerStatusFragment() {
        if(statusFragment==null) {
            statusFragment = new StatusFragment();
        }
        return  statusFragment;
    }

    @AttachToPlayer
    public PlayerChannelsFragment playerChannelsFragment() {
        if(channelsFragment==null) {
            channelsFragment = new ChannelsFragment();
        }
        return channelsFragment.setVideoList(getVideoList());
    }


    @Override
    public int getLayout() {
        return R.layout.player_activity_layout;
    }


    @Override
    public void onBufferChanged(float buffer) {
        if(buffer>=100)
        {
            setCounter(0);
        }
    }

    @Override
    public void onChannelIndex(int channelIndex) {
        position = channelIndex;
    }

    @Override
    public void onChanging(long seconds) {
        setSeconds(seconds);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStatus(String message, boolean isPlaying) {
        try {
            runOnUiThread(() -> {
                if (isPlaying) {
                    if (!Objects.requireNonNull(playerLoaderFragment()).isRemoved)
                        playerLoaderFragment().removeSelf(PlayerActivity.this, playerLoaderFragment().getTag());
                    Log.d(TAG, "onStatus: " + message + " playing? => true");
                } else {
                    if (Objects.requireNonNull(playerLoaderFragment()).isRemoved)
                        playerLoaderFragment().attachSelf(PlayerActivity.this, loaderFragment);
                    Log.e(TAG, "onStatus: " + message + " playing? => false");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onChannelChanged(Object object) {
        if(object!=null)
            if(!channelsFragment.isDrawerOpen())
            {
                if (object instanceof VideoInfo) {
                    playerVLCFragment().play(object,false);
                }
                playerStatusFragment().updateStatus(this,object);
            }
    }

    @Override
    public void onError(String msg) {
        Log.e(TAG, "onError: " +msg);
        Objects.requireNonNull(playerLoaderFragment()).hideProgress(false);
    }

    @Override
    public void onEnd() {
        Objects.requireNonNull(playerLoaderFragment()).hideProgress(false);
        playerVLCFragment().stop();
        playerVLCFragment().play(playerVLCFragment().path,true);
        Log.d(TAG, "onEnd: ");
    }

    //====================================Method====================================================
    //----------------------------------------------------------------------------------------------
    final void getData(ArrayList<VideoInfo> list)
    {
        if(list.size()>0)
        {
            list.size();
            loadFragments(new Channels(list));
            playerVLCFragment().stop();
            tVPlayerFragment.getObject(list.get(getChannelIndex()));
            if(isRestarted())
            {
                playerVLCFragment().play(list.get(getChannelIndex()), false);
                playerStatusFragment().updateStatus(this,list.get(getChannelIndex()));
                setRestarted(false);
            }

        }
    }
}
