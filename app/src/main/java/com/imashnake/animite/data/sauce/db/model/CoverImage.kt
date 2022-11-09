package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imashnake.animite.data.sauce.db.BaseEntity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cover_image")
data class CoverImage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cover_image_id")
    val id: Int = 0,
    /**
     *
     * The cover image url of the media at its largest size. If this size isn't available, large
     * will be provided instead.
     */
    val extraLarge: String?,
    /**
     * The cover image url of the media at a large size
     */
    val large: String?,
) : BaseEntity, Parcelable