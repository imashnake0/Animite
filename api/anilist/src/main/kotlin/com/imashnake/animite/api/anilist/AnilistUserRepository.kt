package com.imashnake.animite.api.anilist

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository for anything user related. Including the [ViewerQuery.Viewer].
 *
 * @param apolloClient Client with the [`Authorization` header](https://anilist.gitbook.io/anilist-apiv2-docs/overview/oauth/implicit-grant#making-authenticated-requests).
 * @property fetchViewer Fetches the current user with an authorized [apolloClient].
 */
class AnilistUserRepository @Inject constructor(
    @AuthorizedClient private val apolloClient: ApolloClient
) {
    fun fetchViewer(): Flow<Result<ViewerQuery.Viewer>> {
        return apolloClient
            .query(ViewerQuery())
            .fetchPolicy(FetchPolicy.CacheFirst)
            .toFlow()
            .asResult { it.viewer!! }
    }
}
