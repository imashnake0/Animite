package com.imashnake.animite.data

import com.imashnake.animite.data.repos.AnimeRepository
import com.imashnake.animite.data.sauce.AnimeNetworkSource
import com.imashnake.animite.data.sauce.ApolloAnimeApi
import com.imashnake.animite.ui.state.HomeViewModelFactory
import kotlinx.coroutines.Dispatchers

/**
 * TODO: Kdoc.
 */
class Container {
    private val animeNetworkSource: AnimeNetworkSource = AnimeNetworkSource(ApolloAnimeApi(), Dispatchers.IO)
    val animeRepository: AnimeRepository = AnimeRepository(animeNetworkSource)

    val homeViewModelFactory = HomeViewModelFactory(animeRepository)
}


