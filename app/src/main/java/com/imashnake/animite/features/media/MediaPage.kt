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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    AnimiteTheme {
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
                        contentDescription = "Banner Image",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.height(168.dp),
                        alignment = Alignment.Center
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(168.dp),
                        color = Color(media.color?.toHexColor() ?: 0).copy(alpha = 0.2f)
                    ) { }
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(168.dp),
                    alignment = Alignment.TopCenter
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 150.dp)
                    .clip(backdropShape)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column {
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
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = media.title ?: "",
                                color = MaterialTheme.colorScheme.onBackground,
                                // Override MediaSmall's text.
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Text(
                                // TODO: Some styles are not applied.
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

                    Spacer(Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp)
                    ) {
                        Stat(label = "SCORE", score = media.averageScore ?: 0) {
                            "$it%"
                        }

                        media.ranks.forEach { stat ->
                            Stat(label = stat.first, score = stat.second) {
                                "#$it"
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // TODO: Monet where?
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
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

                Spacer(modifier = Modifier.size(24.dp))

                if (!media.characters.isNullOrEmpty()) {
                    Text(
                        text = "Characters",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(start = 24.dp)
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    // TODO: Make characters clickable.
                    CharacterRow(characterList = media.characters) {
                        Log.d("Character", it.second ?: "null")
                    }
                }

                Spacer(modifier = Modifier.size(24.dp))

                if (!media.trailer.toList().any { it == null }) {
                    Text(
                        text = "Trailer",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(start = 24.dp)
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    val context = LocalContext.current
                    Box(modifier = Modifier
                        .wrapContentSize()
                        .clickable {
                            val appIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(media.trailer.first))
                            context.startActivity(appIntent)
                        }) {
                        AsyncImage(
                            model = media.trailer.second,
                            contentDescription = "Thumbnail",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .fillMaxWidth()
                                .aspectRatio(1.778f)
                                .clip(RoundedCornerShape(30.dp)),
                            alignment = Alignment.Center
                        )

                        Image(
                            painter = painterResource(id = R.drawable.youtube),
                            contentDescription = "Watch on Youtube",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.size(24.dp))
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
                text = genre?.lowercase() ?: "",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(vertical = 10.dp)
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
            .width(96.dp)
            .clickable(
                enabled = true,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = mediaSmallShape
    ) {
        AsyncImage(
            model = image,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(121.dp)
                .clip(mediaSmallShape)
        )
        Text(
            text = name ?: "",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
fun CharacterRow(
    characterList: List<Pair<String?, String?>> = emptyList(),
    onItemClick: (character: Pair<String?, String?>) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
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
