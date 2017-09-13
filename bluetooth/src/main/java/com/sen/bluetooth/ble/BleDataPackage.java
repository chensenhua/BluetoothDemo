package com.sen.bluetooth.ble;

import android.bluetooth.BluetoothDevice;

import com.sen.bluetooth.callbacks.SendResponse;

import java.util.UUID;

/**
 * Created by 陈森华 on 2017/9/12.
 * 功能：用一句话描述
 */

public class BleDataPackage {
    private byte[] data;
    private SendResponse sendResponse;
    private UUID serviceUuid;
    private UUID characteristicUuid;
    private BluetoothDevice bluetoothDevice;

    public BleDataPackage() {
    }

    public BleDataPackage(byte[] data, SendResponse sendResponse, UUID serviceUuid, UUID characteristicUuid, BluetoothDevice bluetoothDevice) {
        this.data = data;
        this.sendResponse = sendResponse;
        this.serviceUuid = serviceUuid;
        this.characteristicUuid = characteristicUuid;
        this.bluetoothDevice = bluetoothDevice;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public SendResponse getSendResponse() {
        return sendResponse;
    }

    public void setSendResponse(SendResponse sendResponse) {
        this.sendResponse = sendResponse;
    }

    public UUID getServiceUuid() {
        return serviceUuid;
    }

    public void setServiceUuid(UUID serviceUuid) {
        this.serviceUuid = serviceUuid;
    }

    public UUID getCharacteristicUuid() {
        return characteristicUuid;
    }

    public void setCharacteristicUuid(UUID characteristicUuid) {
        this.characteristicUuid = characteristicUuid;
    }
}
