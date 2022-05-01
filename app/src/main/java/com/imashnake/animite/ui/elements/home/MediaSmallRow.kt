package com.imashnake.animite.ui.elements.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imashnake.animite.PopularThisSeasonQuery
import com.imashnake.animite.TrendingNowQuery

// TODO: This is precisely why we need to convert the responses to our own data classes:
//  If we want to use `MediaSmallRow`, we need access to the list's item's content.
@Composable
fun MediaSmallRow(mediaList: List<Any>) {
    LazyRow {
        items(mediaList) {
            TODO()
        }
    }
}

/**
 * TODO:
 *  - Replace this with [MediaSmallRow] once we convert responses to custom data classes.
 *  - Extract dimens to `ui.theme`.
 */
@ExperimentalMaterial3Api
@Composable
fun TrendingNowAnimeSmallRow(mediaList: List<TrendingNowQuery.Medium?>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
    ) {
        items(mediaList) { media ->
            MediaSmall(
                image = media?.coverImage?.large,
                anime = media?.title?.romaji ?:
                        media?.title?.english ?:
                        media?.title?.native
            )
        }
    }
}

/**
 * TODO:
 *  - Replace this with [MediaSmallRow] once we convert responses to custom data classes.
 *  - Extract dimens to `ui.theme`.
 */
@ExperimentalMaterial3Api
@Composable
fun PopularAnimeThisSeasonSmallRow(mediaList: List<PopularThisSeasonQuery.Medium?>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
    ) {
        items(mediaList) { media ->
            MediaSmall(
                image = media?.coverImage?.large,
                anime = media?.title?.romaji ?:
                        media?.title?.english ?:
                        media?.title?.native
            )
        }
    }
}

/**
 * TODO: Replace this with `PreviewMediaSmallRow` once we convert responses to custom data classes.
 */
@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewTrendingNowAnimeSmallRow() {
    // No preview?
    //    ⠀⣞⢽⢪⢣⢣⢣⢫⡺⡵⣝⡮⣗⢷⢽⢽⢽⣮⡷⡽⣜⣜⢮⢺⣜⢷⢽⢝⡽⣝
    //    ⠸⡸⠜⠕⠕⠁⢁⢇⢏⢽⢺⣪⡳⡝⣎⣏⢯⢞⡿⣟⣷⣳⢯⡷⣽⢽⢯⣳⣫⠇
    //    ⠀⠀⢀⢀⢄⢬⢪⡪⡎⣆⡈⠚⠜⠕⠇⠗⠝⢕⢯⢫⣞⣯⣿⣻⡽⣏⢗⣗⠏⠀
    //    ⠀⠪⡪⡪⣪⢪⢺⢸⢢⢓⢆⢤⢀⠀⠀⠀⠀⠈⢊⢞⡾⣿⡯⣏⢮⠷⠁⠀⠀
    //    ⠀⠀⠀⠈⠊⠆⡃⠕⢕⢇⢇⢇⢇⢇⢏⢎⢎⢆⢄⠀⢑⣽⣿⢝⠲⠉⠀⠀⠀⠀
    //    ⠀⠀⠀⠀⠀⡿⠂⠠⠀⡇⢇⠕⢈⣀⠀⠁⠡⠣⡣⡫⣂⣿⠯⢪⠰⠂⠀⠀⠀⠀
    //    ⠀⠀⠀⠀⡦⡙⡂⢀⢤⢣⠣⡈⣾⡃⠠⠄⠀⡄⢱⣌⣶⢏⢊⠂⠀⠀⠀⠀⠀⠀
    //    ⠀⠀⠀⠀⢝⡲⣜⡮⡏⢎⢌⢂⠙⠢⠐⢀⢘⢵⣽⣿⡿⠁⠁⠀⠀⠀⠀⠀⠀⠀
    //    ⠀⠀⠀⠀⠨⣺⡺⡕⡕⡱⡑⡆⡕⡅⡕⡜⡼⢽⡻⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
    //    ⠀⠀⠀⠀⣼⣳⣫⣾⣵⣗⡵⡱⡡⢣⢑⢕⢜⢕⡝⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
    //    ⠀⠀⠀⣴⣿⣾⣿⣿⣿⡿⡽⡑⢌⠪⡢⡣⣣⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
    //    ⠀⠀⠀⡟⡾⣿⢿⢿⢵⣽⣾⣼⣘⢸⢸⣞⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
    //    ⠀⠀⠀⠀⠁⠇⠡⠩⡫⢿⣝⡻⡮⣒⢽⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
}
