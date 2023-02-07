package com.example.wearablesandroidtask.ui.scan

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearablesandroidtask.data.ScanManager
import com.example.wearablesandroidtask.data.models.DeviceItem
import com.example.wearablesandroidtask.data.models.FoundDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val scanManager: ScanManager) : ViewModel() {

    private var searchStarted = false

    private val _searchingStateFlow = MutableStateFlow(false)
    val searchingStateFlow = _searchingStateFlow.asStateFlow()

    private val _devicesListFlow = MutableSharedFlow<List<FoundDevice>>()
    val devicesListFlow = _devicesListFlow.asSharedFlow()

    private val found = hashMapOf<String, BluetoothDevice>()

    init {
        collectDevices()
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

    private fun collectDevices() {
        viewModelScope.launch {
            scanManager.devicesFlow.collectLatest {
                val result = found.put(it.address, it)

                if (result == null) {
                    Log.d("TAG", "New bt device found: ${it.address}")
                    val devicesItemList = ArrayList(found.keys).map { deviceId ->
                        FoundDevice(deviceId)
                    }
                    _devicesListFlow.emit(devicesItemList)
                }
            }
        }
    }

}