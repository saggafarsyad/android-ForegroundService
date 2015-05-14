package com.saggafarsyad.helloapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    // Service bound flags
    private boolean isServiceBound;

    // Service
    private HelloService mService;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getActivity(), HelloService.class);
        getActivity().getApplicationContext().startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = new Intent(getActivity(), HelloService.class);
        getActivity().getApplicationContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        getActivity().getApplicationContext().unbindService(mServiceConnection);

        super.onPause();
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(getActivity(), HelloService.class);
        getActivity().getApplicationContext().stopService(intent);

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Inflate buttons
        Button startForegroundButton = (Button) rootView.findViewById(R.id.button_start_foreground);
        Button stopForegroundButton = (Button) rootView.findViewById(R.id.button_stop_foreground);
        Button sayHelloButton = (Button) rootView.findViewById(R.id.button_hello);

        // Set onClick Listener
        startForegroundButton.setOnClickListener(startForegroundAction);
        stopForegroundButton.setOnClickListener(stopForegroundAction);
        sayHelloButton.setOnClickListener(sayHelloAction);

        return rootView;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Set service
            isServiceBound = true;
            mService = ((HelloService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
            mService = null;
        }
    };

    private final View.OnClickListener sayHelloAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isServiceBound && mService.getState() == HelloService.STATE_START) {
                mService.sayHello();
            } else {
                Toast.makeText(getActivity(), "Service is not start on foreground", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private final View.OnClickListener startForegroundAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isServiceBound) {
                if (mService.getState() == HelloService.STATE_STOP) {
                    mService.start();
                    Toast.makeText(getActivity(), "Service started", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Service has been started", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private final View.OnClickListener stopForegroundAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isServiceBound) {
                if (mService.getState() == HelloService.STATE_START) {
                    mService.stop();
                    Toast.makeText(getActivity(), "Service stopped", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Service has not been started", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
