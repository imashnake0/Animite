package com.imashnake.animite.data.repos.mappers

import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.data.sauce.db.model.CoverImage
import com.imashnake.animite.data.sauce.db.model.Media
import com.imashnake.animite.data.sauce.db.model.Title

object MediaMapper {

    fun mediaApiToDB(media: MediaListQuery.Medium): Media {
        val title = media.title?.let { title -> Title(title.romaji, title.english, title.native) }
        val coverImage = media.coverImage?.let { image -> CoverImage(image.extraLarge, image.large) }

        return Media(media.id, media.type, title, coverImage)
    }
}