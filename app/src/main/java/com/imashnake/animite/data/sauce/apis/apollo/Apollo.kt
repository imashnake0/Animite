package com.imashnake.animite.data.sauce.apis.apollo

import com.apollographql.apollo3.ApolloClient
import com.imashnake.animite.dev.internal.Constants
import javax.inject.Singleton

/**
 * [Create an ApolloClient](https://www.apollographql.com/docs/kotlin/tutorial/04-execute-the-query)
 */
@Singleton
val client = ApolloClient.Builder()
    .serverUrl(Constants.ANILIST_BASE_URL)
    .build()
