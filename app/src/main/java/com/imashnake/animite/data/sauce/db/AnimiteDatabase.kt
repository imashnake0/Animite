package com.imashnake.animite.data.sauce.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.imashnake.animite.data.sauce.db.dao.MediaDAO
import com.imashnake.animite.data.sauce.db.dao.MediaLinkDAO
import com.imashnake.animite.data.sauce.db.model.Media
import com.imashnake.animite.data.sauce.db.model.MediaLink

@Database(
    version = 2,
    exportSchema = false,
    entities = [
        Media::class,
        MediaLink::class
    ]
)
@TypeConverters(RoomConverters::class)
abstract class AnimiteDatabase : RoomDatabase() {
    abstract fun homepageDAO(): MediaDAO
    abstract fun mediumLinkDAO(): MediaLinkDAO
}