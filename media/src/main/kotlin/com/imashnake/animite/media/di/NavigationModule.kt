package com.imashnake.animite.media.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.media.MediaPageViewModel
import com.imashnake.animite.navigation.LocalSharedTransitionScope
import com.imashnake.animite.navigation.Navigator
import com.imashnake.animite.navigation.Nested
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
        navigator: Navigator,
    ): EntryProviderScope<NavKey>.() -> Unit = {
        entry<Nested.MediaRoute> { route ->
            MediaPage(
                onBack = navigator::popBack,
                onNavigateToMediaItem = navigator::navigateTo,
                useDarkTheme = true,
                sharedTransitionScope = LocalSharedTransitionScope.current,
                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                viewModel = hiltViewModel<MediaPageViewModel, MediaPageViewModel.Factory>(
                    key = route.title,
                    creationCallback = { factory ->
                        factory.create(route)
                    }
                )
            )
        }
    }
}