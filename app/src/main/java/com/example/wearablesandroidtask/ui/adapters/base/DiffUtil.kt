package com.example.wearablesandroidtask.ui.adapters.base

import androidx.recyclerview.widget.DiffUtil

internal fun <U : Any, I : RecyclerItem<U>> diffUtil() = object : DiffUtil.ItemCallback<I>() {
    override fun areItemsTheSame(oldItem: I, newItem: I): Boolean {
        return oldItem.unique == newItem.unique
    }

    override fun areContentsTheSame(oldItem: I, newItem: I): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: I, newItem: I): Any? {
        return oldItem.getChangePayload(newItem)
    }
}