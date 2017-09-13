package com.sen.bleclient;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sen.bluetooth.Error;
import com.sen.bluetooth.ble.BluetoothBleClient;
import com.sen.bluetooth.ble.OnServicesDiscoverListener;
import com.sen.bluetooth.callbacks.SendResponse;
import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnClickListener;
import com.sen.bluetooth.listeners.OnConnectStateListener;
import com.sen.bluetooth.listeners.OnDataReceiverListener;
import com.sen.bluetooth.listeners.OnDevicesItemClickListener;
import com.sen.bluetooth.ui.fragments.DeviceListFragment;
import com.sen.bluetooth.utils.CHexConver;
import com.sen.bluetooth.utils.Dbug;

import static com.sen.bleclient.Constants.UUID_NOTIFICATION;
import static com.sen.bleclient.Constants.UUID_SERVICE;

public class MainActivity extends AppCompatActivity {
    private String tag = getClass().getSimpleName();
    private BluetoothBleClient bluetoothBleClient;
    private BluetoothDevice mBluetoothDevice;
    private Button button;
    private Button notify;

    private static int i = 0;


    private SendResponse sendResponse = new SendResponse() {
        @Override
        public void onRespond(Error code) {
            Toast.makeText(getApplicationContext(), code == Error.DATA_SEND_SUCCESS ? "发送成功" : "发送失败", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.send);
        notify = (Button) findViewById(R.id.set_notify);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothBleClient.enableNotify(mBluetoothDevice, UUID_SERVICE, Constants.UUID_NOTIFICATION);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothDevice != null) {
                    switch (i % 4) {
                        case 0:
                            bluetoothBleClient.sendData(mBluetoothDevice, UUID_SERVICE, Constants.UUID_WRITE, Constants.com2, sendResponse);
                            break;
                        case 1:
                            bluetoothBleClient.sendData(mBluetoothDevice, UUID_SERVICE, Constants.UUID_WRITE, Constants.com3, sendResponse);
                            break;
                        case 2:
                            bluetoothBleClient.sendData(mBluetoothDevice, UUID_SERVICE, Constants.UUID_WRITE, Constants.com4, sendResponse);
                            break;
                        case 3:
                            bluetoothBleClient.sendData(mBluetoothDevice, UUID_SERVICE, Constants.UUID_WRITE, Constants.com5, sendResponse);
                            break;
                    }
                    i++;
                }
            }
        });
        final DeviceListFragment deviceListFragment = new DeviceListFragment();
        bluetoothBleClient = BluetoothBleClient.getInstance(getApplicationContext());
        deviceListFragment.setBaseBluetoothManager(bluetoothBleClient);
        deviceListFragment.setmOnDevicesItemClickListener(new OnDevicesItemClickListener() {
            @Override
            public void onItemClick(FoundDevice foundDevice) {
                Dbug.i(tag, foundDevice.toString());
                bluetoothBleClient.connect(foundDevice.getBluetoothDevice());
                bluetoothBleClient.stopBreBleScan();
            }
        });
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, deviceListFragment, deviceListFragment.getClass().getSimpleName());
        fragmentTransaction.commit();
        //bluetoothBleClient.setPrefix("HB");
        deviceListFragment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                bluetoothBleClient.startBreBleScan();
            }
        });
        bluetoothBleClient.registerConnectStateChangeListener(new OnConnectStateListener() {
            @Override
            public void connecting(BluetoothDevice bluetoothDevice, Error error) {
                Dbug.e(tag, "connecting");
                mBluetoothDevice = null;
            }

            @Override
            public void connected(final BluetoothDevice bluetoothDevice, Error error) {
                if (error == Error.CONNECTED_OK) {
                    Dbug.e(tag, "connected");
                    mBluetoothDevice = bluetoothDevice;
                } else {
                    Dbug.e(tag, "connected failed");
                }
            }

            @Override
            public void disconnecting(BluetoothDevice bluetoothDevice, Error error) {
                Dbug.e(tag, "disconnecting");
                mBluetoothDevice = null;
            }

            @Override
            public void disconnected(BluetoothDevice bluetoothDevice, Error error) {
                Dbug.e(tag, "disconnected");
                mBluetoothDevice = null;
            }
        });

        bluetoothBleClient.registerDataReceiverListener(new OnDataReceiverListener() {
            @Override
            public void onReceiver(BluetoothDevice bluetoothDevice, byte[] data) {
                Dbug.e(tag,bluetoothDevice.toString()+"    "+ CHexConver.byte2HexStr(data,data.length));
            }
        });
        bluetoothBleClient.registerServicesDiscoverListener(new OnServicesDiscoverListener() {
            @Override
            public void onDiscoverSuccess(BluetoothDevice bluetoothDevice) {
                bluetoothBleClient.enableNotify(bluetoothDevice,UUID_SERVICE,UUID_NOTIFICATION);
            }

            @Override
            public void onDiscoverFailed(BluetoothDevice bluetoothDevice) {
                bluetoothBleClient.disableNotify(bluetoothDevice,UUID_SERVICE,UUID_NOTIFICATION);
            }
        });
    }
}
