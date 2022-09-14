package com.imashnake.animite.features.media

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.imashnake.animite.dev.ext.toHexColor
import com.imashnake.animite.features.theme.mediaSmallShape
import com.imashnake.animite.type.MediaType
import com.ramcosta.composedestinations.annotation.Destination
import com.imashnake.animite.R as Res

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
    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        if (!media.bannerImage.isNullOrEmpty()) {
            Box {
                AsyncImage(
                    model = media.bannerImage,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.height(
                        dimensionResource(Res.dimen.banner_height)
                    ),
                    alignment = Alignment.Center
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            dimensionResource(Res.dimen.banner_height)
                        ),
                    color = Color(media.color?.toHexColor() ?: 0).copy(alpha = 0.2f)
                ) { }
            }
        } else {
            Image(
                painter = painterResource(Res.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        dimensionResource(Res.dimen.banner_height)
                    ),
                alignment = Alignment.TopCenter
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    top = dimensionResource(Res.dimen.banner_height)
                            - dimensionResource(Res.dimen.backdrop_corner_radius)
                )
                .clip(
                    RoundedCornerShape(
                        topStart = dimensionResource(Res.dimen.backdrop_corner_radius),
                        topEnd = dimensionResource(Res.dimen.backdrop_corner_radius)
                    )
                )
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column {
                Row {
                    Spacer(
                        modifier = Modifier.width(
                            dimensionResource(Res.dimen.media_card_width)
                                    + dimensionResource(Res.dimen.large_padding)
                        )
                    )

                    Column(
                        modifier = Modifier
                            .padding(
                                start = dimensionResource(Res.dimen.large_padding),
                                top = dimensionResource(Res.dimen.large_padding),
                                end = dimensionResource(Res.dimen.large_padding)
                            )
                            .height(
                                WindowInsets.statusBars
                                    .asPaddingValues()
                                    .calculateTopPadding()
                                        + dimensionResource(Res.dimen.media_card_height)
                                        + dimensionResource(Res.dimen.backdrop_corner_radius)
                                        - dimensionResource(Res.dimen.banner_height)
                            )
                            .fillMaxSize()
                    ) {
                        Text(
                            text = media.title.orEmpty(),
                            color = MaterialTheme.colorScheme.onBackground,
                            // Override MediaSmall's text.
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(Modifier.height(dimensionResource(Res.dimen.small_padding)))

                        Text(
                            // TODO: Some attributes are not applied.
                            text = Html
                                .fromHtml(media.description, Html.FROM_HTML_MODE_COMPACT)
                                .toString(),
                            color = MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.6f
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.height(dimensionResource(Res.dimen.large_padding)))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(Res.dimen.large_padding),
                            end = dimensionResource(Res.dimen.large_padding)
                        )
                ) {
                    Stat(
                        label = stringResource(Res.string.score),
                        score = media.averageScore ?: 0
                    ) {
                        "$it%"
                    }

                    media.ranks.forEach { stat ->
                        Stat(
                            label = stat.first,
                            score = stat.second
                        ) {
                            "#$it"
                        }
                    }
                }

                Spacer(Modifier.height(dimensionResource(Res.dimen.large_padding)))

                // TODO: Monet where?
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        dimensionResource(Res.dimen.medium_padding)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = dimensionResource(Res.dimen.large_padding)
                    )
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

            Spacer(Modifier.size(dimensionResource(Res.dimen.large_padding)))

            if (!media.characters.isNullOrEmpty()) {
                Text(
                    text = stringResource(Res.string.characters),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(
                        start = dimensionResource(Res.dimen.large_padding)
                    )
                )

                Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

                // TODO: Make characters clickable.
                CharacterRow(characterList = media.characters) {
                    Log.d("Character", it.second ?: "null")
                }
            }

            Spacer(Modifier.size(dimensionResource(Res.dimen.large_padding)))

            if (!media.trailer.toList().any { it == null }) {
                Text(
                    text = stringResource(Res.string.trailer),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(
                        start = dimensionResource(Res.dimen.large_padding)
                    )
                )

                Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

                val context = LocalContext.current
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            val appIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(media.trailer.first))
                            context.startActivity(appIntent)
                        }
                ) {
                    AsyncImage(
                        model = media.trailer.second,
                        contentDescription = stringResource(Res.string.trailer),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .padding(
                                horizontal = dimensionResource(Res.dimen.large_padding)
                            )
                            .fillMaxWidth()
                            .aspectRatio(1.778f)
                            .clip(
                                RoundedCornerShape(
                                    dimensionResource(Res.dimen.trailer_corner_radius)
                                )
                            ),
                        alignment = Alignment.Center
                    )

                    Image(
                        painter = painterResource(Res.drawable.youtube),
                        contentDescription = stringResource(Res.string.trailer),
                        alignment = Alignment.Center
                    )
                }
            }

            Spacer(modifier = Modifier.size(dimensionResource(Res.dimen.large_padding)))
        }

        // TODO: Make this a reusable component.
        Card(
            modifier = Modifier
                .padding(
                    top = dimensionResource(Res.dimen.large_padding),
                    start = dimensionResource(Res.dimen.large_padding),
                    end = dimensionResource(Res.dimen.large_padding)
                )
                .statusBarsPadding()
                .wrapContentHeight()
                .width(dimensionResource(Res.dimen.media_card_width)),
            shape = mediaSmallShape
        ) {
            AsyncImage(
                model = media.coverImage,
                contentDescription = media.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(dimensionResource(Res.dimen.media_card_height))
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
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = format(score),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun Genre(genre: String?, color: Color) {
    SuggestionChip(
        label = {
            Text(
                text = genre?.lowercase().orEmpty(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(
                    vertical = dimensionResource(Res.dimen.small_padding)
                )
            )
        },
        // TODO: Make genres clickable.
        onClick = { },
        shape = CircleShape,
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = color.copy(alpha = 0.25f)
        ),
        border = SuggestionChipDefaults.suggestionChipBorder(
            borderColor = Color.Transparent
        )
    )
}

@Composable
fun Character(image: String?, name: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .width(dimensionResource(Res.dimen.character_card_width))
            .clickable(
                enabled = true,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(dimensionResource(Res.dimen.backdrop_corner_radius))
    ) {
        AsyncImage(
            model = image,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(dimensionResource(Res.dimen.character_card_height))
                .clip(
                    RoundedCornerShape(dimensionResource(Res.dimen.backdrop_corner_radius))
                )
        )
        Text(
            text = name.orEmpty(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = Modifier.padding(
                dimensionResource(Res.dimen.media_card_text_padding)
            )
        )
    }
}

@Composable
fun CharacterRow(
    characterList: List<Pair<String?, String?>> = emptyList(),
    onItemClick: (character: Pair<String?, String?>) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(Res.dimen.large_padding)),
        contentPadding = PaddingValues(
            start = dimensionResource(Res.dimen.large_padding),
            end = dimensionResource(Res.dimen.large_padding)
        )
    ) {
        items(characterList) { character ->
            Character(
                image = character.first,
                name = character.second,
                onClick = { onItemClick(character) }
            )
        }
    }
}
