package com.example.wearablesandroidtask.di

import android.content.Context
import com.example.wearablesandroidtask.data.ScanManager
import com.example.wearablesandroidtask.data.ScanManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ScanModule {

    @Provides
    internal fun provideScanManagerImpl(@ApplicationContext context: Context, coroutineScope: CoroutineScope): ScanManagerImpl {
        return ScanManagerImpl(context, coroutineScope)
    }

}