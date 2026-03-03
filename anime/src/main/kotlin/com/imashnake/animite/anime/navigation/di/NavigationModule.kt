package com.imashnake.animite.anime.navigation.di

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.imashnake.animite.anime.AnimeScreen
import com.imashnake.animite.navigation.AnimeRoute
import com.imashnake.animite.navigation.Navigator
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
        navigator: Navigator,
    ): EntryProviderScope<NavKey>.(SharedTransitionScope) -> Unit = { sharedScope ->
        entry<AnimeRoute> {
            AnimeScreen(
                onNavigateToMediaItem = navigator::navigate,
                sharedTransitionScope = sharedScope,
                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            )
        }
    }
}