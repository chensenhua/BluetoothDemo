package com.sen.bluetooth.listeners;

import android.bluetooth.BluetoothDevice;

import com.sen.bluetooth.Error;

/**
 * Created by 陈森华 on 2017/8/22.
 * 功能：用一句话描述
 */

public interface OnConnectStateListener {
    void connecting(BluetoothDevice bluetoothDevice, Error error);
    void connected(BluetoothDevice bluetoothDevice,Error error);
    void disconnecting(BluetoothDevice bluetoothDevice,Error error);
    void disconnected(BluetoothDevice bluetoothDevice,Error error);
}
