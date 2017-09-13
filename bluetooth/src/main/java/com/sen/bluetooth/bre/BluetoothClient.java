package com.sen.bluetooth.bre;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.sen.bluetooth.BaseBluetoothManager;
import com.sen.bluetooth.bre.sockets.ClientSocket;
import com.sen.bluetooth.callbacks.SendResponse;
import com.sen.bluetooth.listeners.OnDataReceiverListener;

import com.sen.bluetooth.utils.BluetoothUtil;

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
    private BluetoothDevice mBluetoothDevice;
    private OnDataReceiverListener onDataReceiverListener = new OnDataReceiverListener() {
        @Override
        public void onReceiver(BluetoothDevice bluetoothDevice, byte[] data) {
            handleDataReceive(bluetoothDevice, data);
        }

    };


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
    public void stopScan() {
        super.stopScan();
        BluetoothUtil.stopBreScan();
    }

    @Override
    public void startScan() {
        super.startScan();
        BluetoothUtil.startBreScanl();
    }


    public void sendData(byte[] data, SendResponse sendResponse, int repeat) {
        DataPackage dataPackage = new DataPackage();
        dataPackage.setData(data);
        dataPackage.setSendResponse(sendResponse);
        mClientSocket.sendData(dataPackage);
    }


    public boolean createSocketClient(BluetoothDevice bluetoothDevice, String uuid) {
        mBluetoothDevice = bluetoothDevice;
        try {
            BluetoothSocket bluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(uuid));
            if (bluetoothSocket != null) {
                bluetoothSocket.connect();
                mClientSocket = new ClientSocket(bluetoothSocket);
                mClientSocket.setOnDataReceiverListener(onDataReceiverListener);
                return true;
            } else {
                throw new IOException("bluetoothSocket is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
