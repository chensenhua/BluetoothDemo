package com.sen.bluetooth.bre;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;

import com.sen.bluetooth.BaseBluetoothManager;
import com.sen.bluetooth.bre.sockets.ClientSocket;
import com.sen.bluetooth.bre.sockets.ServerSocket;
import com.sen.bluetooth.callbacks.SendResponse;
import com.sen.bluetooth.listeners.OnDataReceiverListener;

import com.sen.bluetooth.utils.Dbug;

import java.io.IOException;

import java.util.UUID;

/**
 * Created by 陈森华 on 2017/8/25.
 * 功能：用一句话描述
 */

public class BluetoothServer extends BaseBluetoothManager {
    private String tag = getClass().getSimpleName();
    private ServerSocket mServerSocket;

    public BluetoothServer(Context context) {
        super(context);
    }
    private OnDataReceiverListener onDataReceiverListener=new OnDataReceiverListener() {
        @Override
        public synchronized void onReceiver(BluetoothDevice bluetoothDevice, byte[] data) {
         handleDataReceive(bluetoothDevice,data);
        }
    };

    public boolean createSocketServer(String name, String uuid) {
        try {
            BluetoothServerSocket bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(name, UUID.fromString(uuid));
            if (bluetoothServerSocket != null) {
                mServerSocket = new ServerSocket(bluetoothServerSocket);
                mServerSocket.setOnDataReceiverListener(onDataReceiverListener);
                mServerSocket.start();
                Dbug.i(tag, "bluetoothServerSocket create success");
                return true;
            }else
            {
                throw new IOException("create server fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendData(byte[] data, SendResponse sendResponse, ClientSocket clientSocket) {
        DataPackage dataPackage = new DataPackage();
        dataPackage.setData(data);
        dataPackage.setSendResponse(sendResponse);
        mServerSocket.sendData(dataPackage, clientSocket);
    }

    public ClientSocket getClientByName(String name) {
        return mServerSocket.getClientByName(name);
    }
}
