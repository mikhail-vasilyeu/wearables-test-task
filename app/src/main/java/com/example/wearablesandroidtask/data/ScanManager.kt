package com.example.wearablesandroidtask.data

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface ScanManager {

    fun checkBTAvailable(): Boolean

    fun startScan()

    fun stopScan()

    val devicesFlow: Flow<BluetoothDevice>
}