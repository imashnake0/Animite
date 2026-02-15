package com.imashnake.animite.settings.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.imashnake.animite.navigation.Nested
import com.imashnake.animite.settings.SettingsPage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object NavigationModule {

    @IntoSet
    @Provides
    fun provideNavigationEntry(
        versionName: String,
    ): EntryProviderScope<NavKey>.() -> Unit = {
        entry<Nested.SettingsRoute> {
            SettingsPage(
                versionName = versionName
            )
        }
    }
}