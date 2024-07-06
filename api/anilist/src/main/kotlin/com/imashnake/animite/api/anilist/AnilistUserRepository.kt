package com.imashnake.animite.api.anilist

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.imashnake.animite.api.anilist.sanitize.profile.Viewer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository for anything user related. Including the [ViewerQuery.Viewer].
 *
 * @param apolloClient Client with the [`Authorization` header](https://anilist.gitbook.io/anilist-apiv2-docs/overview/oauth/implicit-grant#making-authenticated-requests).
 * @property fetchViewer Fetches the current user with an authorized [apolloClient].
 * @property fetchUserMediaList Fetches a chunked list of media associated with the user.
 */
class AnilistUserRepository @Inject constructor(
    @AuthorizedClient private val apolloClient: ApolloClient
) {
    fun fetchViewer(): Flow<Result<Viewer>> {
        return apolloClient
            .query(ViewerQuery())
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .toFlow()
            .asResult { Viewer(it.viewer?.user!!) }
    }

    /** @param id The id of the user. */
    fun fetchUserMediaList(id: Int?): Flow<Result<UserMediaListQuery.Data>> =
        apolloClient
            .query(UserMediaListQuery(Optional.presentIfNotNull(id)))
            .fetchPolicy(FetchPolicy.CacheFirst)
            .toFlow()
            .asResult()
}
