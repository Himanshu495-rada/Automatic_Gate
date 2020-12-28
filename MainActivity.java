package com.example.auto_gate_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    Button btn, btn2, btn3;
    EditText ip_address, ssid, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        ssid = (EditText) findViewById(R.id.edittext_ssid);
        pass = (EditText) findViewById(R.id.edittext_pass);
        ip_address = (EditText) findViewById(R.id.edittext_pass);
        String SSID = ssid.getText().toString();
        String PASS = pass.getText().toString();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        Toast.makeText(getApplicationContext(), "Please Turn OFF Mobile Data", Toast.LENGTH_LONG);


        wifiManager.setWifiEnabled(true);
        wifiConfiguration.SSID = String.format("\"%s\"", SSID);
        wifiConfiguration.preSharedKey = String.format("\"%s\"", PASS);

        int netId = wifiManager.addNetwork(wifiConfiguration);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new UPThread()).start();
                btn2.setVisibility(View.INVISIBLE);

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new STOPThread()).start();
                btn.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new DOWNThread()).start();
                btn.setVisibility(View.INVISIBLE);
            }
        });

    }


    private PrintWriter output;
    private BufferedReader input;
    class UPThread implements Runnable{
        @Override
        public void run() {
            String ipAdress = "192.168.4.1";
            try {
                socket = new Socket(ipAdress, 80);
                output = new PrintWriter(socket.getOutputStream());
                output.write("1");
                output.flush();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class DOWNThread implements Runnable{
        @Override
        public void run() {
            String ipAdress = "192.168.4.1";
            try{
                socket = new Socket(ipAdress, 80);
                output = new PrintWriter(socket.getOutputStream());
                output.write("2");
                output.flush();
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    class STOPThread implements Runnable{
        @Override
        public void run() {
            String ipAdress = "192.168.4.1";
            try{
                socket = new Socket(ipAdress, 80);
                output = new PrintWriter(socket.getOutputStream());
                output.write("0");
                output.flush();
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}