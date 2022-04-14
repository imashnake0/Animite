package com.imashnake.animite.ui.elements

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.ui.state.HomeViewModel

private const val TAG = "HomeComposable"

/**
 * TODO:
 *  - Kdoc.
 *  - This doesn't work as expected since there is a disconnect between `AnimeRepository` and
 *  `AnimeNetworkSource` (and others) because dependencies are not injected properly.
 */
@Composable
fun Home(
    viewModel: HomeViewModel = viewModel()
) {
    Text(text = "Home")

    viewModel.fetchAnime(132405)
    val animeList = viewModel.uiState.animeList

    Log.d(TAG, animeList[0]?.id.toString())
    Log.d(TAG, animeList[0]?.type.toString())
    Log.d(TAG, animeList[0]?.title?.english.toString())
    Log.d(TAG, animeList[0]?.title?.native.toString())
    Log.d(TAG, animeList[0]?.title?.romaji.toString())
    Log.d(TAG, animeList[0]?.coverImage?.large.toString())
    Log.d(TAG, animeList[0]?.coverImage?.extraLarge.toString())
}
