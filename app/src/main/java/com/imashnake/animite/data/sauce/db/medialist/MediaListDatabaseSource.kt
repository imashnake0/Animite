package com.imashnake.animite.data.sauce.db.medialist

import com.imashnake.animite.data.sauce.db.dao.MediaDAO
import com.imashnake.animite.data.sauce.db.dao.MediaLinkDAO
import com.imashnake.animite.data.sauce.db.model.ListTag
import com.imashnake.animite.data.sauce.db.model.Media
import com.imashnake.animite.data.sauce.db.model.MediumLink
import com.imashnake.animite.data.sauce.db.model.MediumWithLink
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MediaListDatabaseSource @Inject constructor(
    private val mediaDAO: MediaDAO,
    private val mediaLinkDao: MediaLinkDAO
) {

    suspend fun insertMedia(media: List<Media>, tag: ListTag) {
        mediaLinkDao.insertAll(*media.map { MediumLink(it.id, tag) }.toTypedArray())
        mediaDAO.insertAll(*media.toTypedArray())
    }

    fun getMedia(mediaType: MediaType, tag: ListTag): Flow<List<MediumWithLink>> {
        return mediaDAO.getAll(mediaType, tag)
    }
}
