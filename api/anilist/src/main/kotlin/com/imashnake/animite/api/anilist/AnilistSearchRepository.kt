package com.imashnake.animite.api.anilist

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.imashnake.animite.api.anilist.sanitize.search.Search
import com.imashnake.animite.api.anilist.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class AnilistSearchRepository @Inject constructor(
    @Named("unauthorized") private val apolloClient: ApolloClient
) {
    fun fetchSearch(
        type: MediaType,
        perPage: Int,
        search: String?
    ): Flow<Result<List<Search>>> {
        return apolloClient
            .query(
                SearchQuery(
                    type = Optional.presentIfNotNull(type),
                    perPage = Optional.presentIfNotNull(perPage),
                    search = Optional.presentIfNotNull(search)
                )
            )
            .fetchPolicy(FetchPolicy.CacheAndNetwork).toFlow()
            .asResult { it.page!!.media.orEmpty().filterNotNull().map { query -> Search(query) } }
    }
}
