package com.imashnake.animite.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.imashnake.animite.dev.internal.Path
import com.imashnake.animite.ui.elements.home.Home
import com.imashnake.animite.ui.elements.profile.Profile
import com.imashnake.animite.ui.elements.rslash.RSlash
import com.imashnake.animite.ui.theme.NavigationBar
import com.imashnake.animite.ui.theme.NavigationItem
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
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
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    composable(Path.Home.route) { Home(hiltViewModel()) }
                    composable(Path.Profile.route) { Profile() }
                    composable(Path.RSlash.route) { RSlash() }
                }

                NavigationBar(
                    containerColor = NavigationBar,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(80.dp)
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    paths.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,

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
                                    0 -> {
                                        Icon(
                                            imageVector = Icons.Rounded.List,
                                            contentDescription = stringResource(id = item.stringRes)
                                        )
                                    }
                                    1 -> {
                                        Icon(
                                            imageVector = Icons.Rounded.Home,
                                            contentDescription = stringResource(id = item.stringRes)
                                        )
                                    }
                                    2 -> {
                                        Icon(
                                            imageVector = Icons.Rounded.Person,
                                            contentDescription = stringResource(id = item.stringRes)
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


