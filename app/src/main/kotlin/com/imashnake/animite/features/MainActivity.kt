package com.imashnake.animite.features

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.RoundedCorner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.imashnake.animite.BuildConfig
import com.imashnake.animite.anime.AnimeScreen
import com.imashnake.animite.features.theme.AnimiteTheme
import com.imashnake.animite.manga.MangaScreen
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.navigation.AnimeRoute
import com.imashnake.animite.navigation.MangaRoute
import com.imashnake.animite.navigation.NavigationBar
import com.imashnake.animite.navigation.NavigationBarPaths
import com.imashnake.animite.navigation.NavigationRail
import com.imashnake.animite.navigation.ProfileRoute
import com.imashnake.animite.navigation.SocialRoute
import com.imashnake.animite.profile.ProfileScreen
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_DEEPLINK
import com.imashnake.animite.settings.SettingsPage
import com.imashnake.animite.settings.SettingsViewModel
import com.imashnake.animite.settings.Theme
import com.imashnake.animite.social.SocialScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val theme by settingsViewModel.theme
                .filterNotNull()
                .collectAsState(initial = Theme.DEVICE_THEME.name)

            val useSystemColorScheme by settingsViewModel.useSystemColorScheme
                .filterNotNull()
                .collectAsState(initial = false)

            val useDarkTheme = when(Theme.valueOf(theme)) {
                Theme.DARK -> true
                Theme.LIGHT -> false
                Theme.DEVICE_THEME -> isSystemInDarkTheme()
            }

            val dayHour by settingsViewModel.dayHour.collectAsState(initial = null)

            AnimiteTheme(
                useDarkTheme = useDarkTheme,
                useSystemColorScheme = useSystemColorScheme,
                dayHour = dayHour
            ) {
                MainScreen(
                    deviceScreenCornerRadius = getDeviceScreenCornerRadius(),
                    useDarkTheme = useDarkTheme,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }

    private fun getDeviceScreenCornerRadius(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return windowManager
                .currentWindowMetrics
                .windowInsets
                .getRoundedCorner(RoundedCorner.POSITION_TOP_RIGHT)
                ?.radius
                ?: 0
        }
        return 0
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
    deviceScreenCornerRadius: Int,
    useDarkTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val windowSizeClass = currentWindowAdaptiveInfo(true).windowSizeClass

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isNavBarVisible = rememberSaveable(currentBackStackEntry) {
        if (currentBackStackEntry != null) {
            NavigationBarPaths.entries.any {
                it.matchesDestination(currentBackStackEntry!!)
            }
        } else false
    }

    // TODO: Refactor to use Scaffold once AnimatedVisibility issues are fixed;
    //  see https://issuetracker.google.com/issues/258270139.
    Box(modifier) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onBackground
        ) {
            SharedTransitionLayout {
                NavHost(navController = navController, startDestination = AnimeRoute) {
                    composable<AnimeRoute> {
                        AnimeScreen(
                            windowSizeClass = windowSizeClass,
                            onNavigateToMediaItem = navController::navigate,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this,
                        )
                    }
                    composable<MangaRoute> {
                        MangaScreen(
                            onNavigateToMediaItem = navController::navigate,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this,
                        )
                    }
                    composable<MediaPage> {
                        MediaPage(
                            onBack = navController::navigateUp,
                            onNavigateToMediaItem = navController::navigate,
                            useDarkTheme = useDarkTheme,
                            deviceScreenCornerRadius = deviceScreenCornerRadius,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this,
                        )
                    }
                    composable<ProfileRoute>(
                        deepLinks = listOf(
                            navDeepLink {
                                uriPattern = ANILIST_AUTH_DEEPLINK
                                action = Intent.ACTION_VIEW
                            }
                        )
                    ) {
                        ProfileScreen(
                            onNavigateToMediaItem = navController::navigate,
                            onNavigateToSettings = navController::navigate,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this,
                        )
                    }
                    composable<SettingsPage> {
                        SettingsPage(versionName = BuildConfig.VERSION_NAME)
                    }
                    composable<SocialRoute> {
                        SocialScreen()
                    }
                }
            }
        }

        when(LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                AnimatedVisibility(
                    visible = isNavBarVisible,
                    modifier = Modifier.align(Alignment.CenterStart),
                    enter = slideInHorizontally { -it },
                    exit = slideOutHorizontally { -it }
                ) { NavigationRail(navController = navController) }
            }
            else -> {
                AnimatedVisibility(
                    visible = isNavBarVisible,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) { NavigationBar(navController = navController) }
            }
        }
    }
}
