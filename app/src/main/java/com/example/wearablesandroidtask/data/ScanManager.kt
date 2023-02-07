package com.example.wearablesandroidtask.data

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface ScanManager {

    val devicesFlow: Flow<BluetoothDevice>

    fun checkBTAvailable(): Boolean

    fun startScan()

    fun stopScan()

    fun printDeviceData(deviceMac: String)
}