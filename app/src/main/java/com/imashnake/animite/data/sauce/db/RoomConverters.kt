package com.imashnake.animite.data.sauce.db

import androidx.room.TypeConverter
import com.imashnake.animite.data.sauce.db.model.Medium
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// TODO, try to avoid doing this for lists, try to save with ID + relationship links
object RoomConverters {

    @TypeConverter
    fun fromMedium(list: List<Medium?>?): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toMedium(medium: String): List<Medium?>? {
        return Json.decodeFromString(medium)
    }
}