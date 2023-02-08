package com.example.wearablesandroidtask.ui

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearablesandroidtask.data.ScanManager
import com.example.wearablesandroidtask.data.models.FoundDevice
import com.example.wearablesandroidtask.data.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val scanManager: ScanManager) : ViewModel() {

    private var searchStarted = false

    private val _searchingStateFlow = MutableStateFlow(false)
    val searchingStateFlow = _searchingStateFlow.asStateFlow()

    private val _devicesListFlow = MutableSharedFlow<List<FoundDevice>>()
    val devicesListFlow = _devicesListFlow.asSharedFlow()

    private val _deviceInfoFlow = MutableStateFlow<UiState>(UiState.Loading)
    val deviceInfoFlow = _deviceInfoFlow.asStateFlow()

    private val found = hashMapOf<String, BluetoothDevice>()

    init {
        collectDevices()
        collectDeviceInfo()
    }



    fun checkBTEnabled() = scanManager.checkBTAvailable()

    fun onStartClicked() {
        searchStarted = !searchStarted
        _searchingStateFlow.update { searchStarted }

        if (searchStarted) {
            found.clear()
            scanManager.startScan()
        } else {
            scanManager.stopScan()
        }
    }

    fun stopSearch() {
        if (searchStarted) {
            onStartClicked()
        }
    }

    private fun collectDevices() {
        viewModelScope.launch {
            scanManager.devicesFlow.collectLatest {bluetoothDevice->
                val result = found.put(bluetoothDevice.address, bluetoothDevice)

                if (result == null) {
                    Timber.d("New bt device found: ${bluetoothDevice.address}")
                    val devicesItemList = ArrayList(found.keys).map { deviceId ->
                        FoundDevice(deviceId)
                    }.sortedWith(compareBy { it.id })
                    _devicesListFlow.emit(devicesItemList)
                }
            }
        }
    }

    private fun collectDeviceInfo() {
        viewModelScope.launch {
            scanManager.deviceInformationFlow.collectLatest {
                _deviceInfoFlow.emit(it)
            }
        }
    }

    fun printDeviceData(deviceId: String) {
        scanManager.printDeviceData(deviceId)
    }

}