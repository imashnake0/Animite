package com.imashnake.animite.social.navigation.di

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.imashnake.animite.navigation.SocialRoute
import com.imashnake.animite.social.SocialScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object NavigationModule {

    @Provides
    @IntoSet
    fun provideNavEntry(): EntryProviderScope<NavKey>.(SharedTransitionScope) -> Unit =
        { _ ->
            entry<SocialRoute> {
                SocialScreen()
            }
        }
}