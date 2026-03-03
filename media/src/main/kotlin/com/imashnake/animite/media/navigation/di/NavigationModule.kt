package com.imashnake.animite.media.navigation.di

import androidx.compose.animation.SharedTransitionScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.media.MediaPageViewModel
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
        entry<MediaPage> { args ->
            MediaPage(
                onBack = navigator::popBack,
                onNavigateToMediaItem = navigator::navigate,
                useDarkTheme = true, // fix?
                sharedTransitionScope = sharedScope,
                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                viewModel = hiltViewModel<MediaPageViewModel, MediaPageViewModel.Factory> { factory ->
                    factory.create(args)
                }
            )
        }
    }
}