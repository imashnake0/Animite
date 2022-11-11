package com.imashnake.animite.data.repos

import com.imashnake.animite.data.sauce.db.medialist.MediaListDatabaseSource
import com.imashnake.animite.data.sauce.db.model.CoverImage
import com.imashnake.animite.data.sauce.db.model.Medium
import com.imashnake.animite.data.sauce.db.model.Title
import com.imashnake.animite.data.sauce.network.MediaListNetworkSource
import com.imashnake.animite.data.sauce.networkBoundResource
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaListRepository @Inject constructor(
    private val mediaListNetworkSource: MediaListNetworkSource,
    private val mediaListDatabaseSource: MediaListDatabaseSource
) {

    fun getMediaList(
        mediaType: MediaType,
        page: Int,
        perPage: Int,
        sort: List<MediaSort>,
        season: MediaSeason?,
        seasonYear: Int?
    ): Flow<List<Medium>> = networkBoundResource(
        db = mediaListDatabaseSource.getMedia(mediaType, sort, season, seasonYear),
        request = {
            mediaListNetworkSource.fetchMediaList(
                mediaType = mediaType,
                page = page,
                perPage = perPage,
                sort = sort,
                season = season,
                seasonYear = seasonYear
            )
        },
        insert = {
            mediaListDatabaseSource.insertMedia(
                it?.media.orEmpty()
                    .filterNotNull()
                    .map { medium ->
                        val title = medium.title?.let { title -> Title(0, title.romaji, title.english, title.native) }
                        val coverImage = medium.coverImage?.let { image -> CoverImage(0, image.extraLarge, image.large) }

                        Medium(medium.id, medium.type, title, coverImage)
                    }
            )
        }
    )
}
