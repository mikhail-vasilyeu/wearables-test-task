package com.example.wearablesandroidtask.di

import android.content.Context
import com.example.wearablesandroidtask.data.ScanManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope


@Module
@InstallIn(SingletonComponent::class)
object ScanModule {

    @Provides
    internal fun provideScanManagerImpl(@ApplicationContext context: Context, coroutineScope: CoroutineScope): ScanManagerImpl {
        return ScanManagerImpl(context, coroutineScope)
    }

}