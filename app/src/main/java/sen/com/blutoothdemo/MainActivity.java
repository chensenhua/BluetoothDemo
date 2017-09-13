package sen.com.blutoothdemo;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.sen.bluetooth.bre.BluetoothClient;
import com.sen.bluetooth.Constants;
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
             bluetoothClient.createSocketClient(foundDevice.getBluetoothDevice(), Constants.MY_UUID);
            }
        });
        fragmentTransaction.replace(R.id.container,deviceListFragment, deviceListFragment.getClass().getSimpleName());
        fragmentTransaction.commit();
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
