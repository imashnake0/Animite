package com.imashnake.animite.data.sauce.apis.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.executeCacheAndNetwork
import com.imashnake.animite.SearchQuery
import com.imashnake.animite.data.sauce.apis.SearchApi
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApolloSearchApi @Inject constructor(
    private val apolloClient: ApolloClient
) : SearchApi {
    override fun search(
        type: MediaType,
        perPage: Int,
        search: String?
    ): Flow<ApolloResponse<SearchQuery.Data>> {
        return apolloClient
            .query(
                SearchQuery(
                    type = Optional.presentIfNotNull(type),
                    perPage = Optional.presentIfNotNull(perPage),
                    search = Optional.presentIfNotNull(search)
                )
            ).executeCacheAndNetwork()
    }
}
