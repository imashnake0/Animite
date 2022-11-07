package com.imashnake.animite.features

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.imashnake.animite.R
import com.imashnake.animite.features.searchbar.SearchBar
import com.imashnake.animite.features.theme.AnimiteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : NodeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        setContent {
            AnimiteTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
                }

                Box(Modifier.fillMaxSize()) {
//                    DestinationsNavHost(
//                        navGraph = NavGraphs.root,
//                        modifier = Modifier
//                            .align(Alignment.TopCenter)
//                            .background(MaterialTheme.colorScheme.background)
//                            .fillMaxSize(),
//                        navController = navController,
//                        engine = navHostEngine
//                    )
                    NodeHost(integrationPoint = appyxIntegrationPoint) {
                        RootNode(it)
                    }

                    val searchBarBottomPadding: Dp by animateDpAsState(
                        targetValue = dimensionResource(R.dimen.large_padding) + if (
                            true
                        ) dimensionResource(R.dimen.navigation_bar_height) else 0.dp
                    )

                    SearchBar(
                        onItemClicked = { /* TODO */ },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                start = dimensionResource(R.dimen.large_padding),
                                end = dimensionResource(R.dimen.large_padding),
                                bottom = searchBarBottomPadding
                            )
                            .navigationBarsPadding()
                    )

                    AnimatedVisibility(
                        visible = true,
                        modifier = Modifier.align(Alignment.BottomCenter),
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it }
                    ) {
                        // NavigationBar(navController = navController)
                    }
                }
            }
        }
    }
}
