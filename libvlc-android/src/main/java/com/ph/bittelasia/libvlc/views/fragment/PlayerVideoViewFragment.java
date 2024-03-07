package com.ph.bittelasia.libvlc.views.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;


import com.ph.bittelasia.libvlc.control.listener.OnChangeListener;
import com.ph.bittelasia.libvlc.R;

import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class PlayerVideoViewFragment extends PlayerFragment implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    //=============================Variable=========================================================
    //-----------------------------Constant---------------------------------------------------------
    private static final String TAG = "bittel/PlayerVideoViewFragment";
    //------------------------------View------------------------------------------------------------
    private VideoView videoView;
    private MediaController mediaControls;
    //-----------------------------Instance---------------------------------------------------------
    private int counter;
    private String name;
    //----------------------------------------------------------------------------------------------
    //==============================================================================================


    //=================================LifeCycle====================================================
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_view_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        draw(view);
        getVideoPosition();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentListener.onFragmentDetached(this);
        onFragmentListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Uri uri = Uri.parse(getPath());
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================

    //=============================MediaPlayerListeners=============================================
    //----------------------------------------------------------------------------------------------
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.isPlaying();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================

    //===================================Method=====================================================
    //----------------------------------------------------------------------------------------------
    public void draw(View view)
    {
        try {
            videoView = view.findViewById(R.id.vv_video);
            videoView.setOnCompletionListener(this);
            videoView.setOnErrorListener(this);
            videoView.setOnPreparedListener(this);

            if (mediaControls == null) {
                mediaControls = new MediaController(getContext());
                mediaControls.setAnchorView(videoView);
            }
            if(this.hasControl)
            {
                videoView.setMediaController(mediaControls);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @UiThread
    public void getVideoPosition()
    {
        try {
            ScheduledExecutorService mScheduledExecutorService = new ScheduledThreadPoolExecutor(1);
            mScheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    videoView.post(new Runnable() {
                        @Override
                        public void run() {
                            if(videoView!=null)
                            {
                                int mCurrentposition = videoView.getCurrentPosition();
                                if(onChangeListener !=null) {
                                    if (mCurrentposition == 0) {
                                        onChangeListener.onError(Calendar.getInstance().getTime() + "; position -> " + mCurrentposition + ", uri = " + getPath() + ", title = " + getName());
                                        counter++;
                                        if (counter >= 3)
                                            onChangeListener.onEnd();
                                    } else {
                                        counter = 0;
                                        onChangeListener.onStatus(Calendar.getInstance().getTime() + "; position -> " + mCurrentposition + ", uri = " + getPath() + ", title = " + getName(),videoView.isPlaying());
                                    }
                                }
                            }
                        }
                    });
                }

            }, 3000, 1000, TimeUnit.MILLISECONDS);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    public abstract @NonNull String getPath();
}
