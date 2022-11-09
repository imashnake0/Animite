package com.imashnake.animite.data.sauce.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.imashnake.animite.data.sauce.db.model.Medium
import kotlinx.coroutines.flow.Flow


@Dao
interface MediaDAO : BaseDAO<Medium> {

    @Transaction // remove if not using relationships + junctions
    @Query("select * from medium")
    fun getAll(): Flow<List<Medium>>

}