package com.ph.bittelasia.libvlc.views.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ph.bittelasia.libvlc.model.VideoInfo;
import com.ph.bittelasia.libvlc.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public abstract class PlayerStatusFragment extends PlayerFragment {

    private String TAG = PlayerStatusFragment.class.getSimpleName();
    private Timer timer;
    private ConstraintLayout parentLayout;
    private TextView tvNo,tvTitle,tvStatus;
    private ImageView iv_icon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.status_view,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnKeyListener(null);
        tvNo = view.findViewById(R.id.tv_no);
        tvStatus = view.findViewById(R.id.tv_status);
        tvTitle =  view.findViewById(R.id.tv_name);
        iv_icon = view.findViewById(R.id.iv_icon);
        parentLayout = view.findViewById(R.id.cl_layout);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentListener.onFragmentDetached(this);
        onFragmentListener = null;
    }


    public  PlayerStatusFragment updateStatus(final AppCompatActivity activity, final Object object)
    {
        try {
            Log.i(TAG, "updateStatus: displayStatus="+displayStatus());
            if(!displayStatus()) {
                if (timer != null) {
                    timer.purge();
                    timer.cancel();
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    if (tvNo != null)
                                        tvNo.setText("");
                                    if (tvTitle != null)
                                        tvTitle.setText("");
                                    if (tvStatus != null)
                                        tvStatus.setText("");
                                    if (iv_icon != null)
                                        iv_icon.setImageResource(0);
                                    parentLayout.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        timer.purge();
                        timer.cancel();
                    }
                }, 5000);
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (object != null) {
                            if (object instanceof VideoInfo) {
                                if (tvNo != null)
                                    tvNo.setText((((VideoInfo) object).getChannelNo() + ""));
                                if (tvTitle != null)
                                    tvTitle.setText((((VideoInfo) object).getName() + ""));
                                if (tvStatus != null)
                                    tvStatus.setText((((VideoInfo) object).getDescription() + ""));
                                if (iv_icon != null)
                                {
                                    Uri uri = Uri.parse((((VideoInfo) object).getIcon()));
                                    Picasso.get().load(uri).into(iv_icon);
                                  //  Picasso.get().load((((VideoInfo) object).getIcon())).into(iv_icon);
                                }
                            } else if (object instanceof String) {
                                if (tvNo != null)
                                    tvNo.setText((((String) object)));
                            }
                            parentLayout.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            if(reportStatus()){
                try {
                    if(object!=null) {
                        Intent i = new Intent();
                        i.setAction("android.intent.action.SCREEN.RECORD");

                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy@hh:mm:ss", Locale.getDefault());
                        String formattedDate = df.format(c);

                        i.putExtra("room", ((VideoInfo) object).getName() + "-" + formattedDate);
                        getContext().sendBroadcast(i);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public abstract boolean displayStatus();
    public abstract boolean reportStatus();
}
