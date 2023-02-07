package com.example.wearablesandroidtask.ui.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<U : Any, I : RecyclerItem<U>, VH : RecyclerView.ViewHolder> :
    ListAdapter<I, VH>(diffUtil<U, I>()) {

    fun <VB : ViewBinding> ViewGroup.inflateBinding(
        bindingInflater: ViewHolderViewBindingInflater<VB>
    ): VB {
        return bindingInflater.invoke(LayoutInflater.from(context), this, false)
    }
}