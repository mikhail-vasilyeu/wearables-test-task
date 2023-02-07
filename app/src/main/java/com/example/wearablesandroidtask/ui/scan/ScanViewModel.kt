package com.example.wearablesandroidtask.ui.scan

import androidx.lifecycle.ViewModel
import com.example.wearablesandroidtask.data.Data
import com.example.wearablesandroidtask.data.models.BtDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScanViewModel : ViewModel() {

    private var searchStarted = false

   // private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    private val _searchingStateFlow = MutableStateFlow(false)
    val searchingStateFlow = _searchingStateFlow.asStateFlow()

    fun onStartClicked() {
        searchStarted = !searchStarted
        _searchingStateFlow.update { searchStarted }
    }

}