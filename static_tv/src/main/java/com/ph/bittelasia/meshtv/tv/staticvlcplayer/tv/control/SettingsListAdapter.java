package com.ph.bittelasia.meshtv.tv.staticvlcplayer.tv.control;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ph.bittelasia.libvlc.model.VideoInfo;
import com.ph.bittelasia.mesh.tv.libFragment.control.adapter.ListAdapter;
import com.ph.bittelasia.meshtv.tv.staticvlcplayer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SettingsListAdapter extends ListAdapter {

    @Override
    public <T> void filterObjects(ArrayList<T> filters, Object object, CharSequence charSequence) {

    }

    @Override
    public <T> ArrayList sortObjects(ArrayList<T> objects) {
        return objects;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            final VideoInfo info = ((VideoInfo) getObjects().get(i));
            final Context context = viewGroup.getContext().getApplicationContext();
            final ViewHolder viewHolder;


            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.channel_list_view_item_row, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Uri uri = Uri.parse(info.getIcon());
            Picasso.get().load(uri).into(viewHolder.icon);
            //Picasso.get().load(info.getIcon()).into(viewHolder.icon);
            viewHolder.channel.setText(Html.fromHtml(info.getChannelNo()+"  "+info.getName()));
            viewHolder.parent.setClickable(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return view;
    }

    //========================================ViewHolder============================================
    //----------------------------------------------------------------------------------------------
    @SuppressWarnings("SpellCheckingInspection")
    static class ViewHolder{

        final ImageView icon;
        final TextView channel;
        final RelativeLayout parent;

        View view;

        ViewHolder(View itemView) {
            setView(itemView);
            icon = itemView.findViewById(R.id.iv_icon);
            channel = itemView.findViewById(R.id.tv_channel);
            parent = itemView.findViewById(R.id.parent);
        }

        void setView(View view) {
            this.view = view;
        }

        View getView() {
            return view;
        }
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
}
