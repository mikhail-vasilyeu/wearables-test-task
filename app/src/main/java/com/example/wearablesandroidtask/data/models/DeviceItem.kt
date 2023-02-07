package com.example.wearablesandroidtask.data.models

import com.example.wearablesandroidtask.ui.adapters.base.RecyclerItem

data class DeviceItem(
    val id: String,
    ) : RecyclerItem<String> {

    override val unique: String
        get() = id

}