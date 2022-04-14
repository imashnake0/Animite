package com.imashnake.animite

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.imashnake.animite.dev.internal.Path
import com.imashnake.animite.ui.elements.Home
import com.imashnake.animite.ui.elements.Profile
import com.imashnake.animite.ui.elements.RSlash

private const val TAG = "MainActivity"

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
                    composable(Path.Home.route) { Home() }
                    composable(Path.Profile.route) { Profile() }
                    composable(Path.RSlash.route) { RSlash() }
                }

                NavigationBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    paths.forEachIndexed { index, item ->
                        NavigationBarItem(
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

                            label = {
                                Text(stringResource(id = item.stringRes))
                            },

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
                        )
                    }
                }
                // Spaghett
            }
        }
    }
}

/*
@Composable
fun AnimeList(animeList: MutableList<ExampleListQuery.Medium?>) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            // TODO: This is a hack, understand how layouts work and un-hardcode this.
            .padding(bottom = 80.dp)
    ) {
        for (i in 0..19) {
            Row {
                AsyncImage(
                    model = animeList[i]?.coverImage?.large,
                    contentDescription = animeList[i]?.title?.native,
                    modifier = Modifier
                )
                Text(
                    text = (animeList[i]?.title?.native ?: "Null bro"),
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.CenterVertically)
                        .absolutePadding(left = 30.dp),
                    fontSize = 16.sp
                )
            }
        }
    }
}
*/
