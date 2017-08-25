package com.sen.bluetooth.ui.fragments;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sen.bluetooth.BaseBluetoothManager;
import com.sen.bluetooth.R;
import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnBondStateChangeListener;
import com.sen.bluetooth.listeners.OnConnectStateListener;
import com.sen.bluetooth.listeners.OnDevicesItemClickListener;
import com.sen.bluetooth.listeners.OnScanListener;
import com.sen.bluetooth.ui.adapters.DeviceListAdapter;
import com.sen.bluetooth.utils.BluetoothUtil;
import com.sen.bluetooth.utils.Dbug;

/**
 * Created by 陈森华 on 2017/8/25.
 * 功能：用一句话描述
 */

public class DeviceListFragment extends Fragment {
    private String tag = getClass().getSimpleName();
    private View view;
    private BaseBluetoothManager baseBluetoothManager;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DeviceListAdapter deviceListAdapter;
    private OnDevicesItemClickListener onDevicesItemClickListener;
    private ImageButton scanFreshIbtn;
    private OnDevicesItemClickListener mOnDevicesItemClickListener = new OnDevicesItemClickListener() {
        @Override
        public void onItemClick(FoundDevice foundDevice) {
            if (onDevicesItemClickListener != null) {
                onDevicesItemClickListener.onItemClick(foundDevice);
            }
        }
    };


    public void setBaseBluetoothManager(BaseBluetoothManager baseBluetoothManager) {
        this.baseBluetoothManager = baseBluetoothManager;
    }

    public void setmOnDevicesItemClickListener(OnDevicesItemClickListener onDevicesItemClickListener) {
        this.onDevicesItemClickListener = onDevicesItemClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_devices_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rc_devices_list);
        scanFreshIbtn = (ImageButton) view.findViewById(R.id.scan_fresh_ibnt);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        deviceListAdapter = new DeviceListAdapter(getContext());
        recyclerView.setAdapter(deviceListAdapter);
        baseBluetoothManager.registerScanListener(onScanListener);
        deviceListAdapter.setmOnDevicesItemClickListener(mOnDevicesItemClickListener);
        scanFreshIbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothUtil.openBluetooth();
                BluetoothUtil.startScanl();
            }
        });
        return view;
    }

    private OnScanListener onScanListener = new OnScanListener() {
        @Override
        public void startScan() {
            Dbug.i(tag, "startScan");
            deviceListAdapter.clear();
        }

        @Override
        public void finishScan() {
            Dbug.i(tag, "finishScan");
        }

        @Override
        public void deviceFound(FoundDevice device) {
            Dbug.i(tag, "deviceFound->" + device.getBluetoothDevice().getName());
            deviceListAdapter.add(device);

        }
    };

    private OnBondStateChangeListener onBondStateChangeListener = new OnBondStateChangeListener() {
        @Override
        public void bonding(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void bonded(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void fail(BluetoothDevice bluetoothDevice) {

        }
    };

    private OnConnectStateListener onConnectStateListener = new OnConnectStateListener() {
        @Override
        public void connecting(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void connected(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void disconnecting(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void disconnected(BluetoothDevice bluetoothDevice) {

        }
    };

}
