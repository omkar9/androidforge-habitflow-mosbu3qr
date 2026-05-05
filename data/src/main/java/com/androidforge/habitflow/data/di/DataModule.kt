package com.androidforge.habitflow.data.di

import com.androidforge.habitflow.data.local.dao.HabitCompletionDao
import com.androidforge.habitflow.data.local.dao.HabitDao
import com.androidforge.habitflow.data.repository.HabitRepositoryImpl
import com.androidforge.habitflow.domain.repository.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing data layer dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideHabitRepository(
        habitDao: HabitDao,
        habitCompletionDao: HabitCompletionDao
    ): HabitRepository {
        return HabitRepositoryImpl(habitDao, habitCompletionDao)
    }
}