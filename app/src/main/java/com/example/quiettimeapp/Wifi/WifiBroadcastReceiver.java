package com.example.quiettimeapp.Wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiBroadcastReceiver";
    private static final String DESIRED_SSID = "YOUR_SSID_HERE";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        DataBaseHelperForWifi dataBaseHelperForWifi = new DataBaseHelperForWifi(context);

        String action = intent.getAction();
        if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.isConnected()) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if(dataBaseHelperForWifi.SearchInSSIDs(wifiInfo.getSSID()))
                {
                    AudioManager audioManagerobj = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    audioManagerobj.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }
                /*
                TODO: 16-05-2023 should keep off mode. so should get the previous wifi ssid. but is it possible?
                 else just off when ever an unknown wifi is detected
                */
            }
        }
    }
}
