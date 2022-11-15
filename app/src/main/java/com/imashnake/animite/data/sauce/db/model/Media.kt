package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imashnake.animite.type.MediaType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "media")
data class Media(
    /**
     * The id of the media
     */
    @PrimaryKey
    @ColumnInfo(name = "medium_id")
    val id: Int,
    /**
     * The type of the media; anime or manga
     */
    val type: MediaType?,
    /**
     * The official titles of the media in various languages
     */
    @Embedded val title: Title?,
    /**
     * The cover images of the media
     */
    @Embedded val coverImage: CoverImage?
) : BaseEntity, Parcelable