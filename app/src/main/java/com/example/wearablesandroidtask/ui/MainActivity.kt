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
import dagger.hilt.android.AndroidEntryPoint

/*

Task
https://gist.github.com/kacperd/7a28b504447afa2c81052bc4ae16a8e4

Repository
https://github.com/mikhail-vasilyeu/wearables-test-task

*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 1
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 2
        private const val MY_PERMISSIONS_REQUEST_BT_SCAN = 3
        private const val MY_PERMISSIONS_REQUEST_BT_CONNECT = 4
    }


    private val viewModel: ScanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        checkLocationPermission()
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.checkBTEnabled()) {
            Toast.makeText(this, getString(R.string.title_please_enable_bt), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.title_location_permission_request))
                    .setMessage(getString(R.string.message_location_permission_request))
                    .setPositiveButton(getString(R.string.button_ok)) { _, _ ->
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                requestLocationPermission()
            }
        } else {
            checkBackgroundLocation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkBluetoothPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            requestBluetoothScanPermission()
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestBluetoothConnectPermission()
        }
    }


    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestBackgroundLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,), MY_PERMISSIONS_REQUEST_LOCATION)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestBluetoothScanPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN,), MY_PERMISSIONS_REQUEST_BT_SCAN)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestBluetoothConnectPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT,), MY_PERMISSIONS_REQUEST_BT_CONNECT)
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        checkBackgroundLocation()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.toast_message_permission_denied), Toast.LENGTH_LONG).show()

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", this.packageName, null),),
                        )
                    }
                }
                return
            }
            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this,getString(R.string.toast_message_location_permission_granted), Toast.LENGTH_LONG).show()
                    }
                } else {

                    Toast.makeText(this, getString(R.string.toast_message_permission_denied), Toast.LENGTH_LONG).show()
                }
                return
            }
            MY_PERMISSIONS_REQUEST_BT_SCAN -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this,getString(R.string.toast_message_bt_permission_granted), Toast.LENGTH_LONG).show()
                    }
                } else {

                    Toast.makeText(this, getString(R.string.toast_message_permission_denied), Toast.LENGTH_LONG).show()
                }
                return
            }

            MY_PERMISSIONS_REQUEST_BT_CONNECT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this,getString(R.string.toast_message_bt_permission_granted), Toast.LENGTH_LONG).show()
                    }
                } else {

                    Toast.makeText(this, getString(R.string.toast_message_permission_denied), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}