package com.ph.bittelasia.libvlc.control.annotation;

import com.ph.bittelasia.libvlc.model.ScaleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)

public @interface PlayerSettings {
    ScaleType scaleType();
    boolean preventDeadLock();
    boolean enableDelay();
}
