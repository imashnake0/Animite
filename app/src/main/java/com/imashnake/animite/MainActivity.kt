package com.imashnake.animite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.imashnake.animite.ui.theme.AnimiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://graphql.anilist.co/")
            .build()

        val response = apolloClient.query(ExampleQuery(id = Optional.presentIfNotNull(30)))

        setContent {
            AnimiteTheme {
                Text("Hello")
            }
        }
    }
}