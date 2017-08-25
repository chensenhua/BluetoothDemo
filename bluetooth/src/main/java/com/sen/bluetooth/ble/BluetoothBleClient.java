package com.sen.bluetooth.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.sen.bluetooth.BaseBluetoothManager;
import com.sen.bluetooth.Error;
import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnScanListener;

/**
 * Created by 陈森华 on 2017/8/23.
 * 功能：用一句话描述
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothBleClient extends BaseBluetoothManager {
    private String tag = getClass().getSimpleName();
    private Context mContext;

    public BluetoothBleClient(Context context) {
        super(context);
        mContext = context;
    }

    public void stopBleScan() {
        if (Build.VERSION.SDK_INT >= 21) {
            bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    public void startBleScan() {
        getBluetoothBroatcatReceiver().setOnScanListener(null);
        if (Build.VERSION.SDK_INT >= 21) {
            bluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
        } else {
            bluetoothAdapter.startLeScan(leScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            for (OnScanListener onScanListener : onScanListenerList) {
                FoundDevice foundDevice = new FoundDevice();
                foundDevice.setBluetoothDevice(device);
                foundDevice.setRssi(rssi);
                onScanListener.deviceFound(foundDevice);
            }

        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            for (OnScanListener onScanListener : onScanListenerList) {
                FoundDevice foundDevice = new FoundDevice();
                foundDevice.setBluetoothDevice(result.getDevice());
                foundDevice.setRssi(result.getRssi());
                onScanListener.deviceFound(foundDevice);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
            Toast.makeText(mContext, Error.getErrorDesc(Error.BLE_SCAN_FAILED), Toast.LENGTH_SHORT).show();
        }
    };

}
