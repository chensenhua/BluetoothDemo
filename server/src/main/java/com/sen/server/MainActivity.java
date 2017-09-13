package com.sen.server;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sen.bluetooth.bre.BluetoothServer;
import com.sen.bluetooth.Constants;
import com.sen.bluetooth.Error;
import com.sen.bluetooth.callbacks.SendResponse;
import com.sen.bluetooth.listeners.OnDataReceiverListener;
import com.sen.bluetooth.utils.Dbug;

public class MainActivity extends AppCompatActivity {
    private String tag = getClass().getSimpleName();
    private BluetoothServer bluetoothServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothServer = new BluetoothServer(getApplicationContext());
        new Thread()
        {
            @Override
            public void run() {
                bluetoothServer.createSocketServer("test", Constants.MY_UUID);
                bluetoothServer.registerDataReceiverListener(new OnDataReceiverListener() {
                    @Override
                    public void onReceiver(String name, byte[] data) {
                        Dbug.i(tag, "onReceiver->" + new String(data));
                        bluetoothServer.sendData(("receiver from "+name).getBytes(), new SendResponse() {
                            @Override
                            public void onRespond(Error code) {
                                Dbug.i(tag, "sendData->:" + code.toString());
                            }
                        }, bluetoothServer.getClientByName(name));
                    }
                });
            }
        }.start();

    }
}
