package com.imashnake.animite.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.imashnake.animite.dev.internal.Path
import com.imashnake.animite.features.home.Home
import com.imashnake.animite.features.navigationbar.NavigationBar
import com.imashnake.animite.features.profile.Profile
import com.imashnake.animite.features.rslash.RSlash
import com.imashnake.animite.features.searchbar.SearchBar
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = false)
            }

            Box(modifier = Modifier.fillMaxSize()) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .navigationBarsPadding()
                ) {
                    composable(Path.Home.route) { Home(hiltViewModel()) }
                    composable(Path.Profile.route) { Profile() }
                    composable(Path.RSlash.route) { RSlash() }
                }

                SearchBar(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 80.dp, start = 24.dp, end = 24.dp)
                        .navigationBarsPadding()
                        .padding(bottom = 24.dp)
                )

                NavigationBar(
                    navController = navController,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(
                            80.dp + WindowInsets
                                .navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                        )
                )
            }
        }
    }
}
