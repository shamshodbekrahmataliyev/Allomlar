package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mac.allomalar.models.Century

@Dao
interface CenturyDao {
    @Query("Select * from centuries")
    suspend fun getAllCenturies(): List<Century>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Century?>?)

}