package com.example.wearablesandroidtask.data.models

sealed class UiState {
    object Loading : UiState()
    data class Success(val deviceData: String) : UiState()
    data class Error(val message: String) : UiState()
}