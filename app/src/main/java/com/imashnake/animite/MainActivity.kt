package com.imashnake.animite

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
                apolloClient.query(ExampleListQuery(
                    search = Optional.presentIfNotNull("Attack On Titan"),
                    perPage = Optional.presentIfNotNull(20)
                )).execute()

            fun animeAtIndex(index: Int): ExampleListQuery.Medium? {
                return response.data?.page?.media?.get(index)
            }

            val animeList = mutableListOf<ExampleListQuery.Medium?>()

            for (i in 0..19) {
                animeList.add(animeAtIndex(i))
                Log.d(TAG, animeAtIndex(i)?.title?.native ?: "Null bro")
            }

            withContext(Dispatchers.Main) {
                setContent {
                    AnimeList(animeList = animeList)
                }
            }
        }
    }
}

@Composable
fun AnimeList(animeList: MutableList<ExampleListQuery.Medium?>) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
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
