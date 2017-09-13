package com.sen.bluetooth.ble;

import android.bluetooth.BluetoothDevice;

/**
 * Created by 陈森华 on 2017/9/13.
 * 功能：用一句话描述
 */

public interface OnServicesDiscoverListener {
    void onDiscoverSuccess(BluetoothDevice bluetoothDevice);
    void onDiscoverFailed(BluetoothDevice bluetoothDevice);

}
