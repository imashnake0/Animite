package com.imashnake.animite.data.repos

import com.imashnake.animite.data.Resource
import com.imashnake.animite.data.repos.mappers.MediaMapper
import com.imashnake.animite.data.sauce.db.medialist.MediaListDatabaseSource
import com.imashnake.animite.data.sauce.db.model.ListTag
import com.imashnake.animite.data.sauce.db.model.Media
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
        tag: ListTag,
        mediaType: MediaType,
        sort: List<MediaSort>,
        page: Int = 0,
        perPage: Int = 10,
        season: MediaSeason? = null,
        seasonYear: Int? = null
    ): Flow<Resource<List<Media>>> = networkBoundResource(
        db = mediaListDatabaseSource.getMedia(mediaType, tag),
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
                    .map(MediaMapper::mediaApiToDB),
                tag
            )
        }
    )
}
