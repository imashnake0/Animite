package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imashnake.animite.data.sauce.db.BaseEntity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "title")
data class Title(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "title_id")
    val id: Int = 0,
    /**
     * The romanization of the native language title
     */
    val romaji: String?,
    /**
     * The official english title
     */
    val english: String?,
    /**
     * Official title in it's native language
     */
    val native: String?,
) : BaseEntity, Parcelable