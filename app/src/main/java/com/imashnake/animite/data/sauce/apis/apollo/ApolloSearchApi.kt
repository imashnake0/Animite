package com.imashnake.animite.data.sauce.apis.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.imashnake.animite.SearchQuery
import com.imashnake.animite.data.sauce.apis.SearchApi
import com.imashnake.animite.type.MediaType
import javax.inject.Inject

class ApolloSearchApi @Inject constructor(
    private val apolloClient: ApolloClient
) : SearchApi {
    override suspend fun search(
        type: MediaType,
        perPage: Int,
        search: String
    ): SearchQuery.Page? {
        return apolloClient
            .query(
                SearchQuery(
                    type = Optional.presentIfNotNull(type),
                    perPage = Optional.presentIfNotNull(perPage),
                    search = Optional.presentIfNotNull(search)
                )
            ).execute().data?.page
    }
}
