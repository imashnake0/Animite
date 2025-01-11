package com.imashnake.animite.search

import com.imashnake.animite.api.anilist.AnilistSearchRepository
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.mal.data.MyAnimeListSearchRepository
import com.imashnake.animite.search.db.SearchDao
import com.imashnake.animite.search.db.model.AniListSearch
import com.imashnake.animite.search.db.model.MyAnimeListSearch
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

        launch {
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
                        onSuccess = { searchDao.insertAniListResults(it) },
                        onFailure = { it.printStackTrace() }
                    )
                }
        }

        launch {
            runCatching {
                myAnimeListSearch.searchAnime(query, 50)
            }.map {
                it.data.map { MyAnimeListSearch(it.node.id, it.node.title.orEmpty()) }
            }.fold(
                onSuccess = { searchDao.insertMyAnimeListResults(it) },
                onFailure = { it.printStackTrace() }
            )
        }
    }
}
