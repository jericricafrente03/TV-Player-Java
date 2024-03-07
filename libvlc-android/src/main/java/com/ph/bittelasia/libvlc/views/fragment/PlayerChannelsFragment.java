package com.ph.bittelasia.libvlc.views.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ph.bittelasia.libvlc.control.adapter.ListAdapter;
import com.ph.bittelasia.libvlc.control.exception.PlayerException;
import com.ph.bittelasia.libvlc.model.VideoInfo;
import com.ph.bittelasia.libvlc.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("LongLogTag")
public abstract class PlayerChannelsFragment extends PlayerFragment {

    //==================================Variable====================================================
    //----------------------------------Constant----------------------------------------------------
    private static final String TAG = "VLC/PlayerChannelsFragment";
    //------------------------------------View------------------------------------------------------
    private ListView     drawerList;
    //----------------------------------Instance----------------------------------------------------
    private Timer openTimer=null;
    private Timer closeTimer=null;
    private Timer hideTimer=null;
    private TimerTask hideTimerTask=null;
    private TimerTask openTimerTask=null;
    private TimerTask closeTimerTask=null;
    private ArrayList<VideoInfo> videoList=null;
    private int indexPosition =0;
    private int shortAnimationDuration = 0;
    boolean drawerOpen = false;
    //----------------------------------------------------------------------------------------------
    //==============================================================================================


    //==================================LifeCycle===================================================
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            draw(view);
        } catch (PlayerException e) {
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
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================

    //===================================Method=====================================================

    final void draw(View v) {
        drawerList = v.findViewById(R.id.left_drawer);
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        if(getDrawerList()!=null)
        {
            if(getAdapter()!=null)
            {
                getDrawerList().setAdapter(getAdapter());
                getDrawerList().setVisibility(View.GONE);
                Log.d(TAG, "Successfully drawn");
            }
        }else
        {
            Log.e(TAG, "Drawer list is null");
        }
    }
    public void openDrawer(final int position)
    {
        try {
            if(openTimer==null)
                openTimer = new Timer();
            if(openTimerTask!=null)
                openTimerTask.cancel();
            openTimer.schedule(openTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if(getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (drawerList != null) {
                                        if (getDrawerList().getVisibility() == View.GONE) {
                                            getDrawerList().setAlpha(0f);
                                            getDrawerList().setVisibility(View.VISIBLE);
                                            getDrawerList().animate()
                                                    .alpha(1f)
                                                    .setDuration(shortAnimationDuration)
                                                    .setListener(null);

                                            PlayerChannelsFragment.this.indexPosition = position;
                                            getDrawerList().setSelection(position);
                                            getDrawerList().smoothScrollToPosition(position);
                                            getDrawerList().getAdapter().getView(position, null, getDrawerList()).requestFocus();
                                            drawerOpen = true;
                                            openTimerTask.cancel();
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }
            },200);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void closeDrawer()
    {
        try {
            if(closeTimer==null)
                closeTimer= new Timer();
            if(closeTimerTask!=null)
                closeTimerTask.cancel();
            closeTimer.schedule(closeTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if(getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (getDrawerList().getVisibility() == View.VISIBLE) {
                                    getDrawerList().setAlpha(0f);
                                    getDrawerList().setVisibility(View.VISIBLE);
                                    getDrawerList().animate()
                                            .alpha(0f)
                                            .setDuration(shortAnimationDuration)
                                            .setListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    getDrawerList().setVisibility(View.GONE);
                                                }
                                            });
                                    drawerOpen = false;
                                }
                            }
                        });
                    }
                }
            },200);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getClickedVideoItem(final boolean isUp)
    {
        if(getActivity()!=null)
            if(hideTimer!=null){
                hideTimer.cancel();
                hideTimer.purge();
            }
        getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                try {
                    if (!isUp) {
                        indexPosition++;
                        //     1 < 5
                        if (indexPosition < getVideoList().size()) {
                            getDrawerList().setItemChecked(indexPosition, true);
                        } else {
                            indexPosition = 0;
                            getDrawerList().setItemChecked(indexPosition, true);
                        }
                    } else {
                        indexPosition--;
                        // -1 > 0
                        if (indexPosition >= 0) {
                            getDrawerList().setItemChecked(indexPosition, true);
                        } else {
                            indexPosition = getVideoList().size();
                            getDrawerList().setItemChecked(indexPosition, true);
                        }
                    }
                    Log.i(TAG, "getClickedVideoItem: "+indexPosition);
                    getDrawerList().setSelection(indexPosition);

                    // getDrawerList().smoothScrollToPosition(indexPosition);
                    if (onChangeListener != null)
                        onChangeListener.onChannelIndex(indexPosition);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        hideTimer= new Timer();
        hideTimer.schedule(hideTimerTask= new TimerTask() {
            @Override
            public void run() {
                closeDrawer();            }
        },5000);
    }
    //----------------------------------------------------------------------------------------------
    //-----------------------------------Setter-----------------------------------------------------
    public void setIndexPosition(int indexPosition) {
        this.indexPosition = indexPosition;
    }
    public PlayerChannelsFragment setVideoList(ArrayList<VideoInfo> videoList) {
        this.videoList = videoList;
        return this;
    }
    //-----------------------------------Getter-----------------------------------------------------

    public ListView getDrawerList() {
        return drawerList;
    }
    public int getIndexPosition() {
        return indexPosition;
    }
    public ArrayList<VideoInfo> getVideoList() {
        return videoList;
    }

    public boolean isDrawerOpen() {
        return drawerOpen;
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================

    //==============================Abstract Methods================================================
    //----------------------------------------------------------------------------------------------
    public abstract ListAdapter getAdapter() throws PlayerException;
    //----------------------------------------------------------------------------------------------
    //==============================================================================================

}
