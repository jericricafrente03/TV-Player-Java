package com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.fragment;


import android.view.KeyEvent;

import com.ph.bittelasia.libvlc.control.adapter.ListAdapter;
import com.ph.bittelasia.libvlc.views.fragment.PlayerChannelsFragment;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.R;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.control.ChannelListAdapter;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.activity.PlayerActivity;



public class ChannelsFragment extends PlayerChannelsFragment implements ChannelListAdapter.TouchedListInterface {

    @Override
    public ListAdapter getAdapter()  {
        return new ChannelListAdapter(this).sort(getVideoList());
    }

    @Override
    public int containerID() {
        return R.id.list_layout;
    }

    @Override
    public void touchedIndex(int index) {
        setIndexPosition(index+1);
        getClickedVideoItem(true);
    }

    @Override
    public void selectedIndex() {
        ((PlayerActivity) getActivity()).dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        ((PlayerActivity) getActivity()).dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
    }
}
