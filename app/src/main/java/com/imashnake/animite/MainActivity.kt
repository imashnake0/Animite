package com.imashnake.animite

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MainActivity"

// TODO: Separate logic and UI using proper app architecture.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://graphql.anilist.co/")
            .build()

        lifecycleScope.launch(Dispatchers.IO) {
            val response =
                apolloClient.query(
                    ExampleListQuery(
                        search = Optional.presentIfNotNull("Attack On Titan"),
                        perPage = Optional.presentIfNotNull(20)
                    )
                ).execute()

            withContext(Dispatchers.Main) {
                fun animeAtIndex(index: Int): ExampleListQuery.Medium? {
                    return response.data?.page?.media?.get(index)
                }

                val animeList = mutableListOf<ExampleListQuery.Medium?>()

                for (i in 0..19) {
                    animeList.add(animeAtIndex(i))
                    Log.d(TAG, animeAtIndex(i)?.title?.native ?: "Null bro")
                }

                setContent {
                    Box {
                        AnimeList(animeList)
                        // AnimeList(animeList)

                        NavigationBar(Modifier.align(Alignment.BottomCenter)) {
                            var selectedItem by remember { mutableStateOf(0) }
                            val pages = listOf("Subreddit", "Home", "Profile")

                            pages.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        when (index) {
                                            0 -> {
                                                Icon(
                                                    Icons.Rounded.List,
                                                    contentDescription = item
                                                )
                                            }
                                            1 -> {
                                                Icon(
                                                    Icons.Rounded.Home,
                                                    contentDescription = item
                                                )
                                            }
                                            2 -> {
                                                Icon(
                                                    Icons.Rounded.Person,
                                                    contentDescription = item
                                                )
                                            }
                                        }
                                    },

                                    label = {
                                        Text(item)
                                    },

                                    selected = selectedItem == index,

                                    onClick = {
                                        selectedItem = index
                                        Log.d(TAG, "index: $index; item: $item")
                                    }
                                )
                                // Spaghett
                            }
                        }
                    }
                }
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
