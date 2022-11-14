package com.imashnake.animite.data.sauce.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.imashnake.animite.data.sauce.db.model.ListTag
import com.imashnake.animite.data.sauce.db.model.Medium
import com.imashnake.animite.data.sauce.db.model.MediumWithLink
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow


@Dao
interface MediaDAO : BaseDAO<Medium> {

    @Query("SELECT * FROM medium")
    override fun getAll(): Flow<List<Medium>>

    @Query("SELECT * FROM medium_link ml JOIN medium m ON ml.medium_id = m.medium_id WHERE tag = :tag AND type = :type")
    fun getAll(type: MediaType, tag: ListTag): Flow<List<MediumWithLink>>

}