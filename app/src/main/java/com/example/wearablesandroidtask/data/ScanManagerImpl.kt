package com.example.wearablesandroidtask.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess
import com.beepiz.bluetooth.gattcoroutines.ExperimentalBleGattCoroutinesCoroutinesApi
import com.beepiz.bluetooth.gattcoroutines.GattConnection
import com.example.wearablesandroidtask.data.models.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import javax.inject.Inject


class ScanManagerImpl @Inject constructor(@ApplicationContext context: Context, private val coroutineScope: CoroutineScope) : ScanManager {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val _devicesFlow = MutableSharedFlow<BluetoothDevice>()
    override val devicesFlow = _devicesFlow.asSharedFlow()

    private val _deviceInformationFlow = MutableStateFlow<UiState>(UiState.Loading)
    override val deviceInformationFlow = _deviceInformationFlow.asSharedFlow()

    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Timber.d("Found: ${result.device}")
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

    @OptIn(ExperimentalBleGattCoroutinesCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    override fun printDeviceData(deviceMacAddress: String) {
        coroutineScope.launch(Dispatchers.Default) {
            _deviceInformationFlow.emit(UiState.Loading)
            val connectionTimeoutInMillis: Long = 5000L
            val btDevice = bluetoothAdapter.getRemoteDevice(deviceMacAddress)
            val deviceConnection = GattConnection(btDevice)
            try {
                withTimeout(connectionTimeoutInMillis) {
                    deviceConnection.connect()
                }
                val gattServices = deviceConnection.discoverServices() // Suspends until completed
                gattServices.forEach { bluetoothGattService ->
                    bluetoothGattService.characteristics.forEach { bluetoothGattCharacteristic ->
                        try {
                            deviceConnection.readCharacteristic(bluetoothGattCharacteristic) // Suspends until characteristic is read
                        } catch (e: Exception) {
                            Timber.e("Couldn't read characteristic with uuid: ${bluetoothGattCharacteristic.uuid}", e)
                        }
                    }
                }
                with(GenericAccess) {
                    val deviceInfo = StringBuffer()
                    deviceInfo.append("Device MAC: $deviceMacAddress\n")
                    deviceConnection.readAppearance()
                    deviceInfo.append("Device appearance: ${deviceConnection.appearance}\n")
                    Timber.d("Device appearance: ${deviceConnection.appearance}")
                    deviceConnection.readDeviceName()
                    Timber.d("Device name: ${deviceConnection.deviceName}")
                    deviceInfo.append("Device name: ${deviceConnection.deviceName}\n")
                    _deviceInformationFlow.emit(UiState.Success(deviceInfo.toString()))
                }
            } catch (e: TimeoutCancellationException) {
                Timber.e("Connection timed out after $connectionTimeoutInMillis milliseconds!")
                _deviceInformationFlow.emit(UiState.Error("Connection timed out after $connectionTimeoutInMillis milliseconds!"))
                throw e
            } catch (e: CancellationException) {
                _deviceInformationFlow.emit(UiState.Error(e.toString()))
                throw e
            } catch (e: Exception) {
                _deviceInformationFlow.emit(UiState.Error(e.toString()))
                Timber.e(e)
            } finally {
                deviceConnection.close() // Close when no longer used. Also triggers disconnect by default.
            }
        }
    }

}