package com.imashnake.animite.data.sauce.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.imashnake.animite.data.sauce.db.model.CoverImage
import com.imashnake.animite.data.sauce.db.model.Medium
import com.imashnake.animite.data.sauce.db.model.Title

@Database(
    version = 4,
    exportSchema = false,
    entities = [
        //MediaPage::class,
        //Page::class,
        Medium::class,
        Title::class,
        CoverImage::class,
        //PageToMediumLink::class
    ]
)
@TypeConverters(RoomConverters::class)
abstract class AnimiteDatabase : RoomDatabase() {
    abstract fun homepageDAO(): MediaDAO
}