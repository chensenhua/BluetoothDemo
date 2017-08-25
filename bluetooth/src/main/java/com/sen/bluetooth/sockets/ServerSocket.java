package com.sen.bluetooth.sockets;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.sen.bluetooth.Error;
import com.sen.bluetooth.callbacks.SendResponse;
import com.sen.bluetooth.javabeans.DataPackage;
import com.sen.bluetooth.listeners.OnDataReceiverListener;
import com.sen.bluetooth.utils.Dbug;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈森华 on 2017/8/24.
 * 功能：用一句话描述
 */

public class ServerSocket extends Thread {
    private String tag = getClass().getSimpleName();
    private BluetoothServerSocket serverSocket;
    private boolean isActive;
    private List<ClientSocket> clientSockets;
    private List<OnDataReceiverListener> onDataReceiverListenerList;

    public boolean isActive() {
        return isActive;
    }

    public ServerSocket(BluetoothServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        clientSockets = new ArrayList<>();
        onDataReceiverListenerList = new ArrayList<>();
    }

    @Override
    public void run() {
        isActive = true;
        while (isActive) {
            try {
                BluetoothSocket socket = serverSocket.accept();
                Dbug.i(tag, "socket connecting->" + socket.toString());
                if (socket != null && socket.isConnected()) {
                    final ClientSocket clientSocket = new ClientSocket(socket);
                    clientSocket.registerDataReceiverListener(onDataReceiverListener);
                    clientSockets.add(clientSocket);
                    DataPackage dataPackage = new DataPackage();
                    dataPackage.setData(("hello " + socket.getRemoteDevice().getName()).getBytes());
                    dataPackage.setSendResponse(new SendResponse() {
                        @Override
                        public void onRespond(Error code) {

                        }
                    });
                    sendData(dataPackage, clientSocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
                isActive = false;
                break;
            }
        }
    }

    private OnDataReceiverListener onDataReceiverListener = new OnDataReceiverListener() {
        @Override
        public void onReceiver(String name, byte[] data) {
            for (OnDataReceiverListener onDataReceiverListener : onDataReceiverListenerList) {
                onDataReceiverListener.onReceiver(name, data);
            }
        }
    };

    public void registerDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        onDataReceiverListenerList.add(onDataReceiverListener);
    }

    public void unregisterDataReceiverListener(OnDataReceiverListener onDataReceiverListener) {
        onDataReceiverListenerList.remove(onDataReceiverListener);
    }

    public void sendData(DataPackage dataPackage, ClientSocket clientSocket) {
        if (clientSockets.contains(clientSocket)) {
            clientSocket.sendData(dataPackage);
        } else {
            dataPackage.getSendResponse().onRespond(Error.DATA_SEND_FAIL);
        }
    }

    public ClientSocket getClientByName(String name) {
        for (ClientSocket clientSocket : clientSockets) {
            if (clientSocket.getBluetoothSocket().getRemoteDevice().getName().equals(name))
                return clientSocket;
        }
        return null;
    }
}
