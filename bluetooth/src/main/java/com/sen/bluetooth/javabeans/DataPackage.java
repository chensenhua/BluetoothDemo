package com.sen.bluetooth.javabeans;

import com.sen.bluetooth.callbacks.SendResponse;

/**
 * Created by 陈森华 on 2017/8/24.
 * 功能：用一句话描述
 */

public class DataPackage {
    private byte[] data;
    private SendResponse sendResponse;

    public void setSendResponse(SendResponse sendResponse) {
        this.sendResponse = sendResponse;
    }

    public SendResponse getSendResponse() {
        return sendResponse;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
