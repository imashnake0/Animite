package com.imashnake.animite.search

import com.imashnake.animite.api.anilist.AnilistSearchRepository
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.mal.data.MyAnimeListSearchRepository
import com.imashnake.animite.search.db.SearchDao
import com.imashnake.animite.search.db.model.AniListSearch
import com.imashnake.animite.search.db.model.MyAnimeListSearch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val aniListSearch: AnilistSearchRepository,
    private val myAnimeListSearch: MyAnimeListSearchRepository,
    private val searchDao: SearchDao,
) {

    fun getSearchResults(query: String) = channelFlow {
        launch {
            searchDao.getCombinedSearchResult(query)
                .collect(::send)
        }

        fetchAndSaveAniList(query)
        fetchAndSaveMyAnimeList(query)
    }

    private suspend fun fetchAndSaveAniList(query: String) = coroutineScope {
        aniListSearch.fetchSearch(MediaType.ANIME, 50, query)
            .map { searchResults ->
                searchResults.map { searches ->
                    searches.map { result ->
                        AniListSearch(result.id, result.title.orEmpty())
                    }
                }
            }
            .collect {
                it.fold(
                    onSuccess = { results -> searchDao.insertAniListResults(results) },
                    onFailure = { error -> error.printStackTrace() }
                )
            }
    }

    private suspend fun fetchAndSaveMyAnimeList(query: String) = coroutineScope {
        runCatching {
            myAnimeListSearch.searchAnime(query, 50)
        }.map { results ->
            results.data.map { result -> MyAnimeListSearch(result.node.id, result.node.title.orEmpty()) }
        }.fold(
            onSuccess = { results -> searchDao.insertMyAnimeListResults(results) },
            onFailure = { error -> error.printStackTrace() }
        )
    }
}
