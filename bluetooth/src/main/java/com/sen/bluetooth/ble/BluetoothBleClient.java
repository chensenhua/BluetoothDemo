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
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.widget.Toast;

import com.sen.bluetooth.BaseBluetoothManager;
import com.sen.bluetooth.Constants;
import com.sen.bluetooth.Error;
import com.sen.bluetooth.callbacks.SendResponse;
import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnStateChangeListener;
import com.sen.bluetooth.utils.BluetoothUtil;
import com.sen.bluetooth.utils.CHexConver;
import com.sen.bluetooth.utils.Dbug;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 陈森华 on 2017/8/23.
 * 功能：用一句话描述
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothBleClient extends BaseBluetoothManager {
    private static BluetoothBleClient mInstance;
    private String tag = getClass().getSimpleName();
    private Context mContext;
    private List<BluetoothGatt> bluetoothGattList = new ArrayList<>();
    private BleSendDataTask bleSendDataTask;
    private ReceiveDataPool dataPool;
    private List<OnServicesDiscoverListener> onServicesDiscoverListenerList;


    private BluetoothBleClient(Context context) {
        super(context);
        mContext = context;
        bleSendDataTask = new BleSendDataTask("bleSendDataTask", this);
        bleSendDataTask.start();
        onServicesDiscoverListenerList = new ArrayList<>();
    }

    public void registerServicesDiscoverListener(OnServicesDiscoverListener onServicesDiscoverListener) {
        onServicesDiscoverListenerList.add(onServicesDiscoverListener);
    }


    public void unregisterServicesDiscoverListener(OnServicesDiscoverListener onServicesDiscoverListener) {
        onServicesDiscoverListenerList.remove(onServicesDiscoverListener);
    }


    public static BluetoothBleClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BluetoothBleClient(context);
        }
        return mInstance;
    }


    public void connect(BluetoothDevice device) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            device.connectGatt(mContext, false, bluetoothGattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            device.connectGatt(mContext, false, bluetoothGattCallback);
        }
    }


