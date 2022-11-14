package com.imashnake.animite.data.sauce.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.imashnake.animite.type.MediaType
import kotlinx.parcelize.Parcelize

/*@Parcelize
@Entity(tableName = "media_page")
data class MediaPage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Embedded val page: Page?,
) : BaseEntity, Parcelable

@Entity(tableName = "page_medium_link", primaryKeys = ["pageId", "mediumId"], indices = [Index("pageId"), Index("mediumId")])
data class PageToMediumLink(val pageId: Int, val mediumId: Int): BaseEntity

@Parcelize
@Entity(tableName = "page")
data class Page(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "page_id")
    val id: Int = 0,
    val media: List<Medium?>?,
) : BaseEntity, Parcelable*/


/**
 * Ensure that data coming from the API is flattened into List<Medium> to be inserted
 */
@Parcelize
@Entity(tableName = "medium")
data class Medium(
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