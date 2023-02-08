package com.example.wearablesandroidtask.data

import android.bluetooth.BluetoothDevice
import com.example.wearablesandroidtask.data.models.UiState
import kotlinx.coroutines.flow.Flow

interface ScanManager {

    val devicesFlow: Flow<BluetoothDevice>
    val deviceInformationFlow: Flow<UiState>

    fun checkBTAvailable(): Boolean

    fun startScan()

    fun stopScan()

    fun printDeviceData(deviceMacAddress: String)
}