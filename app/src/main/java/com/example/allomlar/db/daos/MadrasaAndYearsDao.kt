package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mac.allomalar.models.MadrasaAndYears
import retrofit2.http.GET

@Dao
interface MadrasaAndYearsDao {

    @Query("select * from madrasa_and_years")
    suspend fun getAllMadrasa(): List<MadrasaAndYears>

    @Insert(onConflict = REPLACE)
    suspend fun insertAllMadrasaYears(list: List<MadrasaAndYears?>?)
}