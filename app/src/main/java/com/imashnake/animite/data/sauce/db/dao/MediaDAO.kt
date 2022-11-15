package com.imashnake.animite.data.sauce.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.imashnake.animite.data.sauce.db.model.ListTag
import com.imashnake.animite.data.sauce.db.model.Media
import com.imashnake.animite.data.sauce.db.model.MediumWithLink
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow


@Dao
interface MediaDAO : BaseDAO<Media> {

    @Query("SELECT * FROM media")
    override fun getAll(): Flow<List<Media>>

    @Query("SELECT * FROM medium_link ml JOIN media m ON ml.medium_id = m.medium_id WHERE tag = :tag AND type = :type")
    fun getAll(type: MediaType, tag: ListTag): Flow<List<MediumWithLink>>

}