package com.example.wearablesandroidtask.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class ScanManagerImpl @Inject constructor(@ApplicationContext context: Context, private val coroutineScope: CoroutineScope) : ScanManager {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val _devicesFlow = MutableSharedFlow<BluetoothDevice>()
    override val devicesFlow = _devicesFlow.asSharedFlow()

    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            coroutineScope.launch(Dispatchers.Default) {
                _devicesFlow.emit(result.device)
            }
        }
    }

    override fun checkBTAvailable(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    @SuppressLint("MissingPermission")
    override fun startScan() {
        bluetoothLeScanner.startScan(leScanCallback)
    }

    @SuppressLint("MissingPermission")
    override fun stopScan() {
        bluetoothLeScanner.stopScan(leScanCallback)
    }


}