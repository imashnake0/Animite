package com.imashnake.animite.features.media

import android.text.Html
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.imashnake.animite.R
import com.imashnake.animite.dev.ext.toHexColor
import com.imashnake.animite.features.theme.*
import com.imashnake.animite.type.MediaType
import com.ramcosta.composedestinations.annotation.Destination

@ExperimentalMaterial3Api
@Destination
@Composable
fun MediaPage(
    id: Int?,
    mediaTypeArg: String,
    viewModel: MediaPageViewModel = hiltViewModel()
) {
    val mediaType = MediaType.safeValueOf(mediaTypeArg)
    viewModel.populateMediaPage(id, mediaType)

    val media = viewModel.uiState

    // TODO: [Add shimmer](https://google.github.io/accompanist/placeholder/)
    Box(Modifier.verticalScroll(rememberScrollState())) {
        // TODO: How do I align this?
        if (!media.bannerImage.isNullOrEmpty()) {
            Box {

                AsyncImage(
                    model = media.bannerImage,
                    contentDescription = "Banner Image",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .height(168.dp)
                        .align(Alignment.TopStart)
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(168.dp),
                    color = Color(media.color?.toHexColor() ?: 0).copy(alpha = 0.35f)
                ) { }
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(168.dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 150.dp)
                .clip(backdropShape)
                .background(Backdrop)
        ) {
            Column(
                Modifier.background(
                    Brush.verticalGradient(
                        listOf(
                            Color(media.color?.toHexColor() ?: 0).copy(
                                alpha = 0.08f
                            ), Color.Transparent
                        )
                    )
                )
            ) {
                Row {
                    Spacer(modifier = Modifier.width((115 + 24).dp))

                    Column(
                        modifier = Modifier
                            .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                            .height(
                                (88 + WindowInsets.statusBars
                                    .asPaddingValues()
                                    .calculateTopPadding().value).dp
                            )
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = media.title ?: "",
                            color = Text,
                            fontSize = 14.sp,
                            fontFamily = manropeFamily,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            // TODO: Some styles are not applied.
                            text = Html.fromHtml(media.description, Html.FROM_HTML_MODE_COMPACT)
                                .toString(),
                            color = Text.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontFamily = manropeFamily,
                            fontWeight = FontWeight.Medium,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = if (media.ranks.size >= 2) Arrangement.SpaceAround else Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Stat(label = "SCORE", score = media.averageScore ?: 0) {
                                "$it%"
                            }

                            for (stat in media.ranks) {
                                Stat(label = stat.first, score = stat.second) {
                                    "#$it"
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
                ) {
                    if (!media.genres.isNullOrEmpty()) {
                        items(media.genres) { genre ->
                            Genre(
                                genre = genre,
                                color = Color(media.color?.toHexColor() ?: 0xFF152232)
                            )
                        }
                    }
                }
            }

            Text(
                text = """
                characters: ${media.characters}
                trailer: ${media.trailer}
            """.trimIndent(),
                color = Text,
                modifier = Modifier.padding(
                    top = 50.dp,
                    start = 50.dp,
                    end = 50.dp,
                    bottom = 300.dp
                )
            )
        }

        Card(
            modifier = Modifier
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                .statusBarsPadding()
                .wrapContentHeight()
                .width(115.dp),
            shape = mediaSmallShape
        ) {
            AsyncImage(
                model = media.coverImage,
                contentDescription = media.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(238.dp)
                    .clip(mediaSmallShape)
            )
        }
    }
}

@Composable
fun Stat(label: String, score: Int, format: (Int) -> String) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = Text,
            fontSize = 10.sp,
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = format(score),
            color = Text,
            fontSize = 24.sp,
            fontFamily = manropeFamily,
            fontWeight = FontWeight.Bold
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun Genre(genre: String?, color: Color) {

    SuggestionChip(
        label = {
            Text(
                text = genre?.lowercase() ?: "",
                color = Text,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        },
        onClick = {  },
        shape = CircleShape,
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = color.copy(alpha = 0.25f)
        ),
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderColor = Color.Transparent
        )
    )
}
