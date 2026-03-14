package com.imashnake.animite.di

import com.apollographql.apollo.ApolloClient
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.AnilistSearchRepository
import com.imashnake.animite.api.anilist.AnilistUserRepository
import com.imashnake.animite.api.anilist.createApolloHttpClient
import com.imashnake.animite.api.anilist.createAuthenticatedApolloHttpClient
import com.imashnake.animite.api.preferences.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnilistApiModule {
    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return createApolloHttpClient()
    }

    @Provides
    @Singleton
    @AuthorizedClient
    fun provideAuthorizedApolloClient(
        preferencesRepository: PreferencesRepository
    ): ApolloClient {
        return createAuthenticatedApolloHttpClient(preferencesRepository)
    }

    @Provides
    @Singleton
    fun provideAnilistMediaRepository(client: ApolloClient): AnilistMediaRepository {
        return AnilistMediaRepository(client)
    }

    @Provides
    @Singleton
    fun provideAnilistSearchRepository(client: ApolloClient): AnilistSearchRepository {
        return AnilistSearchRepository(client)
    }

    @Provides
    @Singleton
    fun provideAnilistUserRepository(@AuthorizedClient client: ApolloClient): AnilistUserRepository {
        return AnilistUserRepository(client)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthorizedClient
