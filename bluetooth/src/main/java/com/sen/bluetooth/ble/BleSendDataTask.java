package com.sen.bluetooth.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.sen.bluetooth.Error;
import com.sen.bluetooth.callbacks.SendResponse;
import com.sen.bluetooth.utils.Dbug;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 陈森华 on 2017/9/12.
 * 功能：用一句话描述
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleSendDataTask extends HandlerThread implements Handler.Callback {

    private String tag = getClass().getSimpleName();
    private Queue<BleDataPackage> dataPackageQueue = new LinkedBlockingQueue<>();
    private Handler handler;
    private Handler uiHandle;
    private static final int SEND_DATA = 0X01;
    private BluetoothBleClient bluetoothBleClient;
    private static final int MAX_LENGTH = 20;


    public BleSendDataTask(String name, BluetoothBleClient bluetoothBleClient) {
        super(name);
        this.bluetoothBleClient = bluetoothBleClient;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new Handler(getLooper(), this);
        uiHandle = new Handler(Looper.getMainLooper());
    }

    @Override
    public boolean handleMessage(Message msg) {
        Dbug.e(tag, "handleMessage-->" + Thread.currentThread().getName());
        switch (msg.what) {
            case SEND_DATA:
                final BleDataPackage bleDataPackage = dataPackageQueue.poll();
                if (bleDataPackage == null) {
                    break;
                }
                for (int i = 0; i < 3; i++) {
                    boolean result = sendData(bleDataPackage);
                    if (result) {
                        uiHandle.post(new Runnable() {
                            @Override
                            public void run() {
                                bleDataPackage.getSendResponse().onRespond(Error.DATA_SEND_SUCCESS);
                            }
                        });
                        return true;
                    }
                }
                uiHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        bleDataPackage.getSendResponse().onRespond(Error.DATA_SEND_FAILED);
                    }
                });
                handler.sendEmptyMessage(SEND_DATA);
                break;
        }
        return false;
    }

    public void sendData(BluetoothDevice bluetoothDevice, UUID serviceUuid, UUID writeUuid, byte[] data, SendResponse sendResponse) {
        BleDataPackage dataPackage = new BleDataPackage(data, sendResponse, serviceUuid, writeUuid, bluetoothDevice);
        dataPackageQueue.add(dataPackage);
        handler.sendEmptyMessage(SEND_DATA);
    }

    public void clearCache() {
        dataPackageQueue.clear();
    }

    private boolean sendData(BleDataPackage bleDataPackage) {
        BluetoothGatt gatt = bluetoothBleClient.getBluetoothGatt(bleDataPackage.getBluetoothDevice());
        BluetoothGattService bluetoothGattService = gatt.getService(bleDataPackage.getServiceUuid());
        BluetoothGattCharacteristic write = bluetoothGattService.getCharacteristic(bleDataPackage.getCharacteristicUuid());
        byte[] value = bleDataPackage.getData();
        boolean result = false;
        if (value.length > 20) {
            List<byte[]> values = new ArrayList<>();
            int count = value.length / MAX_LENGTH;
            for (int i = 0; i < count; i++) {
                byte[] temp = new byte[20];
                System.arraycopy(value, MAX_LENGTH * i, temp, 0, MAX_LENGTH);
                values.add(temp);
            }

            int len = value.length % 20;
            byte[] temp = new byte[len];
            System.arraycopy(value, MAX_LENGTH * count, temp, 0, len);
            values.add(temp);
            for (byte[] data : values) {
                write.setValue(data);
                result = gatt.writeCharacteristic(write);
                if (!result) {
                    return false;
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        } else {
            write.setValue(value);
            result = gatt.writeCharacteristic(write);
        }
        return result;
    }

    private void release() {
        handler.removeCallbacksAndMessages(null);
        uiHandle.removeCallbacksAndMessages(null);
        bluetoothBleClient = null;
        dataPackageQueue.clear();
        dataPackageQueue = null;
    }

    @Override
    public boolean quit() {
        release();
        return super.quit();
    }

    @Override
    public boolean quitSafely() {
        release();
        return super.quitSafely();
    }
}
