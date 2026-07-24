package com.imashnake.animite.api.anilist.sanitize.profile

import android.util.Log
import androidx.compose.runtime.Immutable
import com.imashnake.animite.api.anilist.UserMediaListQuery
import com.imashnake.animite.api.anilist.fragment.User
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.Media.Language
import com.imashnake.animite.api.anilist.sanitize.media.Media.Small.Type
import com.imashnake.animite.api.anilist.sanitize.profile.User.ListNames.Companion.sanitize
import com.imashnake.animite.api.anilist.sanitize.profile.User.ProfileColor.Companion.toHexString
import com.imashnake.animite.api.anilist.type.MediaType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

private const val EXCEPTION_TAG = "exception"

/**
 * Sanitized [User].
 *
 * @param id
 * @param name
 * @param description
 * @param avatar
 * @param banner
 * @param stats
 * @param genres
 * @param favourites
 */
@Immutable
data class User(
    /** @see User.id */
    val id: Int,
    /** @see User.name */
    val name: String,
    /** @see User.about */
    val description: String?,
    /** @see User.avatar */
    val avatar: String?,
    /** @see User.bannerImage */
    val banner: String?,
    /** @see User.Options.profileColor */
    val color: String?,

    // region About
    /** User Stats */
    val stats: ImmutableList<Stat>,
    /** @see User.Anime.genres */
    val genres: ImmutableList<Genre>,
    // endregion

    // region Anime
    /** @see User.AnimeList.sectionOrder */
    val animeListOrder: List<String>,
    /** @see User.MangaList.sectionOrder */
    val mangaListOrder: List<String>,
    // endregion

    // region Fave
    /** @see User.favourites */
    val favourites: ImmutableList<FavouriteCollection.FavouriteList>,
    // endregion
) {
    data class Stat(
        val label: String,
        val value: String,
    )

    /**
     * Sanitized [User.Genre]
     *
     * @param genre
     * @param mediaCount
     */
    @Immutable
    data class Genre(
        /** @see User.Genre.genre */
        val genre: String,
        /** @see User.Genre.count */
        val mediaCount: Int,
    )

    @Immutable
    data class MediaCollection(
        val type: Type,
        val namedLists: ImmutableList<NamedTrackingList>,
    ) {
        @Immutable
        data class NamedTrackingList(
            val name: String?,
            val existingListName: ListNames?,
            val list: ImmutableList<Media.Tracking>,
        ) {
            internal constructor(
                query: UserMediaListQuery.List,
                language: Language
            ) : this(
                name = query.name,
                existingListName = query.name?.sanitize(),
                list = query.entries.orEmpty().mapNotNull {
                    Media.Tracking(
                        query = it?.media?.mediaTracking ?: return@mapNotNull null,
                        score = it.score?.toFloat(),
                        language = language
                    )
                }.toImmutableList()
            )
        }

        internal constructor(
            query: UserMediaListQuery.Data,
            type: MediaType?,
            language: Language,
            mediaListOrder: List<String>
        ) : this(
            type = type?.name?.let { Type.valueOf(it) } ?: Type.UNKNOWN,
            namedLists = query.mediaListCollection?.lists.orEmpty().sortedBy {
                mediaListOrder.indexOf(it?.name)
            }.mapNotNull {
                NamedTrackingList(it ?: return@mapNotNull null, language)
            }.toImmutableList()
        )
    }

    @Immutable
    data class FavouriteCollection(
        val type: Type,
        val favouriteList: ImmutableList<FavouriteList>,
    ) {
        @Immutable
        data class FavouriteList(
            val name: String?,
            val list: ImmutableList<Any>,
        ) {
            internal constructor(
                query: User.Anime1,
                language: Language
            ) : this(
                name = Favouritables.Anime.name,
                list = query.nodes.orEmpty().mapNotNull {
                    Media.Small(it?.mediaSmall ?: return@mapNotNull null, language)
                }.toImmutableList()
            )

            internal constructor(
                query: User.Manga,
                language: Language
            ) : this(
                name = Favouritables.Manga.name,
                list = query.nodes.orEmpty().mapNotNull {
                    Media.Small(it?.mediaSmall ?: return@mapNotNull null, language)
                }.toImmutableList()
            )

            internal constructor(query: User.Characters) : this(
                name = Favouritables.Characters.name,
                list = query.nodes.orEmpty().filter { it?.characterSmall?.name != null }.map {
                    Media.Credit(it!!.characterSmall, null)
                }.toImmutableList()
            )
        }
    }

    internal constructor(
        query: User,
        language: Language
    ) : this(
        id = query.id,
        name = query.name,
        description = query.about,
        avatar = query.avatar?.large,
        banner = query.bannerImage,
        color = query.options?.profileColor?.toHexString(),
        stats = listOfNotNull(
            query.statistics?.anime?.count?.toString()?.let {
                Stat("TOTAL\nANIME", it)
            },
            query.statistics?.anime?.minutesWatched?.minutes?.toDouble(DurationUnit.DAYS)?.let{"%.1f".format(it)}?.let {
                Stat("DAYS\nWATCHED", it)
            },
            query.statistics?.anime?.meanScore?.toFloat()?.let { "%.1f".format(it) }?.let {
                Stat("MEAN\nSCORE", it)
            },
        ).toImmutableList(),
        genres = query.statistics?.anime?.genres.orEmpty().filterNotNull().run {
            val totalCount = this.sumOf { genre -> genre.count }
            mapNotNull {
                Genre(
                    genre = it.genre ?: return@mapNotNull null,
                    mediaCount = it.count
                )
            }.filter {
                // Filters out anime genres that contribute to less than 5%.
                it.mediaCount > totalCount/20
            }.sortedByDescending { it.mediaCount }
        }.toImmutableList(),
        animeListOrder = query.mediaListOptions?.animeList?.sectionOrder.orEmpty().filterNotNull(),
        mangaListOrder = query.mediaListOptions?.mangaList?.sectionOrder.orEmpty().filterNotNull(),
        favourites = listOfNotNull(
            query.favourites?.anime?.let { FavouriteCollection.FavouriteList(it, language) }.takeIf { it?.list?.isNotEmpty() == true },
            query.favourites?.manga?.let { FavouriteCollection.FavouriteList(it, language) }.takeIf { it?.list?.isNotEmpty() == true },
            query.favourites?.characters?.let { FavouriteCollection.FavouriteList(it) }.takeIf { it?.list?.isNotEmpty() == true },
        ).toImmutableList()
    )

    /** Profile highlight color (blue, purple, pink, orange, red, green, gray) */
    enum class ProfileColor {
        BLUE, PURPLE, PINK, ORANGE, RED, GREEN, GRAY;

        companion object {
            fun safeValueOf(rawValue: String): ProfileColor? = try {
                ProfileColor.valueOf(rawValue)
            } catch (e: IllegalArgumentException) {
                Log.e(EXCEPTION_TAG, "safeValueOf: $e; Profile color $rawValue doesn't exist!.")
                null
            }

            internal fun String?.toHexString(): String {
                val color =  when(this?.let { ProfileColor.safeValueOf(it.uppercase()) }) {
                    BLUE -> "#007BA7"
                    PURPLE -> "#E0AFFF"
                    PINK -> "#F2BDCD"
                    ORANGE -> "#F2B949"
                    RED -> "#FA5053"
                    GREEN -> "#0BDA51"
                    GRAY -> "#D9D9D9"
                    null -> "#FF8DA1"
                }
                Log.d("whatiscolor", "toHexString: $color")

                return color
            }
        }
    }

    enum class Favouritables {
        Anime,
        Manga,
        Characters,
    }

    enum class ListNames {
        // Anime lists
        WATCHING,
        COMPLETED,
        PAUSED,
        DROPPED,
        REWATCHING,
        PLANNING,

        // Manga lists
        READING,
        REREADING,
        PLAN_TO_READ,

        CUSTOM_OR_UNKNOWN;

        companion object {
            fun safeValueOf(rawValue: String?): ListNames = try {
                ListNames.valueOf(rawValue?.uppercase().toString())
            } catch (e: IllegalArgumentException) {
                Log.e(EXCEPTION_TAG, "safeValueOf: $e; Format $rawValue not found.")
                CUSTOM_OR_UNKNOWN
            }

            fun String?.sanitize() = safeValueOf(this)
        }
    }
}
