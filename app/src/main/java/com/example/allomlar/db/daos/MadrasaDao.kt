package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mac.allomalar.models.Madrasa

@Dao
interface MadrasaDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertMadrasas(list: List<Madrasa?>?)

    @Query("Select*from madrasas")
    suspend fun getAllMadrasas(): List<Madrasa>
}