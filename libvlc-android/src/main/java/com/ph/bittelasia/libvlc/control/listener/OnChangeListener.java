package com.ph.bittelasia.libvlc.control.listener;

public interface OnChangeListener {

    void onBufferChanged(float buffer);

    void onChannelIndex(int channelIndex);

    void onChanging(long seconds);

    void onStatus(String message,boolean isPlaying);

    void onChannelChanged(Object object);

    void onError(String msg);

    void onEnd();

}