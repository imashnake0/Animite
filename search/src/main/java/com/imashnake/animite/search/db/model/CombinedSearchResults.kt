package com.imashnake.animite.search.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class CombinedSearchResults(
    @Embedded
    val anilist: AniListSearch,
    @Relation(
        parentColumn = "jaTitle",
        entityColumn = "jaTitle"
    )
    val mal: MyAnimeListSearch
)
