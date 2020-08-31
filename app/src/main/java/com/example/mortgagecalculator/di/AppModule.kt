package com.example.mortgagecalculator.di

import android.content.Context
import androidx.room.Room
import com.example.mortgagecalculator.db.InputsDatabase
import com.example.mortgagecalculator.model.AmortizationCalculator
import com.example.mortgagecalculator.model.AppState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object AppModule