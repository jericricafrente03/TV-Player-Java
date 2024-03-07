package com.ph.bittelasia.libvlc.control.listener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ph.bittelasia.libvlc.model.VideoInfo;

/**
 * Interface class that has the following methods
 * @author Steward M. Apostol
 * @since 2020-05-16
 */

public interface OnPlayerListener {

    void playerLoad(@NonNull String message);
    void playerStopped(@Nullable VideoInfo videoInfo, @NonNull String message);
}
