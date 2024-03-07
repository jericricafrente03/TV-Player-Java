package com.ph.bittelasia.libvlc.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ph.bittelasia.libvlc.R;


public abstract class PlayerLoaderFragment extends PlayerFragment {

    //====================================Variable==================================================
    //------------------------------------Constant--------------------------------------------------
    private static final String TAG = "bittel/PlayerLoaderFragment";
    //-------------------------------------View-----------------------------------------------------
    private LinearLayout container;
    //----------------------------------------------------------------------------------------------

    //==============================================================================================

    //====================================LifeCycle=================================================
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnKeyListener(null);
        container = view.findViewById(R.id.ll_loading);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentListener.onFragmentDetached(this);
        onFragmentListener = null;
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================


    //=====================================Method===================================================
    //----------------------------------------------------------------------------------------------
    public LinearLayout getContainer() {
        return container;
    }
    public void hideProgress( final boolean hide)
    {
        if(getActivity()!=null)
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getContainer()!=null) {
                    if (hide) {
                        getContainer().setVisibility(View.GONE);
                    }else {
                        getContainer().setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
}
