package com.example.wearablesandroidtask.ui.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class RecyclerItemAdapter<U : Any, I : RecyclerItem<U>, VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<VH>() {

    private val itemCallback = diffUtil<U, I>()
    val currentList = mutableListOf<I>()

    fun <VB : ViewBinding> ViewGroup.inflateBinding(
        bindingInflater: ViewHolderViewBindingInflater<VB>,
    ): VB {
        return bindingInflater.invoke(LayoutInflater.from(context), this, false)
    }

    override fun getItemCount(): Int = currentList.size

    fun getItem(position: Int): I = currentList[position]

    fun submitList(newList: List<I>?) {
        val saveNewList = newList ?: emptyList()
        val diffUtilCallback = DiffUtilCallback(itemCallback, currentList, saveNewList)


        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        this.currentList.clear()
        this.currentList.addAll(saveNewList)
        diffResult.dispatchUpdatesTo(this)
    }

    private inner class DiffUtilCallback(
        private val differ: DiffUtil.ItemCallback<I>,
        private val oldList: List<I>,
        private val newList: List<I>,
    ) : DiffUtil.Callback() {

        private fun getOldNewItem(oldItemPosition: Int, newItemPosition: Int): Pair<I, I>? {
            val oldItem = oldList.getOrNull(oldItemPosition)
            val newItem = newList.getOrNull(newItemPosition)

            if (oldItem != null && newItem != null) {
                return oldItem to newItem
            } else {
                return null
            }
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return getOldNewItem(oldItemPosition, newItemPosition)
                ?.let { oldNewItems -> differ.areItemsTheSame(oldNewItems.first, oldNewItems.second) }
                ?: false
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return getOldNewItem(oldItemPosition, newItemPosition)
                ?.let { oldNewItems -> differ.areContentsTheSame(oldNewItems.first, oldNewItems.second) }
                ?: false
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return getOldNewItem(oldItemPosition, newItemPosition)
                ?.let { oldNewItems -> differ.getChangePayload(oldNewItems.first, oldNewItems.second) }
        }
    }
}