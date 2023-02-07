package com.example.wearablesandroidtask.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.wearablesandroidtask.data.models.FoundDevice

class FoundDevicesAdapter: ListAdapter<FoundDevice, FoundDeviceViewHolder>(FoundDevice.diffCallback) {

    var selectedListener: ((deviceId: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundDeviceViewHolder {
        return FoundDeviceViewHolder.from(parent, selectedListener)
    }

    override fun onBindViewHolder(holder: FoundDeviceViewHolder, position: Int) {
        val question = getItem(position)
        holder.bind(question)
    }
}