package com.sen.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnBondStateChangeListener;
import com.sen.bluetooth.listeners.OnConnectStateListener;
import com.sen.bluetooth.listeners.OnDataReceiverListener;
import com.sen.bluetooth.listeners.OnScanListener;
import com.sen.bluetooth.listeners.OnStateChangeListener;
import com.sen.bluetooth.interfaces.Listeners;
import com.sen.bluetooth.sockets.ClientSocket;
import com.sen.bluetooth.sockets.ServerSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 陈森华 on 2017/8/22.
 * 功能：用一句话描述
 */

public class BaseBluetoothManager implements Listeners {
    private BluetoothBroatcatReceiver bluetoothBroatcatReceiver;
    private Context mContext;
    private List<OnStateChangeListener> onStateChangeListenerList;
    protected List<OnScanListener> onScanListenerList;
    private List<OnConnectStateListener> connectStateListenerList;
    private List<OnBondStateChangeListener> bondStateChangeListenerList;
    protected BluetoothAdapter bluetoothAdapter;
    protected BluetoothDevice mBluetoothDevice;
    private String prefix = "";

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public BluetoothBroatcatReceiver getBluetoothBroatcatReceiver() {
        return bluetoothBroatcatReceiver;
    }

    public BaseBluetoothManager(Context context) {
        this.mContext = context;
        init();
        registerBroatcast();
    }

    private void init() {
        bluetoothBroatcatReceiver = new BluetoothBroatcatReceiver();
        onStateChangeListenerList = new ArrayList<>();
        onScanListenerList = new ArrayList<>();
        connectStateListenerList = new ArrayList<>();
        bondStateChangeListenerList = new ArrayList<>();
        bluetoothBroatcatReceiver.setOnStateChangeListener(onStateChangeListener);
        bluetoothBroatcatReceiver.setOnScanListener(onScanListener);
        bluetoothBroatcatReceiver.setOnConnectStateListener(onConnectStateListener);
        bluetoothBroatcatReceiver.setOnBondStateChangeListener(onBondStateChangeListener);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    private void registerBroatcast() {
        IntentFilter intentFilter = new IntentFilter();
        //BluetoothA2dp连接广播
        intentFilter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        //BluetoothA2dp播放状态改变的广播
        intentFilter.addAction(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED);
        //BluetoothAdapter
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        intentFilter.addAction(BluetoothAdapter.EXTRA_CONNECTION_STATE);

        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_CLASS_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        intentFilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);

        intentFilter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT);

        intentFilter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
        intentFilter.addAction(BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT);
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);


        mContext.registerReceiver(bluetoothBroatcatReceiver, intentFilter);
    }

    private void unregisterBroatcast() {
        mContext.unregisterReceiver(bluetoothBroatcatReceiver);
    }

    public void release() {
        unregisterBroatcast();
        mContext = null;
        bluetoothBroatcatReceiver = null;
    }


    @Override
    public void registerStateChangeListener(OnStateChangeListener onStateChangeListener) {
        onStateChangeListenerList.add(onStateChangeListener);
    }

    @Override
    public void unregisterStateChangeListener(OnStateChangeListener onStateChangeListener) {
        onStateChangeListenerList.remove(onStateChangeListener);
    }

    @Override
    public void registerScanListener(OnScanListener onScanListener) {
        onScanListenerList.add(onScanListener);
    }

    @Override
    public void unregisterScanListener(OnScanListener onScanListener) {
        onScanListener.finishScan();
    }

    @Override
    public void registerConnectStateChangeListener(OnConnectStateListener onConnectStateListener) {
        connectStateListenerList.add(onConnectStateListener);
    }

    @Override
    public void unregisterConnectStateChangeListener(OnConnectStateListener onConnectStateListener) {
        connectStateListenerList.remove(onConnectStateListener);
    }

    @Override
    public void unregisterBondStateChangeListener(OnBondStateChangeListener onBondStateChangeListener) {
        bondStateChangeListenerList.remove(onBondStateChangeListener);
    }

    @Override
    public void registerBondStateChangeListener(OnBondStateChangeListener onBondStateChangeListener) {
        bondStateChangeListenerList.add(onBondStateChangeListener);
    }

    @Override
    public void registerDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
    }

    @Override
    public void unregisterDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
    }


    private OnStateChangeListener onStateChangeListener = new OnStateChangeListener() {
        @Override
        public void enabled() {
            for (OnStateChangeListener onStateChangeListener : onStateChangeListenerList) {
                onStateChangeListener.enabled();
            }
        }

        @Override
        public void enabling() {
            for (OnStateChangeListener onStateChangeListener : onStateChangeListenerList) {
                onStateChangeListener.enabling();
            }
        }

        @Override
        public void disabled() {
            for (OnStateChangeListener onStateChangeListener : onStateChangeListenerList) {
                onStateChangeListener.disabled();
            }
        }

        @Override
        public void disabling() {
            for (OnStateChangeListener onStateChangeListener : onStateChangeListenerList) {
                onStateChangeListener.disabling();
            }
        }
    };

    private OnScanListener onScanListener = new OnScanListener() {
        @Override
        public void startScan() {
            for (OnScanListener onScanListener : onScanListenerList) {
                onScanListener.startScan();
            }
        }

        @Override
        public void finishScan() {
            for (OnScanListener onScanListener : onScanListenerList) {
                onScanListener.finishScan();
            }
        }

        @Override
        public void deviceFound(FoundDevice device) {
            if (!TextUtils.isEmpty(device.getBluetoothDevice().getName()) && device.getBluetoothDevice().getName().startsWith(getPrefix())) {
                for (OnScanListener onScanListener : onScanListenerList) {
                    onScanListener.deviceFound(device);
                }
            }
        }
    };
    private OnConnectStateListener onConnectStateListener = new OnConnectStateListener() {
        @Override
        public void connecting(BluetoothDevice bluetoothDevice) {
            for (OnConnectStateListener onConnectStateListener : connectStateListenerList) {
                onConnectStateListener.connecting(bluetoothDevice);
            }
        }

        @Override
        public void connected(BluetoothDevice bluetoothDevice) {
            for (OnConnectStateListener onConnectStateListener : connectStateListenerList) {
                onConnectStateListener.connected(bluetoothDevice);
            }
        }

        @Override
        public void disconnecting(BluetoothDevice bluetoothDevice) {
            for (OnConnectStateListener onConnectStateListener : connectStateListenerList) {
                onConnectStateListener.disconnecting(bluetoothDevice);
            }
        }

        @Override
        public void disconnected(BluetoothDevice bluetoothDevice) {
            for (OnConnectStateListener onConnectStateListener : connectStateListenerList) {
                onConnectStateListener.disconnected(bluetoothDevice);
            }
        }
    };

    private OnBondStateChangeListener onBondStateChangeListener = new OnBondStateChangeListener() {
        @Override
        public void bonding(BluetoothDevice bluetoothDevice) {
            for (OnBondStateChangeListener onBondStateChangeListener : bondStateChangeListenerList) {
                onBondStateChangeListener.bonding(bluetoothDevice);
            }
        }

        @Override
        public void bonded(BluetoothDevice bluetoothDevice) {
            for (OnBondStateChangeListener onBondStateChangeListener : bondStateChangeListenerList) {
                onBondStateChangeListener.bonded(bluetoothDevice);
            }
        }

        @Override
        public void fail(BluetoothDevice bluetoothDevice) {
            for (OnBondStateChangeListener onBondStateChangeListener : bondStateChangeListenerList) {
                onBondStateChangeListener.fail(bluetoothDevice);
            }
        }
    };
}
