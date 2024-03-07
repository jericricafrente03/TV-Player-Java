package com.ph.bittelasia.libvlc.control.listener;

public interface OnPermissionListener {
    void onPermissionGranted();
    void onPermissionDenied();
    void onPermissionAlreadyGranted();
}
