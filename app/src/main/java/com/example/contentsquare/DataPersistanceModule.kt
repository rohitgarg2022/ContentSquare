package com.example.contentsquare

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataPersistenceModule {
    @Provides
    @Singleton
    fun providePreferenceDataStore(
        @ApplicationContext context: Context,
        gson: Gson
    ): HousingPrefStore {
        return HousingPrefStore(context.applicationContext, gson)
    }
}