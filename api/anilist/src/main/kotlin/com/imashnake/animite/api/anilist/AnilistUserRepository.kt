package com.imashnake.animite.api.anilist

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.fetchPolicy
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.api.anilist.type.MediaListOptionsInput
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.anilist.type.ScoreFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

/**
 * Repository for anything user related. Including the [ViewerQuery.Viewer].
 *
 * @param apolloClient Client with the [`Authorization` header](https://anilist.gitbook.io/anilist-apiv2-docs/overview/oauth/implicit-grant#making-authenticated-requests).
 * @property fetchViewer Fetches the current user with an authorized [apolloClient].
 * @property fetchUserMediaList Fetches a chunked list of media associated with the user.
 */
class AnilistUserRepository(
    private val apolloClient: ApolloClient
) {
    fun fetchViewer(
        useNetwork: Boolean,
        language: Media.Language = Media.Language.DEFAULT,
    ): Flow<Result<User>> {
        return apolloClient.query(ViewerQuery())
            .fetchPolicy(
                fetchPolicy = if (useNetwork) {
                    FetchPolicy.NetworkFirst
                } else FetchPolicy.CacheFirst
            )
            .toFlow()
            .filter { it.exception == null }
            .asResult { User(it.viewer?.user!!, language) }
    }

    /** @param id The id of the user. */
    fun fetchUserMediaList(
        id: Int?,
        type: MediaType?,
        useNetwork: Boolean,
        language: Media.Language = Media.Language.DEFAULT,
        scoreFormat: ScoreFormat?,
        mediaListOrder: List<String>
    ): Flow<Result<User.MediaCollection>> {
        return apolloClient.query(
            UserMediaListQuery(
                userId = Optional.presentIfNotNull(id),
                type = Optional.presentIfNotNull(type),
                scoreFormat = Optional.presentIfNotNull(scoreFormat)
            )
        )
        .fetchPolicy(
            fetchPolicy = if (useNetwork) {
                FetchPolicy.NetworkFirst
            } else FetchPolicy.CacheFirst
        )
        .toFlow()
        .filter { it.exception == null }
        .asResult { User.MediaCollection(it, type, language, mediaListOrder) }
    }

    fun updateUser(
        profileColor: String? = null,
        scoreFormat: ScoreFormat? = null,
        animeSectionOrder: List<String>? = null,
        mangaSectionOrder: List<String>? = null,
    ): Flow<Result<UpdateUserMutation.UpdateUser?>> {
        return apolloClient
            .mutation(
                UpdateUserMutation(
                    profileColor = Optional.presentIfNotNull(profileColor),
                    scoreFormat = Optional.presentIfNotNull(scoreFormat),
                    animeListOptions = Optional.presentIfNotNull(
                        MediaListOptionsInput(
                            sectionOrder = Optional.presentIfNotNull(animeSectionOrder)
                        )
                    ),
                    mangaListOptions = Optional.presentIfNotNull(
                        MediaListOptionsInput(
                            sectionOrder = Optional.presentIfNotNull(mangaSectionOrder)
                        )
                    )
                )
            )
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .toFlow()
            .asResult { it.UpdateUser }
    }
}
