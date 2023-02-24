package com.imashnake.animite.api.anilist.sanitize.media

import android.graphics.Color
import com.imashnake.animite.api.anilist.MediaListQuery
import com.imashnake.animite.api.anilist.MediaQuery

class Media(
    /** @see MediaQuery.Media.bannerImage */
    val bannerImage: String,
    /** @see MediaQuery.Media.coverImage */
    val coverImage: String,
    /** @see MediaQuery.CoverImage.color */
    val color: Int,
    /** @see MediaQuery.Media.title */
    val title: String,
    /** TODO: https://github.com/imashnake0/Animite/issues/58.
     * @see MediaQuery.Media.description */
    val description: String,
    /** @see MediaQuery.Media.rankings */
    val rankings: List<Ranking>,
    /** @see MediaQuery.Media.genres */
    val genres: List<String>,
    /** @see MediaQuery.Media.characters */
    val characters: List<Character>,
    /** @see MediaQuery.Media.trailer */
    val trailer: Trailer
) {
    data class Ranking(
        /** @see MediaQuery.Ranking.rank */
        val rank: Int,
        /** @see MediaQuery.Ranking.type */
        val type: MediaRankType,
    )

    /** @see MediaQuery.Ranking.type */
    enum class MediaRankType(name: String) {
        RATED("Rated"),
        POPULAR("Popular"),
        SCORE("Score")
    }

    data class Character(
        /** @see MediaQuery.Node.id */
        val id: Int,
        /** @see MediaQuery.Node.image */
        val image: String,
        /** @see MediaQuery.Node.name */
        val name: String,
    )

    data class Trailer(
        /** @see MediaQuery.Trailer.id
         * @see MediaQuery.Trailer.site */
        val url: String,
        /** @see MediaQuery.Trailer.thumbnail */
        val thumbnail: String,
    )

    /** @see MediaQuery.Trailer.thumbnail */
    enum class Site(val baseUrl: String) {
        YOUTUBE("https://www.youtube.com/watch?v="),
        DAILYMOTION("https://www.dailymotion.com/video/"),
        UNKNOWN("")
    }

    companion object {
        // TODO: You fool you're not supposed to sanitize all of it.
        fun sanitize(query: MediaQuery.Media) =
            Media(
                bannerImage = query.bannerImage.orEmpty(),
                coverImage = query.coverImage?.extraLarge ?: query.coverImage?.large ?: query.coverImage?.medium.orEmpty(),
                color = Color.parseColor(query.coverImage?.color ?: Color.TRANSPARENT.toString()),
                title = query.title?.romaji ?: query.title?.english ?: query.title?.native.orEmpty(),
                description = query.description.orEmpty(),
                rankings = query.rankings?.filter { it != null && it.allTime == true }?.mapNotNull {
                    it?.let {
                        Ranking(
                            rank = it.rank,
                            type = MediaRankType.valueOf(it.type.name)
                        )
                    }
                }.orEmpty() + listOfNotNull(query.averageScore?.let { Ranking(rank = it, type = MediaRankType.SCORE) }),
                genres = query.genres?.filterNotNull().orEmpty(),
                characters = query.characters?.nodes?.mapNotNull {
                    it?.let {
                        Character(
                            id = it.id,
                            image = it.image?.large.orEmpty(),
                            name = it.name?.full.orEmpty()
                        )
                    }
                }.orEmpty(),
                trailer = Trailer(
                    url = listOf(query.trailer?.site, query.trailer?.id).takeIf {
                            ti -> ti.all { it != null }
                    }?.joinToString().orEmpty(),
                    thumbnail = query.trailer?.thumbnail.orEmpty()
                )
            )
    }

    data class Medium(
        /** @see MediaListQuery.Medium.id */
        val id: Int,
        /** @see MediaListQuery.Medium.type */
        val type: MediaType,
        /** @see MediaListQuery.Medium.title */
        val title: String,
        /** @see MediaListQuery.Medium.coverImage */
        val coverImage: String
    ) {
        companion object {
            fun sanitize(query: MediaListQuery.Medium) =
                Medium(
                    id = query.id,
                    type = MediaType.valueOf(query.type?.name ?: "UNKNOWN"),
                    coverImage = query.coverImage?.extraLarge ?: query.coverImage?.large ?: "",
                    title = query.title?.romaji ?: query.title?.english
                    ?: query.title?.native.orEmpty()
                )
        }
    }

    /** @see MediaListQuery.Medium.type */
    enum class MediaType(name: String = "") {
        ANIME("Anime"),
        MANGA("Manga"),
        UNKNOWN
    }
}
