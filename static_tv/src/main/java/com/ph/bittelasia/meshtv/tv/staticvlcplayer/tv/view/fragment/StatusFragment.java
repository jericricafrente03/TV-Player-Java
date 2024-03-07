package com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.fragment;

import com.ph.bittelasia.libvlc.views.fragment.PlayerStatusFragment;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.R;


public class StatusFragment extends PlayerStatusFragment {


    private String TAG = StatusFragment.class.getSimpleName();


    @Override
    public int containerID() {
        return R.id.status_layout;
    }

    @Override
    public boolean displayStatus() {
        return false;
    }

    @Override
    public boolean reportStatus() {
        return false;
    }
}
