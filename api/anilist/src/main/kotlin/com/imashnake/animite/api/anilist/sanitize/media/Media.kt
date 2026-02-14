package com.imashnake.animite.api.anilist.sanitize.media

import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.core.graphics.toColorInt
import com.imashnake.animite.api.anilist.MediaQuery
import com.imashnake.animite.api.anilist.fragment.AnimeInfo
import com.imashnake.animite.api.anilist.fragment.CharacterSmall
import com.imashnake.animite.api.anilist.fragment.MediaMedium
import com.imashnake.animite.api.anilist.fragment.MediaSmall
import com.imashnake.animite.api.anilist.sanitize.media.Media.Format.Companion.sanitize
import com.imashnake.animite.api.anilist.sanitize.media.Media.Relation.Companion.sanitize
import com.imashnake.animite.api.anilist.sanitize.media.Media.Season.Companion.sanitize
import com.imashnake.animite.api.anilist.sanitize.media.Media.Source.Companion.sanitize
import com.imashnake.animite.api.anilist.sanitize.media.Media.Status.Companion.sanitize
import com.imashnake.animite.api.anilist.type.MediaFormat
import com.imashnake.animite.api.anilist.type.MediaRankType
import com.imashnake.animite.api.anilist.type.MediaRelation
import com.imashnake.animite.api.anilist.type.MediaSeason
import com.imashnake.animite.api.anilist.type.MediaSource
import com.imashnake.animite.api.anilist.type.MediaStatus
import com.imashnake.animite.core.extensions.addNewlineAfterParagraph
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import kotlin.collections.mapNotNull
import kotlin.collections.orEmpty
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

private const val HQ_DEFAULT = "hqdefault"
private const val MAX_RES_DEFAULT = "maxresdefault"
private const val SD_DEFAULT = "sddefault"

private const val EXCEPTION_TAG = "exception"

