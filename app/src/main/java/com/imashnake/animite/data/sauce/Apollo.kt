package com.imashnake.animite.data.sauce

import com.apollographql.apollo3.ApolloClient
import com.imashnake.animite.dev.internal.Constants

/**
 * [Create an ApolloClient](https://www.apollographql.com/docs/kotlin/tutorial/04-execute-the-query)
 */
val client = ApolloClient.Builder()
    .serverUrl(Constants.ANILIST_BASE_URL)
    .build()
