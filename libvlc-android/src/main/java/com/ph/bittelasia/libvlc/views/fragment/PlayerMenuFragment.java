package com.ph.bittelasia.libvlc.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ph.bittelasia.libvlc.R;
import com.ph.bittelasia.libvlc.control.adapter.ListAdapter;
import com.ph.bittelasia.libvlc.model.Menu;
import com.ph.bittelasia.libvlc.model.VideoInfo;

import java.util.ArrayList;

public abstract class PlayerMenuFragment extends PlayerFragment {

    //==============================Variable========================================================
    //------------------------------Constant--------------------------------------------------------
    private static final String TAG = PlayerMenuFragment.class.getSimpleName();
    //------------------------------Instance--------------------------------------------------------
    private ArrayList<Menu> menus;
    //-------------------------------View-----------------------------------------------------------
    private GridView gridView;
    //----------------------------------------------------------------------------------------------
    //==============================================================================================


    //==============================LifeCycle=======================================================
    //----------------------------------------------------------------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.player_menu_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            draw(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentListener.onFragmentDetached(this);
        onFragmentListener = null;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================

    //====================================Method====================================================
    //----------------------------------------------------------------------------------------------
    final void draw(View view) throws Exception {
        gridView = view.findViewById(R.id.gv_list);
        if(getAdapter()!=null){
            gridView.setAdapter(getAdapter());
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    adapterView.setSelected(true);
                }
            });
            gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            gridView.setOnHoverListener(new View.OnHoverListener() {
                @Override
                public boolean onHover(View view, MotionEvent motionEvent) {
                    view.requestFocus();
                    return false;
                }
            });
            getAdapter().notifyDataSetChanged();
        }

    }
    //----------------------------------------------------------------------------------------------


    public void setMenus(ArrayList<Menu> menus) {
        this.menus = menus;
    }

    public ArrayList<Menu> getMenus() {
        return menus;
    }

    public GridView getGridView() {
        return gridView;
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================

    //================================Abstract Methods==============================================
    //----------------------------------------------------------------------------------------------
    public abstract ListAdapter getAdapter() throws Exception;
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
}
