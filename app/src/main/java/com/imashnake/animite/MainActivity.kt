package com.imashnake.animite

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
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

            fun animeAtIndex(index: Int): String {
                return response.data?.page?.media?.get(index)?.title?.native.toString()
            }

            var animeList = ""
            for (i in 0..19) {
                animeList += animeAtIndex(i) + "\n"
                Log.d(TAG, animeAtIndex(i))
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
fun AnimeList(animeList: String) {
    Text(text = animeList, modifier = Modifier.padding(30.dp))
}