package com.imashnake.animite.search.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.imashnake.animite.search.db.model.AniListSearch
import com.imashnake.animite.search.db.model.CombinedSearchResults
import com.imashnake.animite.search.db.model.MyAnimeListSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Transaction
    @Query(
        """
        SELECT * FROM AniListSearch al
        INNER JOIN MyAnimeListSearch AS mal ON al.jaTitle = mal.jaTitle
        WHERE al.jaTitle LIKE :query
        """
    )
    fun getCombinedSearchResult(query: String): Flow<List<CombinedSearchResults>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAniListResults(results: List<AniListSearch>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyAnimeListResults(results: List<MyAnimeListSearch>)
}
