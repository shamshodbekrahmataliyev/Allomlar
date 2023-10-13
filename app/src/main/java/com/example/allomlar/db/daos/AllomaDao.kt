package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mac.allomalar.models.Alloma


@Dao
interface AllomaDao {

    @Query("Select * from alloma")
    suspend fun getAllAllomas(): List<Alloma>

    @Query("Select * from alloma where id=:id ")
    suspend fun getAlloma(id: Int): Alloma


    @Insert(onConflict = REPLACE)
    suspend fun insertAlloma(alloma: Alloma)

    @Insert(onConflict = REPLACE)
    suspend fun insertAllomas(list: List<Alloma?>?)

}