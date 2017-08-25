package com.sen.bluetooth.listeners;

import android.bluetooth.BluetoothDevice;

/**
 * Created by 陈森华 on 2017/8/22.
 * 功能：用一句话描述
 */

public interface OnConnectStateListener {
    void connecting(BluetoothDevice bluetoothDevice);
    void connected(BluetoothDevice bluetoothDevice);
    void disconnecting(BluetoothDevice bluetoothDevice);
    void disconnected(BluetoothDevice bluetoothDevice);
}
