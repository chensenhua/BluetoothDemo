package com.sen.bluetooth.listeners;

import android.bluetooth.BluetoothDevice;

/**
 * Created by 陈森华 on 2017/8/23.
 * 功能：用一句话描述
 */

public interface OnBondStateChangeListener {
    void bonding(BluetoothDevice bluetoothDevice);
    void bonded(BluetoothDevice bluetoothDevice);
    void fail(BluetoothDevice bluetoothDevice);
}
