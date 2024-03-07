package com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.fragment;

import com.ph.bittelasia.libvlc.views.fragment.PlayerLoaderFragment;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.R;


public class LoaderFragment extends PlayerLoaderFragment {

    @Override
    public int containerID() {
        return R.id.loader_layout;
    }

}
