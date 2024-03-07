package com.ph.bittelasia.mesh.tv.libFragment.view.fragment;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.ph.bittelasia.mesh.tv.libFragment.R;
import com.ph.bittelasia.mesh.tv.libFragment.control.adapter.ListAdapter;
import com.ph.bittelasia.mesh.tv.libFragment.control.listener.OnListClickListener;

import java.util.HashMap;

public abstract class ListViewFragment extends BasicFragment {

    private OnListClickListener listClickListener;
    private ListView listView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listClickListener = (OnListClickListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listClickListener = null;
    }

    @Override
    public void getView(View v) {
        listView = v.findViewById(getLisViewID());
        updateDetails();
    }


    public void setGridView(ListView listView) {
        this.listView = listView;
    }

    public ListView getListView() {
        return listView;
    }

    public void updateDetails()
    {
        if(getAdapter()!=null)
            getListView().setAdapter(getAdapter());
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listClickListener.onClicked(adapterView.getAdapter().getItem(i));
            }
        });
        getListView().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listClickListener.onSelected(adapterView.getAdapter().getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getListView().setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View view, MotionEvent motionEvent) {
                view.requestFocus();
                return false;
            }
        });
        getAdapter().notifyDataSetChanged();
    }

    public abstract int getLisViewID();
    public abstract ListAdapter getAdapter();

}
