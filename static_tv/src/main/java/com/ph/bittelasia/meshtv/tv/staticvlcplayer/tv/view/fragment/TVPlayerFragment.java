package com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.view.fragment;

import androidx.annotation.NonNull;

import com.ph.bittelasia.libvlc.control.annotation.PlayerSettings;
import com.ph.bittelasia.libvlc.model.ScaleType;
import com.ph.bittelasia.libvlc.model.VideoInfo;
import com.ph.bittelasia.libvlc.views.fragment.PlayerVLCFragment;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.R;


@PlayerSettings(scaleType = ScaleType.SURFACE_FILL, preventDeadLock = true, enableDelay =true)
public class TVPlayerFragment extends PlayerVLCFragment {

    private String path = "udp://@239.0.0.5:6010";
    private Object object;
    public TVPlayerFragment getObject(Object object)
    {
        this.object = object;
        return this;
    }

    @Override
    public int containerID() {
        return R.id.player_layout;
    }



    @NonNull
    @Override
    public String getPath() throws Exception {
        if(object==null)
            throw new Exception(TAG+": Object should not be null");
        else if(object instanceof String)
            path =  (String)object;
        else if(object instanceof VideoInfo)
            path =  ((VideoInfo)object).getPath();
        return path;
    };
}
