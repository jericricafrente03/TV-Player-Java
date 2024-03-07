package com.ph.bittelasia.libvlc.model;

import android.content.Context;
import android.util.Log;


import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.util.VLCUtil;

public class VLCInstance {
    public final static String TAG = "VLC/Util/VLCInstance";

    private static LibVLC sLibVLC = null;

    /** A set of utility functions for the VLC application */
    public synchronized static LibVLC get(Context c) throws IllegalStateException {
        if (sLibVLC == null) {
            final Context context =c;
            if(!VLCUtil.hasCompatibleCPU(context)) {
                Log.e(TAG, VLCUtil.getErrorMsg());
                throw new IllegalStateException("LibVLC initialisation failed: " + VLCUtil.getErrorMsg());
            }

            /*
            * Todo: Uncomment this if you want to run on native code
            * TODO: by Steward
            * sLibVLC = new LibVLC(context,VLCOptions.getLibOptions(context));
            * */
            sLibVLC = new LibVLC(context);
        }
        return sLibVLC;
    }

    public static synchronized void restart(Context context) throws IllegalStateException {
        if (sLibVLC != null) {
            sLibVLC.release();
            /*
             * Todo: Uncomment this if you want to run on native code
             * TODO: by Steward
             * sLibVLC = new LibVLC(context,VLCOptions.getLibOptions(context));
             * */
            sLibVLC = new LibVLC(context);
        }
    }
}