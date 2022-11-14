package com.imashnake.animite.di

import android.content.Context
import androidx.room.Room
import com.imashnake.animite.data.sauce.db.AnimiteDatabase
import com.imashnake.animite.data.sauce.db.dao.MediaDAO
import com.imashnake.animite.data.sauce.db.dao.MediaLinkDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context, dispatcher: CoroutineDispatcher): AnimiteDatabase {
        return Room.databaseBuilder(context, AnimiteDatabase::class.java, "animite_db")
            .fallbackToDestructiveMigration()
            .setQueryExecutor(dispatcher.asExecutor())
            .setTransactionExecutor(dispatcher.asExecutor())
            .build()
    }

    @Provides
    @Singleton
    fun provideMediumDAO(db: AnimiteDatabase): MediaDAO = db.homepageDAO()

    @Provides
    @Singleton
    fun provideMediumLinkDAO(db: AnimiteDatabase): MediaLinkDAO = db.mediumLinkDAO()
}