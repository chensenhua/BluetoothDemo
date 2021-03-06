package com.sen.bluetooth.bre.sockets;

import android.bluetooth.BluetoothDevice;

import com.sen.bluetooth.listeners.OnDataReceiverListener;

import java.io.InputStream;

/**
 * Created by 陈森华 on 2017/8/24.
 * 功能：用一句话描述
 */

 class ReceiveDataThread extends Thread {
    private boolean isActive = false;
    private InputStream inputStream = null;
    private OnDataReceiverListener onDataReceiverListener;
    private BluetoothDevice mBluetoothDevice;

    public void setBluetoothDevice(BluetoothDevice mBluetoothDevice) {
        this.mBluetoothDevice = mBluetoothDevice;
    }

    public void setOnDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        this.onDataReceiverListener = onDataReceiverListener;
    }

    public ReceiveDataThread(InputStream inputStream) {
        this.inputStream = inputStream;
        setName("ReceiveDatatThread");
    }

    @Override
    public void run() {
        isActive = true;
        byte buffer[] = new byte[1024];
        int len;
        while (isActive) {
            try {
                len = inputStream.read(buffer);
                if (len <= 0) {
                    Thread.sleep(100);
                } else {
                    byte[] result = new byte[len];
                    System.arraycopy(buffer, 0, result, 0, len);
                    if (onDataReceiverListener != null)
                        onDataReceiverListener.onReceiver(mBluetoothDevice,result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isActive = false;
                break;
            }
        }
    }
}
