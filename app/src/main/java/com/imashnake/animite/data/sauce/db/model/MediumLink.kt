package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "medium_link", primaryKeys = ["medium_id", "tag"], indices = [Index("medium_id")])
data class MediumLink(
    @ColumnInfo(name = "medium_id")
    val id: Int,
    @ColumnInfo(name = "tag")
    val tag: ListTag
) : BaseEntity, Parcelable