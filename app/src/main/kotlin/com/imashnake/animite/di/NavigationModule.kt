package com.imashnake.animite.di

import com.imashnake.animite.navigation.Navigator
import com.imashnake.animite.navigation.Root
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object NavigationModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNavigator() = Navigator(Root.AnimeRoute)
}