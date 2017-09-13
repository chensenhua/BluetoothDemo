package com.sen.bluetooth;

/**
 * Created by 陈森华 on 2017/8/23.
 * 功能：用一句话描述
 */

public enum Error {
    BLE_SCAN_FAILED,
    DATA_SEND_SUCCESS,
    DATA_SEND_FAILED,

    //连接
    CONNECTED_FAILED,
    CONNECTED_OK;


    public static String getErrorDesc(Error value) {
         String desc="unknow";
        switch (value)
        {
            case BLE_SCAN_FAILED:
                desc="扫描失败";
                break;
            case DATA_SEND_SUCCESS:
                desc="发送成功";
                break;
            case DATA_SEND_FAILED:
                desc = "发送失败";
                break;
        }
        return desc;
    }

}
