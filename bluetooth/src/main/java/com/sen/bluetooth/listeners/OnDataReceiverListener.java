package com.sen.bluetooth.listeners;

import android.bluetooth.BluetoothDevice;

/**
 * Created by 陈森华 on 2017/8/24.
 * 功能：用一句话描述
 */

public interface OnDataReceiverListener {
    void onReceiver(BluetoothDevice bluetoothDevice, byte[] data);
}
