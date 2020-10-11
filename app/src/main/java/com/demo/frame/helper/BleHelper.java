package com.demo.frame.helper;

import android.bluetooth.BluetoothAdapter;

public class BleHelper {

    public static boolean isSupport() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {  //有蓝牙功能
            return true;
        }
        //无蓝牙功能
        return false;
    }

    public static boolean isOpen() {
        boolean flag = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {  //有蓝牙功能
            return bluetoothAdapter.isEnabled();
        }
        return flag;
    }

    public static void open() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {  //有蓝牙功能
            if (!bluetoothAdapter.isEnabled()) {  //蓝牙未开启
                bluetoothAdapter.enable(); //开启
            }
        }
    }

    public static void close() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null) {  //有蓝牙功能
            if (bluetoothAdapter.isEnabled()) {  //蓝牙开启
                bluetoothAdapter.disable(); //关闭
            }
        }
    }
}
