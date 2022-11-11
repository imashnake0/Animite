package com.imashnake.animite.di

import com.imashnake.animite.data.sauce.apis.MediaApi
import com.imashnake.animite.data.sauce.apis.MediaListApi
import com.imashnake.animite.data.sauce.apis.SearchApi
import com.imashnake.animite.data.sauce.apis.apollo.ApolloMediaApi
import com.imashnake.animite.data.sauce.apis.apollo.ApolloMediaListApi
import com.imashnake.animite.data.sauce.apis.apollo.ApolloSearchApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonBindings {

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

    @Singleton
    @Binds
    abstract fun providesSearchApi(
        apolloSearchApi: ApolloSearchApi
    ): SearchApi
}
