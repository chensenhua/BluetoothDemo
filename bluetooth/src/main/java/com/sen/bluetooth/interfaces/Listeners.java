package com.sen.bluetooth.interfaces;

import com.sen.bluetooth.listeners.OnBondStateChangeListener;
import com.sen.bluetooth.listeners.OnConnectStateListener;
import com.sen.bluetooth.listeners.OnDataReceiverListener;
import com.sen.bluetooth.listeners.OnScanListener;
import com.sen.bluetooth.listeners.OnStateChangeListener;

/**
 * Created by 陈森华 on 2017/8/22.
 * 功能：用一句话描述
 */

public interface Listeners {
    void registerStateChangeListener(OnStateChangeListener onStateChangeListener);

    void unregisterStateChangeListener(OnStateChangeListener onStateChangeListener);

    void registerScanListener(OnScanListener onScanListener);

    void unregisterScanListener(OnScanListener onScanListener);

    void registerConnectStateChangeListener(OnConnectStateListener onConnectStateListener);

    void unregisterConnectStateChangeListener(OnConnectStateListener onConnectStateListener);

    void unregisterBondStateChangeListener(OnBondStateChangeListener onBondStateChangeListener);

    void registerBondStateChangeListener(OnBondStateChangeListener onBondStateChangeListener);

    void registerDataReceiverListener(OnDataReceiverListener onDataReceiverListener);
    void unregisterDataReceiverListener(OnDataReceiverListener onDataReceiverListener);
}
