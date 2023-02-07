package com.example.wearablesandroidtask.di

import com.example.wearablesandroidtask.data.ScanManager
import com.example.wearablesandroidtask.data.ScanManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class ScanBindModule {

    @Binds
    internal abstract fun bindScanManager(
        scanManagerImpl: ScanManagerImpl,
    ): ScanManager

}