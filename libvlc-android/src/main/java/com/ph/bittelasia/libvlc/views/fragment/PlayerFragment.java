package com.ph.bittelasia.libvlc.views.fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ph.bittelasia.libvlc.control.listener.OnChangeListener;
import com.ph.bittelasia.libvlc.control.listener.OnFragmentListener;

public abstract class PlayerFragment extends Fragment {

    //==================================Variable====================================================
    //----------------------------------Instance----------------------------------------------------
    public OnChangeListener onChangeListener;
    public OnFragmentListener onFragmentListener;
    public Object object;
    public boolean hasControl;
    public boolean isRemoved;
    //----------------------------------------------------------------------------------------------
    //==============================================================================================


    //================================LifeCycle=====================================================
    //----------------------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onChangeListener =(OnChangeListener)context;
        onFragmentListener = (OnFragmentListener)context;
        isRemoved=false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onChangeListener =null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================


    //===================================Method=====================================================
    //-----------------------------------Getter-----------------------------------------------------
    public Object getObject() {
        return object;
    }
    public boolean isHasControl() {
        return hasControl;
    }
    //-----------------------------------Setter-----------------------------------------------------
    public void setObject(Object object) {
        this.object = object;
    }
    public void setHasControl(boolean hasControl) {
        this.hasControl = hasControl;
    }
    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    //----------------------------------------------------------------------------------------------
    public <T extends PlayerFragment> void  attachSelf(final AppCompatActivity activity,final T fragment )
    {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        activity.getSupportFragmentManager().beginTransaction().replace(fragment.containerID(), fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
                        isRemoved = false;
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void  removeSelf(final AppCompatActivity activity, final String TAG)
    {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(TAG);
                        if (fragment != null) {
                            activity.getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
                        }
                        isRemoved = true;
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public <T extends PlayerFragment> void  showSelf(final AppCompatActivity activity,final T fragment )
    {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        activity.getSupportFragmentManager().beginTransaction()
                                .show(fragment)
                                .commitAllowingStateLoss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void  hideSelf(final AppCompatActivity activity, final String TAG)
    {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(TAG);
                        if (fragment != null) {
                            activity.getSupportFragmentManager().beginTransaction()
                                    .hide(fragment)
                                    .commitAllowingStateLoss();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //==============================================================================================
    public abstract int containerID();
}
