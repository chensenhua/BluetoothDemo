package com.sen.bleclient;

import java.util.UUID;

/**
 * Created by 陈森华 on 2017/9/12.
 * 功能：用一句话描述
 */

public interface Constants {

    //
    //  设备服务UUID, 需固件配合同时修改
    //
    UUID UUID_SERVICE = UUID.fromString("0000AE00-0000-1000-8000-00805F9B34FB");

    //
    //  设备特征值UUID, 需固件配合同时修改
    //
    UUID UUID_WRITE = UUID.fromString("0000AE01-0000-1000-8000-00805F9B34FB");  // 用于发送数据到设备
    UUID UUID_NOTIFICATION = UUID.fromString("0000AE02-0000-1000-8000-00805F9B34FB"); // 用于接收设备推送的数据


    byte[] com1 = new byte[]{(byte) 0x4A, 0x4C, 0x42, 0x54,
            (byte) 0x04, (byte) 0x05, 0x06, 0x07,
            0x00, 0x00, 0x00, 0x00,
            0x00,
            0x01,
            0x02,
            (byte) 0x84, 0x04};
    byte[] com2 = new byte[]{(byte) 0x4A, 0x4C, 0x42, 0x54,
            (byte) 0x04, (byte) 0x05, 0x06, 0x07,
            0x00, 0x00, 0x00, 0x00,
            0x00,
            0x01,
            0x02,
            (byte) 0x84, 0x00};
    byte[] com3 = new byte[]{(byte) 0x4A, 0x4C, 0x42, 0x54,
            (byte) 0x04, (byte) 0x05, 0x06, 0x07,
            0x00, 0x00, 0x00, 0x00,
            0x00,
            0x01,
            0x02,
            (byte) 0x84, 0x01};
    byte[] com4 = new byte[]{(byte) 0x4A, 0x4C, 0x42, 0x54,
            (byte) 0x04, (byte) 0x05, 0x06, 0x07,
            0x00, 0x00, 0x00, 0x00,
            0x00,
            0x01,
            0x02,
            (byte) 0x84, 0x02};
    byte[] com5 = new byte[]{(byte) 0x4A, 0x4C, 0x42, 0x54,
            (byte) 0x04, (byte) 0x05, 0x06, 0x07,
            0x00, 0x00, 0x00, 0x00,
            0x00,
            0x01,
            0x02,
            (byte) 0x84, 0x03};
    byte[] com6 = new byte[]{(byte) 0x4A, 0x4C, 0x42, 0x54,
            (byte) 0x04, (byte) 0x05, 0x06, 0x07,
            0x00, 0x00, 0x00, 0x00,
            0x00,
            0x01,
            0x02,
            (byte) 0x81, 0x01};
    byte[] com7 = new byte[]{(byte) 0x4A, 0x4C, 0x42, 0x54,
            (byte) 0x04, (byte) 0x05, 0x06, 0x07,
            0x00, 0x00, 0x00, 0x00,
            0x00,
            0x02,
            0x01,
            (byte) 0x80};
}
