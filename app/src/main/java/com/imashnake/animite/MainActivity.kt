package com.imashnake.animite

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.imashnake.animite.ui.theme.AnimiteTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://graphql.anilist.co/")
            .build()

        // TODO:
        //  See how printing a list of these entries would work; handle errors.
        //  Make better use of [coroutines](https://kotlinlang.org/docs/coroutines-guide.html)
        GlobalScope.launch(Dispatchers.IO) {
            val response = apolloClient.query(ExampleQuery(id = Optional.presentIfNotNull(1))).execute()
            Log.d(TAG, response.data.toString())
            Log.d(TAG, response.data?.media?.id.toString())
            Log.d(TAG, response.data?.media?.title?.english.toString())
            Log.d(TAG, response.data?.media?.title?.native.toString())
            Log.d(TAG, response.data?.media?.title?.romaji.toString())
        }

        setContent {
            AnimiteTheme {
                Text("Hello")
            }
        }
    }
}