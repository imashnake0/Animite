package com.imashnake.animite.data.sauce.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.imashnake.animite.data.sauce.db.model.BaseEntity
import kotlinx.coroutines.flow.Flow

interface BaseDAO<T : BaseEntity> {

    fun getAll(): Flow<List<T>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entity: T)

    @Update
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T)
}