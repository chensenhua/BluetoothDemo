package com.sen.bluetooth.listeners;

import android.bluetooth.BluetoothDevice;

import com.sen.bluetooth.javabeans.FoundDevice;

/**
 * Created by 陈森华 on 2017/8/22.
 * 功能：用一句话描述
 */

public interface OnScanListener {
  /*  void startScan();
    void finishScan();*/
    void deviceFound(FoundDevice device);
}
