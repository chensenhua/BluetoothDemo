package com.sen.bluetooth.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.widget.Toast;

import com.sen.bluetooth.BaseBluetoothManager;
import com.sen.bluetooth.Error;
import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnScanListener;
import com.sen.bluetooth.utils.Dbug;

/**
 * Created by 陈森华 on 2017/8/23.
 * 功能：用一句话描述
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothBleClient extends BaseBluetoothManager {
    private static BluetoothBleClient mInstance;
    private String tag = getClass().getSimpleName();
    private Context mContext;

    private BluetoothBleClient(Context context) {
        super(context);
        mContext = context;
        getBluetoothBroatcatReceiver().setOnScanListener(null);
    }

    public static BluetoothBleClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BluetoothBleClient(context);
        }
        return mInstance;
    }


    public void connect(BluetoothDevice device) {
        BluetoothGatt bluetoothGatt;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothGatt = device.connectGatt(mContext, false, bluetoothGattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            bluetoothGatt = device.connectGatt(mContext, false, bluetoothGattCallback);
        }
    }


    private void printSevices(BluetoothGattService bluetoothGattService) {
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
            Dbug.i(tag, bluetoothGattCharacteristic.toString());
            Dbug.i(tag, bluetoothGattCharacteristic.getDescriptors().toString());
        }
    }

    public void stopBleScan() {
        Dbug.i(tag,"stop ble scan");
        if (Build.VERSION.SDK_INT >= 21) {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }


    public void startBleScan() {
        stopBleScan();
        if (Build.VERSION.SDK_INT >= 21) {
            bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
        } else {
            bluetoothAdapter.startLeScan(leScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (!TextUtils.isEmpty(device.getName()) && device.getName().startsWith(getPrefix())) {
                for (OnScanListener onScanListener : onScanListenerList) {
                    FoundDevice foundDevice = new FoundDevice();
                    foundDevice.setBluetoothDevice(device);
                    foundDevice.setRssi(rssi);
                    Dbug.i(tag, foundDevice.toString());
                    onScanListener.deviceFound(foundDevice);
                }
            }
        }
    };
    private ScanCallback scanCallback;

    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !TextUtils.isEmpty(result.getDevice().getName()) && result.getDevice().getName().startsWith(getPrefix())) {
                        for (OnScanListener onScanListener : onScanListenerList) {
                            FoundDevice foundDevice = new FoundDevice();
                            foundDevice.setBluetoothDevice(result.getDevice());
                            foundDevice.setRssi(result.getRssi());
                            onScanListener.deviceFound(foundDevice);
                            Dbug.i(tag, foundDevice.toString());
                        }
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    Dbug.i(tag, "errorCode=" + errorCode);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
                    }
                    Toast.makeText(mContext, Error.getErrorDesc(Error.BLE_SCAN_FAILED), Toast.LENGTH_SHORT).show();
                }
            };
        }
    }

    private void printGattInfo(BluetoothGatt gatt) {
        for (BluetoothGattService bluetoothGattService : gatt.getServices()) {
            Dbug.i(tag, "=============bluetoothGattService uuid:" + bluetoothGattService.getUuid() + "===================");
            for (BluetoothGattService bl : bluetoothGattService.getIncludedServices()) {
                Dbug.i(tag, "---------------bl uetoothGattService uuid:" + bl.getUuid() + "-------------");
                for (BluetoothGattService b2 : bluetoothGattService.getIncludedServices()) {
                    Dbug.i(tag, "--------------- b2 uuid:" + b2.getUuid() + "-------------");
                    printSevices(b2);

                }
                printSevices(bl);
            }
            printSevices(bluetoothGattService);
        }
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Dbug.i(tag, "--------------------onConnectionStateChange----------------------");
            Dbug.i(tag, "=============onConnectionStateChange  services size:" + gatt.getServices().size() + "===================");
            Dbug.i(tag, "=============onConnectionStateChange  services state:" + newState + "===================");
            Dbug.i(tag, "=============onConnectionStateChange  devices->" + gatt.getDevice().getName() + "===================");
            gatt.discoverServices();
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            printGattInfo(gatt);
            Dbug.i(tag, "--------------------onServicesDiscovered----------------------");
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Dbug.i(tag, "--------------------onCharacteristicRead----------------------");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Dbug.i(tag, "--------------------onCharacteristicWrite----------------------");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Dbug.i(tag, "--------------------onCharacteristicChanged----------------------");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Dbug.i(tag, "--------------------onCharacteristicChanged----------------------");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Dbug.i(tag, "--------------------onDescriptorWrite----------------------");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Dbug.i(tag, "--------------------onReliableWriteCompleted----------------------");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Dbug.i(tag, "--------------------onReadRemoteRssi----------------------");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Dbug.i(tag, "--------------------onReadRemoteRssi----------------------");
        }
    };
}
