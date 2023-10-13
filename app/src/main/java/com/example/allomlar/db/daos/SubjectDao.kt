package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mac.allomalar.models.Alloma
import com.mac.allomalar.models.AllomaAndSubjects
import com.mac.allomalar.models.MadrasaAndAllomas
import com.mac.allomalar.models.Subject


@Dao
interface SubjectDao {

    @Query("Select*from subjects where allomaId =:id")
    suspend fun getSubjects(id: Int): List<Subject?>

    @Insert
    suspend fun insertSubject(subject: Subject)

    @Insert(onConflict = REPLACE)
    suspend fun insertSubjectsAll(list: List<Subject?>?)
}