package com.example.wearablesandroidtask.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wearablesandroidtask.R
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint


/*

Task
https://gist.github.com/kacperd/7a28b504447afa2c81052bc4ae16a8e4

Repository
https://github.com/mikhail-vasilyeu/wearables-test-task

*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val viewModel: ScanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkLocationPM()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkBlueToothPM()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkLocationPM() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, getString(R.string.message_title_location_permissions), getString(R.string.button_ok), getString(R.string.button_cancel))
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, getString(R.string.message_settings_man), getString(R.string.button_ok), getString(R.string.button_cancel))
            }
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, getString(R.string.message_all_granted), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, getString(R.string.message_this_permissions_denied, deniedList), Toast.LENGTH_LONG).show()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkBlueToothPM() {
        PermissionX.init(this)
            .permissions(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, getString(R.string.message_title_bt_permissions), getString(R.string.button_ok), getString(R.string.button_cancel))
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, getString(R.string.message_settings_man), getString(R.string.button_ok), getString(R.string.button_cancel))
            }
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, getString(R.string.message_all_granted), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, getString(R.string.message_this_permissions_denied, deniedList), Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.checkBTEnabled()) {
            Toast.makeText(this, getString(R.string.title_please_enable_bt), Toast.LENGTH_LONG).show()
        }
    }
}