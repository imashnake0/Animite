package com.imashnake.animite.search.di

import android.content.Context
import androidx.room.Room
import com.imashnake.animite.search.db.SearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSearchDatabase(
        @ApplicationContext context: Context
    ): SearchDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = SearchDatabase::class.java,
            name = "search"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchDao(
        searchDatabase: SearchDatabase
    ) = searchDatabase.searchDao()
}
