package com.imashnake.animite.profile.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.imashnake.animite.navigation.LocalSharedTransitionScope
import com.imashnake.animite.navigation.Navigator
import com.imashnake.animite.navigation.Root
import com.imashnake.animite.profile.ProfileScreen
import com.imashnake.animite.profile.ProfileViewModel
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
        entry<Root.ProfileRoute> { route ->
            ProfileScreen(
                onNavigateToMediaItem = navigator::navigateTo,
                onNavigateToSettings = navigator::navigateTo,
                sharedTransitionScope = LocalSharedTransitionScope.current,
                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                viewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.Factory>(
                    key = route.accessToken,
                    creationCallback = { factory ->
                        factory.create(route)
                    }
                )
            )
        }
    }
}