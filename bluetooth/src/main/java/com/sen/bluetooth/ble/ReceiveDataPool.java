package com.sen.bluetooth.ble;

import android.bluetooth.BluetoothDevice;

/**
 * Created by 陈森华 on 2017/9/12.
 * 功能：用一句话描述
 */

public  interface ReceiveDataPool {
    boolean isVerify();
    boolean isReceiveCompletion();
    byte [] getValue();
    void push(byte [] data);
}
