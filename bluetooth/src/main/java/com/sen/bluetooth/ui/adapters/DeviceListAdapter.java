package com.sen.bluetooth.ui.adapters;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sen.bluetooth.R;
import com.sen.bluetooth.javabeans.FoundDevice;
import com.sen.bluetooth.listeners.OnDevicesItemClickListener;
import com.sen.bluetooth.utils.BluetoothUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈森华 on 2017/8/25.
 * 功能：用一句话描述
 */

public class DeviceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String tag = getClass().getSimpleName();
    private List<FoundDevice> bluetoothDevices;
    private Context context;
    private OnDevicesItemClickListener mOnDevicesItemClickListener;

    public void setmOnDevicesItemClickListener(OnDevicesItemClickListener mOnDevicesItemClickListener) {
        this.mOnDevicesItemClickListener = mOnDevicesItemClickListener;
    }

    public boolean add(FoundDevice foundDevice) {
        for (FoundDevice f : bluetoothDevices) {
            if (f.getBluetoothDevice().getAddress().equals(foundDevice.getBluetoothDevice().getAddress())) {
                return false;
            }
        }
        bluetoothDevices.add(bluetoothDevices.size(), foundDevice);
        notifyItemInserted(bluetoothDevices.size());
        return true;
    }

    public void clear() {
        bluetoothDevices.clear();
        notifyDataSetChanged();
    }

    public DeviceListAdapter(Context context) {
        this.bluetoothDevices = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DeviceViewHolder viewholder = (DeviceViewHolder) holder;
        BluetoothDevice bluetoothDevice = bluetoothDevices.get(position).getBluetoothDevice();
        String name = bluetoothDevice.getName();
        if (!TextUtils.isEmpty(name)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                viewholder.nameTv.setText(name + "        " + BluetoothUtil.getType(bluetoothDevice));
            } else {
                viewholder.nameTv.setText(name);
            }
        }
        viewholder.stateTv.setText(getBondState(bluetoothDevice.getBondState()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDevicesItemClickListener != null) {
                    mOnDevicesItemClickListener.onItemClick(bluetoothDevices.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {

        ImageView typeImg;
        TextView nameTv;
        TextView stateTv;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            typeImg = (ImageView) itemView.findViewById(R.id.device_type_img);
            nameTv = (TextView) itemView.findViewById(R.id.device_name_tv);
            stateTv = (TextView) itemView.findViewById(R.id.device_state_tv);
        }
    }

    private String getBondState(int state) {
        String desc = "未配对";
        if (state == BluetoothDevice.BOND_BONDED) {
            desc = "已配对";
        } else if (state == BluetoothDevice.BOND_BONDING) {
            desc = "配对中";
        }
        return desc;
    }
}
