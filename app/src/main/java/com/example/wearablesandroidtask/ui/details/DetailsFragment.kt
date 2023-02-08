package com.example.wearablesandroidtask.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.wearablesandroidtask.R
import com.example.wearablesandroidtask.data.models.UiState
import com.example.wearablesandroidtask.databinding.FragmentDetailsBinding
import com.example.wearablesandroidtask.ui.ScanViewModel
import com.example.wearablesandroidtask.utils.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


const val PARAM_DEVICE_ID = "PARAM_DEVICE_ID"

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: ScanViewModel by activityViewModels()
    private val viewBinding: FragmentDetailsBinding by viewBinding(FragmentDetailsBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModelListeners()

        val deviceID = arguments?.getString(PARAM_DEVICE_ID) ?: ""
        Timber.d("device: $deviceID")
        viewModel.printDeviceData(deviceID)
    }


    private fun setupViewModelListeners() {
        launchAndRepeatWithViewLifecycle {
            viewModel.deviceInfoFlow.collect {
                with(viewBinding) {
                    when (it) {
                        is UiState.Error -> {
                            indicator.visibility = View.INVISIBLE
                            tvDeviceText.text = it.message
                        }
                        UiState.Loading -> {
                            indicator.visibility = View.VISIBLE
                            tvDeviceText.text = ""
                        }
                        is UiState.Success -> {
                            indicator.visibility = View.INVISIBLE
                            tvDeviceText.text = it.deviceData
                        }
                    }
                }
            }
        }

    }

}