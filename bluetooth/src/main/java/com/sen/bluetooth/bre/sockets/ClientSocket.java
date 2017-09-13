package com.sen.bluetooth.bre.sockets;

import android.bluetooth.BluetoothSocket;

import com.sen.bluetooth.bre.DataPackage;
import com.sen.bluetooth.listeners.OnDataReceiverListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈森华 on 2017/8/24.
 * 功能：用一句话描述
 */

public class ClientSocket {

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private ReceiveDataThread receiveDataThread;
    private SendDataThread sendDataThread;
    private List<OnDataReceiverListener> onDataReceiverListenerList;
    private OnDataReceiverListener onDataReceiverListener;

    public void setOnDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        this.onDataReceiverListener = onDataReceiverListener;
    }

    public ClientSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
        init();
    }

    public void init() {
        try {
            onDataReceiverListenerList = new ArrayList<>();
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
            sendDataThread = new SendDataThread(outputStream);
            receiveDataThread = new ReceiveDataThread(inputStream);
            receiveDataThread.setBluetoothDevice(bluetoothSocket.getRemoteDevice());
            receiveDataThread.setOnDataReceiverListener(onDataReceiverListener);
            sendDataThread.start();
            receiveDataThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void release() {
        if (sendDataThread != null) {
            sendDataThread.interrupt();
            sendDataThread = null;
        }
        if (receiveDataThread != null) {
            receiveDataThread.interrupt();
            receiveDataThread = null;
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = null;
        }
    }

    public void sendData(DataPackage dataPackage) {
        sendDataThread.sendData(dataPackage);
    }
}
