package com.imashnake.animite.api.mal.data

import com.imashnake.animite.api.mal.data.model.AnimeSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.encodeURLParameter
import javax.inject.Inject

class MyAnimeListSearchRepository @Inject constructor(
    private val ktor: HttpClient
) {
    companion object {
        private const val CLIENT_AUTH_HEADER = "X-MAL-CLIENT-ID"
        private const val CLIENT_ID = "[redacted]"
        private const val ALL_SEARCH_FIELDS =
            "id,title,main_picture,alternative_titles,start_date,end_date,synopsis,mean,rank,popularity,num_list_users,num_scoring_users,nsfw,created_at,updated_at,media_type,status,genres,my_list_status,num_episodes,start_season,broadcast,source,average_episode_duration,rating,pictures,background,related_anime,related_manga,recommendations,studios,statistics"
    }

    suspend fun searchAnime(
        query: String,
        limit: Int = 50
    ): AnimeSearchResponse {
        val encodedSearchQuery = query.encodeURLParameter(true)
        return ktor.get("anime?q=${encodedSearchQuery}&limit=$limit&fields=$ALL_SEARCH_FIELDS") {
            accept(ContentType.Application.Json)
            headers.append(CLIENT_AUTH_HEADER, CLIENT_ID)
        }.body()
    }
}
