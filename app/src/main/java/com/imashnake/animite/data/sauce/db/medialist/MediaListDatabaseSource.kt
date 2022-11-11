package com.imashnake.animite.data.sauce.db.medialist

import com.imashnake.animite.data.sauce.db.MediaDAO
import com.imashnake.animite.data.sauce.db.model.Medium
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaListDatabaseSource @Inject constructor(
    private val dao: MediaDAO
) {

    suspend fun insertMedia(media: List<Medium>) = dao.insertAll(*media.toTypedArray())

    fun getMedia(mediaType: MediaType, sort: List<MediaSort>, season: MediaSeason?, seasonYear: Int?): Flow<List<Medium>> {
        return dao.getAll(mediaType, sort, season, seasonYear)
    }
}
