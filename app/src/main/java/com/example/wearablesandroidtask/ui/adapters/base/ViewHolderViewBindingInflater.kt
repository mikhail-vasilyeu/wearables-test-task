package com.example.wearablesandroidtask.ui.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup

internal typealias ViewHolderViewBindingInflater<VB> = (
    inflater: LayoutInflater,
    parent: ViewGroup,
    attachToParent: Boolean
) -> VB
