package com.ph.bittelasia.libvlc.control.exception;

import android.util.Log;

public class PlayerException extends RuntimeException {

    public PlayerException(String message) {
        super(message);
        Log.e(this.getClass().getSimpleName(), "PlayerException: "+message );
    }
}