package com.imashnake.animite.api.anilist

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class AnilistUserRepository @Inject constructor(
    @Named("authorized") private val apolloClient: ApolloClient
) {
    fun fetchViewer(): Flow<Result<ViewerQuery.Viewer>> {
        return apolloClient
            .query(ViewerQuery())
            .fetchPolicy(FetchPolicy.CacheAndNetwork).toFlow()
            .asResult { it.viewer!! }
    }
}
