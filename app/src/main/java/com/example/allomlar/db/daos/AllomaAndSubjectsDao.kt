package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mac.allomalar.models.Alloma
import com.mac.allomalar.models.AllomaAndSubjects


@Dao
interface AllomaAndSubjectsDao {

    @Query("Select*from alloma_and_subjects")
    suspend fun getAllomaAndSubjects(): List<AllomaAndSubjects>

    @Insert
    suspend fun insertAllomaAndSubjects(allomaAndSubjects: AllomaAndSubjects)

    @Insert(onConflict = REPLACE)
    suspend fun insertAllomaAndSubjectsAll(list: List<AllomaAndSubjects>)


}