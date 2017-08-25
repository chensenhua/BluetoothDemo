package com.sen.bluetooth;

import static android.bluetooth.le.ScanCallback.SCAN_FAILED_INTERNAL_ERROR;

/**
 * Created by 陈森华 on 2017/8/23.
 * 功能：用一句话描述
 */

public enum Error {
    BLE_SCAN_FAILED,
    DATA_SEND_SUCCESS,
    DATA_SEND_FAIL;

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
            case DATA_SEND_FAIL:
                desc = "发送失败";
                break;
        }
        return desc;
    }

}
