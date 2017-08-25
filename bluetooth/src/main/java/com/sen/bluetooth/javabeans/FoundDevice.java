package com.sen.bluetooth.javabeans;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

/**
 * Created by 陈森华 on 2017/8/22.
 * 功能：用一句话描述
 */

public class FoundDevice {
    private String tag = getClass().getSimpleName();
    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }


    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "FoundDevice{" +
                "bluetoothDevice=" + bluetoothDevice.getName() +
                ", rssi='" + rssi + '\'' +
                '}';
    }
}
