package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mac.allomalar.models.Alloma
import com.mac.allomalar.models.AllomaAndSubjects
import com.mac.allomalar.models.MadrasaAndAllomas


@Dao
interface MadrasaAndAllomasDao {

    @Query("Select*from madrasa_and_allomas")
    suspend fun getAllMadrasaAndAllomas():List<MadrasaAndAllomas>

    @Insert
    suspend fun insertMadrasaAndAllomas(madrasaAndAllomas: MadrasaAndAllomas)

    @Insert(onConflict =REPLACE)
    suspend fun insertMadrasaAndAllomasAll(list: List<MadrasaAndAllomas?>?)



}