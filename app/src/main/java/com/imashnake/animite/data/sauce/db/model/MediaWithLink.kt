package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaWithLink(
    @Embedded
    val link: MediaLink,
    @Relation(
        parentColumn = "media_id",
        entityColumn = "media_id"
    )
    val medium: Media
) : BaseEntity, Parcelable