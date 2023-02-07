package com.example.wearablesandroidtask.ui.scan

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import com.example.wearablesandroidtask.data.ScanManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val scanManager: ScanManager) : ViewModel() {

    private var searchStarted = false

    private val _searchingStateFlow = MutableStateFlow(false)
    val searchingStateFlow = _searchingStateFlow.asStateFlow()

    private val _devicesListFlow = MutableSharedFlow<List<BluetoothDevice>>()
    val devicesListFlow = _devicesListFlow.asSharedFlow()

    private val devicesList: MutableList<BluetoothDevice> = mutableListOf<BluetoothDevice>()

    fun checkBTEnabled() = scanManager.checkBTAvailable()

    fun onStartClicked() {
        searchStarted = !searchStarted
        _searchingStateFlow.update { searchStarted }

        if (searchStarted) {
            devicesList.clear()
            scanManager.startScan()
        } else {
            scanManager.stopScan()
        }

    }

}