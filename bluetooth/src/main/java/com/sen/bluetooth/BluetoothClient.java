package com.sen.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.sen.bluetooth.BaseBluetoothManager;
import com.sen.bluetooth.callbacks.SendResponse;
import com.sen.bluetooth.javabeans.DataPackage;
import com.sen.bluetooth.listeners.OnDataReceiverListener;
import com.sen.bluetooth.sockets.ClientSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 陈森华 on 2017/8/25.
 * 功能：用一句话描述
 */

public class BluetoothClient extends BaseBluetoothManager {
    private static BluetoothClient mInstance;
    private String tag = getClass().getSimpleName();
    private ClientSocket mClientSocket;

    private BluetoothClient(Context context) {
        super(context);
    }

    public static BluetoothClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BluetoothClient(context);
        }
        return mInstance;
    }

    @Override
    public void registerDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        mClientSocket.registerDataReceiverListener(onDataReceiverListener);
    }

    @Override
    public void unregisterDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        mClientSocket.unregisterDataReceiverListener(onDataReceiverListener);
    }


    public boolean createSocketClient(BluetoothDevice bluetoothDevice, String uuid) {
        mBluetoothDevice = bluetoothDevice;
        try {
            BluetoothSocket bluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(uuid));
            if (bluetoothSocket != null) {
                bluetoothSocket.connect();
                mClientSocket = new ClientSocket(bluetoothSocket);
                return true;
            } else {
                throw new IOException("bluetoothSocket is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendData(byte[] data, SendResponse sendResponse) {
        DataPackage dataPackage = new DataPackage();
        dataPackage.setData(data);
        dataPackage.setSendResponse(sendResponse);
        mClientSocket.sendData(dataPackage);
    }
}
