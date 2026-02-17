package com.imashnake.animite.social.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.imashnake.animite.navigation.Root
import com.imashnake.animite.social.SocialScreen
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
    fun provideNavigationEntry(): EntryProviderScope<NavKey>.() -> Unit = {
        entry<Root.SocialRoute> {
            SocialScreen()
        }
    }
}