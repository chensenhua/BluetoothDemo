package com.sen.bluetooth.utils;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by 陈森华 on 2017/8/22.
 * 功能：用一句话描述
 */

public class BluetoothUtil {


    private static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    /**
     * 判断蓝牙是否打开
     *
     * @return
     */
    public static boolean isEnable() {
        return bluetoothAdapter.isEnabled();
    }

    /**
     * 打开蓝牙
     *
     * @return
     */
    public static boolean openBluetooth() {
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            return true;
        }
        return bluetoothAdapter.enable();
    }

    /**
     * 关闭蓝牙
     *
     * @return
     */
    public static boolean closeBluetooth() {
        return bluetoothAdapter.disable();
    }

    /**
     * 蓝牙扫描2.0设备
     */
    public static void startScanl() {
        bluetoothAdapter.startDiscovery();
    }

    /**
     * 停止扫描
     *
     * @return
     */
    public static boolean cancleScan() {
        return bluetoothAdapter.cancelDiscovery();
    }

    /**
     * 蓝牙配对
     *
     * @param bluetoothDevice
     * @return
     */
    public static boolean pair(BluetoothDevice bluetoothDevice) {
        try {
            Method createPair = BluetoothDevice.class.getMethod("createBond");
            return (boolean) createPair.invoke(bluetoothDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除配对
     *
     * @param bluetoothDevice
     * @return
     */
    public static boolean removePair(BluetoothDevice bluetoothDevice) {

        try {
            Method removePair = BluetoothDevice.class.getMethod("removeBond");
            return (boolean) removePair.invoke(bluetoothDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static Set<BluetoothDevice> getBondedDevices() {
        return BluetoothAdapter.getDefaultAdapter().getBondedDevices();
    }

    /**
     * 获取设备类型（细分）
     *
     * @param bluetoothClass
     * @return
     */
    public static String getDeviceType(BluetoothClass bluetoothClass) {
        String name = "unknow";
        switch (bluetoothClass.getDeviceClass()) {
            case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER://录像机
                name = "录像机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO:
                name = "车载设备";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE:
                name = "蓝牙耳机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER:
                name = "扬声器";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE:
                name = "麦克风";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO:
                name = "打印机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX:
                name = "BOX";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED:
                name = "未知的";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VCR:
                name = "录像机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA:
                name = "照相机录像机";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING:
                name = "conferencing";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER:
                name = "显示器和扬声器";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY:
                name = "游戏";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR:
                name = "显示器";
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                name = "可穿戴设备";
                break;
            case BluetoothClass.Device.PHONE_CELLULAR:
                name = "手机";
                break;
            case BluetoothClass.Device.PHONE_CORDLESS:
                name = "无线电设备";
                break;
            case BluetoothClass.Device.PHONE_ISDN:
                name = "手机服务数据网";
                break;
            case BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY:
                name = "手机调节器";
                break;
            case BluetoothClass.Device.PHONE_SMART:
                name = "手机卫星";
                break;
            case BluetoothClass.Device.PHONE_UNCATEGORIZED:
                name = "未知手机";
                break;
            case BluetoothClass.Device.WEARABLE_GLASSES:
                name = "可穿戴眼睛";
                break;
            case BluetoothClass.Device.WEARABLE_HELMET:
                name = "可穿戴头盔";
                break;
            case BluetoothClass.Device.WEARABLE_JACKET:
                name = "可穿戴上衣";
                break;
            case BluetoothClass.Device.WEARABLE_PAGER:
                name = "客串点寻呼机";
                break;
            case BluetoothClass.Device.WEARABLE_UNCATEGORIZED:
                name = "未知的可穿戴设备";
                break;
            case BluetoothClass.Device.WEARABLE_WRIST_WATCH:
                name = "手腕监听设备";
                break;
            case BluetoothClass.Device.TOY_CONTROLLER:
                name = "可穿戴设备";
                break;
            case BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE:
                name = "玩具";
                break;
            case BluetoothClass.Device.TOY_GAME:
                name = "游戏";
                break;
            case BluetoothClass.Device.TOY_ROBOT:
                name = "玩具遥控器";
                break;
            case BluetoothClass.Device.TOY_UNCATEGORIZED:
                name = "玩具未知设备";
                break;
            case BluetoothClass.Device.TOY_VEHICLE:
                name = "vehicle";
                break;
            case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE:
                name = "健康状态-血压";
                break;
            case BluetoothClass.Device.HEALTH_DATA_DISPLAY:
                name = "健康状态数据";
                break;
            case BluetoothClass.Device.HEALTH_GLUCOSE:
                name = "健康状态葡萄糖";
                break;
            case BluetoothClass.Device.HEALTH_PULSE_OXIMETER:
                name = "健康状态脉搏血氧计";
                break;
            case BluetoothClass.Device.HEALTH_PULSE_RATE:
                name = "健康状态脉搏速率";
                break;
            case BluetoothClass.Device.HEALTH_THERMOMETER:
                name = "健康状态体温计";
                break;
            case BluetoothClass.Device.HEALTH_WEIGHING:
                name = "健康状态体重";
                break;
            case BluetoothClass.Device.HEALTH_UNCATEGORIZED:
                name = "未知健康状态设备";
                break;
            case BluetoothClass.Device.COMPUTER_DESKTOP:
                name = "电脑桌面";
                break;
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
                name = "手提电脑或Pad";
                break;
            case BluetoothClass.Device.COMPUTER_LAPTOP:
                name = "便携式电脑";
                break;
            case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA:
                name = "微型电脑";
                break;
            case BluetoothClass.Device.COMPUTER_SERVER:
                name = "电脑服务";
                break;
            case BluetoothClass.Device.COMPUTER_UNCATEGORIZED:
                name = "未知的电脑设备";
                break;
            case BluetoothClass.Device.COMPUTER_WEARABLE:
                name = "可穿戴的电脑";
                break;
        }
        return name;
    }


    /**
     * 获取设备类型（大类）
     *
     * @param bluetoothClass
     * @return
     */
    public static String getMajorDeviceType(BluetoothClass bluetoothClass) {
        String content = "未知....";
        switch (bluetoothClass.getMajorDeviceClass()) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO://音频设备
                content = "音配设备";
                break;
            case BluetoothClass.Device.Major.COMPUTER://电脑
                content = "电脑";
                break;
            case BluetoothClass.Device.Major.HEALTH://健康状况
                content = "健康状况";
                break;
            case BluetoothClass.Device.Major.IMAGING://镜像，映像
                content = "镜像";
                break;
            case BluetoothClass.Device.Major.MISC://麦克风
                content = "麦克风";
                break;
            case BluetoothClass.Device.Major.NETWORKING://网络
                content = "网络";
                break;
            case BluetoothClass.Device.Major.PERIPHERAL://外部设备
                content = "外部设备";
                break;
            case BluetoothClass.Device.Major.PHONE://电话
                content = "电话";
                break;
            case BluetoothClass.Device.Major.TOY://玩具
                content = "玩具";
                break;
            case BluetoothClass.Device.Major.UNCATEGORIZED://未知的
                content = "未知的";
                break;
            case BluetoothClass.Device.Major.WEARABLE://穿戴设备
                content = "穿戴设备";
                break;
        }
        return content;
    }

    /**
     * 根据profile连接蓝牙服务
     *
     * @param context
     * @param profile
     * @param device
     */
    public static void connect(Context context, int profile, BluetoothProfile proxy, final BluetoothDevice device) {
        switch (profile) {
            case BluetoothProfile.A2DP:
                BluetoothA2dp bluetoothA2dp = (BluetoothA2dp) proxy;
                try {
                    Method connect = bluetoothA2dp.getClass().getMethod("connect", new Class[]{BluetoothDevice.class});
                    connect.invoke(bluetoothA2dp, device);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "连接音频设备失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 根据profile断开蓝牙服务
     *
     * @param context
     * @param profile
     * @param proxy
     * @param device
     */
    public static void disconnect(Context context, int profile, BluetoothProfile proxy, final BluetoothDevice device) {
        switch (profile) {
            case BluetoothProfile.A2DP:
                BluetoothA2dp bluetoothA2dp = (BluetoothA2dp) proxy;
                try {
                    Method connect = bluetoothA2dp.getClass().getMethod("disconnect", new Class[]{BluetoothDevice.class});
                    connect.invoke(bluetoothA2dp, device);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "断开连接音频设备失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 获取蓝牙的类型：经典蓝牙还是低功耗蓝牙
     * @param bluetoothDevice
     * @return
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getType(BluetoothDevice bluetoothDevice) {
        String type = "未识别";

        switch (bluetoothDevice.getType()) {
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                type = "蓝牙2.0";
                break;
            case BluetoothDevice.TRANSPORT_LE:
                type = "ble蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                type = "蓝牙2.0和ble蓝牙";
                break;
        }
        return type;
    }

    public static  int getProfileConnnectionState(int profile)
    {
        return BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(profile);
    }


}
