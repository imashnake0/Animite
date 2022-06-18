package com.imashnake.animite.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.imashnake.animite.dev.internal.Path
import com.imashnake.animite.ui.elements.home.CollapsedSearchBar
import com.imashnake.animite.ui.elements.home.ExpandedSearchBar
import com.imashnake.animite.ui.elements.home.Home
import com.imashnake.animite.ui.elements.home.SearchList
import com.imashnake.animite.ui.elements.profile.Profile
import com.imashnake.animite.ui.elements.rslash.RSlash
import com.imashnake.animite.ui.theme.NavigationBar
import com.imashnake.animite.ui.theme.NavigationItem
import dagger.hilt.android.AndroidEntryPoint
import com.imashnake.animite.R as Res

private const val TAG = "MainActivity"

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

                // We iterate through the list in this order.
                val paths = listOf(
                    Path.RSlash,
                    Path.Home,
                    Path.Profile
                )

                Log.d(TAG, Path.numberOfPaths.toString())

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

                // TODO:
                //  - UX concern: This blocks content sometimes!
                //  - Should this be in another file?
                var isExpanded by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier.align(Alignment.BottomEnd)
                        .padding(bottom = 80.dp, start = 24.dp, end = 24.dp)
                        .navigationBarsPadding()
                        .padding(bottom = 24.dp)
                ) {
                    // TODO: Customize this animation.
                    AnimatedContent(targetState = isExpanded) { targetExpanded ->
                        if (targetExpanded) {
                            SearchList(
                                viewModel = hiltViewModel(),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(NavigationBar.copy(alpha = 0.95F))
                                    .align(Alignment.End)
                                    .fillMaxWidth()
                            )
                        }
                    }

                    Surface(
                        color = NavigationBar,
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier
                            .align(Alignment.End)
                            .wrapContentSize(),
                        shadowElevation = 20.dp,
                        shape = CircleShape
                    ) {
                        // TODO: Customize this animation.
                        AnimatedContent(targetState = isExpanded) { targetExpanded ->
                            if (targetExpanded) {
                                ExpandedSearchBar(hiltViewModel())
                            } else {
                                CollapsedSearchBar()
                            }
                        }
                    }
                }

                // TODO: The way padding is handled is still a bit hacky, investigate further.
                NavigationBar(
                    containerColor = NavigationBar,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(
                            80.dp +
                                    WindowInsets
                                        .navigationBars
                                        .asPaddingValues()
                                        .calculateBottomPadding()
                        )
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    paths.forEachIndexed { index, item ->
                        NavigationBarItem(
                            modifier = Modifier.navigationBarsPadding(),

                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,

                            onClick = {
                                Log.d(TAG, "index: $index; item: $item")

                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },

                            icon = {
                                when (index) {
                                    // TODO: Potentially "convert" icons to compose.
                                    0 -> {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(
                                                id = Res.drawable.rslash
                                            ),
                                            contentDescription = stringResource(
                                                id = item.stringRes
                                            )
                                        )
                                    }
                                    1 -> {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(
                                                id = Res.drawable.home
                                            ),
                                            contentDescription = stringResource(
                                                id = item.stringRes
                                            )
                                        )
                                    }
                                    2 -> {
                                        Icon(
                                            imageVector = Icons.Rounded.AccountCircle,
                                            contentDescription = stringResource(
                                                id = item.stringRes
                                            ),
                                            // Adding this modifier lets us control the icon's size.
                                            modifier = Modifier
                                                .padding((3).dp)
                                                .size(18.dp)
                                        )
                                    }
                                }
                            },

                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NavigationBar,
                                unselectedIconColor = NavigationItem,
                                indicatorColor = NavigationItem
                            )
                        )
                    }
                }
            }
        }
    }
}
