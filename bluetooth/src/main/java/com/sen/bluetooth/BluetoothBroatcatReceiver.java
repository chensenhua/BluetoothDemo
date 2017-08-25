package com.sen.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnBondStateChangeListener;
import com.sen.bluetooth.listeners.OnConnectStateListener;
import com.sen.bluetooth.listeners.OnScanListener;
import com.sen.bluetooth.listeners.OnStateChangeListener;
import com.sen.bluetooth.utils.Dbug;

/**
 * Created by 陈森华 on 2017/8/22.
 * 功能：用一句话描述
 */

public class BluetoothBroatcatReceiver extends BroadcastReceiver {
    private String tag = getClass().getSimpleName();

    private OnStateChangeListener onStateChangeListener;
    private OnScanListener onScanListener;
    private OnConnectStateListener onConnectStateListener;
    private OnBondStateChangeListener onBondStateChangeListener;


    public void setOnScanListener(OnScanListener onScanListener) {
        this.onScanListener = onScanListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setOnConnectStateListener(OnConnectStateListener onConnectStateListener) {
        this.onConnectStateListener = onConnectStateListener;
    }

    public void setOnBondStateChangeListener(OnBondStateChangeListener onBondStateChangeListener) {
        this.onBondStateChangeListener = onBondStateChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Dbug.i(tag, intent.getAction());
        String action = intent.getAction();
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            handleStateChangeAction(intent);
        } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) || action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED) || action.equals(BluetoothDevice.ACTION_FOUND)) {
            handleScanAction(intent);
        } else if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
            handleConnectAction(intent);
        } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            handleBondAction(intent);
        }
    }

    /**
     * 蓝牙关闭/打开动广播处理
     *
     * @param intent
     */
    private void handleStateChangeAction(Intent intent) {
        switch (intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE)) {
            case BluetoothAdapter.STATE_OFF:
                if (onStateChangeListener != null)
                    onStateChangeListener.disabled();
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                if (onStateChangeListener != null)
                    onStateChangeListener.disabling();
                break;
            case BluetoothAdapter.STATE_ON:
                if (onStateChangeListener != null)
                    onStateChangeListener.enabled();
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                if (onStateChangeListener != null)
                    onStateChangeListener.enabling();
                break;
        }
    }

    /**
     * 蓝牙扫描回调
     *
     * @param intent
     */
    private void handleScanAction(Intent intent) {
        if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
            if (onScanListener != null) {
                onScanListener.finishScan();
            }
        } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
            if (onScanListener != null) {
                onScanListener.startScan();
            }
        } else if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            BluetoothClass bluetoothClass = intent.getParcelableExtra(BluetoothDevice.EXTRA_CLASS);
            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
            FoundDevice foundDevice = new FoundDevice();
            foundDevice.setBluetoothDevice(bluetoothDevice);
            foundDevice.setRssi(rssi);
            if (onScanListener != null) {
                onScanListener.deviceFound(foundDevice);
            }
        }
    }

    /**
     * 蓝牙配对回调
     *
     * @param intent
     */
    private void handleConnectAction(Intent intent) {
        int state = intent.getExtras().getInt(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED);
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (bluetoothDevice != null) {
            Dbug.i(tag, bluetoothDevice.getName());
        }
        switch (state) {
            case BluetoothAdapter.STATE_CONNECTING:
                if (onConnectStateListener != null) {
                    onConnectStateListener.connecting(bluetoothDevice);
                }
                break;
            case BluetoothAdapter.STATE_CONNECTED:
                if (onConnectStateListener != null) {
                    onConnectStateListener.connected(bluetoothDevice);
                }
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                if (onConnectStateListener != null) {
                    onConnectStateListener.disconnecting(bluetoothDevice);
                }
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                if (onConnectStateListener != null) {
                    onConnectStateListener.disconnected(bluetoothDevice);
                }
                break;
        }
    }


    private void handleBondAction(Intent intent) {
        int state = intent.getExtras().getInt(BluetoothDevice.EXTRA_BOND_STATE);
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        switch (state) {
            case BluetoothDevice.BOND_BONDED:
                if (onBondStateChangeListener != null) {
                    onBondStateChangeListener.bonded(bluetoothDevice);
                }
                break;
            case BluetoothDevice.BOND_BONDING:
                if (onBondStateChangeListener != null) {
                    onBondStateChangeListener.bonding(bluetoothDevice);
                }
                break;
            case BluetoothDevice.BOND_NONE:
                if (onBondStateChangeListener != null) {
                    onBondStateChangeListener.fail(bluetoothDevice);
                }
                break;
        }
    }

}
