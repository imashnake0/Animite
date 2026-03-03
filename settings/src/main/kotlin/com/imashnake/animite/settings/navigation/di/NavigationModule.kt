package com.imashnake.animite.settings.navigation.di

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.imashnake.animite.settings.SettingsPage
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
    fun provideNavEntry(
        versionName: String
    ): EntryProviderScope<NavKey>.(SharedTransitionScope) -> Unit = { _ ->
        entry<SettingsPage> {
            SettingsPage(
                versionName = versionName
            )
        }
    }
}