package com.androidforge.habitflow.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Hilt module for presentation layer dependencies.
 * ViewModels are typically injected directly by Hilt, so this module might be empty
 * or contain providers for specific UI-related utilities if needed.
 */
@Module
@InstallIn(ViewModelComponent::class)
object PresentationModule {
    // No specific provides for now, ViewModels are directly injected
}