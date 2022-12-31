package com.imashnake.animite.data.sauce.apis.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.executeCacheAndNetwork
import com.imashnake.animite.MediaQuery
import com.imashnake.animite.data.sauce.apis.MediaApi
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Example:
 *
 * **Anime:** Sono Bisque Doll wa Koi wo Suru;
 * **ID:** 132405.
 */
class ApolloMediaApi @Inject constructor(
    private val apolloClient: ApolloClient
) : MediaApi {

    override fun fetchMedia(id: Int?, mediaType: MediaType): Flow<ApolloResponse<MediaQuery.Data>> {
        return apolloClient
            .query(
                MediaQuery(
                    id = Optional.presentIfNotNull(id),
                    type = Optional.presentIfNotNull(mediaType)
                )
            )
            .executeCacheAndNetwork()
    }
}
