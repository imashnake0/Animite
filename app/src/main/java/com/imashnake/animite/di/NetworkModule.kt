@file:Suppress("Unused")

package com.imashnake.animite.di

import com.imashnake.animite.data.sauce.apis.MediaApi
import com.imashnake.animite.data.sauce.apis.apollo.ApolloMediaListApi
import com.imashnake.animite.data.sauce.apis.MediaListApi
import com.imashnake.animite.data.sauce.apis.apollo.ApolloMediaApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun providesNetworkDispatcher(): CoroutineDispatcher =
        Dispatchers.IO
}

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkModuleBinds {
    @Singleton
    @Binds
    abstract fun providesMediaApi(
        apolloMediaApi: ApolloMediaApi
    ): MediaApi

    @Singleton
    @Binds
    abstract fun providesMediaListApi(
        apolloMediaListApi: ApolloMediaListApi
    ): MediaListApi
}
