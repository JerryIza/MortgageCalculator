package com.example.mortgagecalculator.di

import android.content.Context
import androidx.room.Room
import com.example.mortgagecalculator.db.InputDatabase
import com.example.mortgagecalculator.db.InputDao
import com.example.mortgagecalculator.repositories.InputRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DBModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun providesInputDatabase(@ApplicationContext app: Context
    ) =  Room.databaseBuilder(
        app,
        InputDatabase::class.java,
        "input_db_name"
    ) .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideInputDao(db: InputDatabase) = db.getInputDao()
}