/*
    private void printSevices(BluetoothGattService bluetoothGattService) {
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
            Dbug.i(tag, "BluetoothGattCharacteristic-->" + bluetoothGattCharacteristic.getUuid().toString());

        }
    }

    private void printGatt(BluetoothGatt bluetoothGatt) {
        for (BluetoothGattService bluetoothGattService : bluetoothGatt.getServices()) {
            printSevices(bluetoothGattService);
            Dbug.i(tag, "server-->" + bluetoothGattService.getUuid().toString());
        }
    }
*/

    public void startBreBleScan() {
        startScan();
        BluetoothUtil.startBreScanl();
    }

    public void stopBreBleScan() {
        stopScan();
        BluetoothUtil.stopBreScan();
    }


    @Override
    public void stopScan() {
        Dbug.i(tag, "stop ble scan");
        if (Build.VERSION.SDK_INT >= 21) {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    @Override
    public void startScan() {
        stopScan();
        if (Build.VERSION.SDK_INT >= 21) {
            bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
        } else {
            bluetoothAdapter.startLeScan(leScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        ;

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            FoundDevice foundDevice = new FoundDevice();
            foundDevice.setBluetoothDevice(device);
            foundDevice.setRssi(rssi);
            handleDeviceFound(foundDevice);
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
                        FoundDevice foundDevice = new FoundDevice();
                        foundDevice.setBluetoothDevice(result.getDevice());
                        foundDevice.setRssi(result.getRssi());
                        handleDeviceFound(foundDevice);
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


    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Dbug.e(tag, "--------------------onConnectionStateChange----------------status------" + status);
            if (status != BluetoothGatt.GATT_SUCCESS) {
                bluetoothGattList.remove(gatt);
                gatt.close();
                gatt.disconnect();
                handleConneted(gatt.getDevice(), Error.CONNECTED_FAILED);
                return;
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothGattList.add(gatt);
                gatt.discoverServices();
                handleConneted(gatt.getDevice(), Error.CONNECTED_OK);
            } else if (newState == BluetoothProfile.STATE_CONNECTING) {
                handleConneting(gatt.getDevice(), Error.CONNECTED_OK);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                handleDisconneting(gatt.getDevice(), Error.CONNECTED_OK);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                handleDisconneted(gatt.getDevice(), Error.CONNECTED_OK);
            }

        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status != BluetoothGatt.GATT_SUCCESS) {
                for (OnServicesDiscoverListener onServicesDiscoverListener : onServicesDiscoverListenerList) {
                    onServicesDiscoverListener.onDiscoverFailed(gatt.getDevice());
                    bluetoothGattList.remove(gatt);
                    gatt.close();
                    gatt.disconnect();
                }
            } else {
                for (OnServicesDiscoverListener onServicesDiscoverListener : onServicesDiscoverListenerList) {
                    onServicesDiscoverListener.onDiscoverSuccess(gatt.getDevice());
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            byte[] value = characteristic.getValue();
            Dbug.e(tag, "onCharacteristicRead  " + CHexConver.byte2HexStr(value, value.length));

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            byte[] value = characteristic.getValue();
            Dbug.e(tag, "onCharacteristicWrite  " + CHexConver.byte2HexStr(value, value.length));
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] value = characteristic.getValue();
            Dbug.e(tag, "onCharacteristicChanged  " + CHexConver.byte2HexStr(value, value.length));
       /*     dataPool.push(value);
            if (dataPool.isReceiveCompletion()) {
            }*/
            handleDataReceive(gatt.getDevice(), value);
        }


        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Dbug.i(tag, "--------------------onDescriptorRead----------------------");
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

    BluetoothGatt getBluetoothGatt(BluetoothDevice bluetoothDevice) {
        for (BluetoothGatt bluetoothGatt : bluetoothGattList) {
            if (bluetoothGatt.getDevice().getAddress().equals(bluetoothDevice.getAddress()))
                return bluetoothGatt;
        }
        return null;
    }


    public void sendData(BluetoothDevice bluetoothDevice, UUID serviceUuid, UUID writeUuid, byte[] data, SendResponse sendResponse) {
        bleSendDataTask.sendData(bluetoothDevice, serviceUuid, writeUuid, data, sendResponse);
    }

    public boolean enableNotify(BluetoothDevice bluetoothDevice, UUID serviceUuid, UUID notifyUuid) {
        BluetoothGatt gatt = getBluetoothGatt(bluetoothDevice);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService bluetoothGattService = gatt.getService(serviceUuid);
        if (bluetoothGattService == null) {
            return false;
        }
        BluetoothGattCharacteristic notify = bluetoothGattService.getCharacteristic(notifyUuid);
        if (notify == null) {
            return false;
        }
        boolean result;
        result = gatt.setCharacteristicNotification(notify, true);
        if (!result) {
            return false;
        }
        for (BluetoothGattDescriptor descriptor : notify.getDescriptors()) {
            result = descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            if (!result) {
                continue;
            }
            result = gatt.writeDescriptor(descriptor);
            if (!result) {
                continue;
            }
        }
        return true;
    }

    public boolean disableNotify(BluetoothDevice bluetoothDevice, UUID serviceUuid, UUID notifyUuid) {
        BluetoothGatt gatt = getBluetoothGatt(bluetoothDevice);
        if (gatt == null) {
            return false;
        }
        BluetoothGattService bluetoothGattService = gatt.getService(serviceUuid);
        if (bluetoothGattService == null) {
            return false;
        }
        BluetoothGattCharacteristic notify = bluetoothGattService.getCharacteristic(notifyUuid);
        if (notify == null) {
            return false;
        }
        boolean result;
        result = gatt.setCharacteristicNotification(notify, false);
        if (!result) {
            return false;
        }
        for (BluetoothGattDescriptor descriptor : notify.getDescriptors()) {
            result = descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            if (!result) {
                continue;
            }
            result = gatt.writeDescriptor(descriptor);
            if (!result) {
                continue;
            }
        }
        return true;
    }


}
