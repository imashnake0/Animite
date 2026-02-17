package com.imashnake.animite.anime.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.imashnake.animite.anime.AnimeScreen
import com.imashnake.animite.navigation.LocalSharedTransitionScope
import com.imashnake.animite.navigation.Navigator
import com.imashnake.animite.navigation.Root
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
        navigator: Navigator
    ): EntryProviderScope<NavKey>.() -> Unit = {
        entry<Root.AnimeRoute> {
            AnimeScreen(
                onNavigateToMediaItem = navigator::navigateTo,
                sharedTransitionScope = LocalSharedTransitionScope.current,
                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            )
        }
    }
}