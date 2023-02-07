package com.example.wearablesandroidtask

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber



@HiltAndroidApp
class WearableApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //Timber init
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}