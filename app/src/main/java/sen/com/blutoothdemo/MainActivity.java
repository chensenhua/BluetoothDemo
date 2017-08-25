package sen.com.blutoothdemo;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.sen.bluetooth.BluetoothClient;
import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnDevicesItemClickListener;
import com.sen.bluetooth.ui.fragments.DeviceListFragment;
import com.sen.bluetooth.utils.BluetoothUtil;
import com.sen.bluetooth.utils.Dbug;

public class MainActivity extends AppCompatActivity {
    private String tag = getClass().getSimpleName();
    private BluetoothDevice mBluetoothDevice;
    BluetoothClient bluetoothClient;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.container);
        DeviceListFragment deviceListFragment = new DeviceListFragment();
        bluetoothClient = BluetoothClient.getInstance(getApplicationContext());
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        deviceListFragment.setBaseBluetoothManager(bluetoothClient);
        deviceListFragment.setmOnDevicesItemClickListener(new OnDevicesItemClickListener() {
            @Override
            public void onItemClick(FoundDevice foundDevice) {
                Dbug.i(tag,foundDevice.toString());
            }
        });
        fragmentTransaction.replace(R.id.container,deviceListFragment, deviceListFragment.getClass().getSimpleName());
        fragmentTransaction.commit();

     /*   for (BluetoothDevice bluetoothDevice : BluetoothUtil.getBondedDevices()) {
            list.add(bluetoothDevice);
        }*/

      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getBondState() == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(MainActivity.this, "已配对", Toast.LENGTH_SHORT).show();
                    mBluetoothDevice = list.get(position);
                    //  BluetoothAdapter.getDefaultAdapter().getProfileProxy(MainActivity.this, serviceListener, BluetoothProfile.A2DP);
                    for (ParcelUuid uuid : mBluetoothDevice.getUuids()) {
                        Dbug.i(tag, " uuid=" + uuid.getUuid().toString());

                    }
                    if (mBluetoothDevice.getUuids() == null || mBluetoothDevice.getUuids().length == 0)
                        return;

                    new Thread() {
                        @Override
                        public void run() {
                            if (bluetoothClient.createSocketClient(mBluetoothDevice, Constants.MY_UUID)) {
                                bluetoothClient.registerDataReceiverListener(new OnDataReceiverListener() {
                                    @Override
                                    public void onReceiver(String name, byte[] data) {
                                        Dbug.i(tag, "onReceiver " + name + "->" + new String(data));
                                        bluetoothClient.sendData(data, new SendResponse() {
                                            @Override
                                            public void onRespond(Error code) {
                                                Dbug.i(tag, "send data->" + code.toString());
                                            }
                                        });
                                    }
                                });

                                bluetoothClient.sendData("send to server ".getBytes(), new SendResponse() {
                                    @Override
                                    public void onRespond(Error code) {
                                        Dbug.i(tag, "send code:" + code);
                                    }
                                });
                            }
                        }
                    }.start();

                } else if (list.get(position).getBondState() == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(MainActivity.this, "配对中", Toast.LENGTH_SHORT).show();
                }
                boolean ret = BluetoothUtil.pair(list.get(position));
                if (!ret) {
                    Toast.makeText(MainActivity.this, "配对失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/
        /*bluetoothClient.registerStateChangeListener(new OnStateChangeListener() {
            @Override
            public void enabled() {
                Dbug.i(tag, "enabled");
            }

            @Override
            public void enabling() {
                Dbug.i(tag, "enabling");
            }

            @Override
            public void disabled() {
                Dbug.i(tag, "disabled");
            }

            @Override
            public void disabling() {
                Dbug.i(tag, "disabling");
            }
        });

        bluetoothClient.registerScanListener(new OnScanListener() {
            @Override
            public void startScan() {
                Dbug.i(tag, "startScan");
            }

            @Override
            public void finishScan() {
                Dbug.i(tag, "finishScan");
            }

            @Override
            public void deviceFound(FoundDevice device) {
                if (device.getBluetoothDevice().getName() != null)
                    Dbug.i(tag, device.getBluetoothDevice().getName());
                else
                    Dbug.i(tag, device.toString());

                Dbug.i(tag, BluetoothUtil.getDeviceType(device.getBluetoothDevice().getBluetoothClass()));
                Dbug.i(tag, BluetoothUtil.getMajorDeviceType(device.getBluetoothDevice().getBluetoothClass()));
            }

        });

        bluetoothClient.registerConnectStateChangeListener(new OnConnectStateListener() {

            @Override
            public void connecting(BluetoothDevice bluetoothDevice) {
                Dbug.i(tag, "connecting " + bluetoothDevice.getName());
            }

            @Override
            public void connected(BluetoothDevice bluetoothDevice) {
                Dbug.i(tag, "connected " + bluetoothDevice.getName());
            }

            @Override
            public void disconnecting(BluetoothDevice bluetoothDevice) {
                Dbug.i(tag, "disconnecting " + bluetoothDevice.getName());
            }

            @Override
            public void disconnected(BluetoothDevice bluetoothDevice) {
                Dbug.i(tag, "disconnected " + bluetoothDevice.getName());
            }
        });

        bluetoothClient.registerBondStateChangeListener(new OnBondStateChangeListener() {
            @Override
            public void bonding(BluetoothDevice bluetoothDevice) {
                Dbug.i(tag, "bonding " + bluetoothDevice.getName());
            }

            @Override
            public void bonded(BluetoothDevice bluetoothDevice) {
                Dbug.i(tag, "bonded " + bluetoothDevice.getName());
                mBluetoothDevice = bluetoothDevice;
                BluetoothAdapter.getDefaultAdapter().getProfileProxy(getApplicationContext(), serviceListener, BluetoothProfile.A2DP);
            }

            @Override
            public void fail(BluetoothDevice bluetoothDevice) {
                Dbug.i(tag, "fail " + bluetoothDevice.getName());
            }
        });*/
    }

    private BluetoothA2dp bluetoothA2dp;

    private BluetoothProfile.ServiceListener serviceListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            switch (profile) {
                case BluetoothProfile.A2DP:
                    bluetoothA2dp = (BluetoothA2dp) proxy;
                    BluetoothUtil.connect(MainActivity.this, profile, bluetoothA2dp, mBluetoothDevice);
                    break;
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            Dbug.i(tag, "onServiceDisconnected profile=" + profile);
        }
    };


    public void open(View v) {
        BluetoothUtil.openBluetooth();
    }

    public void close(View v) {
        BluetoothUtil.closeBluetooth();
    }

    public void scan(View v) {
        BluetoothUtil.startScanl();
    }

    public void cancleScan(View v) {
        BluetoothUtil.cancleScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Dbug.i(tag, "===================onDestory====================");
        bluetoothClient.release();
        bluetoothClient = null;
        BluetoothUtil.disconnect(getApplicationContext(), BluetoothProfile.A2DP, bluetoothA2dp, mBluetoothDevice);
        BluetoothUtil.removePair(mBluetoothDevice);
    }


}
