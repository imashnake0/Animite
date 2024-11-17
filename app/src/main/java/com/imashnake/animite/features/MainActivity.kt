package com.imashnake.animite.features

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.features.home.HomeScreen
import com.imashnake.animite.features.media.MediaPage
import com.imashnake.animite.features.searchbar.SearchFrontDrop
import com.imashnake.animite.features.theme.AnimiteTheme
import com.imashnake.animite.navigation.HomeRoute
import com.imashnake.animite.navigation.NavigationBar
import com.imashnake.animite.navigation.NavigationBarPaths
import com.imashnake.animite.navigation.ProfileRoute
import com.imashnake.animite.navigation.SocialRoute
import com.imashnake.animite.profile.ProfileScreen
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_DEEPLINK
import com.imashnake.animite.social.SocialScreen
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

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // TODO: Refactor to use Scaffold once AnimatedVisibility issues are fixed;
    //  see https://issuetracker.google.com/issues/258270139.
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0)
    ) {
        Box(Modifier.padding(it)) {
            SharedTransitionLayout {
                NavHost(navController = navController, startDestination = HomeRoute) {
                    composable<HomeRoute> {
                        HomeScreen(
                            navController = navController,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this,
                        )
                    }
                    composable<MediaPage> {
                        MediaPage(
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this,
                        )
                    }
                    composable<ProfileRoute>(
                        deepLinks = listOf(
                            navDeepLink { uriPattern = ANILIST_AUTH_DEEPLINK }
                        )
                    ) {
                        ProfileScreen()
                    }
                    composable<SocialRoute> {
                        SocialScreen()
                    }
                }
            }

            SearchFrontDrop(
                onItemClick = { id, mediaType ->
                    navController.navigate(
                        MediaPage(
                            id = id,
                            source = MediaList.Type.SEARCH.name,
                            mediaType.rawValue
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        start = LocalPaddings.current.large,
                        end = LocalPaddings.current.large,
                        bottom = LocalPaddings.current.large
                    )
            )
        }
    }
}
