package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Title(
    /**
     * The romanization of the native language title
     */
    val romaji: String?,
    /**
     * The official english title
     */
    val english: String?,
    /**
     * Official title in it's native language
     */
    val native: String?,
) : BaseEntity, Parcelable