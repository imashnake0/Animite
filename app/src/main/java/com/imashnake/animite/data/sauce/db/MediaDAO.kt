package com.imashnake.animite.data.sauce.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.imashnake.animite.data.sauce.db.model.Medium
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import kotlinx.coroutines.flow.Flow


@Dao
interface MediaDAO : BaseDAO<Medium> {

    @Transaction // remove if not using relationships + junctions
    @Query("select * from medium")
    override fun getAll(): Flow<List<Medium>>

    @Query("SELECT * FROM medium WHERE type = :type")
    fun getAll(type: MediaType, sort: List<MediaSort>, season: MediaSeason?, seasonYear: Int?): Flow<List<Medium>>

}