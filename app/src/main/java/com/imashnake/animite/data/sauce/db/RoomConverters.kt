package com.imashnake.animite.data.sauce.db

import androidx.room.TypeConverter
import com.imashnake.animite.data.sauce.db.model.ListTag

// TODO, try to avoid doing this for lists, try to save with ID + relationship links
object RoomConverters {

    @TypeConverter
    fun toListType(type: ListTag) = type.name

    @TypeConverter
    fun fromListType(type: String) = ListTag.valueOf(type)
}