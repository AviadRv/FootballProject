package com.aviad.footballwithfriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;

import com.aviad.footballwithfriends.MainActivity;
import com.aviad.footballwithfriends.R;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class StopwatchFragment extends Fragment {

    TextView textView ;

    Button start, pause, reset, lap ;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    Handler handler;

    int Seconds, Minutes, MilliSeconds ;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int time = intent.getIntExtra("time", 0);
            textView.setText(String.valueOf(time));
        }
    };

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        initUI(layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getContext() != null)
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver , new IntentFilter(MyTimerService.ACTION_TIMER_CHANGED));
    }



    private void initUI(View layout) {

        textView = (TextView) layout.findViewById(R.id.tvTimer);
        start = (Button) layout.findViewById(R.id.btnStart);
        pause = (Button) layout.findViewById(R.id.btnPause);
        reset = (Button) layout.findViewById(R.id.btnReset);

        handler = new Handler();


        start.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);

            reset.setEnabled(false);

        }
    });

        pause.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            TimeBuff += MillisecondTime;

            handler.removeCallbacks(runnable);

            reset.setEnabled(true);

        }
    });

        reset.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            MillisecondTime = 0L ;
            StartTime = 0L ;
            TimeBuff = 0L ;
            UpdateTime = 0L ;
            Seconds = 0 ;
            Minutes = 0 ;
            MilliSeconds = 0 ;

            textView.setText("00:00:00");

        }
    });

    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            textView.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };

}