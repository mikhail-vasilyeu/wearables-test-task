package com.example.wearablesandroidtask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wearablesandroidtask.data.models.FoundDevice
import com.example.wearablesandroidtask.databinding.ItemDeviceBinding

class FoundDeviceViewHolder (
    private val binding: ItemDeviceBinding,
    private val selectionListener: ((String) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {

        fun from(
            parent: ViewGroup,
            selectionListener: ((String) -> Unit)?
        ): FoundDeviceViewHolder {
            return FoundDeviceViewHolder(
                binding = ItemDeviceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                selectionListener = selectionListener
            )
        }
    }

    fun bind(foundDevice: FoundDevice, ) {
        binding.tvDeviceName.text = foundDevice.id
        binding.root.setOnClickListener {
            selectionListener?.invoke(foundDevice.id)
        }
    }
}