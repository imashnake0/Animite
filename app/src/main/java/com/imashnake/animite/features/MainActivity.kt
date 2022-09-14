package com.imashnake.animite.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.imashnake.animite.R
import com.imashnake.animite.dev.ext.bottomNavigationBarSize
import com.imashnake.animite.features.navigationbar.NavigationBar
import com.imashnake.animite.features.navigationbar.NavigationBarPaths
import com.imashnake.animite.features.searchbar.SearchBar
import com.imashnake.animite.features.theme.AnimiteTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialNavigationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AnimiteTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
                }

                val navController = rememberAnimatedNavController()
                val navHostEngine = rememberAnimatedNavHostEngine(
                    rootDefaultAnimations = RootNavGraphDefaultAnimations(
                        enterTransition = { fadeIn(animationSpec = tween(1000)) },
                        exitTransition = { fadeOut(animationSpec = tween(300)) },
                    )
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .background(MaterialTheme.colorScheme.background),
                        navController = navController,
                        engine = navHostEngine
                    )

                    val searchBarBottomPadding: Dp by animateDpAsState(
                        targetValue = dimensionResource(R.dimen.large_padding) + if (
                            NavigationBarPaths.values().any {
                                it.direction == navController.appCurrentDestinationAsState().value
                            }
                        ) bottomNavigationBarSize else 0.dp
                    )

                    SearchBar(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                start = dimensionResource(R.dimen.large_padding),
                                end = dimensionResource(R.dimen.large_padding),
                                bottom = searchBarBottomPadding
                            )
                            .navigationBarsPadding(),
                        navController = navController
                    )

                    AnimatedVisibility(
                        visible = NavigationBarPaths.values().any {
                            it.direction == navController.appCurrentDestinationAsState().value
                        },
                        modifier = Modifier.align(Alignment.BottomCenter),
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it }
                    ) {
                        NavigationBar(navController = navController)
                    }
                }
            }
        }
    }
}
