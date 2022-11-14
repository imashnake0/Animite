package com.imashnake.animite.data.sauce.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.imashnake.animite.data.sauce.db.dao.MediaDAO
import com.imashnake.animite.data.sauce.db.dao.MediaLinkDAO
import com.imashnake.animite.data.sauce.db.model.Medium
import com.imashnake.animite.data.sauce.db.model.MediumLink

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Medium::class,
        MediumLink::class
    ]
)
@TypeConverters(RoomConverters::class)
abstract class AnimiteDatabase : RoomDatabase() {
    abstract fun homepageDAO(): MediaDAO
    abstract fun mediumLinkDAO(): MediaLinkDAO
}