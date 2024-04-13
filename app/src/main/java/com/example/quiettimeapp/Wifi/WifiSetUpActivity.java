package com.example.quiettimeapp.Wifi;

import static android.net.wifi.WifiManager.UNKNOWN_SSID;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiettimeapp.R;

import java.util.Arrays;
import java.util.Objects;

public class WifiSetUpActivity extends AppCompatActivity {

    String connectedSSID;
    String alertTitle = "No WIFI network Detected !";
    String alertMessageEnableAndConnect = "Enable and Connect to a WIFI to avail the service";
    String alertMessageConnect = "Connect to a WIFI to add";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            getSupportActionBar().hide();
        }
        catch(Exception e){
            Log.d("exceptions", Arrays.toString(e.getStackTrace()));
        }
        setContentView(R.layout.activity_wifi);

        if(!isWifiEnabled())
        {
            ShowAlertDialog(alertTitle, alertMessageEnableAndConnect);
        }
        else{
            getConnectedSSID();
            TextView connectedTo = (TextView) findViewById(R.id.tv_ConnectedTo);
            connectedTo.setText(connectedSSID);
        }

    }

    private String getConnectedSSID()
    {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        connectedSSID= wifiInfo.getSSID();
        return connectedSSID;
    }

    private boolean isWifiEnabled()
    {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public void addSSID(View view)
    {
        if (isWifiEnabled())
        {
            DataBaseHelperForWifi dataBaseHelperForWifi = new DataBaseHelperForWifi(getBaseContext());

            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
                if(!Objects.equals(connectedSSID, UNKNOWN_SSID) && !dataBaseHelperForWifi.SearchInSSIDs(connectedSSID))
                {
                    dataBaseHelperForWifi.addSSID(connectedSSID);
                }
                else{
                    ShowAlertDialog("Already Added", "The current wifi network is alreadty added!" );
                }
                SharedPreferences sharedPreference = getSharedPreferences("OnOffDataBase", MODE_PRIVATE);
                if(sharedPreference.getBoolean("WifiOnOff",false))
                    PutToDND();
            } else {
                ShowAlertDialog(alertTitle, alertMessageConnect);
            }
        }
        else
        {
            ShowAlertDialog(alertTitle, alertMessageEnableAndConnect);
        }
    }

    private void PutToDND()
    {
        AudioManager audioManagerobj = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManagerobj.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    public void ShowAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNeutralButton("OK", (DialogInterface.OnClickListener)(dialog, which) ->{
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}