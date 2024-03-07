package com.ph.bittelasia.libvlc.views.fragment;



import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaList;
import org.videolan.libvlc.MediaPlayer;

import com.ph.bittelasia.libvlc.control.annotation.PlayerSettings;
import com.ph.bittelasia.libvlc.model.VideoInfo;
import com.ph.bittelasia.libvlc.R;

import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static org.videolan.libvlc.MediaPlayer.Event.Buffering;
import static org.videolan.libvlc.MediaPlayer.Event.ESAdded;
import static org.videolan.libvlc.MediaPlayer.Event.ESDeleted;
import static org.videolan.libvlc.MediaPlayer.Event.ESSelected;
import static org.videolan.libvlc.MediaPlayer.Event.EncounteredError;
import static org.videolan.libvlc.MediaPlayer.Event.EndReached;
import static org.videolan.libvlc.MediaPlayer.Event.LengthChanged;
import static org.videolan.libvlc.MediaPlayer.Event.MediaChanged;
import static org.videolan.libvlc.MediaPlayer.Event.Opening;
import static org.videolan.libvlc.MediaPlayer.Event.Paused;
import static org.videolan.libvlc.MediaPlayer.Event.Playing;
import static org.videolan.libvlc.MediaPlayer.Event.PositionChanged;
import static org.videolan.libvlc.MediaPlayer.Event.SeekableChanged;
import static org.videolan.libvlc.MediaPlayer.Event.Stopped;
import static org.videolan.libvlc.MediaPlayer.Event.TimeChanged;
import static org.videolan.libvlc.MediaPlayer.Event.Vout;


public abstract class PlayerVLCFragment extends PlayerFragment{

    //=========================================Variable=============================================
    //-----------------------------------------Constant---------------------------------------------
    public static final String TAG = "VLC/PlayerFragment";
    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;
    //----------------------------------------------------------------------------------------------
    //-----------------------------------------Instance---------------------------------------------
    private Timer playerTimer = null;
    private Timer delayTimer = null;
    private TimerTask playerTask = null;
    private VLCVideoLayout mVideoLayout = null;
    private FrameLayout player_frame = null;
    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;
    private MediaPlayer.ScaleType scaleType = null;
    private Media media=null;
    private PlayerSettings playerSettings = null;
    private long position = 0;
    public long delaySeconds = 300;
    public  String path = null;

    //----------------------------------------------------------------------------------------------
    //==============================================================================================


