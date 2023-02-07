package com.example.wearablesandroidtask.ui.scan

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.wearablesandroidtask.R
import com.example.wearablesandroidtask.databinding.FragmentScanBinding
import com.example.wearablesandroidtask.ui.adapters.FoundDevicesAdapter
import com.example.wearablesandroidtask.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class ScanFragment : Fragment(R.layout.fragment_scan) {

    companion object {
        fun newInstance() = ScanFragment()
    }

    private val viewBinding: FragmentScanBinding by viewBinding(FragmentScanBinding::bind)

    private lateinit var viewModel: ScanViewModel

    private val devicesAdapter = FoundDevicesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ScanViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModelListeners()
    }

    private fun setupViews() {
        with(viewBinding) {
            buttonStart.setOnClickListener {
                if (viewModel.checkBTEnabled()) {
                    viewModel.onStartClicked()
                } else {
                    Toast.makeText(activity, getString(R.string.title_please_enable_bt), Toast.LENGTH_LONG).show()
                }
            }

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = devicesAdapter

            devicesAdapter.selectedListener = { deviceId ->
                Timber.d("TAG", "DeviceId $deviceId clicked")
                viewModel.printDeviceData(deviceId)
                viewModel.stopSearch()
            }
        }
    }

    private fun setupViewModelListeners() {

        launchAndRepeatWithViewLifecycle {
            viewModel.searchingStateFlow.collectLatest {
                when (it) {
                    false -> {
                        with(viewBinding) {
                            buttonStart.text = getString(R.string.button_start)
                            indicator.visibility = View.INVISIBLE
                            tvInformation.text = getString(R.string.label_start_search)
                        }
                    }
                    true -> {
                        with(viewBinding) {
                            buttonStart.text = getString(R.string.button_stop)
                            indicator.visibility = View.VISIBLE
                            tvInformation.text = getString(R.string.label_found_devices, "0")
                        }
                    }
                }
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.devicesListFlow.collectLatest {
                devicesAdapter.submitList(it)
                viewBinding.tvInformation.text = getString(R.string.label_found_devices, "${it.size}")
            }
        }
    }

}