data class Media(
    /** @see MediaQuery.Media.id */
    val id: Int,
    /** @see MediaQuery.Media.bannerImage */
    val bannerImage: String?,
    /** @see MediaQuery.Media.coverImage */
    val coverImage: String?,
    /** @see MediaQuery.CoverImage.color */
    val color: Int,
    /** @see MediaQuery.Media.title */
    val title: String?,
    /** @see MediaQuery.Media.description */
    val description: String,
    /**
     *  For example: "5d 19h" to 2
     *  @see AnimeInfo.NextAiringEpisode
     * */
    val timeToEpisode: Pair<String, Int>?,
    /** @see MediaQuery.Media */
    val info: ImmutableList<Info>,
    /** @see MediaQuery.Media.rankings */
    val rankings: ImmutableList<Ranking>,
    /** @see MediaQuery.Media.genres */
    val genres: ImmutableList<String>,
    /** @see MediaQuery.Media.characters */
    val characters: ImmutableList<Character>,
    /** @see MediaQuery.Media.trailer */
    val trailer: Trailer?,
    /** @see MediaQuery.Media.streamingEpisodes */
    val streamingEpisodes: ImmutableList<Episode>,
    /** @see MediaQuery.Media.relations */
    val relations: ImmutableList<Pair<Relation?, Small>>,
    /** @see MediaQuery.Media.recommendations */
    val recommendations: ImmutableList<Small>,
) {
    companion object {
        fun getFormattedDate(
            year: Int?,
            month: Int?,
            day: Int?,
        ): String? {
            if (year == null && month == null) return null
            if (year != null && month == null && day != null) return year.toString()
            val formattedDayYear = listOfNotNull(day, year).joinToString().ifEmpty { null }
            val formattedMonth = month?.let {
                Month.of(it)
            }?.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            return listOfNotNull(formattedMonth, formattedDayYear).joinToString(" ")
        }

        fun getTimeToEpisode(nextAiringEpisode: AnimeInfo.NextAiringEpisode?) = nextAiringEpisode?.let nextEp@{ nextEp ->
            nextEp.airingAt.let {
                val difference = Instant.fromEpochSeconds(it.toLong()) - Clock.System.now()
                if (difference < Duration.ZERO) return@nextEp null
                difference
            }.let {
                it.toComponents { days, hours, _, _, _ ->
                    if (days == 0L && hours == 0) return@nextEp null
                    if (days == 0L) "${hours}h" else "${days}d ${hours}h"
                }
            } to nextEp.episode
        }

        fun getAnimeInfo(animeInfo: AnimeInfo?) = buildList {
            if (
                addAll(
                    listOfNotNull(
                        animeInfo?.format?.sanitize()?.let  { Info.Format(it) },
                        animeInfo?.episodes?.let { Info.Item(InfoItem.EPISODES, it.toString()) },
                        animeInfo?.duration?.let { Info.Item(InfoItem.DURATION, it.toString()) },
                    )
                )
            ) {
                add(Info.Divider)
            }

            if (
                addAll(
                    listOfNotNull(
                        animeInfo?.status?.sanitize()?.let { Info.Status(it) },
                        animeInfo?.startDate?.let { getFormattedDate(it.year, it.month, it.day) }?.let { Info.Item(InfoItem.START_DATE, it) },
                        animeInfo?.endDate?.let { getFormattedDate(it.year, it.month, it.day) }?.let { Info.Item(InfoItem.END_DATE, it) },
                        animeInfo?.season?.sanitize()?.let { Info.Season(it, animeInfo.seasonYear) },
                    )
                )
            ) {
                add(Info.Divider)
            }

            if (
                !addAll(
                    listOfNotNull(
                        animeInfo?.studios?.nodes?.firstOrNull()?.name?.let { Info.Item(InfoItem.STUDIO, it) },
                        animeInfo?.source?.sanitize()?.let { Info.Source(it) },
                    )
                )
            ) {
                removeLastOrNull()
            }
        }.toImmutableList()

        fun getStreamingEpisodes(streamingEpisodes: List<MediaQuery.StreamingEpisode?>?): ImmutableList<Episode> {
            if (streamingEpisodes.isNullOrEmpty()) return persistentListOf()

            val regex = Regex("""Episode\s+(\d+(?:\.\d+)?)(?:\s*-\s*(.+))?""")
            return streamingEpisodes.mapNotNull {
                it?.thumbnail ?: return@mapNotNull null
                it.url ?: return@mapNotNull null
                val match = it.title?.let { title -> regex.find(title) }
                Episode(
                    number = match?.groupValues[1],
                    title = match?.groups[2]?.value?.lowercase(),
                    thumbnail = it.thumbnail,
                    url = it.url,
                    site = it.site
                )
            }.toImmutableList()
        }
    }

    enum class Format {
        TV,
        TV_SHORT,
        MOVIE,
        SPECIAL,
        OVA,
        ONA,
        MUSIC,
        MANGA,
        NOVEL,
        ONE_SHOT;

        companion object {
            fun safeValueOf(rawValue: String): Format? = try {
                valueOf(rawValue)
            } catch (e: IllegalArgumentException) {
                Log.e(EXCEPTION_TAG, "safeValueOf: $e; Format $rawValue not found.")
                null
            }

            fun MediaFormat.sanitize() = safeValueOf(this.name)
        }
    }

    enum class Status {
        FINISHED,
        RELEASING,
        NOT_YET_RELEASED,
        CANCELLED,
        HIATUS;

        companion object {
            fun safeValueOf(rawValue: String): Status? = try {
                valueOf(rawValue)
            } catch (e: IllegalArgumentException) {
                Log.e(EXCEPTION_TAG, "safeValueOf: $e; Status $rawValue not found.")
                null
            }

            fun MediaStatus.sanitize() = safeValueOf(this.name)
        }
    }

    enum class Season {
        WINTER,
        SPRING,
        SUMMER,
        FALL;

        companion object {
            fun safeValueOf(rawValue: String): Season? = try {
                valueOf(rawValue)
            } catch (e: IllegalArgumentException) {
                Log.e(EXCEPTION_TAG, "safeValueOf: $e; Season $rawValue not found.")
                null
            }

            fun MediaSeason.sanitize() = safeValueOf(this.name)
        }
    }

    enum class Source {
        ORIGINAL,
        MANGA,
        LIGHT_NOVEL,
        VISUAL_NOVEL,
        VIDEO_GAME,
        OTHER,
        NOVEL,
        DOUJINSHI,
        ANIME,
        WEB_NOVEL,
        LIVE_ACTION,
        GAME,
        COMIC,
        MULTIMEDIA_PROJECT,
        PICTURE_BOOK;

        companion object {
            fun safeValueOf(rawValue: String): Source? = try {
                valueOf(rawValue)
            } catch (e: IllegalArgumentException) {
                Log.e(EXCEPTION_TAG, "safeValueOf: $e; Source $rawValue not found.")
                null
            }

            fun MediaSource.sanitize() = safeValueOf(this.name)
        }
    }

    enum class Relation {
        ADAPTATION,
        PREQUEL,
        SEQUEL,
        PARENT,
        SIDE_STORY,
        CHARACTER,
        SUMMARY,
        ALTERNATIVE,
        SPIN_OFF,
        OTHER,
        SOURCE,
        COMPILATION,
        CONTAINS;

        companion object {
            fun safeValueOf(rawValue: String): Relation? = try {
                valueOf(rawValue)
            } catch (e: IllegalArgumentException) {
                Log.e(EXCEPTION_TAG, "safeValueOf: $e; Source $rawValue not found.")
                null
            }

            fun MediaRelation.sanitize() = safeValueOf(this.name)
        }
    }

    @Immutable
    sealed class Info(open val item: InfoItem) {
        data class Format(
            val format: Media.Format,
        ) : Info(InfoItem.FORMAT)

        data class Status(
            val status: Media.Status,
        ) : Info(InfoItem.STATUS)

        data class Season(
            val season: Media.Season,
            val year: Int? = null,
        ) : Info(InfoItem.SEASON)

        data class Source(
            val source: Media.Source,
        ) : Info(InfoItem.SOURCE)

        data class Item(
            override val item: InfoItem,
            val value: String,
        ) : Info(item)

        data object Divider : Info(InfoItem.DIVIDER)
    }

    enum class InfoItem {
        FORMAT,
        EPISODES,
        DURATION,
        STATUS,
        START_DATE,
        END_DATE,
        SEASON,
        STUDIO,
        SOURCE,
        DIVIDER,
    }

    data class Ranking(
        /** @see MediaQuery.Ranking.rank */
        val rank: Int,
        /** @see MediaQuery.Ranking.type */
        val type: Type,
    ) {
        /** @see MediaQuery.Ranking.type */
        enum class Type(val string: String) {
            RATED("Rated"),
            POPULAR("Popular"),
            SCORE("Score")
        }
    }

    @Immutable
    data class Character(
        /** @see CharacterSmall.id */
        val id: Int,
        /** @see CharacterSmall.image */
        val image: String?,
        /** @see CharacterSmall.Name.full */
        val name: String?,

        /**
         * Year, Month, Day.
         * @see CharacterSmall.dateOfBirth
         * */
        val dob: String?,
        /** @see CharacterSmall.favourites */
        val favourites: String?,
        /** @see CharacterSmall.Name.alternative */
        val alternativeNames: String,

        /** @see CharacterSmall.description */
        val description: String?,
    ) {
        companion object {
            fun getFormattedFavourites(favouritesCount: Int?): String? {
                if (favouritesCount == null) return null
                return if (favouritesCount >= 1000) {
                    if (favouritesCount % 1000f < 100) {
                        "${(favouritesCount / 1000f).toInt()}k"
                    } else {
                        "${String.format(Locale.getDefault(), format = "%.1f", favouritesCount / 1000f)}k"
                    }
                } else {
                    favouritesCount.toString()
                }
            }

            fun getDescription(
                age: String?,
                gender: String?,
                description: String?
            ): String? {
                if (age == null && gender == null && description == null) return null
                return listOfNotNull(
                    age?.let { "<b>Age:</b> $it" },
                    gender?.let { "<b>Gender:</b> $it" },
                    description,
                ).joinToString("<br>").addNewlineAfterParagraph()
            }
        }

        internal constructor(query: CharacterSmall) : this(
            id = query.id,
            image = query.image?.large,
            name = query.name?.full,
            dob = getFormattedDate(
                year = query.dateOfBirth?.year,
                month = query.dateOfBirth?.month,
                day = query.dateOfBirth?.day
            ),
            favourites = getFormattedFavourites(query.favourites),
            alternativeNames = query.name?.alternative.orEmpty().filterNotNull().joinToString(),
            description = getDescription(
                age = query.age,
                gender = query.gender,
                description = query.description,
            ),
        )
    }

    @Immutable
    data class Trailer(
        /** @see MediaQuery.Trailer.id
         * @see MediaQuery.Trailer.site */
        val url: String?,
        /** @see MediaQuery.Trailer.thumbnail */
        val thumbnail: Thumbnail,
    ) {
        /** @see MediaQuery.Trailer.thumbnail */
        enum class Site(val baseUrl: String) {
            YOUTUBE("https://www.youtube.com/watch?v="),
            DAILYMOTION("https://www.dailymotion.com/video/"),
            UNKNOWN("")
        }

        data class Thumbnail(
            val maxResDefault: String?,
            val sdDefault: String?,
            val defaultThumbnail: String?
        )
    }

    @Immutable
    data class Episode(
        val number: String?,
        val title: String?,
        val thumbnail: String,
        val url: String,
        val site: String?,
    )

    internal constructor(query: MediaQuery.Media) : this(
        id = query.id,
        bannerImage = query.bannerImage,
        coverImage = query.coverImage?.extraLarge ?: query.coverImage?.large ?: query.coverImage?.medium,
        color = query.coverImage?.color?.toColorInt() ?: Color.TRANSPARENT,
        title = query.title?.romaji ?: query.title?.english ?: query.title?.native,
        description = query.description.orEmpty(),
        // TODO: Make this a proper countdown.
        timeToEpisode = getTimeToEpisode(query.animeInfo.nextAiringEpisode),
        info = getAnimeInfo(query.animeInfo),
        // TODO: Clean this up and add other rank info.
        rankings = if (query.rankings == null) { emptyList() } else {
            // TODO: Is this filter valid?
            query.rankings.filter {
                it?.allTime == true && it.type != MediaRankType.UNKNOWN__
            }.map {
                Ranking(
                    rank = it!!.rank,
                    type = Ranking.Type.valueOf(it.type.name)
                )
            } + listOfNotNull(
                query.averageScore?.let {
                    Ranking(rank = it, type = Ranking.Type.SCORE)
                }
            )
        }.toImmutableList(),
        genres = query.genres?.filterNotNull().orEmpty().toImmutableList(),
        characters = query.characters?.edges.orEmpty().mapNotNull {
            if (it?.node?.characterSmall?.name == null) return@mapNotNull null
            Character(it.node.characterSmall)
        }.toImmutableList(),
        trailer = if(query.trailer?.site == null || query.trailer.id == null) {
            null
        } else {
            Trailer(
                url = "${Trailer.Site.valueOf(query.trailer.site.uppercase()).baseUrl}${query.trailer.id}",
                thumbnail = with(query.trailer) {
                    Trailer.Thumbnail(
                        maxResDefault = thumbnail?.takeIf {
                            it.contains(HQ_DEFAULT)
                        }?.replace(HQ_DEFAULT, MAX_RES_DEFAULT),
                        sdDefault = thumbnail?.takeIf {
                            it.contains(HQ_DEFAULT)
                        }?.replace(HQ_DEFAULT, SD_DEFAULT),
                        defaultThumbnail = thumbnail
                    )
                }
            )
        },
        streamingEpisodes = getStreamingEpisodes(query.streamingEpisodes),
        relations = query.relations?.edges.orEmpty().mapNotNull { edge ->
            edge?.node?.mediaSmall?.let { edge.relationType?.sanitize() to Small(it) }
        }.toImmutableList(),
        recommendations = query.recommendations?.nodes.orEmpty().mapNotNull { node ->
            node?.mediaRecommendation?.mediaSmall?.let { Small(it) }
        }.toImmutableList()
    )

    @Immutable
    data class Small(
        /** @see MediaSmall.id */
        val id: Int,
        /** @see MediaSmall.type */
        val type: Type,
        /** @see MediaSmall.title */
        val title: String?,
        /** @see MediaSmall.coverImage */
        val coverImage: String?
    ) {
        /** @see MediaSmall.type */
        enum class Type(val type: String) {
            ANIME("Anime"),
            MANGA("Manga"),
            UNKNOWN("Unknown"),
        }

        internal constructor(query: MediaSmall) : this(
            id = query.id,
            type = query.type?.name?.let { Type.valueOf(it) } ?: Type.UNKNOWN,
            coverImage = query.coverImage?.extraLarge ?: query.coverImage?.large,
            title = query.title?.romaji ?: query.title?.english ?: query.title?.native
        )
    }

    @Immutable
    data class Medium(
        /** @see id */
        val id: Int,
        /** @see coverImage */
        val coverImage: String?,
        /** @see title */
        val title: String?,
        /** @see season */
        val season: Season?,
        /** @see seasonYear */
        val seasonYear: Int?,
        /** @see studios */
        val studios: ImmutableList<String>,
        /** @see format */
        val format: Format?,
        /** @see episodes */
        val episodes: Int?,
    ) {
        internal constructor(query: MediaMedium) : this(
            id = query.id,
            coverImage = query.coverImage?.extraLarge,
            title = query.title?.romaji ?: query.title?.english ?: query.title?.native,
            season = query.season?.sanitize(),
            seasonYear = query.seasonYear,
            studios = query.studios?.nodes
                .orEmpty()
                .filter { it?.name != null }
                .map { it!!.name }
                .toImmutableList(),
            format = query.format?.sanitize(),
            episodes = query.episodes
        )
    }
}
