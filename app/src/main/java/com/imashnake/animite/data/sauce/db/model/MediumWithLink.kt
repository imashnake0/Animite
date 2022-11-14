package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediumWithLink(
    @Embedded
    val link: MediumLink,
    @Relation(
        parentColumn = "medium_id",
        entityColumn = "medium_id"
    )
    val medium: Medium
) : BaseEntity, Parcelable