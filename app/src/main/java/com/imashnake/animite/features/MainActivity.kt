package com.imashnake.animite.features

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.features.media.MediaPageArgs
import com.imashnake.animite.features.navigationbar.NavigationBar
import com.imashnake.animite.features.searchbar.SearchFrontDrop
import com.imashnake.animite.features.theme.AnimiteTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.utils.navGraph
import com.ramcosta.composedestinations.utils.route
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
                MainScreen(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navHostEngine = rememberNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
            enterTransition = { fadeIn(animationSpec = tween(1000)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
        )
    )
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isNavBarVisible = remember(currentBackStackEntry) {
        currentBackStackEntry?.navGraph()?.startRoute == currentBackStackEntry?.route()
    }

    // TODO: Refactor to use Scaffold once AnimatedVisibility issues are fixed;
    //  see https://issuetracker.google.com/issues/258270139.
    Box(modifier) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onBackground
        ) {
            DestinationsNavHost(
                navGraph = RootNavGraph,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                navController = navController,
                engine = navHostEngine
            )
        }

        SearchFrontDrop(
            hasExtraPadding = isNavBarVisible,
            onItemClick = { id, mediaType ->
                navController.navigate(
                    MediaPageDestination(
                        MediaPageArgs(
                            id,
                            mediaType.rawValue
                        )
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
