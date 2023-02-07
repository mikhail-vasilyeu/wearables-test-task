package com.example.wearablesandroidtask.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import com.beepiz.blegattcoroutines.genericaccess.GenericAccess
import com.beepiz.bluetooth.gattcoroutines.ExperimentalBleGattCoroutinesCoroutinesApi
import com.beepiz.bluetooth.gattcoroutines.GattConnection
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
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

    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Timber.d("ScanManager", "Found: ${result.device}")
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

    override fun printDeviceData(deviceMac: String) {
        coroutineScope.launch(Dispatchers.Default) {
            logNameAndAppearance(deviceMac)
        }
    }

    @OptIn(ExperimentalBleGattCoroutinesCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun logNameAndAppearance(deviceMacAddress: String) {

        val btDevice = bluetoothAdapter.getRemoteDevice(deviceMacAddress)
        btDevice.useBasic { device, services ->
            services.forEach { Timber.d("Service found with UUID: ${it.uuid}") }
            with(GenericAccess) {
                device.readAppearance()
                Timber.d("Device appearance: ${device.appearance}")
                device.readDeviceName()
                Timber.d("Device name: ${device.deviceName}")
            }
        }
    }


    @OptIn(ExperimentalBleGattCoroutinesCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    @Suppress("InlinedApi")
    suspend inline fun BluetoothDevice.useBasic(
        connectionTimeoutInMillis: Long = 5000L,
        block: (GattConnection, List<BluetoothGattService>) -> Unit
    ) {
        val deviceConnection = GattConnection(this)
        val loggingJob = MainScope().launch(Dispatchers.Main) {
            deviceConnection.stateChanges.collect {
                Timber.i("connection state changed: $it")
            }
        }
        try {
            withTimeout(connectionTimeoutInMillis) {
                deviceConnection.connect()
            }
            Timber.i("Connected!")
            val services = deviceConnection.discoverServices()
            Timber.i("Services discovered!")
            block(deviceConnection, services)
        } catch (e: TimeoutCancellationException) {
            Timber.e("Connection timed out after $connectionTimeoutInMillis milliseconds!")
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            deviceConnection.close()
            loggingJob.cancel()
            Timber.i("Closed!")
        }
    }

}