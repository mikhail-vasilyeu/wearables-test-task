package com.example.wearablesandroidtask.ui.details

import androidx.lifecycle.ViewModel
import com.example.wearablesandroidtask.data.ScanManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val scanManager: ScanManager) : ViewModel() {
    // TODO: Implement the ViewModel
}