package com.example.wearablesandroidtask.ui.adapters.base

interface RecyclerItem<U : Any> {
    val unique: U
    override fun equals(other: Any?): Boolean
    open fun getChangePayload(other: Any?): Any? = null
}
