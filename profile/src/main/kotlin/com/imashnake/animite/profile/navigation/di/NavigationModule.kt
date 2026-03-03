package com.imashnake.animite.profile.navigation.di

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.imashnake.animite.navigation.Navigator
import com.imashnake.animite.navigation.ProfileRoute
import com.imashnake.animite.navigation.di.EntryInstaller
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

    @Provides
    @IntoSet
    fun provideNavEntry(
        navigator: Navigator,
    ): EntryInstaller = { sharedScope ->
        entry<ProfileRoute> { args ->
            ProfileScreen(
                onNavigateToSettings = navigator::navigate,
                onNavigateToMediaItem = navigator::navigate,
                sharedTransitionScope = sharedScope,
                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                viewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.Factory> { factory ->
                    factory.create(args)
                }
            )
        }
    }
}