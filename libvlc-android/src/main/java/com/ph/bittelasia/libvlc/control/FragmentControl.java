package com.ph.bittelasia.libvlc.control;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ph.bittelasia.libvlc.control.annotation.AttachPlayerFragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public final class FragmentControl {


    @UiThread
    public synchronized static void showFragment(AppCompatActivity activity,@NonNull String TAG, boolean show)
    {
        Fragment fragment =activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if(fragment!=null) {
            if(show)
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .show(fragment)
                        .commitAllowingStateLoss();
            else
                activity.getSupportFragmentManager().beginTransaction()
                        .hide(fragment)
                        .commitAllowingStateLoss();
        }
    }

    @UiThread
    public synchronized static void removeFragment(AppCompatActivity activity,@NonNull String TAG)
    {
        Fragment fragment =activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if(fragment!=null) {
            activity.getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }
    @UiThread
    public synchronized static void removeFragment(AppCompatActivity activity,@NonNull Fragment fragment)
    {
        activity.getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
    }

    @UiThread
    public synchronized static void attachFragment(AppCompatActivity activity,int container, @NonNull Fragment fragment,@NonNull String TAG)
    {
        activity.getSupportFragmentManager().beginTransaction().replace(container, fragment, TAG).commitAllowingStateLoss();
    }



    @SuppressWarnings("rawtypes")
    public static void setAttachFragments(FragmentManager fragmentManager, Class cls, Object o)
    {
        ArrayList<Method> fragments = new ArrayList<>(getFragmentMethods(cls));
        attachFragments(fragments,fragmentManager,o);
    }

    @UiThread
    synchronized static void attachFragments(ArrayList<Method> ms, FragmentManager fm, Object o)
    {
        for(Method m:ms)
        {
            attachFragment(fm,m,o);
        }
    }

    @UiThread
    static synchronized void attachFragment(FragmentManager fm, Method m, Object o)
    {
        try
        {
            Fragment f = (Fragment) m.invoke(o);
            AttachPlayerFragment a = m.getAnnotation(AttachPlayerFragment.class);
            Fragment prev = fm.findFragmentById(a.container());
            if(prev!=null)
            {
                fm.beginTransaction().remove(prev).commit();
            }
            fm.beginTransaction().add(a.container(),f,a.tag()).commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    static synchronized ArrayList<Method> getFragmentMethods(Class c)
    {
        ArrayList<Method> methods = new ArrayList<>();
        for(Method m:c.getMethods())
        {
            if(m.getAnnotation(AttachPlayerFragment.class)!=null)
            {
                methods.add(m);
            }
        }
        Collections.sort(methods, new Comparator<Method>() {
            @SuppressWarnings("ComparatorMethodParameterNotUsed")
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getAnnotation(AttachPlayerFragment.class).order()>o2.getAnnotation(AttachPlayerFragment.class).order()?1:-1;
            }
        });
        return methods;
    }
}
