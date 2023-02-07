package com.example.wearablesandroidtask.ui.scan

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.wearablesandroidtask.R
import com.example.wearablesandroidtask.databinding.FragmentScanBinding
import com.example.wearablesandroidtask.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ScanFragment : Fragment(R.layout.fragment_scan) {

    companion object {
        fun newInstance() = ScanFragment()
    }

    private val viewBinding: FragmentScanBinding by viewBinding(FragmentScanBinding::bind)

    private lateinit var viewModel: ScanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ScanViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModelListeners()
    }

    override fun onResume() {
        super.onResume()
        val isBTEnabled = viewModel.checkBTEnabled()

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
        }
    }

    private fun setupViewModelListeners() {

        launchAndRepeatWithViewLifecycle {
            viewModel.searchingStateFlow.collectLatest {
                when(it) {
                    false -> {
                        with(viewBinding) {
                            buttonStart.text = getString(R.string.button_start)
                            indicator.visibility = View.INVISIBLE
                        }
                    }
                    true -> {
                        with(viewBinding) {
                            buttonStart.text = getString(R.string.button_stop)
                            indicator.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }


    }

}