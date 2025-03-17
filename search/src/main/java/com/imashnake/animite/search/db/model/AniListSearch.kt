package com.imashnake.animite.search.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AniListSearch(
    @PrimaryKey val id: Int,
    val jaTitle: String,
)
