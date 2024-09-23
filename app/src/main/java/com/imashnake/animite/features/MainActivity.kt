package com.imashnake.animite.features

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.features.home.Home
import com.imashnake.animite.features.home.HomeScreen
import com.imashnake.animite.features.media.MediaPage
import com.imashnake.animite.features.navigationbar.NavigationBar
import com.imashnake.animite.features.navigationbar.NavigationBarPaths
import com.imashnake.animite.features.searchbar.SearchFrontDrop
import com.imashnake.animite.features.theme.AnimiteTheme
import com.imashnake.animite.profile.Profile
import com.imashnake.animite.profile.ProfileScreen
import com.imashnake.animite.profile.dev.internal.ACCESS_TOKEN
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_DEEPLINK
import com.imashnake.animite.profile.dev.internal.EXPIRES_IN
import com.imashnake.animite.profile.dev.internal.TOKEN_TYPE
import com.imashnake.animite.rslash.RSlash
import com.imashnake.animite.rslash.RSlashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        setContent {
            AnimiteTheme {
                MainScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isNavBarVisible = remember(currentBackStackEntry) {
        if (currentBackStackEntry != null) {
            NavigationBarPaths.entries.any { it.matchesDestination(currentBackStackEntry!!) }
        } else {
            false
        }
    }

    // TODO: Refactor to use Scaffold once AnimatedVisibility issues are fixed;
    //  see https://issuetracker.google.com/issues/258270139.
    Box(modifier) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onBackground
        ) {
            NavHost(navController = navController, startDestination = Home) {
                composable<Home> {
                    HomeScreen(
                        onNavigateToMediaItem = {
                            navController.navigate(it)
                        }
                    )
                }
                composable<MediaPage> {
                    MediaPage()
                }
                composable<Profile>(
                    deepLinks = listOf(
                        navDeepLink { uriPattern = ANILIST_AUTH_DEEPLINK }
                    )
                ) { backStackEntry ->
                    ProfileScreen(
                        accessToken = backStackEntry.arguments?.getString(ACCESS_TOKEN),
                        tokenType = backStackEntry.arguments?.getString(TOKEN_TYPE),
                        expiresIn = backStackEntry.arguments?.getString(EXPIRES_IN)?.toIntOrNull()
                    )
                }
                composable<RSlash> {
                    RSlashScreen()
                }
            }
        }

        SearchFrontDrop(
            hasExtraPadding = isNavBarVisible,
            onItemClick = { id, mediaType ->
                navController.navigate(MediaPage(id = id, mediaType.rawValue))
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    start = LocalPaddings.current.large,
                    end = LocalPaddings.current.large,
                    bottom = LocalPaddings.current.large
                )
        )

        AnimatedVisibility(
            visible = isNavBarVisible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            NavigationBar(navController = navController)
        }
    }
}
