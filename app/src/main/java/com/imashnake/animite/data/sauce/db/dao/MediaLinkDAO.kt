package com.imashnake.animite.data.sauce.db.dao

import androidx.room.Dao
import com.imashnake.animite.data.sauce.db.model.MediumLink
import kotlinx.coroutines.flow.flowOf


@Dao
interface MediaLinkDAO : BaseDAO<MediumLink> {

    // no-op, not interested in getting links, just using it for the `insertAll` for now
    override fun getAll() = flowOf<List<MediumLink>>()

}