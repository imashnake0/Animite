package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "media_link", primaryKeys = ["media_id", "tag"], indices = [Index("media_id")])
data class MediaLink(
    @ColumnInfo(name = "media_id")
    val id: Int,
    @ColumnInfo(name = "tag")
    val tag: ListTag
) : BaseEntity, Parcelable