package com.sen.bleclient;

import android.bluetooth.BluetoothDevice;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sen.bluetooth.ble.BluetoothBleClient;
import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnClickListener;
import com.sen.bluetooth.listeners.OnDevicesItemClickListener;
import com.sen.bluetooth.ui.fragments.DeviceListFragment;
import com.sen.bluetooth.utils.Dbug;

public class MainActivity extends AppCompatActivity {
    private String tag = getClass().getSimpleName();
    private BluetoothBleClient bluetoothBleClient;
    private BluetoothDevice mBluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DeviceListFragment deviceListFragment = new DeviceListFragment();
        bluetoothBleClient = BluetoothBleClient.getInstance(getApplicationContext());
        deviceListFragment.setBaseBluetoothManager(bluetoothBleClient);
        deviceListFragment.setmOnDevicesItemClickListener(new OnDevicesItemClickListener() {
            @Override
            public void onItemClick(FoundDevice foundDevice) {
                Dbug.i(tag, foundDevice.toString());
                bluetoothBleClient.connect(foundDevice.getBluetoothDevice());
            }
        });
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, deviceListFragment, deviceListFragment.getClass().getSimpleName());
        fragmentTransaction.commit();
        //bluetoothBleClient.setPrefix("HB");
        deviceListFragment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                bluetoothBleClient.startBleScan();
            }
        });
    }
}
