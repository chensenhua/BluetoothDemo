package com.sen.bleclient;

import com.sen.bluetooth.ble.ReceiveDataPool;
import com.sen.bluetooth.utils.Dbug;

/**
 * Created by 陈森华 on 2017/9/12.
 * 功能：用一句话描述
 */

public class ReceiveDataPoolImpl implements ReceiveDataPool {
    private final static int MAX_SIZE = 128;
    private byte[] buffer = new byte[MAX_SIZE];
    private int index = 0;

    @Override
    public boolean isVerify() {
        return false;
    }

    @Override
    public void push(byte[] data) {
        if (index + data.length > buffer.length) {
            int len = (index + data.length) > (buffer.length * 2) ? (index + data.length) * 2 : buffer.length * 2;
            byte[] temp = new byte[len];
            System.arraycopy(buffer, index, temp, 0, index);
            buffer = temp;
        }
        System.arraycopy(data, 0, buffer, index, data.length);
        return ;
    }

    @Override
    public boolean isReceiveCompletion() {
        return false;
    }

    @Override
    public byte[] getValue() {
        byte [] temp=new byte[index];
        return temp ;
    }
}
