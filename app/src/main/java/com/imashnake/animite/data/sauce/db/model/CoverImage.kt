package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoverImage(
    /**
     * The cover image url of the media at its largest size. If this size isn't available, large
     * will be provided instead.
     */
    val extraLarge: String?,
    /**
     * The cover image url of the media at a large size
     */
    val large: String?,
) : BaseEntity, Parcelable