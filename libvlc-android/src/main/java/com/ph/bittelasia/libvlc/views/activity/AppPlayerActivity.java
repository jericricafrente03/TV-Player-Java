package com.ph.bittelasia.libvlc.views.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ph.bittelasia.libvlc.control.annotation.ActivityFragment;
import com.ph.bittelasia.libvlc.control.annotation.ActivityLayout;
import com.ph.bittelasia.libvlc.control.annotation.UpdateContents;
import com.ph.bittelasia.libvlc.views.fragment.AppFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public abstract class AppPlayerActivity extends AppCompatActivity {

    private static final String TAG = "AppPlayerActivity";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(checkLayout(this));
            initializeObject(this);
            updateObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.e(TAG, "onAttachFragment: "+fragment.getClass().getSimpleName());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private int checkLayout(Object object) {
        if (object==null) {
            throw new RuntimeException(this.getClass().getSimpleName()+"The object to layout is null");
        }
        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(ActivityLayout.class)) {
            throw new RuntimeException(clazz.getSimpleName() + " is not annotated with Layout");
        }
        else
        {
            return Objects.requireNonNull(clazz.getAnnotation(ActivityLayout.class)).value();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initializeObject(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ActivityFragment.class)) {
                int container = Objects.requireNonNull(field.getAnnotation(ActivityFragment.class)).containerID();
                if(field.get(object) instanceof AppFragment) {
                    AppFragment fragment = (AppFragment) field.get(object);
                    assert fragment != null;
                    attachFragment(fragment, container);
                }else{
                    throw new RuntimeException(TAG+": instance is not a subclass of BasicFragment");
                }
            }
        }
    }

    private void updateObject(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(UpdateContents.class)) {
                method.setAccessible(true);
                method.invoke(object);
            }
        }
    }

    public <T extends AppFragment>void attachFragment(@NonNull T player, int containerID) {
        if(containerID==0)
            throw new RuntimeException(player.getClass().getSimpleName() + " -> Must use non-zero containerViewId");
        getSupportFragmentManager().beginTransaction().replace(
                containerID,
                player,
                player.getClass().getSimpleName()).
                commitAllowingStateLoss();
    }
}
