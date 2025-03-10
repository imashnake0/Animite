package com.imashnake.animite.search.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imashnake.animite.search.db.model.AniListSearch
import com.imashnake.animite.search.db.model.MyAnimeListSearch

@Database(
    entities = [
        AniListSearch::class,
        MyAnimeListSearch::class
    ],
    version = 1
)
abstract class SearchDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
}
