package com.androidforge.habitflow.di

import android.content.Context
import com.androidforge.habitflow.core.AdManager
import com.androidforge.habitflow.core.data.AdManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for application-level dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAdManager(@ApplicationContext context: Context): AdManager {
        return AdManagerImpl(context)
    }
}