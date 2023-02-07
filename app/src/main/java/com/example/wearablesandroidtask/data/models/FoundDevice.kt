package com.example.wearablesandroidtask.data.models

import androidx.recyclerview.widget.DiffUtil

data class FoundDevice(val id: String) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<FoundDevice>() {

            override fun areItemsTheSame(oldItem: FoundDevice, newItem: FoundDevice): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FoundDevice, newItem: FoundDevice): Boolean {
                return areItemsTheSame(
                    oldItem, newItem
                ) //&& oldItem.id == newItem.id  implement if more fields
            }
        }
    }

}