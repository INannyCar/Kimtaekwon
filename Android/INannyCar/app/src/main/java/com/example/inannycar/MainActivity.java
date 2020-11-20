package com.example.inannycar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {
    private BluetoothSPP bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        bt = new BluetoothSPP(this);

        if(!bt.isBluetoothAvailable())
        {
            Toast.makeText(getApplicationContext()
                    ,"Bluetooth is not available"
                    ,Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                TextView tempure = findViewById(R.id.tempure);
                tempure.setText(message);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        ,"Connected to" + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        ,"Connection lost"
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        ,"Unable to connect"
                        , Toast.LENGTH_SHORT).show();
            }
        });

        Button btnBluetooth = findViewById(R.id.btnBluetooth);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt.getServiceState() == BluetoothState.STATE_CONNECTED){
                    bt.disconnect();
                }
                else{
                    Intent intnet = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intnet, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }
    public void onDestroy()
    {
        super.onDestroy();
        bt.stopService();
    }
    public void onStart(){
        super.onStart();
        if(!bt.isBluetoothEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        }else{
            if(!bt.isServiceAvailable()){
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }
    private void setup() {
        ToggleButton btnLed = findViewById(R.id.btnLed);
        btnLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    bt.send("H", true);
                }
                else{
                    bt.send("L", true);
                }
            }
        });
        ToggleButton btnLedMode = findViewById(R.id.btnLedMode);
        btnLedMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }
                else{

                }
            }
        });
        Button btnTempure = findViewById(R.id.btnTempure);
        btnTempure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.send("T", true);
            }
        });
        ToggleButton btnBreak = findViewById(R.id.btnBreak);
        btnBreak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }
                else{

                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK) {
                bt.connect(data);
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (requestCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled"
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}