    //========================================LifeCycle=============================================
    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.surface_layout,container,false);
    }

    @SuppressLint("LongLogTag")
    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Log.i(TAG, "onViewCreated: start");
            initialize();
            mVideoLayout = view.findViewById(R.id.video_layout);
            player_frame = view.findViewById(R.id.player_frame);
            if(onChangeListener ==null)
            {
                throw new RuntimeException(TAG+": OnChangeListener is not implemented");
            }
            Log.i(TAG, "onViewCreated: end");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentListener.onFragmentDetached(this);
        onFragmentListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mMediaPlayer.release();
            mLibVLC.release();
        }catch (Exception e)
        {
           e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
            try {
                media = new Media(mLibVLC, Uri.parse(getPath()));
                mMediaPlayer.setMedia(media);
                mMediaPlayer.updateVideoSurfaces();
                mMediaPlayer.setVideoScale(scaleType = scaleType());
                media.setHWDecoderEnabled(true, true);
                preventDeadLock(playerSettings,media);
                media.release();
                this.path = getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaPlayer.play();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            mMediaPlayer.stop();
            mMediaPlayer.detachViews();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================

    //==================================Method======================================================
    //----------------------------------------------------------------------------------------------
    public synchronized void play(@NonNull final Object object, final boolean isReplay){
        try {
            if(playerTimer==null)
                playerTimer = new Timer();
            if(playerTask!=null)
                playerTask.cancel();
            if(delayTimer!=null) {
                delayTimer.cancel();
                delayTimer.purge();
            }
            playerTimer.schedule(playerTask = new TimerTask() {
                @Override
                public void run() {
                    if(getActivity()!=null)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mMediaPlayer != null) {
                                    try {
                                        if(object instanceof VideoInfo) {
                                            if(PlayerVLCFragment.this.path.contains(((VideoInfo) object).getPath()) && !isReplay)
                                                return;
                                            PlayerVLCFragment.this.path = ((VideoInfo) object).getPath();
                                        }else if(object instanceof String) {
                                            if(PlayerVLCFragment.this.path.contains( ((String) object)) && !isReplay)
                                                return;
                                            PlayerVLCFragment.this.path = ((String) object);
                                        }
                                        stop();
                                        initialize();
                                        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
                                        media = new Media(mLibVLC, Uri.parse(PlayerVLCFragment.this.path));
                                        preventDeadLock(playerSettings,media);
                                        mMediaPlayer.setMedia(media);
                                        mMediaPlayer.updateVideoSurfaces();
                                        mMediaPlayer.setVideoScale(scaleType = scaleType());
                                        media.release();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if(playerSettings!=null)
                                        if(playerSettings.enableDelay())
                                        {
                                            onChangeListener.onError("delaying video..");
                                            if(delayTimer!=null)
                                            {
                                                delayTimer.cancel();
                                                delayTimer.purge();
                                                delayTimer=null;
                                            }
                                            delayTimer = new Timer();
                                            delayTimer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    try{
                                                        mMediaPlayer.play();
                                                        playerTask.cancel();
                                                        delayTimer.cancel();
                                                        delayTimer.purge();
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },delaySeconds  ,10);
                                        }else {
                                            mMediaPlayer.play();
                                            playerTask.cancel();
                                        }
                                }
                            }
                        });
                    }
                }
            },500);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public synchronized void stop(){
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.detachViews();
                mMediaPlayer.release();
                mLibVLC.release();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initialize(){
        playerSettings = getClass().getAnnotation(PlayerSettings.class);
        if(playerSettings ==null)
        {
            throw  new RuntimeException(TAG+": PlayerSettings annotation is required");
        }

        final ArrayList<String> args = new ArrayList<>();

        if(playerSettings.preventDeadLock()) {
            args.add("--no-sub-autodetect-file");
            args.add("--swscale-mode=0");
            args.add("--network-caching=100");
            args.add("--no-drop-late-frames");
            args.add("--no-skip-frames");
        }

        args.add("-vvv");
        mLibVLC = new LibVLC(Objects.requireNonNull(getContext()), args);
        mMediaPlayer = new MediaPlayer(mLibVLC);
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                try {
                    switch (event.type) {
                        case MediaChanged:
                            onChangeListener.onStatus("@MediaChanged",mMediaPlayer.isPlaying());
                            break;
                        case Opening:
                            onChangeListener.onStatus("@Opening",mMediaPlayer.isPlaying());
                            break;
                        case Buffering:
                            onChangeListener.onBufferChanged(Math.round(event.getBuffering()));
                            break;
                        case Playing:
                            onChangeListener.onStatus("@Playing",mMediaPlayer.isPlaying());
                            break;
                        case Paused:
                            onChangeListener.onStatus("@Paused",mMediaPlayer.isPlaying());
                            break;
                        case Stopped:
                            onChangeListener.onEnd();
                            break;
                        case EndReached:
                            onChangeListener.onStatus("@EndReached",mMediaPlayer.isPlaying());
                            break;
                        case EncounteredError:
                            onChangeListener.onError("@EncounteredError:  "+path);
                            break;
                        case TimeChanged:
                            position = event.getTimeChanged() / 1000L;
                            onChangeListener.onChanging(position);
                            break;
                        case PositionChanged:
                            onChangeListener.onStatus(Calendar.getInstance().getTime()+" => "+ path,mMediaPlayer.isPlaying());
                            break;
                        case SeekableChanged:
                            onChangeListener.onStatus("@SeekableChanged",mMediaPlayer.isPlaying());
                            break;
                        case LengthChanged:
                            onChangeListener.onStatus("@LengthChanged",mMediaPlayer.isPlaying());
                            break;
                        case Vout:
                            onChangeListener.onStatus("@Vout",mMediaPlayer.isPlaying());
                            break;
                        case ESAdded:
                            onChangeListener.onStatus("@ESAdded",mMediaPlayer.isPlaying());
                            break;
                        case ESDeleted:
                            onChangeListener.onStatus("@ESDeleted",mMediaPlayer.isPlaying());
                            break;
                        case ESSelected:
                            onChangeListener.onStatus("@ESSelected",mMediaPlayer.isPlaying());
                            break;
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    public synchronized void refresh(){
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.updateVideoSurfaces();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void preventDeadLock(@NonNull PlayerSettings setting, @NonNull Media media){
        try {
            if(setting.preventDeadLock()) {
                media.addOption(":network-caching=100");
                media.addOption(":clock-jitter=0");
                media.addOption(":clock-synchro=0");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public MediaPlayer.ScaleType scaleType() {
        if(playerSettings!=null)
        switch (playerSettings.scaleType()) {
            case SURFACE_4_3:
                return MediaPlayer.ScaleType.SURFACE_4_3;
            case SURFACE_16_9:
                return MediaPlayer.ScaleType.SURFACE_16_9;
            case SURFACE_BEST_FIT:
                return MediaPlayer.ScaleType.SURFACE_BEST_FIT;
            case SURFACE_ORIGINAL:
                return MediaPlayer.ScaleType.SURFACE_ORIGINAL;
            case SURFACE_FIT_SCREEN:
                return MediaPlayer.ScaleType.SURFACE_FIT_SCREEN;
            default:
                return MediaPlayer.ScaleType.SURFACE_FILL;
        }
        return MediaPlayer.ScaleType.SURFACE_FILL;
    }
    public LibVLC getmLibVLC() {
        return mLibVLC;
    }

    public FrameLayout getPlayerFrame() {
        return player_frame;
    }

    public VLCVideoLayout getmVideoLayout() {
        return mVideoLayout;
    }

    public Media getMedia() {
        return media;
    }

    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public MediaPlayer.ScaleType getScaleType() {
        return scaleType;
    }

    public abstract @NonNull String getPath() throws Exception;
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
}
